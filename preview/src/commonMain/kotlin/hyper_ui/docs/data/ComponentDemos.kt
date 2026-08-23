/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/ComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import androidx.compose.runtime.Composable

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
    val content: @Composable () -> Unit
)

/** 聚合稳定 Preview ID；组件尺寸、公共描边、阴影和交互状态以分组描述与 Showcase 为准。 */
fun componentDemos(): List<ComponentDemo> = buildList {
    addAll(basicComponentDemos())
    addAll(formComponentDemos())
    addAll(containerComponentDemos())
    addAll(navigationComponentDemos())
    addAll(listComponentDemos())
    addAll(feedbackComponentDemos())
}
