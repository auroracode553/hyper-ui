package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@PublishedApi
internal fun resolveHyperContainerColor(
    containerColor: Color,
    fallbackColor: Color
): Color = if (containerColor == Color.Unspecified) fallbackColor else containerColor

@PublishedApi
internal fun resolveHyperDisabledContainerColor(
    containerColor: Color,
    usesDefaultContainerColor: Boolean,
    fallbackDisabledColor: Color,
    disabledAlpha: Float = HyperStyleDefaults.DisabledAlpha
): Color = if (usesDefaultContainerColor) {
    fallbackDisabledColor
} else {
    containerColor.copy(alpha = containerColor.alpha * disabledAlpha)
}

@Composable
@PublishedApi
internal fun Modifier.hyperGlassSurface(
    containerColor: Color,
    shape: Shape,
    elevation: Dp = 0.dp,
    border: BorderStroke? = null,
    clipContent: Boolean = true
): Modifier {
    val highlightModifier = if (containerColor.alpha > 0f) {
        Modifier.background(
            brush = HyperColors.glassHighlightBrush,
            shape = shape
        )
    } else {
        Modifier
    }

    return this
        .then(
            if (elevation > 0.dp) {
                Modifier.shadow(elevation = elevation, shape = shape, clip = false)
            } else {
                Modifier
            }
        )
        .then(if (clipContent) Modifier.clip(shape) else Modifier)
        .background(color = containerColor, shape = shape)
        .then(highlightModifier)
        .then(if (border != null) Modifier.border(border, shape) else Modifier)
}
