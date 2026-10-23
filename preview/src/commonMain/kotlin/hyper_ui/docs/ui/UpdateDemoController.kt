/** 文件职责：页面持有的更新模拟任务；由 UpdateDialogShowcase 注入组件，不进入 UI 库。 */
package hyper_ui.docs.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import hyper_ui.HyperUpdateDownloadProgress
import hyper_ui.HyperUpdateRelease
import hyper_ui.HyperUpdateStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

internal class UpdateDemoController(private val scope: CoroutineScope) {
    val release = HyperUpdateRelease(
        version = "2.8.0", releaseDate = "2026.10.10", sizeLabel = "48.6 MB",
        highlights = listOf("全新的柔光界面，交互细节更自然。", "优化启动与页面切换，让每一次操作更流畅。", "修复已知问题，提升使用稳定性。")
    )
    private val totalBytes = 48_600_000L
    private val bytesPerSecond = 4_800_000.0
    private var task: Job? = null
    private var downloadedBytes by mutableStateOf(0L)
    private var updated by mutableStateOf(false)
    private var latest = false
    var status by mutableStateOf(HyperUpdateStatus.available)
        private set
    var installProgress by mutableStateOf<Float?>(null)
        private set
    var unknownTotal by mutableStateOf(false)
        private set

    val currentVersion: String get() = if (updated) release.version else "2.7.3"
    val busy: Boolean get() = status in setOf(HyperUpdateStatus.checking,
        HyperUpdateStatus.downloading, HyperUpdateStatus.installing)
    val downloadProgress: HyperUpdateDownloadProgress get() = HyperUpdateDownloadProgress(
        downloadedBytes, totalBytes.takeUnless { unknownTotal }, bytesPerSecond)

    // 避免与属性生成的 JVM setter 同名，Desktop 预览与 Wasm 共用此入口。
    fun updateUnknownTotal(value: Boolean) { unknownTotal = value }

    fun check(latest: Boolean = false, fail: Boolean = false) {
        if (busy) return
        task?.cancel()
        this.latest = latest
        status = HyperUpdateStatus.checking
        installProgress = null
        task = scope.launch {
            delay(1400)
            status = when {
                fail -> HyperUpdateStatus.error
                latest || updated -> HyperUpdateStatus.upToDate
                else -> HyperUpdateStatus.available
            }
        }
    }

    fun download() {
        if (status != HyperUpdateStatus.available) return
        task?.cancel()
        status = HyperUpdateStatus.downloading
        downloadedBytes = 0
        installProgress = null
        task = scope.launch {
            while (downloadedBytes < totalBytes) {
                delay(700)
                downloadedBytes = (downloadedBytes + (bytesPerSecond * 0.7).roundToLong()).coerceAtMost(totalBytes)
            }
            status = HyperUpdateStatus.readyToInstall
        }
    }

    fun install() {
        if (status != HyperUpdateStatus.readyToInstall) return
        task?.cancel()
        status = HyperUpdateStatus.installing
        installProgress = 0f
        task = scope.launch {
            while ((installProgress ?: 0f) < 1f) {
                delay(250)
                installProgress = ((installProgress ?: 0f) + 0.08f).coerceAtMost(1f)
            }
            status = HyperUpdateStatus.completed
            updated = true
        }
    }

    fun retry() { check(latest = latest) }

    fun cancel() {
        if (status != HyperUpdateStatus.checking && status != HyperUpdateStatus.downloading) return
        val downloading = status == HyperUpdateStatus.downloading
        task?.cancel()
        task = null
        status = if (downloading) HyperUpdateStatus.available else HyperUpdateStatus.idle
        downloadedBytes = 0
    }

    // 状态切片静止展示，与真实模拟任务分开；关闭弹层不会销毁页面任务。
    fun preview(value: HyperUpdateStatus) {
        if (busy && task?.isActive == true) return
        status = value
        downloadedBytes = if (value == HyperUpdateStatus.downloading) (totalBytes * 0.64).roundToLong() else 0
        installProgress = 0.35f.takeIf { value == HyperUpdateStatus.installing }
        updated = value == HyperUpdateStatus.completed
    }

    fun dispose() { task?.cancel() }
}
