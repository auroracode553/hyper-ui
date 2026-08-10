/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/NavigationComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.BottomBarDemo
import hyper_ui.docs.ui.DrawerDemo
import hyper_ui.docs.ui.GroupMenusDemo
import hyper_ui.docs.ui.TopBarDemo

private const val GROUP_NAVIGATION = "导航组件"

internal fun navigationComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "topbar",
        group = GROUP_NAVIGATION,
        title = "HyperTopBar",
        description = "顶部栏容器。navigation、title、action 三个区域都由调用方通过 slot 渲染。",
        code = """
            HyperTopBar(
                navigationContent = {
                    HyperIconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                titleContent = {
                    Text("通知设置")
                },
                actionContent = {
                    HyperIconButton(onClick = onSearch) {
                        Icon(Icons.Default.Search, contentDescription = "搜索")
                    }
                }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("三段布局", "navigation/title/action", "左右操作与居中标题"),
            DemoVariant("Slot 内容", "content slots", "图标按钮和标题文字")
        ),
        apiDocumentPaths = listOf("navigation/hyper-top-bar.md"),
        content = { TopBarDemo() }
    ),
    ComponentDemo(
        id = "drawer",
        group = GROUP_NAVIGATION,
        title = "HyperDrawer",
        description = "抽屉使用不透明实色面板和纯滑动动画，支持四个方向；Header 和 Item 使用 slot 渲染，无遮罩。",
        code = """
            HyperDrawer(
                open = open,
                onDismissRequest = { open = false },
                position = HyperDrawerPosition.Left,
                drawerContent = {
                    HyperDrawerHeader(
                        leadingContent = { Icon(Icons.Default.Menu, null) },
                        headlineContent = { Text("HyperUI") },
                        supportingContent = { Text("左侧抽屉") }
                    )
                    HyperDrawerItem(
                        selected = selectedPageId == "home",
                        onClick = { selectedPageId = "home" },
                        leadingContent = { Icon(Icons.Default.Home, null) },
                        headlineContent = { Text("首页") }
                    )
                }
            ) {
                content()
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("方向", "position = Left/Right/Top/Bottom", "四向滑入，不使用淡入"),
            DemoVariant("选中项", "selected = true", "主题混合实色容器"),
            DemoVariant("无蒙层", "dismissOnClickOutside", "仅处理外部点击，不绘制背景或遮罩")
        ),
        apiDocumentPaths = listOf("navigation/hyper-drawer.md"),
        content = { DrawerDemo() }
    ),
    ComponentDemo(
        id = "group-menus",
        group = GROUP_NAVIGATION,
        title = "HyperGroupMenus",
        description = "横向分组菜单。未选中项默认带细描边，菜单文字、计数或图标由 item slot 渲染。",
        code = """
            HyperGroupMenus(
                items = categories,
                selectedItem = selected,
                onSelected = { selected = it }
            ) { item ->
                Text(item)
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("选中项", "selectedItem", "主题实色胶囊"),
            DemoVariant("未选中项", "itemEnabled = true", "中性实色与轻描边"),
            DemoVariant("禁用项", "itemEnabled = false", "禁用实色状态")
        ),
        apiDocumentPaths = listOf("navigation/hyper-group-menus.md"),
        content = { GroupMenusDemo() }
    ),
    ComponentDemo(
        id = "bottom-bar",
        group = GROUP_NAVIGATION,
        title = "HyperBottomBar",
        description = "底部栏浅色模式保留透明玻璃效果，深色模式使用不透明实色；支持完整 slot 与泛型 items 入口。",
        code = """
            HyperBottomBar {
                bottomItems.forEach { item ->
                    Column(
                        modifier = Modifier.weight(1f).clickable { selectedItemId = item.id },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(item.icon, contentDescription = item.label)
                        Text(item.label)
                    }
                }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("完整 Slot", "content: RowScope", "调用方控制按钮布局"),
            DemoVariant("泛型项目", "items + itemSelected", "统一点击、选中与禁用状态"),
            DemoVariant("浅色容器", "colors.containerColor", "唯一保留的浅色半透明组件容器")
        ),
        apiDocumentPaths = listOf("navigation/hyper-bottom-bar.md"),
        content = { BottomBarDemo() }
    )
)
