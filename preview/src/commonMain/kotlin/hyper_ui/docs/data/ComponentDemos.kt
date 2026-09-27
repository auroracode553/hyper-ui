/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/ComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import androidx.compose.runtime.Composable
import hyper_ui.docs.ui.FlutterReferencePreview

data class DemoVariant(
    val label: String,
    val properties: String,
    val style: String
)

data class ComponentDemo(
    val id: String,
    val group: String,
    val title: String,
    val description: String,
    val code: String,
    val variants: List<DemoVariant>,
    val apiDocumentPaths: List<String>,
    val content: @Composable () -> Unit,
    /** 组件是否直接占用嵌入预览的完整手机视口。 */
    val fullScreenPreview: Boolean = false,
    /** 是否包裹通用主题、背景和方向控制条。 */
    val useReferencePreview: Boolean = true
)

/** 聚合稳定 Preview ID；Toast 语义色与 Switch 关闭态由对应分组数据和 Showcase 呈现。 */
fun componentDemos(): List<ComponentDemo> = buildList<ComponentDemo> {
    addAll(basicComponentDemos())
    addAll(formComponentDemos())
    addAll(containerComponentDemos())
    addAll(navigationComponentDemos())
    addAll(listComponentDemos())
    addAll(feedbackComponentDemos())
}
.map { demo ->
    if (demo.useReferencePreview) {
        demo.copy(content = { FlutterReferencePreview { demo.content() } })
    } else demo
}
