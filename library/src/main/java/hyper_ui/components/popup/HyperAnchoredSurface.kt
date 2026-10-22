/** 文件职责：Dropdown/Popover 共用的锚点定位、可滚动材质和安全区约束。 */
package hyper_ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties

@Composable
internal fun HyperAnchoredSurface(
    onDismissRequest: () -> Unit,
    width: Dp,
    maxHeight: Dp,
    padding: PaddingValues,
    modifier: Modifier = Modifier,
    anchorBounds: Rect? = null,
    heightFraction: Float? = null,
    visible: Boolean = true,
    scrollable: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val progress = hyperOverlayProgress(visible, durationMillis = HyperMotionSpec.MenuDuration)
    if (!visible && progress == 0f) return
    var origin by remember { mutableStateOf(TransformOrigin(0.5f, 0f)) }
    val density = LocalDensity.current
    val direction = LocalLayoutDirection.current
    val safe = WindowInsets.safeDrawing.exclude(WindowInsets.ime)
    val ime = WindowInsets.ime
    val inset = with(density) { 12.dp.roundToPx() }
    val left = safe.getLeft(density, direction) + inset
    val right = safe.getRight(density, direction) + inset
    val top = safe.getTop(density) + inset
    val bottom = safe.getBottom(density) + ime.getBottom(density) + inset
    val gap = with(density) { 6.dp.roundToPx() }
    Popup(
        popupPositionProvider = HyperAnchoredPositionProvider(left, top, right, bottom, gap, anchorBounds) { origin = it },
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = visible)
    ) {
        BoxWithConstraints {
            val availableWidth = (maxWidth - with(density) { (left + right).toDp() }).coerceAtLeast(0.dp)
            val viewportHeight = this.maxHeight
            val anchorHeight = anchorBounds?.let { bounds ->
                with(density) {
                    maxOf(bounds.top - top - gap,
                        viewportHeight.toPx() - bottom - bounds.bottom - gap).coerceAtLeast(0f).toDp()
                }
            } ?: (viewportHeight - with(density) { (top + bottom).toDp() }).coerceAtLeast(0.dp)
            val availableHeight = minOf(maxHeight, anchorHeight, heightFraction?.let { viewportHeight * it } ?: viewportHeight)
            Column(modifier.graphicsLayer {
                alpha = progress
                scaleX = 0.96f + 0.04f * progress
                scaleY = scaleX
                transformOrigin = origin
            }.width(width.coerceAtMost(availableWidth)).heightIn(max = availableHeight)
                .hyperFrostedSurface(hyperGlass.surfaceStrong, RoundedCornerShape(18.dp), blur = hyperGlass.blurStrong)
                .padding(padding)
                .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier)) {
                CompositionLocalProvider(LocalHyperSurfacePresent provides true) { content() }
            }
        }
    }
}

private class HyperAnchoredPositionProvider(
    val left: Int, val top: Int, val right: Int, val bottom: Int, val gap: Int,
    val measuredAnchor: Rect?, val onPositioned: (TransformOrigin) -> Unit
) : PopupPositionProvider {
    override fun calculatePosition(anchorBounds: IntRect, windowSize: IntSize,
        layoutDirection: LayoutDirection, popupContentSize: IntSize): IntOffset {
        val maxX = (windowSize.width - right - popupContentSize.width).coerceAtLeast(left)
        val maxY = (windowSize.height - bottom - popupContentSize.height).coerceAtLeast(top)
        val anchor = measuredAnchor?.let { IntRect(it.left.toInt(), it.top.toInt(), it.right.toInt(), it.bottom.toInt()) } ?: anchorBounds
        val below = anchor.bottom + gap
        val y = if (below + popupContentSize.height <= windowSize.height - bottom) below
            else anchor.top - gap - popupContentSize.height
        val x = anchor.left.coerceIn(left, maxX)
        onPositioned(TransformOrigin(if (popupContentSize.width > 0) ((anchor.left + anchor.right) / 2f - x).div(popupContentSize.width).coerceIn(0f, 1f) else 0.5f,
            if (y == below) 0f else 1f))
        return IntOffset(x, y.coerceIn(top, maxY))
    }
}
