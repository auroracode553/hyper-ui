/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/components/input/HyperInputFieldVisuals 模块实现，并集中维护其依赖协作与核心逻辑。 */
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
    val borderColor = when {
        isError -> colors.errorColor
        enabled -> HyperColors.fieldBorder
        else -> HyperColors.divider
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
