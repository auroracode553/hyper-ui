/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/components/input/HyperInputFieldVisuals 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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

@Immutable
internal data class HyperInputFieldVisuals(
    val surface: HyperTextFieldSurfaceVisuals,
    val contentColor: Color,
    val placeholderColor: Color,
    val labelColor: Color,
    val supportingColor: Color,
    val cursorColor: Color
)

@Composable
internal fun hyperInputFieldVisuals(
    enabled: Boolean,
    readOnly: Boolean,
    isError: Boolean,
    isFocused: Boolean,
    colors: HyperTextFieldColors
): HyperInputFieldVisuals {
    val isLight = HyperColors.isLight
    val showsFocus = enabled && !readOnly && isFocused
    val containerColor = when {
        !enabled -> colors.disabledContainerColor
        isError -> colors.errorContainerColor
        else -> colors.containerColor
    }
    val indicatorColor = when {
        isError && showsFocus -> colors.errorColor
        isError -> colors.errorColor.copy(alpha = 180 / 255f)
        showsFocus -> colors.cursorColor.copy(alpha = 190 / 255f)
        !enabled -> HyperColors.fieldBorder.copy(alpha = 110 / 255f)
        else -> HyperColors.fieldBorder
    }
    val elevation = when {
        !enabled -> HyperTextFieldSurfaceVisuals.DisabledElevation
        showsFocus -> HyperTextFieldSurfaceVisuals.FocusedElevation
        else -> HyperTextFieldSurfaceVisuals.RestingElevation
    }
    val depthState = if (enabled) {
        HyperSurfaceDepthState.Resting
    } else {
        HyperSurfaceDepthState.Disabled
    }
    val defaultDepth = hyperSurfaceDepthVisuals(
        role = HyperSurfaceDepthRole.CompactControl,
        elevation = elevation,
        state = depthState
    )
    val depth = if (indicatorColor.alpha > 0f) {
        defaultDepth.copy(strokeColor = indicatorColor, strokeWidth = if (showsFocus) 1.5.dp else 1.dp)
    } else {
        defaultDepth
    }

    return HyperInputFieldVisuals(
        surface = HyperTextFieldSurfaceVisuals(
            containerColor = containerColor,
            depth = depth
        ),
        contentColor = if (enabled) colors.contentColor else colors.disabledContentColor,
        placeholderColor = if (enabled) colors.placeholderColor else colors.disabledContentColor,
        labelColor = if (isError) colors.errorColor else if (enabled) colors.labelColor else colors.disabledContentColor,
        supportingColor = if (isError) colors.errorColor else if (enabled) colors.supportingColor else colors.disabledContentColor,
        cursorColor = if (isError) colors.errorColor else colors.cursorColor
    )
}
