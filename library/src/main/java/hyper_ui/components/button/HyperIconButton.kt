/** 文件职责：在 hyper_ui 中负责提供 library/src/main/java/hyper_ui/components/button/HyperIconButton 可复用界面组件及交互封装。 */
package hyper_ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Immutable
data class HyperIconButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val pressedContainerColor: Color,
    val pressedContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

@Composable
fun HyperIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = HyperIconButtonDefaults.Shape,
    colors: HyperIconButtonColors = HyperIconButtonDefaults.colors(),
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val targetContainerColor = when {
        !enabled -> colors.disabledContainerColor
        pressed -> colors.pressedContainerColor
        else -> colors.containerColor
    }
    val targetContentColor = when {
        !enabled -> colors.disabledContentColor
        pressed -> colors.pressedContentColor
        else -> colors.contentColor
    }
    val glassVisuals = HyperIconButtonDefaults.glassVisuals(
        enabled = enabled,
        pressed = pressed
    )
    val pressedScale = if (enabled && pressed) PRESSED_SCALE else 1f
    Box(
        modifier = modifier
            .size(HyperIconButtonDefaults.Size)
            .graphicsLayer {
                scaleX = pressedScale
                scaleY = pressedScale
            }
            .hyperSurfaceDepth(
                shape = shape,
                visuals = glassVisuals.depth
            )
            .clip(shape)
            .hyperIconButtonGlass(
                containerColor = targetContainerColor,
                visuals = glassVisuals
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = contentAlignment
    ) {
        CompositionLocalProvider(LocalContentColor provides targetContentColor) {
            content()
        }
    }
}

object HyperIconButtonDefaults {
    val Size = 38.dp
    val IconSize = 18.dp
    val Shape: Shape = CircleShape

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        pressedContainerColor: Color = Color.Unspecified,
        pressedContentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperIconButtonColors {
        val defaultContainerColor = rgba(
            red = 255,
            green = 255,
            blue = 255,
            alpha = if (HyperColors.isLight) 0.72f else 0.34f
        )
        val defaultContentColor = HyperColors.primaryText
        val defaultPressedContainerColor = rgba(
            red = 255,
            green = 255,
            blue = 255,
            alpha = if (HyperColors.isLight) 0.62f else 0.28f
        )
        val resolvedContainerColor = resolveHyperContainerColor(
            containerColor = containerColor,
            fallbackColor = defaultContainerColor
        )
        val resolvedContentColor = resolveHyperContainerColor(
            containerColor = contentColor,
            fallbackColor = defaultContentColor
        )
        val resolvedPressedContainerColor = resolveHyperContainerColor(
            containerColor = pressedContainerColor,
            fallbackColor = defaultPressedContainerColor
        )
        val resolvedPressedContentColor = resolveHyperContainerColor(
            containerColor = pressedContentColor,
            fallbackColor = resolvedContentColor.copy(alpha = resolvedContentColor.alpha * 0.8f)
        )
        val resolvedDisabledContainerColor = if (disabledContainerColor == Color.Unspecified) {
            rgba(
                red = 255,
                green = 255,
                blue = 255,
                alpha = if (HyperColors.isLight) 0.38f else 0.16f
            )
        } else {
            disabledContainerColor
        }
        val resolvedDisabledContentColor = if (disabledContentColor == Color.Unspecified) {
            HyperColors.disabledText
        } else {
            disabledContentColor
        }
        return HyperIconButtonColors(
            containerColor = resolvedContainerColor,
            contentColor = resolvedContentColor,
            pressedContainerColor = resolvedPressedContainerColor,
            pressedContentColor = resolvedPressedContentColor,
            disabledContainerColor = resolvedDisabledContainerColor,
            disabledContentColor = resolvedDisabledContentColor
        )
    }

    @Composable
    internal fun glassVisuals(
        enabled: Boolean,
        pressed: Boolean
    ): HyperIconButtonGlassVisuals {
        val isLight = HyperColors.isLight
        return when {
            !enabled -> HyperIconButtonGlassVisuals(
                topLightColor = rgba(255, 255, 255, if (isLight) 0.06f else 0.08f),
                edgeLightColor = rgba(255, 255, 255, if (isLight) 0.06f else 0.10f),
                bottomShadeColor = rgba(0, 0, 0, if (isLight) 0.01f else 0.035f),
                depth = hyperSurfaceDepthVisuals(
                    role = HyperSurfaceDepthRole.CompactControl,
                    elevation = 0.dp,
                    state = HyperSurfaceDepthState.Disabled
                )
            )
            pressed -> HyperIconButtonGlassVisuals(
                topLightColor = rgba(255, 255, 255, if (isLight) 0.08f else 0.12f),
                edgeLightColor = rgba(255, 255, 255, if (isLight) 0.08f else 0.16f),
                bottomShadeColor = rgba(0, 0, 0, if (isLight) 0.025f else 0.08f),
                depth = hyperSurfaceDepthVisuals(
                    role = HyperSurfaceDepthRole.CompactControl,
                    elevation = 1.75.dp,
                    state = HyperSurfaceDepthState.Pressed
                )
            )
            else -> HyperIconButtonGlassVisuals(
                topLightColor = rgba(255, 255, 255, if (isLight) 0.12f else 0.18f),
                edgeLightColor = rgba(255, 255, 255, if (isLight) 0.10f else 0.22f),
                bottomShadeColor = rgba(0, 0, 0, if (isLight) 0.018f else 0.07f),
                depth = hyperSurfaceDepthVisuals(
                    role = HyperSurfaceDepthRole.CompactControl,
                    elevation = 6.dp
                )
            )
        }
    }
}

private const val PRESSED_SCALE = 0.97f
