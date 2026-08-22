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
        isError -> colors.errorColor.copy(
            alpha = colors.errorColor.alpha * if (isLight) 0.46f else 0.62f
        )
        showsFocus -> colors.cursorColor.copy(
            alpha = colors.cursorColor.alpha * if (isLight) 0.38f else 0.58f
        )
        else -> Color.Transparent
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
        defaultDepth.copy(strokeColor = indicatorColor)
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
