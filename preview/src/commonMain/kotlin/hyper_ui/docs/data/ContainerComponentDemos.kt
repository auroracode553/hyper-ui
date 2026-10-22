/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/ContainerComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data
import hyper_ui.*
import hyper_ui.docs.theme.LocalDocsColorScheme

import hyper_ui.docs.ui.ColorPickerDemo
import hyper_ui.docs.ui.PanelDemo

private const val GROUP_CONTAINER = "容器组件"

internal fun containerComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "glass_material", group = GROUP_CONTAINER, title = "HyperBackdrop / HyperGlassTokens",
        description = "背景采样与 Solid、Soft、Clear 三档材质；观察 subtle、regular、strong 表面层级。",
        code = """
            HyperThemeConfig {
                HyperBackdrop {
                    HyperCard { HyperText("玻璃容器") }
                }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("材质重量", "glass.surfaceSubtle / surface / surfaceStrong / cardContainer", "不同表面使用同一背景采样"),
            DemoVariant("透明度", "glass.surface.alpha", "材质令牌通过主题注入")
        ),
        apiDocumentPaths = listOf("container/hyper-backdrop.md"),
        content = { hyper_ui.docs.ui.GlassMaterialDemo() }
    ),

    ComponentDemo(
        id = "color-picker",
        group = GROUP_CONTAINER,
        title = "HyperColorPicker",
        description = "主题色选择板，色块默认带细描边，选中状态由调用方管理。",
        code = """
            HyperColorPicker(
                selectedId = selectedColorId,
                onChanged = { option -> selectedColorId = option.id }
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
        title = "HyperCard",
        description = "通用 slot 容器，标题、副标题、正文、页脚与操作插槽；24dp 圆角、12dp 内边距，支持选中状态。",
        code = """
            HyperCard(
                colors = HyperCardDefaults.colors(
                    containerColor = LocalDocsColorScheme.current.surface
                )
            ) {
                HyperText("系统状态")
                HyperText("运行正常")
                HyperButton(onPressed = onOpen) {
                    HyperText("查看详情")
                }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("表面", "selected / colors", "regular 材质，选中时使用 strong 与实色强调边框"),
            DemoVariant("默认留白", "ContentPadding = 12.dp", "无需在直接子节点重复添加整块 padding"),
            DemoVariant("自定义内容", "content slot", "标题、状态与操作组合"),
            DemoVariant("内容布局", "contentModifier = Modifier.padding(...)", "独立控制面板内部内容区")
        ),
        apiDocumentPaths = listOf("container/hyper-card.md"),
        content = { PanelDemo() }
    )
)
