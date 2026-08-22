/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/NavigationComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.NavBarDemo
import hyper_ui.docs.ui.ImmersiveNavBarDemo
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
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
        id = "immersive-nav-bar",
        group = GROUP_NAVIGATION,
        title = "HyperImmersiveNavBar",
        description = "固定透明导航按钮，首屏内容位于导航栏下方；滚动后内容可进入导航栏与状态栏后方。",
        code = """
            HyperImmersiveNavBar(
                navigationContent = { BackButton(onClick = onBack) },
                titleContent = { Text("详情") },
                actionContent = { MoreButton(onClick = onMore) },
                contentPadding = PaddingValues(bottom = bottomClearance)
            ) { immersivePadding ->
                LazyColumn(contentPadding = immersivePadding) {
                    items(notes) { note -> NoteCard(note) }
                }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("固定操作层", "navigation/title/action", "按钮不随正文滚动"),
            DemoVariant("附加头部", "headerContent", "搜索或筛选区域固定并计入首屏净空"),
            DemoVariant("首屏净空", "immersivePadding", "初始内容从状态栏与导航栏下方开始"),
            DemoVariant("沉浸滚动", "LazyColumn.contentPadding", "顶部净空滚出后，内容绘制到透明栏后方"),
            DemoVariant("系统栏", "windowInsets = WindowInsets.statusBars", "默认避让状态栏，不修改窗口配置")
        ),
        apiDocumentPaths = listOf("navigation/hyper-immersive-nav-bar.md"),
        content = { ImmersiveNavBarDemo() }
    ),
    ComponentDemo(
        id = "drawer",
        group = GROUP_NAVIGATION,
        title = "HyperDrawer",
        description = "抽屉使用不透明实色面板和状态色并即时显示、关闭；深色模式默认与页面背景同色且无灰色边界。支持四个方向，默认启用方向化内容间距与系统安全区，也可关闭以使用完整内容区；支持可配置内容滚动，无遮罩、无动画。",
        code = """
            HyperDrawer(
                open = open,
                onDismissRequest = { open = false },
                position = HyperDrawerPosition.Left,
                defaultSetPadding = true,
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
            DemoVariant("默认 Padding", "defaultSetPadding = true", "方向化内容留白并避让 safeDrawing"),
            DemoVariant("完整内容区", "defaultSetPadding = false", "不注入默认间距或系统栏避让"),
            DemoVariant("场景间距", "drawerContentModifier", "在默认策略之后追加调用方布局"),
            DemoVariant("滚动职责", "drawerContentScrollEnabled", "普通内容由面板滚动，懒列表关闭外层滚动"),
            DemoVariant("不透明背景", "colors.containerColor", "浅色使用实色卡片背景，深色继承页面背景"),
            DemoVariant("选中项", "selected = true", "使用不透明主题容器色"),
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
        description = "贴底栏默认使用 0.5dp 低对比度顶部发丝线，不使用阴影或整框描边；深色模式与页面同色，浅色模式保留轻量透明度。包含 55dp 操作区和 5dp 轻量底部留白，总高度 60dp。",
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
            DemoVariant("顶部发丝线", "topDivider = HyperTabBarDefaults.topDivider()", "0.5dp 低对比分隔，可交互关闭"),
            DemoVariant("底部留白", "BottomPadding = 5.dp", "与系统手势小白条保持少量距离"),
            DemoVariant("操作区高度", "Height = 55.dp", "搭配留白后总高度为 60dp"),
            DemoVariant("完整 Slot", "content: RowScope", "调用方控制按钮布局"),
            DemoVariant("泛型项目", "items + itemSelected", "统一点击、选中与禁用状态"),
            DemoVariant("明暗背景", "colors.containerColor", "深色继承页面背景并隐藏分隔线，浅色保留轻量透明度")
        ),
        apiDocumentPaths = listOf("navigation/hyper-tab-bar.md"),
        content = { TabBarDemo() }
    )
)
