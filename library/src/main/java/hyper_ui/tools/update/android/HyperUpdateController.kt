package hyper_ui

import android.app.DownloadManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** 通用 APK 检查、下载观察和窗口状态；不包含项目业务资源，只保留一个在途任务。 */
class HyperUpdateController(context: Context, val configuration: HyperUpdateConfig) : AutoCloseable {
    @Suppress("DEPRECATION")
    private val installedPackage = context.packageManager.getPackageInfo(context.packageName, 0)
    val currentVersionName: String = installedPackage.versionName ?: error("应用缺少 versionName")
    val currentVersionCode: Long = installedPackage.longVersionCode
    private val history = HyperUpdateCheckHistory(context,
        "apk:${configuration.repository}:${configuration.branch}:${configuration.indexFile}",
        configuration.autoCheckWindowMs, configuration.autoCheckLimit)
    private val main = Handler(Looper.getMainLooper())
    private val schedule = HyperUpdateCheckSchedule(main) { check(manual = false) }
    private val worker = hyperUpdateExecutor("app-update")
    private val http = HyperUpdateHttpClient()
    private val releases = HyperUpdateRepository(configuration, http)
    private val downloadRecords = HyperUpdateDownloadRecords(context)
    private val downloads = HyperUpdateDownload(context, releases, downloadRecords)
    private var closed = false
    private var checking by mutableStateOf(false)
    private var restoring by mutableStateOf(true)
    private var starting by mutableStateOf(false)
    private var cancelling by mutableStateOf(false)
    private var polling = false
    private var foreground by mutableStateOf(false)
    internal val isForeground get() = foreground
    private var downloadId: Long? = null
    private var downloadComplete = false
    private var retryCheck = true
    private var release: HyperUpdateInfo? = null
    private var previousBytes = 0L
    private var previousSample = 0L
    private val observe = Runnable { pollDownload() }

    var state by mutableStateOf(HyperUpdateState())
        private set
    val summary: String get() = state.summary

    var dialogVisible by mutableStateOf(false)
        private set
    private val busy get() = restoring || checking || starting || cancelling ||
        state.status in setOf(HyperUpdateStatus.downloadQueued, HyperUpdateStatus.downloading)
    val canCancel get() = !starting && !cancelling && downloadId != null &&
        state.status in setOf(HyperUpdateStatus.downloadQueued, HyperUpdateStatus.downloading)

    init {
        worker.execute {
            runCatching { downloadRecords.cleanupInstalled() }
                .onFailure { Log.w("HyperUpdate", "安装包清理未完成，保留记录待重试", it) }
            val result = runCatching { downloads.restore() }
            main.post {
                if (closed) return@post
                restoring = false
                result.fold(onSuccess = { stored ->
                    if (stored != null && stored.release.versionCode > currentVersionCode) {
                        release = stored.release
                        downloadId = stored.id
                        state = releaseState(HyperUpdateStatus.downloadQueued)
                        if (foreground) pollDownload()
                    } else {
                        // 已安装旧包由清理入口负责，失败时保留 ID，不能只清除记录。
                        if (dialogVisible) check(manual = true)
                    }
                }, onFailure = {
                    forgetDownload()
                    fail(it)
                })
            }
        }
    }

    fun check(manual: Boolean = true) {
        if (closed) return
        if (manual) dialogVisible = true
        // 手动检查接管现有检查或下载窗口，不另发请求，也不覆盖下载进度。
        if (restoring) {
            if (manual) state = HyperUpdateState(HyperUpdateStatus.checking, message = "正在恢复系统下载记录")
            return
        }
        if (downloadId != null && !downloadComplete) {
            if (manual) pollDownload()
            return
        }
        if (busy) return
        if (!manual && !history.tryStartAutomaticCheck()) return
        val prepared = release?.takeIf { downloadComplete && downloadId != null }
        checking = true
        retryCheck = true
        release = null
        state = HyperUpdateState(HyperUpdateStatus.checking)
        worker.execute {
            val result = runCatching {
                releases.latest().also { latest ->
                    if (prepared != null && latest.versionCode > prepared.versionCode) downloadRecords.retireCurrent()
                }
            }
            main.post {
                if (closed) return@post
                checking = false
                result.fold(onSuccess = { latest ->
                    retryCheck = false
                    if (prepared != null && latest.versionCode <= prepared.versionCode) {
                        release = prepared
                        state = releaseState(HyperUpdateStatus.readyToInstall)
                    } else {
                        // 已完成的旧包不能阻断新检查；索引指向哪个最新完整版本，就直接提供该版本。
                        if (downloadId != null && prepared == null) forgetDownload()
                        downloadId = null
                        downloadComplete = false
                        release = latest.takeIf { it.versionCode > currentVersionCode }
                        state = if (release != null) releaseState(HyperUpdateStatus.available)
                            else HyperUpdateState(HyperUpdateStatus.upToDate, version = latest.version)
                    }
                    release?.let { if (history.shouldRemindApp(it.versionCode)) dialogVisible = true }
                }, onFailure = {
                    release = prepared
                    fail(it)
                })
            }
        }
    }

    fun dismiss() {
        dialogVisible = false
        release?.let { history.dismissApp(it.versionCode) }
    }

