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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
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
        Box(Modifier.fillMaxSize().background(hyperSurfaces.scrim.copy(alpha = hyperSurfaces.scrim.alpha * transition))) {
            // 对齐 Flutter show 的 SafeArea + 20dp 外间距，避免通用 Dialog 的尺寸约定介入。
            Box(Modifier.fillMaxSize().windowInsetsPadding(hyperWindowSafeAreaInsets()).imePadding().padding(20.dp),
                contentAlignment = Alignment.Center) {
                HyperUpdatePanel(view, locked, mandatory, onClose, maxWidth,
                    modifier.hyperOverlayMotion(transition, beginScale = 0.96f))
            }
        }
    }
}

/** 模态和内嵌面板共用窗口高度上限，正文独立滚动，操作区保持固定。 */
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
    val density = LocalDensity.current
    val safe = hyperWindowSafeAreaInsets()
    val windowHeight = with(density) { LocalWindowInfo.current.containerSize.height.toDp() }
    val safeHeight = with(density) {
        (safe.getTop(this) + maxOf(safe.getBottom(this), WindowInsets.ime.getBottom(this))).toDp()
    }
    // 即使调用方提供无限高度，也只使用窗口安全区内扣除既有 20dp 外间距的空间。
    val panelMaxHeight = (windowHeight - safeHeight - 40.dp).coerceAtLeast(0.dp)
    CompositionLocalProvider(
        LocalHyperSurfacePresent provides true,
        LocalHyperContentColor provides HyperColors.primaryText,
        // 对齐 Flutter 14/1.4 默认文字；小字号在正文中显式按 1.4 换算行高。
        LocalHyperTextStyle provides HyperTheme.typography.bodyMedium.copy(
            fontSize = 14.sp, lineHeight = 19.6.sp, fontWeight = FontWeight.Normal, letterSpacing = 0.sp)
    ) {
        Box(modifier.widthIn(max = maxWidth).heightIn(max = panelMaxHeight).fillMaxWidth()) {
            Column(Modifier.fillMaxWidth().hyperControlSurface(shape, HyperControlSurfaceRole.Dialog)) {
                val bodyModifier = Modifier.weight(1f, fill = false).verticalScroll(scrollState)
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
