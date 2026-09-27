/** 文件职责：移植 HyButton 的迎光渐变、品牌色阴影及单层轮廓。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

internal fun Modifier.hyperButtonSurface(
    containerColor: Color,
    shape: Shape,
    variant: HyperButtonVariant,
    enabled: Boolean,
    border: BorderStroke?
): Modifier {
    val filled = enabled && (variant == HyperButtonVariant.Filled || variant == HyperButtonVariant.Danger)
    return then(if (filled) Modifier.dropShadow(shape, Shadow(
        radius = 16.dp, spread = (-5).dp, offset = DpOffset(0.dp, 7.dp),
        color = containerColor.copy(alpha = 50 / 255f)
    )) else Modifier)
        .clip(shape)
        .then(if (filled) Modifier.background(Brush.verticalGradient(listOf(
            lerp(containerColor, Color(1f, 1f, 1f, 1f), 0.1f), containerColor
        ))) else Modifier.background(containerColor))
        .then(if (border != null) Modifier.border(border, shape) else Modifier)
}
