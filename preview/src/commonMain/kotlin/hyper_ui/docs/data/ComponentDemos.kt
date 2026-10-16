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
    /** 是否注入示例通用主题、语义色与布局方向。 */
    val useReferencePreview: Boolean = true
)

/** 聚合稳定 Preview ID；分组示例同步参考展示结构，页面示例自行滚动，普通示例由统一视口滚动。 */
fun componentDemos(): List<ComponentDemo> = buildList<ComponentDemo> {
    addAll(basicComponentDemos()) // button 预览对齐 Flutter ButtonsExample；禁用态保留单个示例。
    addAll(formComponentDemos()) // radio 标签行不叠加触摸留白，选项与 Flutter 参考一致。
    addAll(containerComponentDemos())
    addAll(navigationComponentDemos()) // nav-bar 展示 scrolledUnder 驱动的透明与向下渐隐过渡。
    addAll(listComponentDemos()) // list-tile 直接展示条目；sectioned_list 固定 8dp 紧凑间距。
    addAll(feedbackComponentDemos()) // dropdown/popover 共用紧凑宽度规则；update_dialog 对齐 Flutter 更新流程。
    addAll(themeComponentDemos())
}
.map { demo ->
    if (demo.useReferencePreview) {
        demo.copy(content = { FlutterReferencePreview { demo.content() } })
    } else demo
}
