/** 文件职责：在 hyper_ui 中负责维护 library/src/main/java/hyper_ui/theme/HyperStyle 的主题、样式与布局规范。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

fun rgba(
    red: Int,
    green: Int,
    blue: Int,
    alpha: Float = 1f
): Color = Color(
    red = red.coerceIn(0, 255) / 255f,
    green = green.coerceIn(0, 255) / 255f,
    blue = blue.coerceIn(0, 255) / 255f,
    alpha = alpha.coerceIn(0f, 1f)
)

@Immutable
data class HyperThemeColors(
    val themeColor: Color = HyperStyleDefaults.DefaultThemeColor,
    val successColor: Color = HyperStyleDefaults.SuccessColor
)

private val LocalHyperThemeColors = staticCompositionLocalOf {
    HyperThemeColors()
}

@Composable
fun HyperThemeConfig(
    themeColor: Color = HyperStyleDefaults.DefaultThemeColor,
    successColor: Color = HyperStyleDefaults.SuccessColor,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalHyperThemeColors provides HyperThemeColors(
            themeColor = themeColor,
            successColor = successColor
        ),
        content = content
    )
}

object HyperTheme {
    val colors: HyperThemeColors
        @Composable @ReadOnlyComposable
        get() = LocalHyperThemeColors.current
}

object HyperStyleDefaults {
    val DefaultThemeColor = rgba(255, 103, 0, 1f)
    val SuccessColor = rgba(52, 199, 89, 1f)
    val InfoColor = rgba(144, 147, 153, 1f)
    val WarningColor = rgba(230, 162, 60, 1f)
    val DangerColor = rgba(255, 59, 48, 1f)
    val SmallCornerRadius = 12.dp
    val MediumCornerRadius = 16.dp
    val LargeCornerRadius = 24.dp
    val ExtraLargeCornerRadius = 28.dp

    val CardElevation = 4.dp
}

object HyperColors {
    val accent: Color
        @Composable @ReadOnlyComposable
        get() = HyperTheme.colors.themeColor

    val success: Color
        @Composable @ReadOnlyComposable
        get() = HyperTheme.colors.successColor

    val info: Color
        @Composable @ReadOnlyComposable
        get() = if (isLight) HyperStyleDefaults.InfoColor else rgba(142, 142, 147, 1f)

    val warning: Color
        @Composable @ReadOnlyComposable
        get() = if (isLight) HyperStyleDefaults.WarningColor else rgba(255, 159, 10, 1f)

    val danger: Color
        @Composable @ReadOnlyComposable
        get() = if (isLight) HyperStyleDefaults.DangerColor else rgba(255, 69, 58, 1f)

    val isLight: Boolean
        @Composable @ReadOnlyComposable
        get() = MaterialTheme.colorScheme.background.luminance() > 0.5f

    val pageBackground: Color
        @Composable @ReadOnlyComposable
        get() = if (isLight) rgba(247, 248, 250, 1f) else rgba(17, 17, 19, 1f)

    val cardContainer: Color
        @Composable @ReadOnlyComposable
        get() = if (isLight) rgba(255, 255, 255, 1f) else rgba(44, 44, 46, 1f)

    val softContainer: Color
        @Composable @ReadOnlyComposable
        get() = if (isLight) rgba(242, 242, 247, 1f) else rgba(58, 58, 60, 1f)

    val fieldContainer: Color
        @Composable @ReadOnlyComposable
        get() = if (isLight) rgba(236, 237, 243, 1f) else rgba(64, 64, 68, 1f)

    val elevatedContainer: Color
        @Composable @ReadOnlyComposable
        get() = cardContainer

    val disabledContainer: Color
        @Composable @ReadOnlyComposable
        get() = if (isLight) rgba(232, 233, 238, 1f) else rgba(52, 52, 55, 1f)

    val primaryText: Color
        @Composable @ReadOnlyComposable
        get() = if (isLight) rgba(28, 28, 30, 1f) else rgba(245, 245, 247, 1f)

    val secondaryText: Color
        @Composable @ReadOnlyComposable
        get() = if (isLight) rgba(142, 142, 147, 1f) else rgba(174, 174, 178, 1f)

    val disabledText: Color
        @Composable @ReadOnlyComposable
        get() = if (isLight) rgba(154, 154, 160, 1f) else rgba(124, 124, 130, 1f)

    val divider: Color
        @Composable @ReadOnlyComposable
        get() = if (isLight) rgba(224, 225, 230, 1f) else rgba(72, 72, 76, 1f)

    val fieldBorder: Color
        @Composable @ReadOnlyComposable
        get() = if (isLight) rgba(199, 200, 206, 1f) else rgba(94, 94, 100, 1f)

    val accentContainer: Color
        @Composable @ReadOnlyComposable
        get() = lerp(accent, softContainer, 0.84f)

    val panelBorder: BorderStroke
        @Composable @ReadOnlyComposable
        get() = BorderStroke(
            width = 1.dp,
            color = divider
        )
}
