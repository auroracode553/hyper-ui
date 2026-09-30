/** 文件职责：在 hyper_ui 中负责提供 library/src/main/java/hyper_ui/components/button/HyperIconButton 可复用界面组件及交互封装。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import hyper_ui.core.interaction.hyperNoRippleClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
    size: String = "default",
    shape: Shape = HyperIconButtonDefaults.Shape,
    colors: HyperIconButtonColors = HyperIconButtonDefaults.colors(),
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    val resolvedSize = hyperComponentSize(size, small = 32.dp, normal = HyperIconButtonDefaults.Size, large = 56.dp)
    val interactionSource = remember { MutableInteractionSource() }
    val interactionPressed by interactionSource.collectIsPressedAsState()
    var immediatePressed by remember { mutableStateOf(false) }
    val pressed = enabled && (immediatePressed || interactionPressed)
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
    Box(
        modifier = modifier
            .size(resolvedSize)
            .hyperNoRippleClickable(
                interactionSource = interactionSource,
                enabled = enabled,
                role = Role.Button,
                onPressChanged = { immediatePressed = it },
                onClick = onClick
            )
            .clip(shape)
            .hyperBackdropEffect(14.dp)
            .background(targetContainerColor)
            .border(1.dp, if (enabled) hyperGlass.edgeHighlight else HyperColors.divider.copy(alpha = 110 / 255f), shape),
        contentAlignment = contentAlignment
    ) {
        CompositionLocalProvider(LocalHyperContentColor provides targetContentColor) {
            content()
        }
    }
}

object HyperIconButtonDefaults {
    val Size = 36.dp
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
        val defaultContainerColor = hyperGlass.surface
        val defaultContentColor = HyperColors.primaryText
        val defaultPressedContainerColor = defaultContainerColor
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
        val resolvedDisabledContainerColor = resolveHyperContainerColor(disabledContainerColor, hyperGlass.controlTrack)
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

}
