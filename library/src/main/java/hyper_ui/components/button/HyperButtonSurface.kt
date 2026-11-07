/** 文件职责：将 Button 的配色与禁用态适配到共用玻璃表面，保持尺寸和自定义覆盖。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

/**
 * 将颜色适配到共用按钮玻璃，返回保留布局的材质修饰符。
 * hasTone 表示显式指定语义配色；无配色 filled 与 icon 使用相同默认材质。
 * defaultContainer 为 false 时保留调用方 colors 的背景覆盖。
 */
internal fun Modifier.hyperButtonSurface(
    containerColor: Color,
    shape: Shape,
    type: String?,
    enabled: Boolean,
    border: BorderStroke?,
    defaultContainer: Boolean,
    hasTone: Boolean,
    progress: () -> Float
): Modifier {
    if (!enabled) return clip(shape).background(containerColor)
        .then(if (border != null) Modifier.border(border, shape) else Modifier)
    val tint = when {
        !defaultContainer -> containerColor
        type == "filled" && hasTone -> containerColor.copy(alpha = containerColor.alpha * 0.9f)
        else -> Color.Unspecified
    }
    return hyperControlSurface(shape, HyperControlSurfaceRole.Button,
        progress = progress, containerColor = tint, border = border)
}
