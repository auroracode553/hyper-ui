/** 文件职责：更新弹窗的标识、版本、更新摘要、进度和固定操作区。由 HyperUpdateDialog 组合。 */
package hyper_ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ArrowRight
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Download
import com.composables.icons.lucide.X

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun HyperUpdateBody(view: HyperUpdatePresentation, locked: Boolean, mandatory: Boolean, onClose: (() -> Unit)?) {
    val isError = view.status == HyperUpdateStatus.error
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            HyperText("软件更新", Modifier.weight(1f), color = HyperColors.secondaryText,
                fontSize = 12.sp, lineHeight = 16.8.sp, fontWeight = FontWeight.Medium)
            if (!locked && onClose != null) HyperButton(onClose, type = "icon") {
                HyperIcon(Lucide.X, "关闭")
            }
        }
        Spacer(Modifier.height(20.dp))
        // 错误状态仅保留具体原因和操作，不重复展示状态标题与当前版本。
        if (!isError) {
            val shape = RoundedCornerShape(16.dp)
            Box(Modifier.size(40.dp)
                .dropShadow(shape, Shadow(radius = 16.dp, color = view.color.copy(alpha = 18 / 255f), offset = DpOffset(0.dp, 6.dp)))
                .background(Brush.linearGradient(listOf(view.color.copy(alpha = 28 / 255f).compositeOver(hyperGlass.surface),
                    view.color.copy(alpha = 10 / 255f).compositeOver(hyperGlass.surface))), shape)
                .hyperSolidSurface(Color.Transparent, shape, border = BorderStroke(1.dp, view.color.copy(alpha = 35 / 255f))),
                contentAlignment = Alignment.Center) {
                HyperIcon(view.icon, null, Modifier.size(20.dp), color = view.color)
                if (view.busy) HyperProgress(modifier = Modifier.size(30.dp), type = "circular", showLabel = false,
                    strokeWidth = 1.5.dp, colors = HyperProgressColors(view.color.copy(alpha = 12 / 255f), view.color))
            }
            Spacer(Modifier.height(20.dp))
            HyperText(view.title, fontSize = 24.sp, lineHeight = 28.8.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
        }
        HyperText(view.message,
            color = if (isError) view.color else HyperColors.secondaryText,
            fontSize = 14.sp, lineHeight = 21.7.sp)
        if (!isError && view.status != HyperUpdateStatus.checking && (view.version != null || view.release != null)) {
            Spacer(Modifier.height(20.dp))
            HyperUpdateVersion(view, mandatory && locked)
        }
        if (view.status == HyperUpdateStatus.available) {
            val highlights = view.release?.highlights.orEmpty()
            if (highlights.isNotEmpty()) {
                Spacer(Modifier.height(20.dp))
                HyperText("本次更新", fontSize = 13.sp, lineHeight = 18.2.sp, fontWeight = FontWeight.SemiBold)
                highlights.forEach { text ->
                    Row(Modifier.padding(top = 12.dp)) {
                        HyperIcon(Lucide.Check, null, Modifier.padding(top = 3.dp).size(14.dp), color = HyperColors.accent)
                        Spacer(Modifier.width(8.dp))
                        HyperText(text, Modifier.weight(1f), color = HyperColors.secondaryText, fontSize = 13.sp, lineHeight = 19.5.sp)
                    }
                }
            }
        }
        if (view.hasProgress) {
            Spacer(Modifier.height(20.dp))
            val installing = view.status == HyperUpdateStatus.installing
            HyperUpdateProgressBadges(installing)
            Spacer(Modifier.height(16.dp))
            HyperText(view.progress?.let { "${hyperProgressPercentage(it)}%" } ?: if (installing) "正在安装…" else "正在下载…",
                fontSize = 28.sp, lineHeight = 33.6.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))
            HyperProgress(view.progress, showLabel = false)
            Spacer(Modifier.height(8.dp))
            view.downloadProgress?.let { snapshot ->
                HyperText(snapshot.sizeLabel, color = HyperColors.secondaryText, fontSize = 12.sp, lineHeight = 16.8.sp)
                HyperUpdateTransferDetails(snapshot)
            }
            if (view.detail != null || view.downloadProgress == null) HyperText(
                view.detail ?: if (installing) "正在应用更新，请稍候" else "正在下载更新文件",
                color = HyperColors.secondaryText, fontSize = 12.sp, lineHeight = 18.sp)
        }
        if (mandatory && locked) HyperText("完成本次更新后即可继续使用", Modifier.padding(top = 16.dp),
            color = HyperColors.secondaryText, fontSize = 12.sp, lineHeight = 16.8.sp)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HyperUpdateVersion(view: HyperUpdatePresentation, mandatory: Boolean) {
    val completed = view.status == HyperUpdateStatus.completed
    Column(Modifier.fillMaxWidth()) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp),
            itemVerticalAlignment = Alignment.CenterVertically) {
            if (!completed && view.version != null) HyperText("当前 ${view.version}",
                color = HyperColors.secondaryText, fontSize = 12.sp, lineHeight = 16.8.sp)
            if (view.release != null) {
                if (!completed && view.version != null) HyperIcon(Lucide.ArrowRight, null,
                    Modifier.size(14.dp), color = HyperColors.secondaryText)
                HyperUpdateBadge("${if (completed) "已更新至" else "新版本"} ${view.release.version}", view.color)
            }
            if (completed && view.release == null && view.version != null) HyperText("当前 ${view.version}")
            if (mandatory) HyperUpdateBadge("必要更新", HyperColors.warning)
        }
        val detail = listOfNotNull(view.release?.releaseDate, view.release?.sizeLabel).joinToString(" · ")
        if (detail.isNotBlank()) HyperText(detail, Modifier.padding(top = 8.dp),
            color = HyperColors.secondaryText, fontSize = 12.sp, lineHeight = 16.8.sp)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HyperUpdateTransferDetails(snapshot: HyperUpdateDownloadProgress) {
    val details = listOfNotNull(snapshot.speedLabel, snapshot.remainingLabel)
    if (details.isEmpty()) return
    FlowRow(Modifier.padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)) {
        details.forEach { HyperText(it, color = HyperColors.secondaryText, fontSize = 12.sp, lineHeight = 16.8.sp) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HyperUpdateProgressBadges(installing: Boolean) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HyperUpdateBadge(if (installing) "下载完成" else "1 · 下载更新",
            if (installing) HyperColors.success else HyperColors.accent,
            if (installing) Lucide.Check else Lucide.Download)
        HyperUpdateBadge("2 · 安装更新", if (installing) HyperColors.accent else HyperColors.secondaryText)
    }
}

