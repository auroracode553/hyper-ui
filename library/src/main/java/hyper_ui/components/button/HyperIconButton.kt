package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable

@Suppress("UNUSED_PARAMETER")
@Composable
fun HyperIconButton(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    enabled: Boolean = true,
    size: Dp = 40.dp,
    iconSize: Dp = 22.dp,
    showBorder: Boolean = true
) {
    val usesDefaultBackground = backgroundColor == Color.Unspecified
    val resolvedBackground = if (usesDefaultBackground) {
        HyperColors.elevatedContainer
    } else {
        backgroundColor
    }
    val contentAlpha = if (enabled) 1f else HyperStyleDefaults.DisabledAlpha
    val resolvedTint = if (tint == Color.Unspecified) {
        HyperColors.primaryText
    } else {
        tint
    }
    val containerColor = if (enabled) {
        resolvedBackground
    } else if (usesDefaultBackground) {
        HyperColors.disabledContainer
    } else {
        resolvedBackground.copy(alpha = resolvedBackground.alpha * contentAlpha)
    }
    val hasVisibleBackground = resolvedBackground.alpha > 0f
    val highlightModifier = if (hasVisibleBackground) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(containerColor)
            .then(highlightModifier)
            .hyperNoRippleClickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = resolvedTint.copy(alpha = resolvedTint.alpha * contentAlpha),
            modifier = Modifier.size(iconSize)
        )
    }
}
