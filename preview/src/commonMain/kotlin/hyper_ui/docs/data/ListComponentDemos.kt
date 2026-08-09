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
        description = "不透明实色的轻圆角页面级列表，支持切换懒加载、普通列表渲染和容器形状。",
        code = """
            HyperList(
                items = items,
                lazyLoading = true,
                shape = HyperListDefaults.Shape
            ) { item ->
                HyperListItem(
                    leadingContent = { Icon(item.icon, null) },
                    headlineContent = { Text(item.title) },
                    supportingContent = { Text(item.description) },
                    dividerVisible = true
                )
            }

            HyperList(
                items = items,
                lazyLoading = false
            ) { item ->
                HyperListItem(headlineContent = { Text(item.title) })
            }

            HyperList(state = listState) {
                item { HyperListItem(headlineContent = { Text("概览") }) }
                items(items, key = { it.id }) { item ->
                    HyperListItem(headlineContent = { Text(item.title) })
                }
            }
        """.trimIndent(),
        content = { HyperListDemo() }
    ),
    ComponentDemo(
        id = "hyper_menu_list",
        group = GROUP_LIST,
        title = "HyperMenuList",
        description = "不透明实色的圆角菜单列表，适合少量静态菜单、设置分组和操作入口。",
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
        content = { HyperMenuListDemo() }
    )
)
