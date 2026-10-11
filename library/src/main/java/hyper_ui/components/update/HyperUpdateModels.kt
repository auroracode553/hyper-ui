package hyper_ui

import androidx.compose.runtime.Immutable

/** 两端共用的更新展示状态；检查、下载和安装由业务层执行。 */
enum class HyperUpdateStatus {
    idle, checking, available, upToDate, downloadQueued, downloading,
    readyToInstall, installing, completed, error
}

@Immutable
data class HyperUpdateRelease(
    val version: String,
    val releaseDate: String? = null,
    val sizeLabel: String? = null,
    val highlights: List<String> = emptyList()
) {
    init { require(version.isNotEmpty()) { "version 不能为空" } }
}

/** 下载服务的一次字节快照，不持有下载任务。 */
@Immutable
data class HyperUpdateDownloadProgress(
    val downloadedBytes: Long,
    val totalBytes: Long? = null,
    val bytesPerSecond: Double? = null
) {
    private val received get() = downloadedBytes.coerceAtLeast(0L)
    private val hasTotal get() = totalBytes != null && totalBytes > 0
    private val hasSpeed get() = bytesPerSecond != null && bytesPerSecond.isFinite() && bytesPerSecond > 0
    val fraction: Float? get() = if (hasTotal) normalizeHyperProgress((received.toDouble() / totalBytes!!).toFloat()) else null
    val sizeLabel: String get() = if (hasTotal)
        "${formatHyperTransferBytes(received.toDouble())} / ${formatHyperTransferBytes(totalBytes!!.toDouble())}"
        else "已下载 ${formatHyperTransferBytes(received.toDouble())}"
    val speedLabel: String? get() = if (hasSpeed) "${formatHyperTransferBytes(bytesPerSecond!!)} / s" else null
    val remainingLabel: String? get() {
        if (!hasTotal || !hasSpeed || received >= totalBytes!!) return null
        val seconds = (totalBytes - received) / bytesPerSecond!!
        return if (seconds.isFinite()) "预计剩余 ${formatHyperTransferSeconds(seconds)}" else null
    }
}
