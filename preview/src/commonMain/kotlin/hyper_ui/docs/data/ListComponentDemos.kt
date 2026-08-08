package hyper_ui.docs.data

import hyper_ui.docs.ui.HyperListDemo
import hyper_ui.docs.ui.LazyListDemo

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
        description = "非懒加载列表容器，支持数据项入口和设置分组 slot 入口。",
        code = """
            HyperList(items = items) { item ->
                HyperListItem(
                    headlineContent = { Text(item.title) },
                    trailingContent = { Text(item.value) }
                )
            }

            HyperList {
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
        content = { HyperListDemo() }
    )
)
