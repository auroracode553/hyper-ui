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
    val glass: HyperTextFieldGlassVisuals,
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
        !enabled -> HyperTextFieldGlassVisuals.DisabledElevation
        showsFocus -> HyperTextFieldGlassVisuals.FocusedElevation
        else -> HyperTextFieldGlassVisuals.RestingElevation
    }
    val ambientShadowColor = when {
        !enabled -> Color.Transparent
        showsFocus -> rgba(0, 0, 0, if (isLight) 0.018f else 0.08f)
        else -> rgba(0, 0, 0, if (isLight) 0.01f else 0.06f)
    }
    val spotShadowColor = when {
        !enabled -> Color.Transparent
        showsFocus -> rgba(0, 0, 0, if (isLight) 0.055f else 0.16f)
        else -> rgba(0, 0, 0, if (isLight) 0.035f else 0.12f)
    }

    return HyperInputFieldVisuals(
        glass = HyperTextFieldGlassVisuals(
            containerColor = containerColor,
            topLightColor = rgba(
                255,
                255,
                255,
                when {
                    !enabled -> if (isLight) 0.04f else 0.06f
                    showsFocus -> if (isLight) 0.14f else 0.16f
                    else -> if (isLight) 0.10f else 0.12f
                }
            ),
            bottomShadeColor = rgba(
                0,
                0,
                0,
                when {
                    !enabled -> if (isLight) 0.005f else 0.025f
                    else -> if (isLight) 0.012f else 0.05f
                }
            ),
            indicatorColor = indicatorColor,
            depth = hyperSurfaceDepthVisuals(
                strokeColor = Color.Transparent,
                elevation = elevation,
                ambientShadowColor = ambientShadowColor,
                spotShadowColor = spotShadowColor
            )
        ),
        contentColor = if (enabled) colors.contentColor else colors.disabledContentColor,
        placeholderColor = if (enabled) colors.placeholderColor else colors.disabledContentColor,
        labelColor = if (isError) colors.errorColor else if (enabled) colors.labelColor else colors.disabledContentColor,
        supportingColor = if (isError) colors.errorColor else if (enabled) colors.supportingColor else colors.disabledContentColor,
        cursorColor = if (isError) colors.errorColor else colors.cursorColor
    )
}
