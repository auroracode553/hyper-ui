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
        content = { ButtonDemo() }
    ),
    ComponentDemo(
        id = "icon_button",
        group = GROUP_BASIC,
        title = "HyperIconButton",
        description = "Slot-first 紧凑型圆形图标按钮容器。默认 40dp，浅色模式恢复既有填充与描边，深色模式默认使用半透明控制按钮样式。",
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
                size = 56.dp,
                colors = HyperIconButtonDefaults.colors(
                    containerColor = rgba(255, 255, 255, 0.18f),
                    pressedContainerColor = rgba(255, 255, 255, 0.28f),
                    contentColor = rgba(255, 255, 255, 1f),
                    outlineColor = rgba(255, 255, 255, 0f),
                    pressedOutlineColor = rgba(255, 255, 255, 0f),
                    disabledOutlineColor = rgba(255, 255, 255, 0f)
                )
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "播放")
            }
        """.trimIndent(),
        content = { IconButtonDemo() }
    )
)
