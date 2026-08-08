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
        content = { TopBarDemo() }
    ),
    ComponentDemo(
        id = "drawer",
        group = GROUP_NAVIGATION,
        title = "HyperDrawer",
        description = "抽屉容器默认带轻描边，支持四个方向。Header 和 Item 均使用 slot 渲染，无遮罩。",
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
        content = { GroupMenusDemo() }
    ),
    ComponentDemo(
        id = "bottom-bar",
        group = GROUP_NAVIGATION,
        title = "HyperBottomBar",
        description = "底部栏容器默认带轻描边。调用方可以传入完整内容 slot，也可以使用泛型 items 入口。",
        code = """
            HyperBottomBar(
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
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
        content = { BottomBarDemo() }
    )
)
