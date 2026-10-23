/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/NavigationComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.NavBarDemo
import hyper_ui.docs.ui.DrawerDemo
import hyper_ui.docs.ui.SlideMenuDemo
import hyper_ui.docs.ui.SingleTabBarDemo
import hyper_ui.docs.ui.MultipleTabBarDemo

private const val GROUP_NAVIGATION = "导航组件"

internal fun navigationComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "slide_menu", group = GROUP_NAVIGATION, title = "HyperSlideMenu",
        description = "共用内容卡片表面的列表侧滑操作，保留逻辑方向、速度投影和弹簧吸附。展开状态由调用方管理。",
        code = """
            HyperSlideMenu(
                reveal = reveal,
                onRevealChange = { reveal = it },
                endActions = listOf(HyperSlideAction("删除", onDelete))
            ) {
                HyperListTile(title = { HyperText("向左滑动") })
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("两侧操作", "startActions / endActions", "每项 64dp，逻辑方向自动镜像"),
            DemoVariant("禁用", "enabled = false", "停止拖动并收起操作"),
            DemoVariant("吸附", "release", "0.09s 速度投影，携带释放速度进入 470 刚度弹簧")
        ),
        apiDocumentPaths = listOf("navigation/hyper-slide-menu.md"),
        content = { hyper_ui.docs.ui.SlideActionMenuDemo() }
    ),

    ComponentDemo(
        id = "nav-bar",
        group = GROUP_NAVIGATION,
        title = "HyperNavBar",
        description = "顶部透明，滚动毛玻璃在状态栏顶部至导航底部内渐隐，正文区域不被覆盖。type 覆盖固定布局与 custom 插槽。",
        code = """
            val scrollState = rememberLazyListState()
            HyperNavBarPage(scrollState = scrollState,
                navBar = {
                    HyperNavBar(
                        type = HyperNavBarDefaults.TypeBackWithTitle,
                        title = { HyperText("详情") },
                        onBackPressed = onBack
                    )
                }
            ) { contentPadding ->
                LazyColumn(state = scrollState, contentPadding = contentPadding) { /* 正文 */ }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("基础返回", "type = backOnly", "只显示返回按钮"),
            DemoVariant("仅标题", "type = titleOnly", "不显示返回按钮"),
            DemoVariant("返回与标题", "type = backWithTitle", "返回图标与页面标题相邻显示"),
            DemoVariant("更多操作", "type = more", "右侧显示更多按钮"),
            DemoVariant("编辑返回", "type = edit", "标题右侧显示保存操作"),
            DemoVariant("自定义", "type = custom", "使用 leading / trailing / subtitle / child 等插槽"),
            DemoVariant("操作反馈", "onBackPressed / onMorePressed / onSavePressed", "返回、更多与保存均可点击，正文显示反馈"),
            DemoVariant("滚动材质", "HyperNavBarPage.scrollState / active", "库内观察真实主状态，仅导航读取派生值；非活动视口停用来源，160ms 过渡与20dp 模糊保持原样"),
            DemoVariant("默认尺寸", "size = default", "高度不包含顶部安全区"),
            DemoVariant("固定安全区", "库内自动处理", "背景覆盖状态栏，操作行在安全区下方；预览注入相同层级的模拟 Insets"),
            DemoVariant("叠加页面", "HyperNavBarPage", "首屏净空属于滚动内容，正文可经过透明操作层")
        ),
        apiDocumentPaths = listOf("navigation/hyper-nav-bar.md"),
        content = { NavBarDemo() },
        fullScreenPreview = true,
        fillsTopSafeArea = true,
        useReferencePreview = false
    ),
    ComponentDemo(
        id = "drawer",
        group = GROUP_NAVIGATION,
        title = "HyperDrawer",
        description = "贴边全高的模态抽屉，正文零额外间距，支持遮罩、返回键与滑动关闭。",
        code = """
            HyperDrawer(open = open, onClose = { open = false }, title = "导航") {
                HyperListTile(title = { HyperText("首页") }, onPressed = onHome)
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("方向", "placement = start/end", "逻辑方向与 RTL 镜像"),
            DemoVariant("宽度", "width = 320.dp", "最多占窗口宽度减 56dp，面板贴边全高"),
            DemoVariant("正文", "contentPadding / scrollable", "默认零间距，正文可独立滚动"),
            DemoVariant("关闭", "dismissOnClickOutside / dismissOnBackPress / gesturesEnabled", "遮罩、返回键与横向滑动分别控制"),
            DemoVariant("固定区域", "title / footer", "正文滚动时头尾保持固定")
        ),
        apiDocumentPaths = listOf("navigation/hyper-drawer.md"),
        content = { DrawerDemo() },
        fullScreenPreview = true
    ),
    ComponentDemo(
        id = "tabs",
        group = GROUP_NAVIGATION,
        title = "HyperTabs",
        description = "共享的 54dp 分类胶囊，选中底面使用材质 selection；支持等宽与横向滚动。",
        code = """
            HyperTabs(tabs = categories, selectedIndex = categories.indexOf(selected), onChanged = { selected = categories[it] }, scrollable = false) {
                HyperText(it)
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("等宽", "scrollable = false", "统一共享表面"),
            DemoVariant("滚动", "scrollable = true", "每项最小 76dp"),
            DemoVariant("选中", "selectedIndex", "44dp 指示底面；重复点击当前项不回调"),
            DemoVariant("禁用项", "itemEnabled", "阻止选中回调")
        ),
        apiDocumentPaths = listOf("navigation/hyper-tabs.md"),
        content = { SlideMenuDemo() }
    ),
    ComponentDemo(
        id = "tab-bar-single",
        group = GROUP_NAVIGATION,
        title = "HyperTabBar · 单动作",
        description = "56dp 单动作玻璃按钮，点击触发操作并更新角标。",
        code = """
            HyperTabBar(items = listOf(action), type = "single", selectedIndex = 0, onChanged = { onAction() }, badgeCount = 3) {
                HyperIcon(it.icon, it.label)
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("单动作", "type = single", "56dp 方形容器，圆角 28dp"),
            DemoVariant("角标", "badgeCount", "16dp 角标，最大显示 99+")
        ),
        apiDocumentPaths = listOf("navigation/hyper-tab-bar.md"),
        content = { SingleTabBarDemo() },
        fullScreenPreview = true
    ),
    ComponentDemo(
        id = "tab-bar-multiple",
        group = GROUP_NAVIGATION,
        title = "HyperTabBar · 多标签",
        description = "56dp 内容收缩玻璃导航，点击或拖动标签切换 160ms 平移指示器。",
        code = """
            HyperTabBar(items = tabs, itemLabel = { it.label }, selectedIndex = tabs.indexOfFirst { it.id == selectedId },
                onChanged = { selectedId = tabs[it].id }) { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    HyperIcon(item.icon, null)
                    HyperText(item.label)
                }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("多标签", "type = multiple", "单元格最小 76dp，外侧 20dp 间距"),
            DemoVariant("选中", "selectedIndex", "160ms easeOutCubic"),
            DemoVariant("拖动", "onChanged", "松手后通知调用方"),
            DemoVariant("底部安全区", "safeArea", "避让系统手势区域")
        ),
        apiDocumentPaths = listOf("navigation/hyper-tab-bar.md"),
        content = { MultipleTabBarDemo() },
        fullScreenPreview = true
    )
)
