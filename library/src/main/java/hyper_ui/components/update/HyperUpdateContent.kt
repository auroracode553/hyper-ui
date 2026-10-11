/** 文件职责：更新弹窗的标识、版本、更新摘要、进度和固定操作区。由 HyperUpdateDialog 组合。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
internal fun HyperUpdateBody(view: HyperUpdatePresentation, locked: Boolean, mandatory: Boolean, onClose: () -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            HyperText("软件更新", Modifier.weight(1f), color = HyperColors.secondaryText, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            if (!locked) HyperButton(onClose, type = "icon", size = "small", colors = HyperButtonDefaults.colors(type = "ghost"), border = null) {
                HyperIcon(HyperReferenceIcons.Close, "关闭")
            }
        }
        Spacer(Modifier.height(20.dp))
        val shape = RoundedCornerShape(24.dp)
        Box(Modifier.size(64.dp)
            .dropShadow(shape, Shadow(radius = 24.dp, color = view.color.copy(alpha = 18 / 255f), offset = DpOffset(0.dp, 8.dp)))
            .background(Brush.linearGradient(listOf(view.color.copy(alpha = 28 / 255f).compositeOver(hyperGlass.surface),
                view.color.copy(alpha = 10 / 255f).compositeOver(hyperGlass.surface))), shape)
            .hyperSolidSurface(Color.Transparent, shape, border = BorderStroke(1.dp, view.color.copy(alpha = 35 / 255f))),
            contentAlignment = Alignment.Center) {
            HyperIcon(view.icon, null, Modifier.size(26.dp), tint = view.color)
            if (view.busy) HyperProgress(modifier = Modifier.size(48.dp), type = "circular", showLabel = false,
                strokeWidth = 1.5.dp, colors = HyperProgressColors(view.color.copy(alpha = 12 / 255f), view.color))
        }
        Spacer(Modifier.height(20.dp))
        HyperText(view.title, fontSize = 24.sp, lineHeight = 28.8.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        HyperText(if (mandatory && view.state is HyperUpdateDialogState.UpdateAvailable) "此版本为必要更新，请更新后继续使用。" else view.message,
            color = if (view.state is HyperUpdateDialogState.Error) view.color else HyperColors.secondaryText,
            fontSize = 14.sp, lineHeight = 21.7.sp)
        Spacer(Modifier.height(20.dp))
        HyperUpdateVersion(view, mandatory && locked)
        if (view.state is HyperUpdateDialogState.UpdateAvailable) {
            val highlights = view.release?.highlights.orEmpty().ifEmpty {
                view.release?.releaseNotes?.takeIf { it.isNotBlank() }?.lines()?.filter { it.isNotBlank() }.orEmpty()
            }
            if (highlights.isNotEmpty()) {
                Spacer(Modifier.height(20.dp))
                HyperText("本次更新", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                highlights.forEach { text ->
                    Row(Modifier.padding(top = 12.dp)) {
                        HyperIcon(HyperReferenceIcons.Check, null, Modifier.padding(top = 3.dp).size(14.dp), tint = HyperColors.accent)
                        Spacer(Modifier.width(8.dp))
                        HyperText(text, Modifier.weight(1f), color = HyperColors.secondaryText, fontSize = 13.sp, lineHeight = 19.5.sp)
                    }
                }
            }
        }
        if (view.hasProgress) {
            Spacer(Modifier.height(20.dp))
            val installing = view.state is HyperUpdateDialogState.Installing
            HyperUpdateProgressBadges(installing)
            Spacer(Modifier.height(16.dp))
            HyperText(view.progress?.let { "${(it * 100).roundToInt()}%" } ?: if (installing) "正在安装…" else "正在下载…",
                fontSize = 28.sp, lineHeight = 33.6.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            HyperProgress(view.progress, showLabel = false)
            Spacer(Modifier.height(8.dp))
            HyperText(view.detail ?: if (installing) "正在应用更新，请稍候" else "正在下载更新文件",
                color = HyperColors.secondaryText, fontSize = 12.sp, lineHeight = 18.sp)
        }
        if (mandatory && locked) HyperText("完成本次更新后即可继续使用", Modifier.padding(top = 16.dp),
            color = HyperColors.secondaryText, fontSize = 12.sp)
        // 常规 Dialog 的操作间距为 18dp；更新弹窗正文底部留白为 24dp。
        if (hyperUpdateHasActions(view, locked)) Spacer(Modifier.height(6.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HyperUpdateVersion(view: HyperUpdatePresentation, mandatory: Boolean) {
    val completed = view.state is HyperUpdateDialogState.Completed
    Column(Modifier.fillMaxWidth().hyperSolidSurface(hyperGlass.surfaceSubtle, RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, HyperColors.divider)).padding(13.dp)) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp),
            itemVerticalAlignment = Alignment.CenterVertically) {
            if (!completed || view.release == null) HyperText("当前 ${view.version}", color = HyperColors.secondaryText, fontSize = 12.sp)
            if (view.release != null) {
                if (!completed) HyperIcon(HyperReferenceIcons.ArrowForward, null, Modifier.size(14.dp), tint = HyperColors.secondaryText)
                HyperUpdateBadge("${if (completed) "已更新至" else "新版本"} ${view.release.versionName}", view.color)
            }
            if (mandatory) HyperUpdateBadge("必要更新", HyperColors.warning)
        }
        val detail = listOfNotNull(view.release?.releaseDate, view.release?.sizeLabel).joinToString(" · ")
        if (detail.isNotBlank()) HyperText(detail, Modifier.padding(top = 8.dp), color = HyperColors.secondaryText, fontSize = 12.sp)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HyperUpdateProgressBadges(installing: Boolean) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HyperUpdateBadge(if (installing) "下载完成" else "1 · 下载更新",
            if (installing) HyperColors.success else HyperColors.accent,
            if (installing) HyperReferenceIcons.Check else HyperReferenceIcons.Download)
        HyperUpdateBadge("2 · 安装更新", if (installing) HyperColors.accent else HyperColors.secondaryText)
    }
}

@Composable
private fun HyperUpdateBadge(label: String, color: Color, icon: ImageVector? = null) {
    Row(Modifier.background(color.copy(alpha = 31 / 255f).compositeOver(hyperGlass.surfaceSubtle),
        RoundedCornerShape(50)).padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        if (icon != null) HyperIcon(icon, null, Modifier.size(13.dp), tint = color)
        HyperText(label, color = color, fontSize = 12.sp, lineHeight = 14.4.sp, fontWeight = FontWeight.SemiBold)
    }
}

internal fun hyperUpdateHasActions(view: HyperUpdatePresentation, locked: Boolean): Boolean = !view.busy || !locked

@Composable
internal fun HyperUpdateActions(view: HyperUpdatePresentation, texts: HyperUpdateDialogTexts, locked: Boolean,
    onClose: () -> Unit, onRetry: () -> Unit, onDownload: () -> Unit, onInstall: () -> Unit, onCheck: () -> Unit, onCancel: () -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        HyperDivider()
        Spacer(Modifier.height(20.dp))
        if (!view.busy) {
            val (label, action, icon) = when (view.state) {
                is HyperUpdateDialogState.UpdateAvailable -> Triple(texts.downloadAction, onDownload, HyperReferenceIcons.Download)
                is HyperUpdateDialogState.ReadyToInstall -> Triple("立即安装", onInstall, HyperReferenceIcons.PackageCheck)
                is HyperUpdateDialogState.Error -> Triple(texts.retryAction, onRetry, HyperReferenceIcons.Refresh)
                is HyperUpdateDialogState.Completed -> Triple("开始使用", onClose, HyperReferenceIcons.Check)
                else -> Triple("再次检查", onCheck, HyperReferenceIcons.Refresh)
            }
            HyperButton(action, Modifier.fillMaxWidth()) { HyperIcon(icon, null); HyperText(label) }
        }
        val secondary = !locked && (view.busy || view.state is HyperUpdateDialogState.UpdateAvailable ||
            view.state is HyperUpdateDialogState.ReadyToInstall || view.state is HyperUpdateDialogState.Error)
        if (secondary) {
            if (!view.busy) Spacer(Modifier.height(8.dp))
            HyperButton(if (view.busy) onCancel else onClose, Modifier.fillMaxWidth(), type = "ghost") {
                HyperText(if (view.busy) texts.cancelAction else if (view.state is HyperUpdateDialogState.Error) texts.closeAction else "稍后再说")
            }
        }
    }
}
