/** 文件职责：为组件示例提供主题、文档语义色和布局方向，不额外绘制展示容器。 */
package hyper_ui.docs.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import hyper_ui.*
import hyper_ui.docs.LocalThemeColor
import hyper_ui.docs.theme.LocalDocsColorScheme
import hyper_ui.docs.theme.docsGlassTokens

@Composable
internal fun FlutterReferencePreview(content: @Composable () -> Unit) {
    val settings = LocalThemeColor.current
    val dark = settings.darkTheme
    val accent = settings.color
    HyperThemeConfig(
        themeColor = accent,
        darkTheme = dark,
        typography = HyperTheme.typography,
        glass = docsGlassTokens(dark, settings.material)
    ) {
        val scheme = LocalDocsColorScheme.current.copy(
            background = HyperColors.pageBackground,
            onBackground = HyperColors.primaryText,
            surface = HyperColors.cardContainer,
            onSurface = HyperColors.primaryText,
            surfaceVariant = HyperColors.softContainer,
            onSurfaceVariant = HyperColors.secondaryText,
            primary = accent,
            primaryContainer = HyperColors.accentContainer,
            outlineVariant = HyperColors.divider
        )
        CompositionLocalProvider(
            LocalDocsColorScheme provides scheme,
            LocalLayoutDirection provides if (settings.rtl) LayoutDirection.Rtl else LayoutDirection.Ltr
        ) {
            content()
        }
    }
}
