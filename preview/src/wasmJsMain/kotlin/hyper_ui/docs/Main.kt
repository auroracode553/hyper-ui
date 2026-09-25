/** 文件职责：在 hyper_ui 中负责承载 preview/src/wasmJsMain/kotlin/hyper_ui/docs/Main 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui.docs

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // VitePress iframe 通过 hash 指定组件，embedded=1 只渲染该组件的交互示例。
    val initialSelectedId = window.location.hash
        .removePrefix("#")
        .trim()
        .takeIf { it.isNotEmpty() }
    val embeddedPreview = window.location.search
        .removePrefix("?")
        .split("&")
        .any { it == "embedded=1" }

    ComposeViewport(viewportContainerId = "hyper-ui-preview-root") {
        HyperDocsRoot(initialSelectedId = initialSelectedId, embeddedPreview = embeddedPreview)
    }
}
