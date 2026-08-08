/** 文件职责：在 hyper_ui 中负责承载 preview/src/desktopMain/kotlin/hyper_ui/docs/Main 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui.docs

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "HyperUI Docs",
        state = WindowState(size = DpSize(width = 1200.dp, height = 820.dp))
    ) {
        HyperDocsRoot()
    }
}
