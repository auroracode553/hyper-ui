/** 文件职责：供导航与自定义覆盖区复用滚动渐变；不采样背景，不增加栏高或安全区。 */
package hyper_ui

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

/** 默认读取已有主滚动作用域；外部宿主可传 scrolledUnder，前景内容保持清晰。 */
@Composable
fun HyperScrollGradient(
    scrolledUnder: Boolean = false,
    modifier: Modifier = Modifier,
    child: @Composable BoxScope.() -> Unit = {}
) {
    val progress by animateFloatAsState(
        targetValue = if (scrolledUnder || LocalHyperNavBarScrolledUnder.current?.value == true) 1f else 0f,
        animationSpec = tween(160, easing = CubicBezierEasing(0.215f, 0.61f, 0.355f, 1f)),
        label = "scrollGradient"
    )
    val color = HyperColors.pageBackground
    Box(modifier) {
        if (progress > 0f) {
            Box(Modifier.matchParentSize().background(Brush.verticalGradient(
                0f to color.copy(alpha = 0.96f * progress),
                0.55f to color.copy(alpha = 0.94f * progress),
                0.78f to color.copy(alpha = 0.74f * progress),
                1f to color.copy(alpha = 0f)
            )))
        }
        child()
    }
}
