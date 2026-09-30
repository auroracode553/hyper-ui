/** 文件职责：每个分组共用的浅深色、方向与动态背景对照环境。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
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
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                HyperBackdrop(
                    modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
                    background = {
                        if (settings.patternedBackground) {
                            HyperSoftBackground(Modifier.matchParentSize())
                            Canvas(Modifier.matchParentSize()) {
                                repeat(12) { index ->
                                    drawCircle(accent.copy(alpha = 0.3f), 30.dp.toPx(),
                                        Offset(size.width * index / 11f, size.height * (index % 3 + 1) / 4f))
                                }
                            }
                        } else Box(Modifier.matchParentSize().background(HyperColors.cardContainer))
                    }
                ) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) { content() }
                }
            }
        }
    }
}
