/** 文件职责：提供轻量提示浮层。 */
package hyper_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.focusable
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.unit.sp
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
import androidx.compose.ui.platform.LocalLayoutDirection
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
    message: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    hoverDelay: Long = 500L,
    maxWidth: Dp = 240.dp,
    child: @Composable () -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    var pointerPressed by remember { mutableStateOf(false) }
    var hovered by remember { mutableStateOf(false) }
    var focused by remember { mutableStateOf(false) }
    require(hoverDelay >= 0 && maxWidth > 0.dp) { "延迟不能为负，maxWidth 必须大于 0.dp" }
    LaunchedEffect(pointerPressed, hovered, focused, enabled) {
        visible = false
        if (enabled && (pointerPressed || hovered || focused)) {
            if (!focused) delay(if (pointerPressed) 500L else hoverDelay)
            visible = true
        }
    }
    var anchorBounds by remember { mutableStateOf<Rect?>(null) }
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .onGloballyPositioned { anchorBounds = it.boundsInWindow() }
            .onFocusChanged { focused = it.hasFocus }
            .focusable(enabled)
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        pointerPressed = event.changes.any { it.pressed }
                        when (event.type) {
                            PointerEventType.Enter -> hovered = true
                            PointerEventType.Exit -> { hovered = false; pointerPressed = false }
                            else -> Unit
                        }
                    }
                }
            },
    ) {
        child()
    }

    val progress = if (focused && visible) 1f else hyperOverlayProgress(visible, durationMillis = HyperMotionSpec.TooltipDuration)
    val bounds = anchorBounds
    if (enabled && (visible || progress > 0f) && message.isNotBlank() && bounds != null) {
        val safe = hyperWindowSafeAreaInsets()
        val direction = LocalLayoutDirection.current
        val margin = with(density) { 12.dp.roundToPx() }
        val left = safe.getLeft(density, direction) + margin
        val top = safe.getTop(density) + margin
        val right = safe.getRight(density, direction) + margin
        val bottom = maxOf(safe.getBottom(density), WindowInsets.ime.getBottom(density)) + margin
        val tooltipMaxWidth = maxWidth
        Popup(
            popupPositionProvider = HyperTooltipPositionProvider(
                bounds, with(density) { 8.dp.roundToPx() }, left, top, right, bottom),
            onDismissRequest = { visible = false },
            properties = PopupProperties(focusable = false),
        ) {
            BoxWithConstraints {
                val availableWidth = (this.maxWidth - with(density) { (left + right).toDp() }).coerceAtLeast(0.dp)
                val availableHeight = (this.maxHeight - with(density) { (top + bottom).toDp() }).coerceAtLeast(0.dp)
                HyperText(
                    text = message,
                    modifier = Modifier
                        .hyperOverlayMotion(progress, beginScale = 0.97f)
                        .widthIn(max = tooltipMaxWidth.coerceAtMost(availableWidth))
                        .heightIn(max = availableHeight)
                        .hyperFrostedSurface(hyperSurfaces.surfaceStrong, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    color = HyperColors.primaryText,
                    style = HyperTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 15.6.sp),
                )
            }
        }
    }
}

private class HyperTooltipPositionProvider(
    private val anchor: Rect,
    private val gap: Int,
    /// 提示可用区域的左侧边界，包含横向系统区和外间距。
    private val left: Int,
    /// 提示可用区域的顶部边界，包含状态栏和挖孔。
    private val top: Int,
    /// 右侧系统区和外间距。
    private val right: Int,
    /// 底部系统区、键盘和外间距的合并值。
    private val bottom: Int,
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
        val y = if (above >= top) above else below
        return IntOffset(
            centerX.coerceIn(left, (windowSize.width - popupContentSize.width - right).coerceAtLeast(left)),
            y.coerceIn(top, (windowSize.height - popupContentSize.height - bottom).coerceAtLeast(top)),
        )
    }
}
