/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/ListComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.HyperListDemo
import hyper_ui.docs.ui.LazyListDemo

private const val GROUP_LIST = "列表组件"

internal fun listComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "lazy_list",
        group = GROUP_LIST,
        title = "HyperLazyList",
        description = "懒加载列表容器，支持同构数据和异构内容 DSL，外观统一由 HyperUI 提供。",
        code = """
            HyperLazyList(items = items) { item ->
                HyperListItem(
                    leadingContent = { Icon(item.icon, null) },
                    headlineContent = { Text(item.title) },
                    supportingContent = { Text(item.description) },
                    dividerVisible = item != items.last()
                )
            }

            HyperLazyList(state = listState) {
                item { HyperListItem(headlineContent = { Text("概览") }) }
                items(items, key = { it.id }) { item ->
                    HyperListItem(headlineContent = { Text(item.title) })
                }
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
