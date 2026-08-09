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
        description = "Slot-first 按钮容器。组件负责点击、禁用态、tone 和容器视觉，按钮内容由调用方传入。",
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
        description = "Slot-first 圆形图标按钮容器。默认半透明控制按钮样式，深浅色自适应，颜色和按压态由调用方配置。",
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
                    contentColor = rgba(255, 255, 255, 1f)
                )
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "播放")
            }
        """.trimIndent(),
        content = { IconButtonDemo() }
    )
)
