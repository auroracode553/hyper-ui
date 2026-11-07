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

/** 指定 ID 必须精确匹配；仅未指定组件的独立文档首页使用首项。 */
internal fun findComponentDemo(demos: List<ComponentDemo>, id: String?): ComponentDemo? =
    if (id == null) demos.firstOrNull() else demos.firstOrNull { it.id == id }

/** 聚合稳定 Preview ID；Drawer 覆盖顶部安全区及选中行留白，保留原分组与视口。 */
fun componentDemos(): List<ComponentDemo> = buildList<ComponentDemo> {
    addAll(basicComponentDemos()) // Button 使用完整文字行框与共用玻璃样式，示例覆盖中英文、图文及加载态中心线。
    addAll(formComponentDemos()) // TextField 单行与多行共用 16dp 圆角；Checkbox/Radio/DatePicker 复用分层玻璃与原有状态；Switch 为 52×32dp/28dp、内侧 2dp，核对弹性、速度形变、拖动与 Ambient；Slider 保留 20dp。
    addAll(containerComponentDemos()) // HyperSafeArea 直接在整屏手机边缘演示；HyperColorPicker 展示连续调色板，HyperBackground 提供静态渐变。
    addAll(navigationComponentDemos()) // filter-bar 精确对应 FilterBarDemo；Tabs 使用玻璃轨道与位置弹簧，Drawer 保留贴边全高；TabBar 支持 2–5 项、左右 12dp 留白，点击时整栏保持原比例，展示固定玻璃、镜片受限轻放大与短回弹、拖动回正与松手提交；非悬浮动作栏按图文与 HyperSafeArea 测量，56dp 为含安全区的整体最小高度。
    addAll(listComponentDemos()) // HyperGroupedList 与设置菜单共用标题样式；HyperSpacing 由页面消费一次；页面列表无卡片。
    addAll(feedbackComponentDemos()) // Dialog 与 UpdateDialog 展示高遮蔽玻璃面板和全屏遮罩。其他反馈组件按角色绘制玻璃并保留状态过渡。
    addAll(toolsComponentDemos()) // Android 更新工具只在 Preview 模拟流程，不引入平台网络或下载。
    addAll(themeComponentDemos()) // HyperThemeDialog 展示三选项主题设置；HyperSpacing 展示三种内容间距开关；全面屏入口见组件文档。
}
.map { demo ->
    if (demo.useReferencePreview) {
        demo.copy(content = { FlutterReferencePreview { demo.content() } })
    } else demo
}
