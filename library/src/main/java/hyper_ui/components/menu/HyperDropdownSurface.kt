/** 文件职责：绘制 HyperDropdown 参考图风格的柔雾面板，并集中维护主题视觉参数。 */
package hyper_ui

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
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
internal data class HyperDropdownSurfaceVisuals(
    val topLightColor: Color,
    val bottomShadeColor: Color,
    val edgeColor: Color,
    val edgeWidth: Dp,
    val elevation: Dp,
    val ambientShadowColor: Color,
    val spotShadowColor: Color
)

/**
 * 面板保持接近纯色，仅用极弱纵向明暗、细软边缘和单层阴影建立浮层层级。
 * 该材质刻意避免强高光与多重折射，视觉以 UI 参考图为准。
 */
internal fun Modifier.hyperDropdownSurface(
    shape: Shape,
    containerColor: Color,
    visuals: HyperDropdownSurfaceVisuals
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
                0.42f to visuals.topLightColor.copy(alpha = 0f),
                0.68f to visuals.bottomShadeColor.copy(alpha = 0f),
                1f to visuals.bottomShadeColor
            )
        )

        onDrawWithContent {
            drawRect(color = containerColor)
            drawRect(brush = faceBrush)
            drawContent()
        }
    }
    .border(
        width = visuals.edgeWidth,
        color = visuals.edgeColor,
        shape = shape
    )

@Composable
internal fun hyperDropdownSurfaceVisuals(): HyperDropdownSurfaceVisuals {
    val isLight = HyperColors.isLight
    return HyperDropdownSurfaceVisuals(
        topLightColor = if (isLight) {
            Color(1f, 1f, 1f, 0.05f)
        } else {
            Color(1f, 1f, 1f, 0.035f)
        },
        bottomShadeColor = if (isLight) {
            Color(0f, 0f, 0f, 0.02f)
        } else {
            Color(0f, 0f, 0f, 0.035f)
        },
        edgeColor = if (isLight) {
            Color(0f, 0f, 0f, 0.065f)
        } else {
            Color(1f, 1f, 1f, 0.14f)
        },
        edgeWidth = 1.dp,
        elevation = HyperDropdownDefaults.Elevation,
        ambientShadowColor = if (isLight) {
            Color(0f, 0f, 0f, 0.075f)
        } else {
            Color(0f, 0f, 0f, 0.14f)
        },
        spotShadowColor = if (isLight) {
            Color(0f, 0f, 0f, 0.15f)
        } else {
            Color(0f, 0f, 0f, 0.24f)
        }
    )
}

@Composable
internal fun resolveHyperDropdownColors(colors: HyperDropdownColors): HyperDropdownColors {
    val defaults = HyperDropdownDefaults.colors()
    return HyperDropdownColors(
        containerColor = resolveHyperContainerColor(colors.containerColor, defaults.containerColor),
        contentColor = resolveHyperContainerColor(colors.contentColor, defaults.contentColor),
        dangerContentColor = resolveHyperContainerColor(
            colors.dangerContentColor,
            defaults.dangerContentColor
        ),
        disabledContentColor = resolveHyperContainerColor(
            colors.disabledContentColor,
            defaults.disabledContentColor
        ),
        pressedContainerColor = resolveHyperContainerColor(
            colors.pressedContainerColor,
            defaults.pressedContainerColor
        ),
        dividerColor = resolveHyperContainerColor(colors.dividerColor, defaults.dividerColor)
    )
}
