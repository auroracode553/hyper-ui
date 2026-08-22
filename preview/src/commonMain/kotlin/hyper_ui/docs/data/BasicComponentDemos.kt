/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/BasicComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.ButtonDemo
import hyper_ui.docs.ui.IconButtonDemo

private const val GROUP_BASIC = "基础组件"

internal fun basicComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "button",
        group = GROUP_BASIC,
        title = "HyperButton",
        description = "Slot-first 实色按钮容器。所有 tone 复用公共控件描边与单层阴影，按下时立即收低，禁用态移除阴影。",
        code = """
            HyperButton(onClick = onSave) {
                Icon(Icons.Default.Search, contentDescription = null)
                Text("搜索")
            }

            HyperButton(
                onClick = onDelete,
                tone = HyperButtonTone.Danger
            ) {
                Text("删除")
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("轮廓", "tone = Outline", "主题描边替换公共中性描边，并保留公共阴影"),
            DemoVariant("主要", "tone = Primary", "主题实色与公共控件描边、阴影"),
            DemoVariant("弱强调", "tone = Tonal", "主题混合实色"),
            DemoVariant("语义色", "tone = Success / Danger", "成功与危险实色"),
            DemoVariant("按压", "pointer down", "公共描边和阴影立即收低"),
            DemoVariant("禁用", "enabled = false", "弱公共描边、移除阴影的禁用实色状态"),
            DemoVariant("紧凑", "modifier = Modifier.height(32.dp)", "小尺寸 slot"),
            DemoVariant("组合复用", "contentPadding / role", "供分段等组合组件复用布局与语义")
        ),
        apiDocumentPaths = listOf("basic/hyper-button.md"),
        content = { ButtonDemo() }
    ),
    ComponentDemo(
        id = "icon_button",
        group = GROUP_BASIC,
        title = "HyperIconButton",
        description = "Slot-first 磨砂玻璃图标按钮。默认 38dp，不绘制描边；浅色模式使用更清晰的公共单层阴影。",
        code = """
            HyperIconButton(
                onClick = onSearch
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                )
            }

            HyperIconButton(
                onClick = onPlay,
                modifier = Modifier.size(56.dp),
                colors = HyperIconButtonDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.64f),
                    pressedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.76f),
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "播放")
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("默认玻璃", "shape = CircleShape", "浅色 8dp、深色 6dp 的无描边单层阴影"),
            DemoVariant("主题玻璃", "colors = primaryContainer.copy(alpha = 0.52f)", "主题色玻璃与按压反馈"),
            DemoVariant("危险玻璃", "shape = RoundedCornerShape(12.dp)", "危险语义色玻璃"),
            DemoVariant("按压状态", "pointer down", "容器和阴影立即收低"),
            DemoVariant("禁用状态", "enabled = false", "无描边并移除投影"),
            DemoVariant("大尺寸主题", "modifier = Modifier.size(56.dp)", "主题色媒体按钮"),
            DemoVariant("大尺寸中性", "modifier = Modifier.size(56.dp)", "中性玻璃工具按钮")
        ),
        apiDocumentPaths = listOf("basic/hyper-icon-button.md"),
        content = { IconButtonDemo() }
    )
)
