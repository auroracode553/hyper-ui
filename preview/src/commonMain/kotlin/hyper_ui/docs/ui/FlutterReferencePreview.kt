/** 文件职责：每个分组共用的浅深色、方向与动态背景对照环境。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import hyper_ui.*
import hyper_ui.docs.theme.LocalDocsColorScheme

@Composable
internal fun FlutterReferencePreview(content: @Composable () -> Unit) {
    val embeddedDarkMode = LocalEmbeddedDarkMode.current
    var localDark by remember { mutableStateOf(false) }
    val dark = embeddedDarkMode?.value ?: localDark
    var rtl by remember { mutableStateOf(false) }
    var patterned by remember { mutableStateOf(false) }
    var customColor by remember { mutableStateOf(false) }
    val accent = if (customColor) rgba(154, 130, 215) else HyperStyleDefaults.DefaultThemeColor
    HyperThemeConfig(themeColor = accent, darkTheme = dark, typography = HyperTheme.typography) {
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
            LocalLayoutDirection provides if (rtl) LayoutDirection.Rtl else LayoutDirection.Ltr
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                HyperBackdrop(
                    modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp),
                    background = {
                        if (patterned) {
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
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    HyperButton({
                        if (embeddedDarkMode == null) localDark = !dark else embeddedDarkMode.value = !dark
                    }, variant = HyperButtonVariant.Tonal, height = 32.dp) {
                        HyperText(if (dark) "深色" else "浅色")
                    }
                    HyperButton({ rtl = !rtl }, variant = HyperButtonVariant.Tonal, height = 32.dp) {
                        HyperText(if (rtl) "RTL" else "LTR")
                    }
                    HyperButton({ patterned = !patterned }, variant = HyperButtonVariant.Tonal, height = 32.dp) {
                        HyperText(if (patterned) "纹理背景" else "柔色背景")
                    }
                    HyperButton({ customColor = !customColor }, variant = HyperButtonVariant.Tonal, height = 32.dp) {
                        HyperText(if (customColor) "紫色" else "蓝色")
                    }
                }
            }
        }
    }
}
