/** 文件职责：导航背景向正文渐隐，蒙版同时淡出采样与底色。 */
package hyper_ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp

@Composable
internal fun HyperNavBarScrollSurface(
    scrolledUnder: Boolean,
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    val progress by animateFloatAsState(
        targetValue = if (scrolledUnder) 1f else 0f,
        animationSpec = tween(160, easing = CubicBezierEasing(0.215f, 0.61f, 0.355f, 1f)),
        label = "navBarScrollSurface"
    )
    val color = HyperColors.cardContainer
    Box(modifier) {
        if (progress > 0f) {
            Box(Modifier.matchParentSize()
                // 只扩展背景绘制区域，不为正文占位，也不拦截手势。
                .layout { measurable, constraints ->
                    val extension = 56.dp.roundToPx()
                    val layer = measurable.measure(constraints.copy(
                        minHeight = constraints.minHeight + extension,
                        maxHeight = constraints.maxHeight + extension
                    ))
                    layout(constraints.maxWidth, constraints.maxHeight) { layer.place(0, 0) }
                }
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    drawContent()
                    val white = Color(1f, 1f, 1f, 1f)
                    drawRect(Brush.verticalGradient(
                        0f to white.copy(alpha = progress),
                        0.45f to white.copy(alpha = progress),
                        0.68f to white.copy(alpha = 0.8f * progress),
                        0.86f to white.copy(alpha = 0.3f * progress),
                        1f to white.copy(alpha = 0f)
                    ), blendMode = BlendMode.DstIn)
                }
                .hyperBackdropEffect(20.dp * progress)
                .background(Brush.verticalGradient(
                    0f to color.copy(alpha = 0.96f),
                    0.4f to color.copy(alpha = 0.94f),
                    0.66f to color.copy(alpha = 0.74f),
                    1f to color.copy(alpha = 0f)
                )))
        }
        content()
    }
}
