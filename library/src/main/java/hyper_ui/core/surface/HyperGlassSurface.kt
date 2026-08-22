/** 文件职责：为结构面板、浮层和紧凑反馈组件提供统一的连续磨砂玻璃材质。 */
package hyper_ui

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
internal data class HyperGlassSurfaceVisuals(
    val containerColor: Color,
    val topLightColor: Color,
    val bottomShadeColor: Color,
    val elevation: Dp,
    val ambientShadowColor: Color,
    val spotShadowColor: Color
)

/**
 * 以单一连续表面绘制玻璃：容器色负责透光，上方宽柔光与底部弱阴影负责厚度。
 * 这里不绘制边框，避免在圆角或胶囊表面形成塑料感的双重轮廓。
 */
internal fun Modifier.hyperGlassSurface(
    shape: Shape,
    visuals: HyperGlassSurfaceVisuals
): Modifier = then(
    if (visuals.elevation > 0.dp) {
        Modifier.shadow(
            elevation = visuals.elevation,
            shape = shape,
            clip = false,
            ambientColor = visuals.ambientShadowColor,
            spotColor = visuals.spotShadowColor
        )
    } else {
        Modifier
    }
)
    .clip(shape)
    .drawWithCache {
        val faceBrush = Brush.verticalGradient(
            colorStops = arrayOf(
                0f to visuals.topLightColor,
                0.30f to visuals.topLightColor.copy(
                    alpha = visuals.topLightColor.alpha * 0.28f
                ),
                0.70f to visuals.bottomShadeColor.copy(alpha = 0f),
                1f to visuals.bottomShadeColor
            )
        )
        onDrawWithContent {
            drawRect(color = visuals.containerColor)
            drawRect(brush = faceBrush)
            drawContent()
        }
    }

/** 默认玻璃的光源与单层柔和阴影；组件只需要决定透明度和抬升高度。 */
internal fun hyperGlassSurfaceVisuals(
    containerColor: Color,
    elevation: Dp,
    topLightAlpha: Float,
    bottomShadeAlpha: Float,
    shadowAlpha: Float
): HyperGlassSurfaceVisuals = HyperGlassSurfaceVisuals(
    containerColor = containerColor,
    topLightColor = Color(1f, 1f, 1f, topLightAlpha.coerceIn(0f, 1f)),
    bottomShadeColor = Color(0f, 0f, 0f, bottomShadeAlpha.coerceIn(0f, 1f)),
    elevation = elevation,
    ambientShadowColor = Color(0f, 0f, 0f, (shadowAlpha * 0.72f).coerceIn(0f, 1f)),
    spotShadowColor = Color(0f, 0f, 0f, shadowAlpha.coerceIn(0f, 1f))
)
