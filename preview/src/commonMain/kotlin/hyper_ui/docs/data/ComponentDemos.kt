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
    /** 页面示例占用有限的剩余视口并自行滚动；普通示例由宿主滚动，禁止重复嵌套。 */
    val fullScreenPreview: Boolean = false,
    /** 沉浸式页面自行避让状态栏时，宿主允许内容绘制到屏幕顶端。 */
    val fillsTopSafeArea: Boolean = false,
    /** 贴底组件自行绘制手势条区域时，宿主不预留底部空白。 */
    val fillsBottomSafeArea: Boolean = false,
    /** 是否注入示例通用主题、语义色与布局方向。 */
    val useReferencePreview: Boolean = true
)

/** 聚合稳定 Preview ID；分组示例同步参考展示结构，页面示例自行滚动，普通示例由统一视口滚动。 */
fun componentDemos(): List<ComponentDemo> = buildList<ComponentDemo> {
    addAll(basicComponentDemos()) // button 预览对齐 Flutter ButtonsExample；禁用态保留单个示例。
    addAll(formComponentDemos())
    addAll(containerComponentDemos())
    addAll(navigationComponentDemos())
    addAll(listComponentDemos())
    addAll(feedbackComponentDemos()) // update_dialog 统一小图标、版本直接排版、简洁错误反馈与描边次操作。
    addAll(themeComponentDemos())
}
.map { demo ->
    if (demo.useReferencePreview) {
        demo.copy(content = { FlutterReferencePreview { demo.content() } })
    } else demo
}
