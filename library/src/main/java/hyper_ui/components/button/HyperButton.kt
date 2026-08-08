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
    // 对齐 HyperIconButton 玻璃托盘范式：
    // 1. 标记是否使用默认背景 → 禁用态使用 disabledContainer 回退
    // 2. 背景可见（alpha > 0）时叠加 glassHighlightBrush 顶部高光
    val usesDefaultContainerColor = when (variant) {
        HyperButtonVariant.Default -> false // Default 本身是透明 + 描边语义，不走默认容器回退
        else -> true
    }
    val resolvedContainerColor = when (variant) {
        HyperButtonVariant.Default -> Color.Transparent
        HyperButtonVariant.Primary -> HyperColors.accent
        HyperButtonVariant.Success -> HyperColors.success
        HyperButtonVariant.Info -> HyperColors.info
        HyperButtonVariant.Warning -> HyperColors.warning
        HyperButtonVariant.Danger -> HyperColors.danger
    }
    val enabledAlpha = if (enabled) 1f else HyperStyleDefaults.DisabledAlpha
    val containerColor = if (enabled) {
        resolvedContainerColor
    } else if (usesDefaultContainerColor) {
        HyperColors.disabledContainer
    } else {
        resolvedContainerColor.copy(alpha = resolvedContainerColor.alpha * enabledAlpha)
    }
    val hasVisibleBackground = resolvedContainerColor.alpha > 0f
    val highlightModifier = if (hasVisibleBackground) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
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
        .background(containerColor)
        .then(highlightModifier)
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
