/** 文件职责：在 hyper_ui 中负责承载 preview/src/wasmJsMain/kotlin/hyper_ui/docs/Main 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui.docs

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import hyper_ui.docs.data.componentDemos
import hyper_ui.docs.data.findComponentDemo
import kotlinx.browser.document
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // VitePress iframe 通过 hash 指定组件，embedded=1 只渲染该组件的交互示例。
    val initialSelectedId = window.location.hash
        .removePrefix("#")
        .trim()
        .takeIf { it.isNotEmpty() }
    val options = window.location.search
        .removePrefix("?")
        .split("&")
        .associate { it.substringBefore("=") to it.substringAfter("=", "") }
    val embeddedPreview = options["embedded"] == "1"

    // 由实际注册表确认组件身份，网页不能把旧产物的任意 Canvas 当成目标预览。
    val actualDemo = findComponentDemo(componentDemos(), initialSelectedId ?: if (embeddedPreview) "" else null)
    document.getElementById("hyper-ui-preview-root")
        ?.setAttribute("data-preview-demo", actualDemo?.id.orEmpty())

    ComposeViewport(viewportContainerId = "hyper-ui-preview-root") {
        HyperDocsRoot(initialSelectedId = initialSelectedId, embeddedPreview = embeddedPreview)
    }
}
