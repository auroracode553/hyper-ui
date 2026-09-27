/** 文件职责：移植 Flutter 浮层的 280ms easeOutCubic，显隐动画可从当前值中断。 */
package hyper_ui

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
internal fun hyperOverlayProgress(visible: Boolean): Float {
    var target by remember { mutableStateOf(false) }
    LaunchedEffect(visible) { target = visible }
    val progress by animateFloatAsState(
        if (target) 1f else 0f,
        tween(280, easing = CubicBezierEasing(0.215f, 0.61f, 0.355f, 1f)),
        label = "HyperOverlay"
    )
    return progress
}

internal fun Modifier.hyperOverlayMotion(progress: Float): Modifier = graphicsLayer {
    alpha = progress
    scaleX = 0.96f + progress * 0.04f
    scaleY = scaleX
}
