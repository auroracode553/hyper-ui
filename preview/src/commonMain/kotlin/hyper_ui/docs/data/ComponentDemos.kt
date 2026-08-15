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

/** 聚合稳定 Preview ID；Android 电池工具由电池 Showcase 模拟状态并展示真实调用代码。 */
fun componentDemos(): List<ComponentDemo> = listOf(
    basicComponentDemos(),
    formComponentDemos(),
    containerComponentDemos(),
    navigationComponentDemos(),
    listComponentDemos(),
    feedbackComponentDemos()
).flatten()
