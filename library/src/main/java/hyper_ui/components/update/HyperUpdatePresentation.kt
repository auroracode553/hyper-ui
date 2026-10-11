package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

internal data class HyperUpdateAction(val label: String, val icon: ImageVector, val onPressed: (() -> Unit)?)
internal data class HyperUpdatePresentation(
    val status: HyperUpdateStatus,
    val title: String,
    val message: String,
    val version: String?,
    val release: HyperUpdateRelease?,
    val icon: ImageVector,
    val color: Color,
    val busy: Boolean,
    val hasProgress: Boolean,
    val progress: Float?,
    val detail: String?,
    val downloadProgress: HyperUpdateDownloadProgress?,
    val primary: HyperUpdateAction?,
    val secondary: Pair<String, (() -> Unit)>?
)

@Composable
internal fun hyperUpdatePresentation(
    status: HyperUpdateStatus, currentVersion: String?, release: HyperUpdateRelease?, progress: Float?,
    downloadProgress: HyperUpdateDownloadProgress?, progressText: String?, title: String?, message: String?,
    mandatory: Boolean, locked: Boolean, primaryLabel: String?, secondaryLabel: String?,
    onCheck: (() -> Unit)?, onDownload: (() -> Unit)?, onInstall: (() -> Unit)?, onRetry: (() -> Unit)?,
    onCancel: (() -> Unit)?, onLater: (() -> Unit)?, onClose: (() -> Unit)?
): HyperUpdatePresentation {
    val busy = status in setOf(HyperUpdateStatus.checking, HyperUpdateStatus.downloadQueued,
        HyperUpdateStatus.downloading, HyperUpdateStatus.installing)
    val downloading = status == HyperUpdateStatus.downloading || status == HyperUpdateStatus.downloadQueued
    val icon = when (status) {
        HyperUpdateStatus.available -> HyperReferenceIcons.Sparkles
        HyperUpdateStatus.downloadQueued, HyperUpdateStatus.downloading -> HyperReferenceIcons.Download
        HyperUpdateStatus.readyToInstall, HyperUpdateStatus.installing -> HyperReferenceIcons.PackageCheck
        HyperUpdateStatus.upToDate, HyperUpdateStatus.completed -> HyperReferenceIcons.Check
        HyperUpdateStatus.error -> HyperReferenceIcons.CircleAlert
        else -> HyperReferenceIcons.Refresh
    }
    val color = when (status) {
        HyperUpdateStatus.upToDate, HyperUpdateStatus.completed -> HyperColors.success
        HyperUpdateStatus.error -> HyperColors.danger
        else -> HyperColors.accent
    }
    val defaultTitle = when (status) {
        HyperUpdateStatus.idle -> "让体验保持最新"
        HyperUpdateStatus.checking -> "正在检查更新"
        HyperUpdateStatus.available -> "发现新版本"
        HyperUpdateStatus.upToDate -> "已是最新版本"
        HyperUpdateStatus.downloadQueued -> "已开始下载"
        HyperUpdateStatus.downloading -> "正在下载更新"
        HyperUpdateStatus.readyToInstall -> "更新已准备就绪"
        HyperUpdateStatus.installing -> "正在安装更新"
        HyperUpdateStatus.completed -> "更新完成"
        HyperUpdateStatus.error -> ""
    }
    val defaultMessage = when (status) {
        HyperUpdateStatus.idle -> "检查新版本，获取最新功能与体验优化。"
        HyperUpdateStatus.checking -> "正在获取版本信息，请稍候。"
        HyperUpdateStatus.available -> if (mandatory) "此版本为必要更新，请更新后继续使用。" else "为你带来了新的功能与细节优化。"
        HyperUpdateStatus.upToDate -> "所有新功能与体验优化，都已为你准备好。"
        HyperUpdateStatus.downloadQueued -> "更新包已交给系统下载管理器。"
        HyperUpdateStatus.downloading -> "新版本正在路上，请保持网络连接。"
        HyperUpdateStatus.readyToInstall -> "下载已完成，你可以开始安装新版本。"
        HyperUpdateStatus.installing -> "正在应用新版本，请保持应用开启。"
        HyperUpdateStatus.completed -> "新版本已就绪，继续探索更好的体验。"
        HyperUpdateStatus.error -> "请检查网络连接或稍后重试。"
    }
    val primary = when (status) {
        HyperUpdateStatus.available -> HyperUpdateAction("立即更新", HyperReferenceIcons.Download, onDownload)
        HyperUpdateStatus.readyToInstall -> HyperUpdateAction("立即安装", HyperReferenceIcons.PackageCheck, onInstall)
        HyperUpdateStatus.completed -> HyperUpdateAction("开始使用", HyperReferenceIcons.Check, onClose)
        HyperUpdateStatus.error -> HyperUpdateAction("重试", HyperReferenceIcons.Refresh, onRetry)
        HyperUpdateStatus.upToDate -> HyperUpdateAction("再次检查", HyperReferenceIcons.Refresh, onCheck)
        else -> null
    }?.let { it.copy(label = primaryLabel ?: it.label) }
    val secondary = if (locked) null else when (status) {
        HyperUpdateStatus.checking, HyperUpdateStatus.downloadQueued, HyperUpdateStatus.downloading -> onCancel?.let { "取消" to it }
        HyperUpdateStatus.available, HyperUpdateStatus.readyToInstall -> onLater?.let { "稍后再说" to it }
        HyperUpdateStatus.error -> onClose?.let { "关闭" to it }
        else -> null
    }?.let { (label, callback) -> (secondaryLabel ?: label) to callback }
    val showRelease = status in setOf(HyperUpdateStatus.available, HyperUpdateStatus.downloadQueued,
        HyperUpdateStatus.downloading, HyperUpdateStatus.readyToInstall, HyperUpdateStatus.installing, HyperUpdateStatus.completed)
    return HyperUpdatePresentation(status, title ?: defaultTitle, message ?: defaultMessage, currentVersion,
        release.takeIf { showRelease }, icon, color, busy, downloading || status == HyperUpdateStatus.installing,
        if (downloading && downloadProgress != null) downloadProgress.fraction else normalizeHyperProgress(progress),
        progressText, downloadProgress.takeIf { downloading }, primary, secondary)
}
