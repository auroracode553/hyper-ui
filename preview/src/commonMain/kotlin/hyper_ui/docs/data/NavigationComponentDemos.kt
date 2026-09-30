/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/NavigationComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.NavBarDemo
import hyper_ui.docs.ui.DrawerDemo
import hyper_ui.docs.ui.SlideMenuDemo
import hyper_ui.docs.ui.DockedTabBarDemo
import hyper_ui.docs.ui.FloatingTabBarDemo

private const val GROUP_NAVIGATION = "导航组件"

internal fun navigationComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "slide_menu", group = GROUP_NAVIGATION, title = "HyperSlideMenu",
        description = "列表侧滑操作，逻辑方向、速度投影和弹簧吸附。展开状态由调用方管理。",
        code = """
            HyperSlideMenu(
                reveal = reveal,
                onRevealChange = { reveal = it },
                endActions = listOf(HyperSlideAction("删除", onDelete))
            ) {
                HyperListTile(headlineContent = { HyperText("向左滑动") })
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("两侧操作", "startActions / endActions", "每项 64dp，逻辑方向自动镜像"),
            DemoVariant("禁用", "enabled = false", "停止拖动并收起操作"),
            DemoVariant("吸附", "release", "0.09s 速度投影与 470 刚度弹簧")
        ),
        apiDocumentPaths = listOf("navigation/hyper-slide-menu.md"),
        content = { hyper_ui.docs.ui.SlideActionMenuDemo() }
    ),

    ComponentDemo(
        id = "nav-bar",
        group = GROUP_NAVIGATION,
        title = "HyperNavBar",
        description = "固定透明导航操作层，首屏内容从状态栏与导航栏下方开始；上滚后内容进入导航栏与状态栏后方。",
        code = """
            HyperNavBar(
                navigationContent = { BackButton(onClick = onBack) },
                titleContent = { Text("详情") },
                trailingContent = { MoreButton(onClick = onMore) }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("固定操作层", "navigation/title/trailing", "按钮不随正文滚动"),
            DemoVariant("透明表面", "containerColor = Transparent", "导航栏不绘制背景、模糊、描边或阴影"),
            DemoVariant("默认尺寸", "size = default", "高度不包含顶部安全区"),
            DemoVariant("标题布局", "centerTitle = false/true", "支持起始侧与整栏居中"),
            DemoVariant("安全区", "safeArea = true/false", "顶部安全区独立于导航栏高度")
        ),
        apiDocumentPaths = listOf("navigation/hyper-nav-bar.md"),
        content = { NavBarDemo() },
        fullScreenPreview = true,
        useReferencePreview = false
    ),
    ComponentDemo(
        id = "drawer",
        group = GROUP_NAVIGATION,
        title = "HyperDrawer",
        description = "抽屉使用共享结构描边和低抬升阴影的不透明玻璃；支持四向、方向化间距、安全区与可配置滚动。",
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
            DemoVariant("结构玻璃", "colors.containerColor", "玻璃表面、柔和描边与双层阴影"),
            DemoVariant("选中项", "selected = true", "轻量主题染色，不重复铺设面板底色"),
            DemoVariant("无蒙层", "dismissOnClickOutside", "仅处理外部点击，不绘制背景或遮罩")
        ),
        apiDocumentPaths = listOf("navigation/hyper-drawer.md"),
        content = { DrawerDemo() },
        fullScreenPreview = true
    ),
    ComponentDemo(
        id = "tabs",
        group = GROUP_NAVIGATION,
        title = "HyperTabs",
        description = "横向分组菜单。每个项目直接复用 HyperButton 的表面、描边、按压与禁用态，菜单文字、计数或图标由 item slot 渲染。",
        code = """
            HyperTabs(
                items = categories,
                selectedItem = selected,
                onSelected = { selected = it }
            ) { item ->
                Text(item)
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("选中项", "selectedItem", "默认使用 type = filled"),
            DemoVariant("未选中项", "unselectedType", "默认使用 type = tonal"),
            DemoVariant("描边选中态", "selectedType = Outline", "直接使用 HyperButton 的 1dp 强调色描边"),
            DemoVariant("自定义配色", "selectedColors", "直接接收 HyperButtonColors"),
            DemoVariant("禁用项", "itemEnabled = false", "复用 HyperButton 禁用态")
        ),
        apiDocumentPaths = listOf("navigation/hyper-tabs.md"),
        content = { SlideMenuDemo() }
    ),
    ComponentDemo(
        id = "tab-bar-docked",
        group = GROUP_NAVIGATION,
        title = "HyperTabBar · 贴底",
        description = "贴底导航预览。点击标签可查看选中态，顶部发丝线与底部留白采用默认值。",
        code = """
            HyperTabBar(
                items = tabs,
                itemSelected = { it.id == selectedTabId },
                onItemClick = { selectedTabId = it.id }
            ) { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(item.icon, contentDescription = item.label)
                    Text(item.label)
                }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("贴底容器", "type = docked", "55dp 操作区与 5dp 底部留白"),
            DemoVariant("顶部发丝线", "topDivider", "默认显示 0.5dp 低对比分隔线"),
            DemoVariant("选中态", "itemSelected", "点击标签切换当前页面和内容色")
        ),
        apiDocumentPaths = listOf("navigation/hyper-tab-bar.md"),
        content = { DockedTabBarDemo() },
        fullScreenPreview = true,
        fillsBottomSafeArea = true
    ),
    ComponentDemo(
        id = "tab-bar-floating",
        group = GROUP_NAVIGATION,
        title = "HyperTabBar · 悬浮胶囊",
        description = "悬浮玻璃胶囊预览。点击或拖动标签，可查看托盘按压、跟手和弹簧吸附。",
        code = """
            HyperTabBar(
                items = tabs,
                type = "floating",
                itemSelected = { it.id == selectedTabId },
                onItemClick = { selectedTabId = it.id }
            ) { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(item.icon, contentDescription = item.label)
                    Text(item.label)
                }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("玻璃胶囊", "type = floating", "轻薄磨砂底座与单层柔和阴影"),
            DemoVariant("液态按压", "press", "按下时指示托盘展开"),
            DemoVariant("拖动吸附", "drag / release", "托盘跟手并吸附到最近标签"),
            DemoVariant("选中态", "itemSelected", "点击标签切换当前页面和内容色")
        ),
        apiDocumentPaths = listOf("navigation/hyper-tab-bar.md"),
        content = { FloatingTabBarDemo() },
        fullScreenPreview = true
    )
)
