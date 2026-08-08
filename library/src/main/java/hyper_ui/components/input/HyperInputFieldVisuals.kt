package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal data class HyperInputFieldVisuals(
    val containerColor: Color,
    val focusOverlayColor: Color,
    val outlineColor: Color,
    val outlineWidth: Dp,
    val contentAlpha: Float
)

@Composable
internal fun hyperInputFieldVisuals(
    focused: Boolean,
    enabled: Boolean
): HyperInputFieldVisuals {
    val contentAlpha = if (enabled) 1f else HyperStyleDefaults.DisabledAlpha
    val containerAlpha = if (enabled) 1f else 0.72f
    val focusedAndEnabled = focused && enabled

    return HyperInputFieldVisuals(
        containerColor = HyperColors.elevatedContainer.copy(alpha = containerAlpha),
        focusOverlayColor = if (focusedAndEnabled) {
            HyperColors.accent.copy(alpha = 0.08f)
        } else {
            Color.Transparent
        },
        outlineColor = if (focusedAndEnabled) {
            HyperColors.accent.copy(alpha = 0.42f)
        } else {
            HyperColors.fieldBorder.copy(alpha = containerAlpha)
        },
        outlineWidth = if (focusedAndEnabled) 1.5.dp else 1.dp,
        contentAlpha = contentAlpha
    )
}
