/** 文件职责：在 hyper_ui 中负责承载 preview/src/commonMain/kotlin/hyper_ui/docs/DocsApp 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui.docs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import hyper_ui.rgba
import hyper_ui.docs.theme.DefaultDocsThemeColor
import hyper_ui.docs.theme.DocsMaterial
import hyper_ui.docs.theme.HyperDocsTheme
import hyper_ui.docs.ui.HyperDocsApp

val LocalThemeColor = compositionLocalOf<ThemeColorController> {
    error("ThemeColorController not provided. Wrap content with HyperDocsApp or supply a value.")
}

class ThemeColorController(initial: Color) {
    var color by mutableStateOf(initial)
        private set

    var darkTheme by mutableStateOf(false)
        private set

    var rtl by mutableStateOf(false)
        private set

    var patternedBackground by mutableStateOf(false)
        private set

    var customAccent by mutableStateOf(false)
        private set

    var material by mutableStateOf(DocsMaterial.Soft)
        private set

    fun update(newColor: Color) {
        color = newColor
        customAccent = newColor != DefaultDocsThemeColor
    }

    fun toggleDarkTheme() { darkTheme = !darkTheme }

    fun toggleLayoutDirection() { rtl = !rtl }

    fun toggleBackground() { patternedBackground = !patternedBackground }

    fun toggleAccent() {
        update(if (customAccent) DefaultDocsThemeColor else rgba(154, 130, 215))
    }

    fun updateMaterial(newMaterial: DocsMaterial) { material = newMaterial }
}

@Composable
fun rememberThemeColorController(
    initial: Color = DefaultDocsThemeColor,
    initialDarkTheme: Boolean = false
): ThemeColorController = remember {
    ThemeColorController(initial).also { it.setInitialDarkTheme(initialDarkTheme) }
}

private fun ThemeColorController.setInitialDarkTheme(value: Boolean) {
    if (value) toggleDarkTheme()
}

/** Shared document root used by the Desktop window and the browser viewport. */
@Composable
fun HyperDocsRoot(initialSelectedId: String? = null, embeddedPreview: Boolean = false) {
    val themeColorController = rememberThemeColorController(
        initialDarkTheme = if (embeddedPreview) false else isSystemInDarkTheme()
    )
    HyperDocsTheme(
        themeColor = themeColorController.color,
        darkTheme = themeColorController.darkTheme,
        material = themeColorController.material,
        patternedBackground = themeColorController.patternedBackground
    ) {
        HyperDocsApp(
            themeColorController = themeColorController,
            initialSelectedId = initialSelectedId,
            embeddedPreview = embeddedPreview
        )
    }
}
