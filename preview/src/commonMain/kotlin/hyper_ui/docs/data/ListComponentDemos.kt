/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/ListComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.HyperMenuListDemo
import hyper_ui.docs.ui.HyperListDemo

private const val GROUP_LIST = "列表组件"

internal fun listComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "hyper_list",
        group = GROUP_LIST,
        title = "HyperList",
        description = "不透明实色的轻圆角页面级懒列表，通过 LazyListScope Slot 组合项目。",
        code = """
            HyperList(state = listState) {
                item(key = "overview", contentType = "header") {
                    HyperListItem(headlineContent = { Text("概览") })
                }
                items(
                    items = items,
                    key = { it.id },
                    contentType = { "account" }
                ) { item ->
                    HyperListItem(headlineContent = { Text(item.title) })
                }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("懒加载", "LazyListScope", "固定使用 LazyColumn 页面列表"),
            DemoVariant("内容布局", "contentModifier = Modifier.padding(...)", "使用 Modifier 控制容器内部留白"),
            DemoVariant("Slot 列表", "item/items/itemsIndexed", "调用方组合异构项目"),
            DemoVariant("默认外观", "colors + shape", "不透明卡片背景与 12dp 轻圆角"),
            DemoVariant("自适应行高", "52.dp / 56.dp", "根据 supportingContent 自动平衡单行与双行密度"),
            DemoVariant("列表条目", "leading/headline/supporting", "图标、双行文字与分隔线")
        ),
        apiDocumentPaths = listOf(
            "list/hyper-list.md",
            "list/hyper-list-item.md"
        ),
        content = { HyperListDemo() }
    ),
    ComponentDemo(
        id = "hyper_menu_list",
        group = GROUP_LIST,
        title = "HyperMenuList",
        description = "只用于少量菜单、设置项和操作入口的不透明圆角菜单容器，不用于数据列表。",
        code = """
            HyperMenuList(items = items) { item ->
                HyperListItem(
                    headlineContent = { Text(item.title) },
                    trailingContent = { Text(item.value) }
                )
            }

            HyperMenuList {
                HyperListItem(
                    headlineContent = { Text("推送通知") },
                    supportingContent = { Text("接收重要消息提醒") },
                    trailingContent = {
                        HyperSwitch(
                            checked = enabled,
                            onCheckedChange = { enabled = it }
                        )
                    }
                )
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("数据入口", "items + itemContent", "静态菜单数据"),
            DemoVariant("外部修饰", "modifier", "间距或额外边框由调用方组合"),
            DemoVariant("首尾留白", "ContentPadding = 6.dp", "默认避开卡片上下圆角边界"),
            DemoVariant("Slot 入口", "content slot", "设置项与选择控件组合"),
            DemoVariant("列表条目", "trailingContent", "值、开关、复选与单选尾部内容")
        ),
        apiDocumentPaths = listOf(
            "list/hyper-menu-list.md",
            "list/hyper-list-item.md"
        ),
        content = { HyperMenuListDemo() }
    )
)
