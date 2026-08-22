/** 文件职责：绘制 HyperIconButton 的均匀磨砂材质与柔和折射边缘。 */
package hyper_ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Immutable
internal data class HyperIconButtonGlassVisuals(
    val topLightColor: Color,
    val edgeLightColor: Color,
    val bottomShadeColor: Color,
    val elevation: Dp,
    val ambientShadowColor: Color,
    val spotShadowColor: Color
)

/**
 * 用宽而弱的迎光面、底部阴影和渐隐折射带建立磨砂玻璃厚度。
 * 折射带是面内渐变而非描边，因此不会出现硬边或双重同心圆。
 */
internal fun Modifier.hyperIconButtonGlass(
    containerColor: Color,
    visuals: HyperIconButtonGlassVisuals
): Modifier = drawWithCache {
    val faceBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0f to visuals.topLightColor,
            0.32f to visuals.topLightColor.copy(alpha = visuals.topLightColor.alpha * 0.32f),
            0.68f to visuals.bottomShadeColor.copy(alpha = 0f),
            1f to visuals.bottomShadeColor
        )
    )
    val edgeLightBrush = Brush.radialGradient(
        colorStops = arrayOf(
            0f to visuals.edgeLightColor.copy(alpha = 0f),
            0.82f to visuals.edgeLightColor.copy(alpha = 0f),
            0.94f to visuals.edgeLightColor.copy(alpha = visuals.edgeLightColor.alpha * 0.34f),
            1f to visuals.edgeLightColor
        ),
        center = Offset(x = size.width * 0.5f, y = size.height * 0.5f),
        radius = (size.minDimension * 0.5f).coerceAtLeast(1f)
    )
    onDrawWithContent {
        drawRect(color = containerColor)
        drawRect(brush = faceBrush)
        drawRect(brush = edgeLightBrush)
        drawContent()
    }
}
