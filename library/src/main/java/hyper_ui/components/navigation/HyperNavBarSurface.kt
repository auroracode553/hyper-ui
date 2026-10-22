/** 文件职责：在状态栏顶部至导航底部内渐隐采样与底色，不向正文越界绘制。 */
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
    val color = HyperColors.pageBackground
    Box(modifier) {
        if (progress > 0f) {
            Box(Modifier.matchParentSize()
                // 仅裁切材质层；前景控件及页面布局保持原有尺寸。
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                    clip = true
                }
                .drawWithContent {
                    drawContent()
                    val white = Color(1f, 1f, 1f, 1f)
                    drawRect(Brush.verticalGradient(
                        0f to white.copy(alpha = progress),
                        0.6f to white.copy(alpha = progress),
                        0.78f to white.copy(alpha = 0.8f * progress),
                        0.92f to white.copy(alpha = 0.3f * progress),
                        1f to white.copy(alpha = 0f)
                    ), blendMode = BlendMode.DstIn)
                }
                .hyperBackdropEffect(20.dp * progress)
                .background(Brush.verticalGradient(
                    0f to color.copy(alpha = 0.96f),
                    0.55f to color.copy(alpha = 0.94f),
                    0.78f to color.copy(alpha = 0.74f),
                    1f to color.copy(alpha = 0f)
                )))
        }
        content()
    }
}
