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
    /** 沉浸式页面自行避让状态栏时，宿主允许内容绘制到屏幕顶端。 */
    val fillsTopSafeArea: Boolean = false,
    /** 贴底组件自行绘制手势条区域时，宿主不预留底部空白。 */
    val fillsBottomSafeArea: Boolean = false,
    /** 是否注入示例通用主题、语义色与布局方向。 */
    val useReferencePreview: Boolean = true
)

/** 聚合稳定 Preview ID；导航栏固定顶部避让与透明叠加滚动由导航分组示例呈现。 */
fun componentDemos(): List<ComponentDemo> = buildList<ComponentDemo> {
    addAll(basicComponentDemos())
    addAll(formComponentDemos())
    addAll(containerComponentDemos())
    addAll(navigationComponentDemos())
    addAll(listComponentDemos())
    addAll(feedbackComponentDemos())
    addAll(themeComponentDemos())
}
.map { demo ->
    if (demo.useReferencePreview) {
        demo.copy(content = { FlutterReferencePreview { demo.content() } })
    } else demo
}