@Composable
private fun HyperUpdateBadge(label: String, color: Color, icon: ImageVector? = null) {
    Row(Modifier.background(color.copy(alpha = 31 / 255f).compositeOver(hyperGlass.surfaceSubtle),
        RoundedCornerShape(50)).padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        if (icon != null) HyperIcon(icon, null, Modifier.size(13.dp), color = color)
        HyperText(label, color = color, fontSize = 12.sp, lineHeight = 14.4.sp,
            fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

internal fun hyperUpdateHasActions(view: HyperUpdatePresentation): Boolean = view.primary != null || view.secondary != null

@Composable
internal fun HyperUpdateActions(view: HyperUpdatePresentation) {
    Column(Modifier.fillMaxWidth()) {
        if (view.status != HyperUpdateStatus.error) {
            HyperDivider()
            Spacer(Modifier.height(20.dp))
        }
        view.primary?.let { action ->
            HyperButton(action.onPressed, Modifier.fillMaxWidth(), colorStyle = "primary", type = "filled") {
                HyperIcon(action.icon, null)
                HyperText(action.label, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        view.secondary?.let { (label, callback) ->
            if (view.primary != null) Spacer(Modifier.height(8.dp))
            HyperButton(callback, Modifier.fillMaxWidth()) { HyperText(label, maxLines = 1, overflow = TextOverflow.Ellipsis) }
        }
    }
}
