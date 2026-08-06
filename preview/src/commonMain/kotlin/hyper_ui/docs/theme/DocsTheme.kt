package hyper_ui.docs.theme

import hyper_ui.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

internal val DocsBackground = rgba(247, 248, 250, 1f)
internal val DocsSidebar = rgba(255, 255, 255, 1f)
internal val DocsBorder = rgba(229, 229, 234, 1f)
internal val DocsCodeBackground = rgba(17, 24, 39, 1f)
internal val DocsCodeText = rgba(229, 236, 247, 1f)
internal val DocsPreviewBackground = rgba(242, 242, 247, 1f)

internal val DefaultDocsThemeColor = rgba(255, 103, 0, 1f)

internal fun Color.blendWith(white: Color, ratio: Float): Color {
    val r = red * (1f - ratio) + white.red * ratio
    val g = green * (1f - ratio) + white.green * ratio
    val b = blue * (1f - ratio) + white.blue * ratio
    return Color(
        red = r.coerceIn(0f, 1f),
        green = g.coerceIn(0f, 1f),
        blue = b.coerceIn(0f, 1f),
        alpha = 1f
    )
}

internal fun Color.darken(ratio: Float): Color {
    return Color(
        red = (red * (1f - ratio)).coerceIn(0f, 1f),
        green = (green * (1f - ratio)).coerceIn(0f, 1f),
        blue = (blue * (1f - ratio)).coerceIn(0f, 1f),
        alpha = 1f
    )
}

@Composable
fun HyperDocsTheme(
    themeColor: Color = DefaultDocsThemeColor,
    content: @Composable () -> Unit
) {
    val isLightTheme = themeColor.luminance() < 0.7f
    val onPrimaryColor = if (themeColor.luminance() > 0.6f) {
        rgba(28, 28, 30, 1f)
    } else {
        rgba(255, 255, 255, 1f)
    }
    val primaryContainerColor = themeColor.blendWith(rgba(255, 255, 255, 1f), 0.78f)
    val onPrimaryContainerColor = if (isLightTheme) {
        themeColor.darken(0.55f)
    } else {
        themeColor.blendWith(rgba(255, 255, 255, 1f), 0.2f)
    }

    HyperThemeConfig(themeColor = themeColor) {
        MaterialTheme(
            colorScheme = lightColorScheme(
                primary = themeColor,
                onPrimary = onPrimaryColor,
                primaryContainer = primaryContainerColor,
                onPrimaryContainer = onPrimaryContainerColor,
                secondary = rgba(5, 150, 105, 1f),
                background = DocsBackground,
                onBackground = rgba(28, 28, 30, 1f),
                surface = rgba(255, 255, 255, 1f),
                onSurface = rgba(28, 28, 30, 1f),
                surfaceVariant = rgba(242, 242, 247, 1f),
                onSurfaceVariant = rgba(142, 142, 147, 1f),
                outlineVariant = DocsBorder,
                error = rgba(220, 38, 38, 1f),
                errorContainer = rgba(254, 226, 226, 1f)
            ),
            typography = docsTypography(),
            content = content
        )
    }
}
