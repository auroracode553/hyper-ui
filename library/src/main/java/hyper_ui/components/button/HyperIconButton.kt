/** 文件职责：在 hyper_ui 中负责提供 library/src/main/java/hyper_ui/components/button/HyperIconButton 可复用界面组件及交互封装。 */
package hyper_ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.Dp
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
    size: Dp = HyperIconButtonDefaults.Size,
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
    val animatedContainerColor by animateColorAsState(
        targetValue = targetContainerColor,
        label = "hyper-icon-button-container"
    )
    val animatedContentColor by animateColorAsState(
        targetValue = targetContentColor,
        label = "hyper-icon-button-content"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (enabled && pressed) HyperIconButtonDefaults.PressedScale else 1f,
        label = "hyper-icon-button-scale"
    )

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .clip(shape)
            .background(animatedContainerColor, shape)
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
    val Size = 48.dp
    val IconSize = 24.dp
    val Shape: Shape = CircleShape
    const val PressedScale = 0.92f

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        pressedContainerColor: Color = Color.Unspecified,
        pressedContentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperIconButtonColors {
        val defaultContainerColor = if (HyperColors.isLight) {
            rgba(0, 0, 0, 0.08f)
        } else {
            rgba(255, 255, 255, 0.16f)
        }
        val defaultContentColor = if (HyperColors.isLight) {
            rgba(28, 28, 30, 1f)
        } else {
            rgba(255, 255, 255, 1f)
        }
        val defaultPressedContainerColor = if (HyperColors.isLight) {
            rgba(0, 0, 0, 0.14f)
        } else {
            rgba(255, 255, 255, 0.24f)
        }
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
        val resolvedDisabledContainerColor = if (disabledContainerColor == Color.Unspecified) {
            resolvedContainerColor.copy(
                alpha = resolvedContainerColor.alpha * HyperStyleDefaults.DisabledAlpha
            )
        } else {
            disabledContainerColor
        }
        val resolvedDisabledContentColor = if (disabledContentColor == Color.Unspecified) {
            resolvedContentColor.copy(alpha = HyperStyleDefaults.DisabledAlpha)
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
}
