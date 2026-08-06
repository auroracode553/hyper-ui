package hyper_ui.core.interaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role

/**
 * 无水波纹点击修饰符。
 *
 * 用于需要保留 clickable 语义、禁用态和 role，但不显示 Material ripple 的轻量组件。
 */
internal fun Modifier.hyperNoRippleClickable(
    enabled: Boolean = true,
    role: Role? = null,
    onClick: () -> Unit
): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
        role = role,
        onClick = onClick
    )
}
