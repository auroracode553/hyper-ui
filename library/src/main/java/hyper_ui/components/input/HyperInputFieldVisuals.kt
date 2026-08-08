package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class HyperTextFieldColors(
    val containerColor: Color,
    val focusedContainerColor: Color,
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
    val contentColor: Color,
    val placeholderColor: Color,
    val labelColor: Color,
    val supportingColor: Color,
    val cursorColor: Color
)

@Composable
internal fun hyperInputFieldVisuals(
    focused: Boolean,
    enabled: Boolean,
    isError: Boolean,
    colors: HyperTextFieldColors
): HyperInputFieldVisuals {
    val containerColor = when {
        !enabled -> colors.disabledContainerColor
        isError -> colors.errorContainerColor
        focused -> colors.focusedContainerColor
        else -> colors.containerColor
    }

    return HyperInputFieldVisuals(
        containerColor = containerColor,
        contentColor = if (enabled) colors.contentColor else colors.disabledContentColor,
        placeholderColor = if (enabled) colors.placeholderColor else colors.disabledContentColor,
        labelColor = if (isError) colors.errorColor else if (enabled) colors.labelColor else colors.disabledContentColor,
        supportingColor = if (isError) colors.errorColor else if (enabled) colors.supportingColor else colors.disabledContentColor,
        cursorColor = if (isError) colors.errorColor else colors.cursorColor
    )
}
