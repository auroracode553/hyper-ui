package hyper_ui.docs.data

import hyper_ui.docs.ui.ColorPickerDemo
import hyper_ui.docs.ui.PanelDemo

private const val GROUP_CONTAINER = "容器组件"

internal fun containerComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "color-picker",
        group = GROUP_CONTAINER,
        title = "HyperColorPicker",
        description = "主题色选择板，选中状态由调用方管理。",
        code = """
            HyperColorPicker(
                selectedId = selectedColorId,
                onSelected = { option -> selectedColorId = option.id }
            )
        """.trimIndent(),
        content = { ColorPickerDemo() }
    ),
    ComponentDemo(
        id = "panel",
        group = GROUP_CONTAINER,
        title = "HyperPanel",
        description = "通用 slot 容器，支持 colors、shape、elevation、border、padding 和 clipContent。",
        code = """
            HyperPanel(
                colors = HyperPanelDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Text("系统状态")
                Text("运行正常")
                HyperButton(onClick = onOpen) {
                    Text("查看详情")
                }
            }
        """.trimIndent(),
        content = { PanelDemo() }
    )
)
