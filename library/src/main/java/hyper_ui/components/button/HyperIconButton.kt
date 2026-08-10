/** 文件职责：在 hyper_ui 中负责提供 library/src/main/java/hyper_ui/components/button/HyperIconButton 可复用界面组件及交互封装。 */
package hyper_ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
    val outlineColor: Color,
    val pressedOutlineColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val disabledOutlineColor: Color
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
    val targetOutlineColor = when {
        !enabled -> colors.disabledOutlineColor
        pressed -> colors.pressedOutlineColor
        else -> colors.outlineColor
    }
    val animatedContainerColor by animateColorAsState(
        targetValue = targetContainerColor,
        label = "hyper-icon-button-container"
    )
    val animatedContentColor by animateColorAsState(
        targetValue = targetContentColor,
        label = "hyper-icon-button-content"
    )
    val animatedOutlineColor by animateColorAsState(
        targetValue = targetOutlineColor,
        label = "hyper-icon-button-outline"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (enabled && pressed) HyperIconButtonDefaults.PressedScale else 1f,
        label = "hyper-icon-button-scale"
    )

    Box(
        modifier = modifier
            .size(HyperIconButtonDefaults.Size)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .clip(shape)
            .background(animatedContainerColor, shape)
            .border(
                width = HyperIconButtonDefaults.OutlineWidth,
                color = animatedOutlineColor,
                shape = shape
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
        CompositionLocalProvider(LocalContentColor provides animatedContentColor) {
            content()
        }
    }
}

object HyperIconButtonDefaults {
    val Size = 40.dp
    val IconSize = 22.dp
    val Shape: Shape = CircleShape
    val OutlineWidth = 1.dp
    const val PressedScale = 0.92f

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        pressedContainerColor: Color = Color.Unspecified,
        pressedContentColor: Color = Color.Unspecified,
        outlineColor: Color = Color.Unspecified,
        pressedOutlineColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        disabledOutlineColor: Color = Color.Unspecified
    ): HyperIconButtonColors {
        val defaultContainerColor = HyperColors.elevatedContainer
        val defaultContentColor = HyperColors.primaryText
        val defaultPressedContainerColor = HyperColors.fieldContainer
        val defaultOutlineColor = HyperColors.fieldBorder
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
            fallbackColor = resolvedContentColor
        )
        val resolvedOutlineColor = resolveHyperContainerColor(
            containerColor = outlineColor,
            fallbackColor = defaultOutlineColor
        )
        val resolvedPressedOutlineColor = resolveHyperContainerColor(
            containerColor = pressedOutlineColor,
            fallbackColor = resolvedOutlineColor
        )
        val resolvedDisabledContainerColor = if (disabledContainerColor == Color.Unspecified) {
            HyperColors.disabledContainer
        } else {
            disabledContainerColor
        }
        val resolvedDisabledContentColor = if (disabledContentColor == Color.Unspecified) {
            HyperColors.disabledText
        } else {
            disabledContentColor
        }
        val resolvedDisabledOutlineColor = if (disabledOutlineColor == Color.Unspecified) {
            HyperColors.divider
        } else {
            disabledOutlineColor
        }

        return HyperIconButtonColors(
            containerColor = resolvedContainerColor,
            contentColor = resolvedContentColor,
            pressedContainerColor = resolvedPressedContainerColor,
            pressedContentColor = resolvedPressedContentColor,
            outlineColor = resolvedOutlineColor,
            pressedOutlineColor = resolvedPressedOutlineColor,
            disabledContainerColor = resolvedDisabledContainerColor,
            disabledContentColor = resolvedDisabledContentColor,
            disabledOutlineColor = resolvedDisabledOutlineColor
        )
    }
}
