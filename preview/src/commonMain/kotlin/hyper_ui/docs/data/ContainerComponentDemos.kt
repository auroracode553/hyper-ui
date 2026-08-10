/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/ContainerComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.ColorPickerDemo
import hyper_ui.docs.ui.PanelDemo

private const val GROUP_CONTAINER = "容器组件"

internal fun containerComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "color-picker",
        group = GROUP_CONTAINER,
        title = "HyperColorPicker",
        description = "主题色选择板，色块默认带细描边，选中状态由调用方管理。",
        code = """
            HyperColorPicker(
                selectedId = selectedColorId,
                onSelected = { option -> selectedColorId = option.id }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("预设色板", "options = presetOptions", "圆形色块、选中环与文字标签"),
            DemoVariant("自定义布局", "colorSize / spacing", "尺寸与横纵间距可调")
        ),
        apiDocumentPaths = listOf("container/hyper-color-picker.md"),
        content = { ColorPickerDemo() }
    ),
    ComponentDemo(
        id = "panel",
        group = GROUP_CONTAINER,
        title = "HyperPanel",
        description = "通用 slot 容器，默认带轻描边、内容间距并始终按 shape 裁剪；外壳和内容区分别使用 modifier、contentModifier。",
        code = """
            HyperPanel(
                colors = HyperPanelDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text("系统状态")
                Text("运行正常")
                HyperButton(onClick = onOpen) {
                    Text("查看详情")
                }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("默认面板", "colors = defaults", "实色容器、圆角与轻描边"),
            DemoVariant("自定义内容", "content slot", "标题、状态与操作组合"),
            DemoVariant("内容布局", "contentModifier = Modifier.padding(...)", "独立控制面板内部内容区")
        ),
        apiDocumentPaths = listOf("container/hyper-panel.md"),
        content = { PanelDemo() }
    )
)
