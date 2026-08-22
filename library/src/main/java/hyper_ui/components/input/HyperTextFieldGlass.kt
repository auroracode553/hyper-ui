/** 文件职责：绘制 HyperTextField 的结构性磨砂玻璃材质和单一语义边缘。 */
package hyper_ui

import androidx.compose.foundation.border
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
internal data class HyperTextFieldGlassVisuals(
    val containerColor: Color,
    val topLightColor: Color,
    val bottomShadeColor: Color,
    val indicatorColor: Color,
    val elevation: Dp,
    val ambientShadowColor: Color,
    val spotShadowColor: Color
) {
    companion object {
        val RestingElevation = 2.dp
        val FocusedElevation = 3.dp
        val DisabledElevation = 0.dp
    }
}

/** 普通态无描边；聚焦或错误时只绘制一条渐变语义边缘。 */
internal fun Modifier.hyperTextFieldGlass(
    shape: Shape,
    visuals: HyperTextFieldGlassVisuals
): Modifier {
    val material = shadow(
        elevation = visuals.elevation,
        shape = shape,
        clip = false,
        ambientColor = visuals.ambientShadowColor,
        spotColor = visuals.spotShadowColor
    )
        .clip(shape)
        .drawWithCache {
            val faceBrush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0f to visuals.topLightColor,
                    0.30f to visuals.topLightColor.copy(alpha = visuals.topLightColor.alpha * 0.30f),
                    0.72f to visuals.bottomShadeColor.copy(alpha = 0f),
                    1f to visuals.bottomShadeColor
                )
            )
            onDrawWithContent {
                drawRect(color = visuals.containerColor)
                drawRect(brush = faceBrush)
                drawContent()
            }
        }

    if (visuals.indicatorColor.alpha <= 0f) return material

    val indicatorBrush = Brush.verticalGradient(
        colors = listOf(
            visuals.indicatorColor,
            visuals.indicatorColor.copy(alpha = visuals.indicatorColor.alpha * 0.55f),
            visuals.indicatorColor.copy(alpha = visuals.indicatorColor.alpha * 0.78f)
        )
    )
    return material.border(
        width = StateIndicatorWidth,
        brush = indicatorBrush,
        shape = shape
    )
}

private val StateIndicatorWidth = 1.dp
