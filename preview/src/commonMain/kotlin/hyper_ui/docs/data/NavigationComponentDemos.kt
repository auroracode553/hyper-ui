package hyper_ui.docs.data

import hyper_ui.docs.ui.DrawerDemo
import hyper_ui.docs.ui.BottomBarDemo
import hyper_ui.docs.ui.TopBarDemo

private const val GROUP_NAVIGATION = "导航组件"

internal fun navigationComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "topbar",
        group = GROUP_NAVIGATION,
        title = "HyperTopBar",
        description = "顶部标题栏。后退按钮由 onBack 参数控制：传入回调则显示，默认 null 则不显示（非自动显示）。支持右侧 slot 自定义操作区。",
        code = """
            // ① 无后退按钮、无右侧操作（最简用法）
            HyperTopBar(title = "无返回按钮")

            // ② 无后退按钮、仅右侧操作
            HyperTopBar(
                title = "仅右侧操作",
                rightSlot = {
                    HyperIconButton(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "更多",
                        onClick = {}
                    )
                }
            )

            // ③ 条件显示后退按钮（配合状态变量切换）
            var showBack by remember { mutableStateOf(false) }
            HyperTopBar(
                title = "可切换示例",
                onBack = if (showBack) ({}) else null,
                rightSlot = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HyperIconButton(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索",
                            onClick = {}
                        )
                        HyperIconButton(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "更多",
                            onClick = {}
                        )
                    }
                }
            )

            // ④ 完整示例：后退按钮 + 右侧双操作
            HyperTopBar(
                title = "通知设置",
                onBack = {},
                rightSlot = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HyperIconButton(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索",
                            onClick = {}
                        )
                        HyperIconButton(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "更多",
                            onClick = {}
                        )
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
        description = "抽屉容器，支持从左侧、右侧、顶部或底部弹出，无遮罩，包含标题区和抽屉菜单项。可通过 dismissOnClickOutside 配置点击空白区域是否关闭。",
        code = """
            var open by remember { mutableStateOf(false) }
            var selectedPageId by remember { mutableStateOf("home") }

            HyperDrawer(
                open = open,
                onDismissRequest = { open = false },
                position = HyperDrawerPosition.Left,
                dismissOnClickOutside = true,
                drawerContent = {
                    HyperDrawerHeader(
                        title = "HyperUI",
                        description = "左侧抽屉",
                        leadingIcon = Icons.Default.Menu
                    )
                    HyperDrawerItem(
                        title = "首页",
                        description = "组件概览",
                        leadingIcon = Icons.Default.Home,
                        selected = selectedPageId == "home",
                        onClick = {
                            selectedPageId = "home"
                            open = false
                        }
                    )
                    HyperDrawerItem(
                        title = "通知",
                        description = "Toast 与提醒",
                        leadingIcon = Icons.Default.Notifications,
                        selected = selectedPageId == "notice",
                        onClick = {
                            selectedPageId = "notice"
                            open = false
                        }
                    )
                    HyperDrawerItem(
                        title = "设置",
                        leadingIcon = Icons.Default.Settings,
                        selected = selectedPageId == "settings",
                        showDivider = true,
                        onClick = {
                            selectedPageId = "settings"
                            open = false
                        }
                    )
                    HyperDrawerItem(
                        title = "关于",
                        leadingIcon = Icons.Default.Info,
                        enabled = false
                    )
                }
            ) {
                HyperButton(
                    text = "打开抽屉",
                    onClick = { open = true }
                )
            }
        """.trimIndent(),
        content = { DrawerDemo() }
    ),
    ComponentDemo(
        id = "bottom-bar",
        group = GROUP_NAVIGATION,
        title = "HyperBottomBar",
        description = "纯底部导航栏组件；点击底部项只回调给调用方，可配置高度、菜单分布、槽内对齐、modifier 和图标文字尺寸。",
        code = """
            var selectedItemId by remember { mutableStateOf("home") }
            val bottomItems = listOf(
                HyperBottomBarItem("home", "首页", Icons.Default.Home),
                HyperBottomBarItem("recent", "最近", Icons.Default.Info),
                HyperBottomBarItem("settings", "设置", Icons.Default.Settings)
            )

            HyperBottomBar(
                items = bottomItems,
                selectedItemId = selectedItemId,
                onItemClick = { item -> selectedItemId = item.id },
                config = HyperBottomBarConfig(
                    height = 72.dp,
                    contentHeight = 64.dp,
                    horizontalPadding = 28.dp,
                    backgroundAlpha = 0.90f
                )
            )
        """.trimIndent(),
        content = { BottomBarDemo() }
    )
)
