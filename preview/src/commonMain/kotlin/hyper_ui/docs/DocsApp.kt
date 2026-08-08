/** 文件职责：在 hyper_ui 中负责承载 preview/src/commonMain/kotlin/hyper_ui/docs/DocsApp 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui.docs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import hyper_ui.docs.theme.DefaultDocsThemeColor
import hyper_ui.docs.theme.HyperDocsTheme
import hyper_ui.docs.ui.HyperDocsApp

val LocalThemeColor = compositionLocalOf<ThemeColorController> {
    error("ThemeColorController not provided. Wrap content with HyperDocsApp or supply a value.")
}

class ThemeColorController(initial: Color) {
    var color by mutableStateOf(initial)
        private set

    fun update(newColor: Color) {
        color = newColor
    }
}

@Composable
fun rememberThemeColorController(initial: Color = DefaultDocsThemeColor): ThemeColorController =
    remember { ThemeColorController(initial) }

/** Shared document root used by the Desktop window and the browser viewport. */
@Composable
fun HyperDocsRoot(initialSelectedId: String? = null) {
    val themeColorController = rememberThemeColorController()
    HyperDocsTheme(themeColor = themeColorController.color) {
        HyperDocsApp(
            themeColorController = themeColorController,
            initialSelectedId = initialSelectedId
        )
    }
}