    fun download() {
        val next = release ?: return
        if (closed || busy || downloadId != null) return
        starting = true
        retryCheck = false
        downloadComplete = false
        dialogVisible = true
        state = releaseState(HyperUpdateStatus.downloadQueued)
        worker.execute {
            val result = runCatching { downloads.start(next) }
            main.post {
                if (closed) return@post
                starting = false
                result.fold(onSuccess = {
                    downloadId = it
                    previousBytes = 0
                    previousSample = SystemClock.elapsedRealtime()
                    if (foreground) pollDownload()
                }, onFailure = ::fail)
            }
        }
    }

    fun pageReady() { schedule.pageReady() }

    fun foreground(visible: Boolean) {
        schedule.foreground(visible)
        if (closed || foreground == visible) return
        foreground = visible
        main.removeCallbacks(observe)
        previousSample = 0
        if (visible && downloadId != null) pollDownload()
    }

    private fun pollDownload() {
        val id = downloadId ?: return
        if (closed || !foreground || polling || checking || cancelling) return
        polling = true
        worker.execute {
            val result = runCatching { downloads.snapshot(id) }
            main.post {
                polling = false
                if (closed || downloadId != id) return@post
                if (cancelling || checking) return@post
                result.fold(onSuccess = { snapshot ->
                    val now = SystemClock.elapsedRealtime()
                    val speed = if (previousSample > 0 && now > previousSample && snapshot.downloaded >= previousBytes)
                        (snapshot.downloaded - previousBytes) * 1000.0 / (now - previousSample) else null
                    previousSample = now
                    previousBytes = snapshot.downloaded
                    when (snapshot.status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            downloadComplete = true
                            state = releaseState(HyperUpdateStatus.readyToInstall)
                        }
                        DownloadManager.STATUS_FAILED -> {
                            downloadId = null
                            downloadComplete = false
                            retryCheck = false
                            forgetDownload()
                            fail(IllegalStateException(downloadError(snapshot.reason)))
                        }
                        else -> {
                            state = releaseState(if (snapshot.status == DownloadManager.STATUS_RUNNING)
                                HyperUpdateStatus.downloading else HyperUpdateStatus.downloadQueued).copy(
                                downloadedBytes = snapshot.downloaded,
                                totalBytes = snapshot.total.takeIf { it > 0 } ?: release?.size,
                                bytesPerSecond = speed,
                                message = if (snapshot.status == DownloadManager.STATUS_PAUSED)
                                    "系统下载已暂停，正在等待网络或系统调度" else null)
                            if (foreground) main.postDelayed(observe, 700)
                        }
                    }
                }, onFailure = {
                    downloadId = null
                    downloadComplete = false
                    retryCheck = false
                    forgetDownload()
                    fail(it)
                })
            }
        }
    }

    fun cancelDownload() {
        val id = downloadId ?: return
        if (closed || cancelling) return
        cancelling = true
        main.removeCallbacks(observe)
        worker.execute {
            val result = runCatching { downloads.cancel(id) }
            main.post {
                if (closed || downloadId != id) return@post
                cancelling = false
                result.fold(onSuccess = {
                    downloadId = null
                    downloadComplete = false
                    state = releaseState(HyperUpdateStatus.available)
                }, onFailure = { fail(it); if (foreground) pollDownload() })
            }
        }
    }

    fun install() {
        val id = downloadId ?: return
        retryCheck = false
        runCatching {
            if (!downloads.isAvailable(id)) {
                downloadId = null
                downloadComplete = false
                forgetDownload()
                error("已下载的 APK 不存在，请重新下载")
            }
            downloads.install(id)
        }.fold(onSuccess = { launched ->
            if (launched) dismiss() else state = releaseState(HyperUpdateStatus.readyToInstall).copy(
                message = "请允许此来源安装应用，返回后再次点击立即安装。")
        }, onFailure = ::fail)
    }

    fun retry() {
        if (retryCheck) check(manual = true)
        else if (downloadId != null && state.status == HyperUpdateStatus.error) install()
        else if (release != null && state.status == HyperUpdateStatus.error) download()
        else check(manual = true)
    }

    private fun releaseState(status: HyperUpdateStatus) = HyperUpdateState(status, version = release?.version,
        notes = release?.notes.orEmpty(), totalBytes = release?.size)

    private fun forgetDownload() {
        worker.execute { runCatching { downloadRecords.forget() }.onFailure { Log.w("HyperUpdate", "清理下载记录失败", it) } }
    }

    private fun fail(error: Throwable) {
        state = releaseState(HyperUpdateStatus.error).copy(message = error.message ?: "客户端更新失败")
        Log.w("HyperUpdate", "客户端更新失败", error)
    }

    private fun downloadError(reason: Int): String = when (reason) {
        DownloadManager.ERROR_INSUFFICIENT_SPACE -> "存储空间不足，请释放空间后重试"
        DownloadManager.ERROR_FILE_ERROR -> "无法写入 APK 文件"
        DownloadManager.ERROR_HTTP_DATA_ERROR, DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> "APK 下载失败，请检查网络或仓库附件"
        DownloadManager.ERROR_TOO_MANY_REDIRECTS -> "APK 下载重定向次数过多"
        else -> "系统下载失败（错误码 $reason）"
    }

    override fun close() {
        closed = true
        foreground = false
        schedule.close()
        http.close()
        main.removeCallbacksAndMessages(null)
        // 只停止观察。已提交的系统下载和持久记录继续保留。
        worker.shutdown()
    }
}
