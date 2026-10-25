/** 文件职责：共用 Flutter 更新面板布局，隔离模态宿主与 Preview 内嵌展示。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Flutter Widget 可以内嵌，show 提供模态宿主；Preview 注入宿主方式，共用同一份面板。
internal val LocalHyperUpdateDialogInlinePreview = staticCompositionLocalOf { false }

@Composable
internal fun HyperUpdateLayout(
    view: HyperUpdatePresentation,
    locked: Boolean,
    mandatory: Boolean,
    onClose: (() -> Unit)?,
    maxWidth: Dp,
    modifier: Modifier
) {
    if (LocalHyperUpdateDialogInlinePreview.current) {
        HyperUpdatePanel(view, locked, mandatory, onClose, maxWidth, modifier)
        return
    }
    val transition = hyperOverlayProgress(true)
    HyperDialogHost(onDismissRequest = { if (!locked) onClose?.invoke() }, dismissOnBackPress = !locked) {
        Box(Modifier.fillMaxSize().background(hyperGlass.scrim.copy(alpha = hyperGlass.scrim.alpha * transition))) {
            // 对齐 Flutter show 的 SafeArea + 20dp 外间距，避免通用 Dialog 的尺寸约定介入。
            Box(Modifier.fillMaxSize().windowInsetsPadding(HyperTheme.systemSafeAreaInsets).padding(20.dp),
                contentAlignment = Alignment.Center) {
                HyperUpdatePanel(view, locked, mandatory, onClose, maxWidth,
                    modifier.hyperOverlayMotion(transition, beginScale = 0.96f))
            }
        }
    }
}

@Composable
private fun HyperUpdatePanel(
    view: HyperUpdatePresentation,
    locked: Boolean,
    mandatory: Boolean,
    onClose: (() -> Unit)?,
    maxWidth: Dp,
    modifier: Modifier
) {
    val shape = RoundedCornerShape(28.dp)
    val scrollState = rememberScrollState()
    CompositionLocalProvider(
        LocalHyperSurfacePresent provides true,
        LocalHyperContentColor provides HyperColors.primaryText,
        // 对齐 Flutter 14/1.4 默认文字；小字号在正文中显式按 1.4 换算行高。
        LocalHyperTextStyle provides HyperTheme.typography.bodyMedium.copy(
            fontSize = 14.sp, lineHeight = 19.6.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
    ) {
        BoxWithConstraints(modifier.widthIn(max = maxWidth).fillMaxWidth()) {
            val boundedHeight = constraints.hasBoundedHeight
            Column(Modifier.fillMaxWidth().hyperFrostedSurface(
                containerColor = hyperGlass.surfaceStrong, shape = shape, blur = hyperGlass.blurStrong)) {
                val bodyModifier = if (boundedHeight) {
                    Modifier.weight(1f, fill = false).verticalScroll(scrollState)
                } else Modifier // 内嵌示例由 Preview 外层滚动，不能在无限高度下再套滚动容器。
                Box(bodyModifier.fillMaxWidth().padding(24.dp)) {
                    HyperUpdateBody(view, locked, mandatory, onClose)
                }
                if (hyperUpdateHasActions(view)) {
                    Box(Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, bottom = 24.dp)) {
                        HyperUpdateActions(view)
                    }
                }
            }
        }
    }
}
