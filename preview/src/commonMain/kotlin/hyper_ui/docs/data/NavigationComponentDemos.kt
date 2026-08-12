/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/NavigationComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.NavBarDemo
import hyper_ui.docs.ui.DrawerDemo
import hyper_ui.docs.ui.SlideMenuDemo
import hyper_ui.docs.ui.TabBarDemo

private const val GROUP_NAVIGATION = "导航组件"

internal fun navigationComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "nav-bar",
        group = GROUP_NAVIGATION,
        title = "HyperNavBar",
        description = "默认透明的顶部栏容器；navigation、title、action 三个区域由调用方通过 slot 渲染。",
        code = """
            HyperNavBar(
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
            DemoVariant("默认背景", "Color.Transparent", "继承页面底色，不绘制独立色块"),
            DemoVariant("Slot 内容", "content slots", "图标按钮和标题文字")
        ),
        apiDocumentPaths = listOf("navigation/hyper-nav-bar.md"),
        content = { NavBarDemo() }
    ),
    ComponentDemo(
        id = "drawer",
        group = GROUP_NAVIGATION,
        title = "HyperDrawer",
        description = "抽屉即时显示和关闭，支持四个方向、默认安全区留白和可配置内容滚动；Header 和 Item 使用 slot 渲染，无遮罩、无动画。",
        code = """
            HyperDrawer(
                open = open,
                onDismissRequest = { open = false },
                position = HyperDrawerPosition.Left,
                drawerContentScrollEnabled = true,
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
            DemoVariant("方向", "position = Left/Right/Top/Bottom", "四向直接显示，不执行过渡"),
            DemoVariant("面板尺寸", "drawerModifier = Modifier.width/height", "按方向定制独立面板节点"),
            DemoVariant("默认安全区", "safeDrawing + contentPadding", "自动避让系统栏并提供方向化留白"),
            DemoVariant("滚动职责", "drawerContentScrollEnabled", "普通内容由面板滚动，懒列表关闭外层滚动"),
            DemoVariant("选中项", "selected = true", "主题混合实色容器"),
            DemoVariant("无蒙层", "dismissOnClickOutside", "仅处理外部点击，不绘制背景或遮罩")
        ),
        apiDocumentPaths = listOf("navigation/hyper-drawer.md"),
        content = { DrawerDemo() }
    ),
    ComponentDemo(
        id = "slide-menu",
        group = GROUP_NAVIGATION,
        title = "HyperSlideMenu",
        description = "横向分组菜单。未选中项默认带细描边，菜单文字、计数或图标由 item slot 渲染。",
        code = """
            HyperSlideMenu(
                items = categories,
                selectedItem = selected,
                onSelected = { selected = it }
            ) { item ->
                Text(item)
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("选中项", "selectedItem", "主题实色胶囊"),
            DemoVariant("单项布局", "contentModifier = Modifier.padding(...)", "独立控制单项内容区"),
            DemoVariant("未选中项", "itemEnabled = true", "中性实色与轻描边"),
            DemoVariant("禁用项", "itemEnabled = false", "禁用实色状态")
        ),
        apiDocumentPaths = listOf("navigation/hyper-slide-menu.md"),
        content = { SlideMenuDemo() }
    ),
    ComponentDemo(
        id = "tab-bar",
        group = GROUP_NAVIGATION,
        title = "HyperTabBar",
        description = "默认 56dp 的底部栏；浅色模式保留透明玻璃效果，深色模式使用不透明实色，支持完整 slot 与泛型 items 入口。",
        code = """
            HyperTabBar {
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
            DemoVariant("默认高度", "Height = 56.dp", "紧凑但保留标准触控区域"),
            DemoVariant("完整 Slot", "content: RowScope", "调用方控制按钮布局"),
            DemoVariant("泛型项目", "items + itemSelected", "统一点击、选中与禁用状态"),
            DemoVariant("浅色容器", "colors.containerColor", "唯一保留的浅色半透明组件容器")
        ),
        apiDocumentPaths = listOf("navigation/hyper-tab-bar.md"),
        content = { TabBarDemo() }
    )
)
