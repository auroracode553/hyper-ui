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
    addAll(basicComponentDemos()) // button 使用 content 主体插槽，默认外观与状态对齐 Flutter。
    addAll(formComponentDemos()) // 日期共用正式网格；Checkbox/Radio/Switch 直接展示无卡片的默认标签行。
    addAll(containerComponentDemos()) // 内容表面共用无普通边框与双层阴影，选中强调边界保留。
    addAll(navigationComponentDemos()) // Navbar 自动监听；TabBar 覆盖悬浮、贴底、重复动作与禁用项。
    addAll(listComponentDemos()) // list-tile 直接展示条目；sectioned_list 固定 8dp 紧凑间距。
    addAll(feedbackComponentDemos()) // action_sheet 对齐 Flutter；toast 文字操作先关闭再回调；update_dialog 共用更新状态。
    addAll(themeComponentDemos())
}
.map { demo ->
    if (demo.useReferencePreview) {
        demo.copy(content = { FlutterReferencePreview { demo.content() } })
    } else demo
}
