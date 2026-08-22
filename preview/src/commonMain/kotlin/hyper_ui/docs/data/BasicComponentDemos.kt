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
        description = "Slot-first 实色按钮容器。所有 tone、禁用态和自定义颜色都会以不透明颜色渲染。",
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
            DemoVariant("轮廓", "tone = Outline", "卡片实色填充与主题描边"),
            DemoVariant("主要", "tone = Primary", "主题实色"),
            DemoVariant("弱强调", "tone = Tonal", "主题混合实色"),
            DemoVariant("语义色", "tone = Success / Danger", "成功与危险实色"),
            DemoVariant("禁用", "enabled = false", "禁用实色状态"),
            DemoVariant("紧凑", "modifier = Modifier.height(32.dp)", "小尺寸 slot")
        ),
        apiDocumentPaths = listOf("basic/hyper-button.md"),
        content = { ButtonDemo() }
    ),
    ComponentDemo(
        id = "icon_button",
        group = GROUP_BASIC,
        title = "HyperIconButton",
        description = "Slot-first 磨砂玻璃图标按钮。默认 38dp，以均匀白色透明底材、宽上沿柔光、面内折射边缘与单层悬浮阴影还原澎湃 OS 风格，不绘制硬边框。",
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
            DemoVariant("默认玻璃", "shape = CircleShape", "38dp 容器、18dp 图标与柔和折射边缘"),
            DemoVariant("主题玻璃", "colors = primaryContainer.copy(alpha = 0.52f)", "主题色玻璃与按压反馈"),
            DemoVariant("危险玻璃", "shape = RoundedCornerShape(12.dp)", "危险语义色玻璃"),
            DemoVariant("禁用状态", "enabled = false", "降低磨砂底材、内容和投影强度"),
            DemoVariant("大尺寸主题", "modifier = Modifier.size(56.dp)", "主题色媒体按钮"),
            DemoVariant("大尺寸中性", "modifier = Modifier.size(56.dp)", "中性玻璃工具按钮")
        ),
        apiDocumentPaths = listOf("basic/hyper-icon-button.md"),
        content = { IconButtonDemo() }
    )
)
