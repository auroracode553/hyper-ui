package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable

@Immutable
data class HyperIconButtonColors(
    val containerColor: Color,
    val contentColor: Color,
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
    border: BorderStroke? = null,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    val containerColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor

    Box(
        modifier = modifier
            .size(size)
            .hyperGlassSurface(
                containerColor = containerColor,
                shape = shape,
                border = border
            )
            .hyperNoRippleClickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = contentAlignment
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}

object HyperIconButtonDefaults {
    val Size = 40.dp
    val IconSize = 22.dp
    val Shape: Shape = CircleShape

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperIconButtonColors {
        val resolvedContainerColor = resolveHyperContainerColor(
            containerColor = containerColor,
            fallbackColor = HyperColors.elevatedContainer
        )
        val resolvedContentColor = resolveHyperContainerColor(
            containerColor = contentColor,
            fallbackColor = HyperColors.primaryText
        )
        val resolvedDisabledContainerColor = if (disabledContainerColor == Color.Unspecified) {
            resolveHyperDisabledContainerColor(
                containerColor = resolvedContainerColor,
                usesDefaultContainerColor = containerColor == Color.Unspecified,
                fallbackDisabledColor = HyperColors.disabledContainer
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
            disabledContainerColor = resolvedDisabledContainerColor,
            disabledContentColor = resolvedDisabledContentColor
        )
    }
}
