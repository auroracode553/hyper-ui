/** 文件职责：所有轻量点击控件共享 HyPressable 的按压反馈与键盘语义。 */
package hyper_ui.core.interaction

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role

internal fun Modifier.hyperNoRippleClickable(
    enabled: Boolean = true,
    role: Role? = null,
    interactionSource: MutableInteractionSource? = null,
    onClick: () -> Unit
): Modifier = composed {
    val source = interactionSource ?: remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val progress by animateFloatAsState(
        targetValue = if (enabled && pressed) 1f else 0f,
        animationSpec = tween(if (pressed) 85 else 180, easing = CubicBezierEasing(0.215f, 0.61f, 0.355f, 1f)),
        label = "HyperPress"
    )
    Modifier.graphicsLayer {
        scaleX = 1f - 0.025f * progress
        scaleY = scaleX
        alpha = 1f - 0.08f * progress
    }.clickable(interactionSource = source, indication = null, enabled = enabled, role = role, onClick = onClick)
}
