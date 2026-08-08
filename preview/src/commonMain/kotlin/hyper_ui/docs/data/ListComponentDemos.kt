package hyper_ui.docs.data

import hyper_ui.docs.ui.HyperListDemo
import hyper_ui.docs.ui.LazyListDemo
import hyper_ui.docs.ui.MenuDemo

private const val GROUP_LIST = "列表组件"

internal fun listComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "lazy_list",
        group = GROUP_LIST,
        title = "HyperLazyList",
        description = "懒加载列表容器，外层默认带轻描边，子项内容完全由调用方决定。",
        code = """
            HyperLazyList(items = items) { item ->
                HyperListItem(
                    leadingContent = { Icon(item.icon, null) },
                    headlineContent = { Text(item.title) },
                    supportingContent = { Text(item.description) },
                    dividerVisible = item != items.last()
                )
            }
        """.trimIndent(),
        content = { LazyListDemo() }
    ),
    ComponentDemo(
        id = "hyper_list",
        group = GROUP_LIST,
        title = "HyperList",
        description = "非懒加载列表容器，外层默认带轻描边，适合少量静态数据。",
        code = """
            HyperList(items = items) { item ->
                HyperListItem(
                    headlineContent = { Text(item.title) },
                    trailingContent = { Text(item.value) }
                )
            }
        """.trimIndent(),
        content = { HyperListDemo() }
    ),
    ComponentDemo(
        id = "menu_group",
        group = GROUP_LIST,
        title = "HyperMenuGroup",
        description = "菜单分组容器默认带轻描边；菜单项使用 leading/headline/supporting/trailing slots。",
        code = """
            HyperMenuGroup {
                HyperMenuItem(
                    leadingContent = { Icon(Icons.Default.Settings, null) },
                    headlineContent = { Text("主题外观") },
                    supportingContent = { Text("颜色、圆角和显示密度") },
                    trailingContent = {
                        HyperSwitch(
                            checked = enabled,
                            onCheckedChange = { enabled = it }
                        )
                    }
                )
            }
        """.trimIndent(),
        content = { MenuDemo() }
    )
)
