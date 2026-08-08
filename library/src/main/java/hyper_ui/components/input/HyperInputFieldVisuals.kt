package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class HyperTextFieldColors(
    val containerColor: Color,
    val errorContainerColor: Color,
    val contentColor: Color,
    val placeholderColor: Color,
    val labelColor: Color,
    val supportingColor: Color,
    val errorColor: Color,
    val cursorColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

internal data class HyperInputFieldVisuals(
    val containerColor: Color,
    val borderColor: Color,
    val contentColor: Color,
    val placeholderColor: Color,
    val labelColor: Color,
    val supportingColor: Color,
    val cursorColor: Color
)

@Composable
internal fun hyperInputFieldVisuals(
    enabled: Boolean,
    isError: Boolean,
    colors: HyperTextFieldColors
): HyperInputFieldVisuals {
    val containerColor = when {
        !enabled -> colors.disabledContainerColor
        isError -> colors.errorContainerColor
        else -> colors.containerColor
    }
    val defaultBorderColor = HyperColors.fieldBorder
    val errorBorderAlpha = colors.errorColor.alpha * if (enabled) 0.58f else 0.32f
    val borderColor = when {
        isError -> colors.errorColor.copy(alpha = errorBorderAlpha)
        enabled -> defaultBorderColor
        else -> defaultBorderColor.copy(alpha = defaultBorderColor.alpha * 0.72f)
    }

    return HyperInputFieldVisuals(
        containerColor = containerColor,
        borderColor = borderColor,
        contentColor = if (enabled) colors.contentColor else colors.disabledContentColor,
        placeholderColor = if (enabled) colors.placeholderColor else colors.disabledContentColor,
        labelColor = if (isError) colors.errorColor else if (enabled) colors.labelColor else colors.disabledContentColor,
        supportingColor = if (isError) colors.errorColor else if (enabled) colors.supportingColor else colors.disabledContentColor,
        cursorColor = if (isError) colors.errorColor else colors.cursorColor
    )
}
