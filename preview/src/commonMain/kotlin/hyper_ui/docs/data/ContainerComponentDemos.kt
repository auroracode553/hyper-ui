/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/ContainerComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data
import hyper_ui.*
import hyper_ui.docs.theme.LocalDocsColorScheme

import hyper_ui.docs.ui.ColorPickerDemo
import hyper_ui.docs.ui.PanelDemo
import hyper_ui.docs.ui.SafeAreaDemo

private const val GROUP_CONTAINER = "容器组件"

internal fun containerComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "safe-area", group = GROUP_CONTAINER, title = "HyperSafeArea",
        description = "按需保护顶部或底部内容，统一底部最小 20dp，并避免嵌套重复留白。",
        code = "HyperSafeArea { BottomControls() }",
        variants = listOf(DemoVariant("方向", "edge = Top / Bottom", "背景保持全面屏；只保护内容"),
            DemoVariant("嵌套", "同侧消费一次", "系统区与最小留白均不叠加")),
        apiDocumentPaths = listOf("container/hyper-safe-area.md"), content = { SafeAreaDemo() }
    ),
    ComponentDemo(
        id = "surface", group = GROUP_CONTAINER, title = "HyperBackdrop",
        description = "HyperBackground 提供静态渐变，HyperBackdrop 组合背景与实色内容。",
        code = "HyperBackdrop { HyperCard { HyperText(\"实色容器\") } }",
        variants = listOf(DemoVariant("背景组合", "background / content", "背景与内容独立组合")),
        apiDocumentPaths = listOf("container/hyper-backdrop.md"),
        content = { hyper_ui.docs.ui.GlassMaterialDemo() }
    ),

    ComponentDemo(
        id = "color-picker",
        group = GROUP_CONTAINER,
        title = "HyperColorPicker",
        description = "连续 HSV 调色板，支持点击、拖动取色与禁用，颜色由调用方管理。",
        code = """
            HyperColorPicker(
                value = color,
                onChanged = { color = it }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("连续取色", "value / onChanged", "渐变选色区、色相条与颜色值"),
            DemoVariant("禁用", "enabled = false", "保留颜色并停止取色")
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
            DemoVariant("表面", "selected / colors", "共用无普通边框与双层柔阴影，选中使用 strong 与实色强调边框"),
            DemoVariant("默认留白", "ContentPadding = 12.dp", "无需在直接子节点重复添加整块 padding"),
            DemoVariant("自定义内容", "content slot", "标题、状态与操作组合"),
            DemoVariant("内容布局", "contentModifier = Modifier.padding(...)", "独立控制面板内部内容区")
        ),
        apiDocumentPaths = listOf("container/hyper-card.md"),
        content = { PanelDemo() }
    )
)
