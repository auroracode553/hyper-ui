package hyper_ui

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings

internal data class HyperUpdateDownloadSnapshot(val status: Int, val reason: Int, val downloaded: Long, val total: Long)
internal data class HyperStoredUpdateDownload(val id: Long, val release: HyperUpdateInfo)

/** 系统下载与持久记录共用一个入口；关闭界面或退出进程不会取消系统任务。 */
internal class HyperUpdateDownload(
    context: Context,
    private val releases: HyperUpdateRepository,
    private val records: HyperUpdateDownloadRecords,
) {
    private val context = context.applicationContext
    private val downloads = this.context.getSystemService(DownloadManager::class.java)

    fun restore(): HyperStoredUpdateDownload? {
        val task = records.current() ?: return null
        val id = task.getLong("id")
        require(id > 0) { "更新下载任务编号无效" }
        return HyperStoredUpdateDownload(id, releases.parse(task.getJSONObject("release")))
    }

    fun start(release: HyperUpdateInfo): Long {
        val fileName = "${context.packageName}-${release.versionCode}-${System.currentTimeMillis()}.apk"
        val request = DownloadManager.Request(Uri.parse(release.url)).apply {
            setTitle("${context.applicationInfo.loadLabel(context.packageManager)} ${release.version}")
            setDescription("正在下载客户端更新")
            setMimeType("application/vnd.android.package-archive")
            setAllowedOverMetered(true)
            setAllowedOverRoaming(false)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        }
        val id = downloads.enqueue(request)
        try {
            records.save(id, release.toJson())
        } catch (error: Exception) {
            downloads.remove(id)
            throw error
        }
        return id
    }

    fun snapshot(id: Long): HyperUpdateDownloadSnapshot {
        val cursor = downloads.query(DownloadManager.Query().setFilterById(id)) ?: error("无法查询系统下载")
        return cursor.use {
            check(it.moveToFirst()) { "系统下载任务已被移除，请重新下载" }
            HyperUpdateDownloadSnapshot(it.getInt(it.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)),
                it.getInt(it.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON)),
                it.getLong(it.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)).coerceAtLeast(0),
                it.getLong(it.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)))
        }
    }

    fun cancel(id: Long) { downloads.remove(id); records.forget() }
    fun isAvailable(id: Long): Boolean = downloads.getUriForDownloadedFile(id) != null

    fun install(id: Long): Boolean {
        val uri = downloads.getUriForDownloadedFile(id) ?: error("已下载的 APK 不存在，请重新下载")
        if (!context.packageManager.canRequestPackageInstalls()) {
            context.startActivity(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:${context.packageName}")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            return false
        }
        context.startActivity(Intent(Intent.ACTION_VIEW).setDataAndType(uri, "application/vnd.android.package-archive")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION))
        return true
    }
}
