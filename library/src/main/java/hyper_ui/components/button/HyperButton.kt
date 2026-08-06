package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable

enum class HyperButtonVariant {
    Default,
    Primary,
    Success,
    Info,
    Warning,
    Danger
}

@Composable
fun HyperButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: HyperButtonVariant = HyperButtonVariant.Primary,
    minHeight: Dp = 40.dp,
    horizontalPadding: Dp = 14.dp,
    fontSize: TextUnit = 15.sp,
    verticalPadding: Dp = 8.dp,
    defaultBorderColor: Color = Color.Unspecified,
    leadingIcon: (@Composable RowScope.() -> Unit)? = null,
    trailingIcon: (@Composable RowScope.() -> Unit)? = null
) {
    val shape = RoundedCornerShape(HyperStyleDefaults.LargeCornerRadius)
    val enabledAlpha = if (enabled) 1f else HyperStyleDefaults.DisabledAlpha
    val containerColor = when (variant) {
        HyperButtonVariant.Default -> Color.Transparent
        HyperButtonVariant.Primary -> HyperColors.accent
        HyperButtonVariant.Success -> HyperColors.success
        HyperButtonVariant.Info -> rgba(144, 147, 153, 1f)
        HyperButtonVariant.Warning -> rgba(230, 162, 60, 1f)
        HyperButtonVariant.Danger -> MaterialTheme.colorScheme.error
    }
    val resolvedDefaultBorderColor = if (defaultBorderColor == Color.Unspecified) {
        HyperColors.accent.copy(alpha = 0.50f)
    } else {
        defaultBorderColor
    }
    val borderStroke = when (variant) {
        HyperButtonVariant.Default -> BorderStroke(
            width = 1.dp,
            color = resolvedDefaultBorderColor.copy(alpha = enabledAlpha)
        )
        else -> null
    }
    val contentColor = when (variant) {
        HyperButtonVariant.Default -> HyperColors.primaryText
        else -> Color.White
    }.copy(alpha = enabledAlpha)

    val rowModifier = modifier
        .heightIn(min = minHeight)
        .clip(shape)
        .background(containerColor.copy(alpha = if (variant == HyperButtonVariant.Default) 0f else enabledAlpha))
        .let { base ->
            if (borderStroke != null) {
                base.border(borderStroke, shape)
            } else {
                base
            }
        }
        .hyperNoRippleClickable(enabled = enabled, onClick = onClick)
        .padding(horizontal = horizontalPadding, vertical = verticalPadding)

    Row(
        modifier = rowModifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.invoke(this)
        Text(
            text = text,
            color = contentColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium,
            lineHeight = fontSize
        )
        trailingIcon?.invoke(this)
    }
}
