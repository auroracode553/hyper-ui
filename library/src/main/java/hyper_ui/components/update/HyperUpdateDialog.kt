package hyper_ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** 两端共用的受控更新 API；组件只通知业务层，不持有更新任务。 */
@Composable
fun HyperUpdateDialog(
    status: HyperUpdateStatus = HyperUpdateStatus.idle,
    currentVersion: String? = null,
    release: HyperUpdateRelease? = null,
    progress: Float? = null,
    downloadProgress: HyperUpdateDownloadProgress? = null,
    progressText: String? = null,
    title: String? = null,
    message: String? = null,
    mandatory: Boolean = false,
    primaryLabel: String? = null,
    secondaryLabel: String? = null,
    onCheck: (() -> Unit)? = null,
    onDownload: (() -> Unit)? = null,
    onInstall: (() -> Unit)? = null,
    onRetry: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    onLater: (() -> Unit)? = null,
    onClose: (() -> Unit)? = null,
    maxWidth: Dp = 420.dp,
    modifier: Modifier = Modifier
) {
    require(maxWidth > 0.dp && maxWidth.value.isFinite()) { "maxWidth 必须为有限正数" }
    if (status == HyperUpdateStatus.idle) return
    val locked = status == HyperUpdateStatus.installing ||
        (mandatory && status != HyperUpdateStatus.completed && status != HyperUpdateStatus.upToDate)
    val view = hyperUpdatePresentation(status, currentVersion, release, progress, downloadProgress, progressText,
        title, message, mandatory, locked, primaryLabel, secondaryLabel,
        onCheck, onDownload, onInstall, onRetry, onCancel, onLater, onClose)
    HyperDialog(
        visible = true,
        onClose = if (locked) ({}) else ({ onClose?.invoke() }),
        modifier = modifier,
        maxWidth = maxWidth,
        shape = RoundedCornerShape(28.dp),
        dismissOnBackPress = !locked,
        dismissOnClickOutside = false,
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        actions = if (hyperUpdateHasActions(view)) ({ HyperUpdateActions(view) }) else null
    ) { HyperUpdateBody(view, locked, mandatory, onClose) }
}
