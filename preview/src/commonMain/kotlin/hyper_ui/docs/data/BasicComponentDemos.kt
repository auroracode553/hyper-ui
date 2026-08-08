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
        description = "Slot-first 图标按钮容器。默认带轻描边，形状、颜色和尺寸归组件，图标内容由调用方传入。",
        code = """
            HyperIconButton(
                onClick = onSearch,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                )
            }
        """.trimIndent(),
        content = { IconButtonDemo() }
    )
)
