/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/ListComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.ListTileDemo
import hyper_ui.docs.ui.HyperMenuGroupDemo
import hyper_ui.docs.ui.HyperListDemo
import hyper_ui.docs.ui.HyperSectionedListDemo

private const val GROUP_LIST = "列表组件"

internal fun listComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "hyper_list",
        group = GROUP_LIST,
        title = "HyperList",
        description = "18dp 圆角玻璃页面级懒列表，通过 LazyListScope Slot 组合项目。",
        code = """
            HyperList(state = listState) {
                item(key = "overview", contentType = "header") {
                    HyperListTile(title = { HyperText("概览") })
                }
                items(
                    items = items,
                    key = { it.id },
                    contentType = { "account" }
                ) { item ->
                    HyperListTile(title = { HyperText(item.title) })
                }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("懒加载", "LazyListScope", "固定使用 LazyColumn 页面列表"),
            DemoVariant("内容布局", "contentModifier = Modifier.padding(...)", "使用 Modifier 控制容器内部留白"),
            DemoVariant("滚动留白", "contentPadding", "Padding 随 LazyColumn 内容一起滚动"),
            DemoVariant("Slot 列表", "item/items/itemsIndexed", "调用方组合异构项目"),
            DemoVariant("默认外观", "colors + shape", "玻璃背景与 18dp 圆角"),
            DemoVariant("自适应行高", "最小 46dp", "单行保持紧凑，带描述项增加纵向呼吸空间"),
            DemoVariant("列表条目", "leading/headline/supporting", "图标、双行文字与分隔线")
        ),
        apiDocumentPaths = listOf(
            "list/hyper-list.md",
            "list/hyper-list-tile.md"
        ),
        content = { HyperListDemo() },
        fullScreenPreview = true
    ),
    ComponentDemo(
        id = "hyper_menu_list",
        group = GROUP_LIST,
        title = "HyperMenuGroup",
        description = "账户与应用偏好的分组菜单；展示静态设置入口、开关与复选状态。",
        code = """
            HyperMenuGroup(items = items) { item ->
                HyperListTile(
                    title = { HyperText(item.title) },
                    trailing = { HyperText(item.value) }
                )
            }

            HyperMenuGroup {
                HyperListTile(
                    title = { HyperText("推送通知") },
                    subtitle = { HyperText("接收重要消息提醒") },
                    trailing = {
                        HyperSwitch(
                            value = enabled,
                            onChanged = { enabled = it }
                        )
                    }
                )
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("数据入口", "items + itemContent", "静态菜单数据"),
            DemoVariant("外部修饰", "modifier", "间距或额外边框由调用方组合"),
            DemoVariant("统一留白", "ContentPadding = 4.dp", "容器 4dp 留白，条目保留自身 14×8dp padding"),
            DemoVariant("Slot 入口", "content slot", "设置项与选择控件组合"),
            DemoVariant("列表条目", "trailingContent", "值、开关、复选与单选尾部内容")
        ),
        apiDocumentPaths = listOf(
            "list/hyper-menu-group.md",
            "list/hyper-list-tile.md"
        ),
        content = { HyperMenuGroupDemo() }
    ),
    ComponentDemo(
        id = "hyper_sectioned_list",
        group = GROUP_LIST,
        title = "HyperSectionedList",
        description = "日期、历史等动态数据使用的分段懒列表，固定 8dp 紧凑分段间距，自动处理组内圆角与分割线。",
        code = """
            HyperSectionedList(
                sections = groups,
                items = { group -> group.entries },
                sectionKey = { group -> "section-${'$'}{group.id}" },
                itemKey = { _, entry -> "entry-${'$'}{entry.id}" },
                headerContent = { group -> HyperText(group.title) }
            ) { _, entry ->
                HyperListTile(
                    title = { HyperText(entry.title) },
                    subtitle = { HyperText(entry.subtitle) }
                )
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("懒分段", "sections + items", "标题与每个数据行独立懒加载"),
            DemoVariant("自动圆角", "首项 / 中间项 / 尾项", "每组形成独立的 18dp 圆角卡片"),
            DemoVariant("自动分割线", "dividerModifier", "组内非末项绘制，末项自动隐藏"),
            DemoVariant("稳定复用", "sectionKey + itemKey", "分段标题和数据行使用独立稳定 key"),
            DemoVariant("紧凑间距", "固定 8dp", "分段间距由组件统一管理，无需设置"),
            DemoVariant("整屏滚动", "fullScreenPreview + Modifier.weight(1f)", "列表占满视口，滚动到最后一组")
        ),
        apiDocumentPaths = listOf(
            "list/hyper-sectioned-list.md",
            "list/hyper-list-tile.md"
        ),
        content = { HyperSectionedListDemo() },
        fullScreenPreview = true
    ),
    ComponentDemo(id = "list-tile", group = GROUP_LIST, title = "HyperListTile",
        description = "直接展示 46dp 条目、34dp 图标块，auto/plain/glass、选中与禁用可操作，不额外包装卡片。",
        code = """HyperListTile(title = { HyperText("系统设置") }, onPressed = onOpen)""",
        variants = listOf(DemoVariant("表面", "type / selected", "直接切换 auto/plain/glass；leading 间距 12dp、trailing 间距 8dp，无额外卡片容器"),
            DemoVariant("文字", "supportingContent / metaContent", "14sp 主标题、12sp 描述、11sp 元信息")),
        apiDocumentPaths = listOf("list/hyper-list-tile.md"), content = { ListTileDemo() })

)
