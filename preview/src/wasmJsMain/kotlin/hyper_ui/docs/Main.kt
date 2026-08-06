package hyper_ui.docs

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // VitePress iframe 通过 index.html#组件-id 指定初始文档项，非法 id 由公共 UI 回退处理。
    val initialSelectedId = window.location.hash
        .removePrefix("#")
        .trim()
        .takeIf { it.isNotEmpty() }

    ComposeViewport(viewportContainerId = "hyper-ui-preview-root") {
        HyperDocsRoot(initialSelectedId = initialSelectedId)
    }
}
