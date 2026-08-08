package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * 输入框视觉参数。
 *
 * 澎湃OS4 风格：无硬描边，靠玻璃托盘容器色温变化体现聚焦态。
 *
 * - 未聚焦：`HyperColors.elevatedContainer` 半透明玻璃托盘，无描边。
 * - 聚焦：在容器上叠一层主题色覆盖层 [focusOverlayColor]，容器色温融入主题色，无硬描边。
 * - 禁用：容器透明度降至 0.72，内容整体按 DisabledAlpha 淡化。
 */
internal data class HyperInputFieldVisuals(
    val containerColor: Color,
    val focusOverlayColor: Color,
    val contentAlpha: Float
)

/**
 * 计算输入框视觉参数。
 *
 * @param focused 是否聚焦
 * @param enabled 是否可用
 */
@Composable
internal fun hyperInputFieldVisuals(
    focused: Boolean,
    enabled: Boolean
): HyperInputFieldVisuals {
    val contentAlpha = if (enabled) 1f else HyperStyleDefaults.DisabledAlpha
    val containerAlpha = if (enabled) 1f else 0.72f
    val focusedAndEnabled = focused && enabled

    return HyperInputFieldVisuals(
        containerColor = HyperColors.elevatedContainer.copy(alpha = containerAlpha),
        focusOverlayColor = if (focusedAndEnabled) {
            HyperColors.accent.copy(alpha = 0.14f)
        } else {
            Color.Transparent
        },
        contentAlpha = contentAlpha
    )
}
