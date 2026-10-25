package hyper_ui

/** 工具提供真实任务状态；展示仍由受控的 HyperUpdateDialog 完成。 */
data class HyperUpdateState(
    val status: HyperUpdateStatus = HyperUpdateStatus.idle,
    val version: String? = null,
    val notes: String = "",
    val downloadedBytes: Long = 0,
    val totalBytes: Long? = null,
    val bytesPerSecond: Double? = null,
    val message: String? = null,
) {
    val summary: String get() = when (status) {
        HyperUpdateStatus.idle -> "点击检查新版本"
        HyperUpdateStatus.checking -> "正在检查，点击查看"
        HyperUpdateStatus.available -> "发现新版本 ${version.orEmpty()}"
        HyperUpdateStatus.upToDate -> "已是最新版本"
        HyperUpdateStatus.downloadQueued -> "系统正在等待下载，点击查看"
        HyperUpdateStatus.downloading -> totalBytes?.takeIf { it > 0 }?.let {
            "下载中 ${(downloadedBytes.toDouble() / it * 100).toInt().coerceIn(0, 100)}%，点击查看"
        } ?: "正在下载，点击查看"
        HyperUpdateStatus.readyToInstall -> "更新 ${version.orEmpty()} 已就绪，点击查看"
        HyperUpdateStatus.installing -> "正在应用更新"
        HyperUpdateStatus.completed -> "更新已完成"
        HyperUpdateStatus.error -> "更新失败，点击重试"
    }
}
