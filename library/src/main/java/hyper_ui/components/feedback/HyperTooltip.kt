/** 文件职责：提供轻量提示浮层。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties

/**
 * 为任意锚点提供 HyperOS 风格提示。
 *
 * 提示由指针进入锚点触发，点击和子组件的点击事件不会被占用。
 */
@Composable
fun HyperTooltip(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    var anchorBounds by remember { mutableStateOf<Rect?>(null) }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .onGloballyPositioned { anchorBounds = it.boundsInWindow() }
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Enter -> visible = true
                            PointerEventType.Exit -> visible = false
                            PointerEventType.Release -> visible = false
                            else -> Unit
                        }
                    }
                }
            },
    ) {
        content()
    }

    val bounds = anchorBounds
    if (enabled && visible && text.isNotBlank() && bounds != null) {
        Popup(
            popupPositionProvider = HyperTooltipPositionProvider(bounds, with(density) { 8.dp.roundToPx() }),
            onDismissRequest = { visible = false },
            properties = PopupProperties(focusable = false),
        ) {
            HyperText(
                text = text,
                modifier = Modifier
                    .background(HyperColors.primaryText, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                color = HyperColors.pageBackground,
                style = HyperTheme.typography.labelMedium,
            )
        }
    }
}

private class HyperTooltipPositionProvider(
    private val anchor: Rect,
    private val gap: Int,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val centerX = anchor.center.x.toInt() - popupContentSize.width / 2
        val above = anchor.top.toInt() - popupContentSize.height - gap
        val below = anchor.bottom.toInt() + gap
        val y = if (above >= 0) above else below
        return IntOffset(
            centerX.coerceIn(0, (windowSize.width - popupContentSize.width).coerceAtLeast(0)),
            y.coerceIn(0, (windowSize.height - popupContentSize.height).coerceAtLeast(0)),
        )
    }
}
