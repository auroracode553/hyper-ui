/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/NavigationComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.FilterBarDemo

import hyper_ui.docs.ui.NavBarDemo
import hyper_ui.docs.ui.ScrollGradientDemo
import hyper_ui.docs.ui.DrawerDemo
import hyper_ui.docs.ui.SingleTabBarDemo
import hyper_ui.docs.ui.MultipleTabBarDemo
import hyper_ui.docs.ui.TrailingActionTabBarDemo
import hyper_ui.docs.ui.DockedTabBarDemo

private const val GROUP_NAVIGATION = "导航组件"

internal fun navigationComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "scroll-gradient", group = GROUP_NAVIGATION, title = "HyperScrollGradient",
        description = "可独立组合的滚动渐隐材质，不增加栏高或安全区，不采样正文。",
        code = """
            HyperNavBarScrollScope(scrollState) {
                Box {
                    /* 主滚动正文 */
                    HyperScrollGradient(modifier = Modifier.fillMaxWidth()) { /* 清晰的前景操作 */ }
                }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("自动感知", "HyperNavBarScrollScope", "共用主滚动状态，160ms 渐显"),
            DemoVariant("受控显示", "scrolledUnder = true", "强制显示；默认跟随库内作用域"),
            DemoVariant("覆盖范围", "modifier / child", "调用方决定实际尺寸，组件不增加安全区或永久占位")
        ),
        apiDocumentPaths = listOf("navigation/hyper-scroll-gradient.md"),
        content = { ScrollGradientDemo() },
        fullScreenPreview = true, fillsTopSafeArea = true
    ),
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
        description = "standard 显示导航操作区，simple 仅保护状态栏与挖孔；两种形态共用滚动渐变，不采样正文。",
        code = """
            val scrollState = rememberLazyListState()
            HyperNavBarPage(scrollState = scrollState,
                navBar = {
                    HyperNavBar(
                        layout = HyperNavBarDefaults.LayoutBackWithTitle,
                        title = { HyperText("详情") },
                        onBackPressed = onBack
                    )
                }
            ) { contentPadding ->
                LazyColumn(state = scrollState, contentPadding = contentPadding) { /* 正文 */ }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("简约", "type = simple", "仅真实状态栏/挖孔高度，无标题或操作行"),
            DemoVariant("标准", "type = standard", "保留 44dp 操作行、标题与返回/操作插槽"),
            DemoVariant("基础返回", "layout = backOnly", "只显示返回按钮"),
            DemoVariant("仅标题", "layout = titleOnly", "不显示返回按钮"),
            DemoVariant("返回与标题", "layout = backWithTitle", "返回图标与页面标题相邻显示"),
            DemoVariant("更多操作", "layout = more", "右侧显示更多按钮"),
            DemoVariant("编辑返回", "layout = edit", "标题右侧显示保存操作"),
            DemoVariant("自定义", "layout = custom", "使用 leading / trailing / subtitle / child 等插槽"),
            DemoVariant("操作反馈", "onBackPressed / onMorePressed / onSavePressed", "返回、更多与保存均可点击，正文显示反馈"),
            DemoVariant("滚动材质", "HyperNavBarPage.scrollState / active", "库内观察真实主状态，仅导航读取派生值；非活动视口停用来源，160ms 渐变过渡，不采样正文"),
            DemoVariant("统一规格", "Height = 44.dp", "高度不包含顶部安全区"),
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
        description = "默认总高 40dp 的等宽或内容宽度分段控制器；共用轨道、选中底板与受控标签插槽。",
        code = """
            HyperTabs(
                tabs = periods,
                selectedIndex = periods.indexOf(selectedPeriod),
                onChanged = { selectedPeriod = periods[it] }
            ) { period ->
                HyperText(period.label)
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("默认尺寸", "Height = 40.dp", "32dp 分段与 4dp 轨道留白；等宽底板平移 260ms"),
            DemoVariant("禁用项", "itemEnabled", "单独禁用指定分段"),
            DemoVariant("自定义颜色", "HyperTabsDefaults.colors", "覆盖选中项和内容色")
        ),
        apiDocumentPaths = listOf("navigation/hyper-tabs.md"),
        content = { hyper_ui.docs.ui.TabsDemo() }
    ),
    ComponentDemo(
        id = "filter-bar", group = GROUP_NAVIGATION, title = "HyperFilterBar",
        description = "浅底细线的紧凑分类按钮，淡主题色选中态，最小高度 32dp，支持横向滑动。",
        code = "HyperFilterBar(items = categories, selectedIndex = selected, onChanged = { selected = it }) { HyperText(it) }",
        variants = listOf(
            DemoVariant("轻量按钮", "默认样式", "普通项浅底细线；选中淡主题底色与主题色文字；无阴影"),
            DemoVariant("横向滚动", "items / selectedIndex", "超出宽度可左右滑动，外部切换时显示选中项"),
            DemoVariant("禁用", "enabled / itemEnabled", "支持整栏及单项禁用")
        ),
        apiDocumentPaths = listOf("navigation/hyper-filter-bar.md"),
        content = { FilterBarDemo() }
    ),
    ComponentDemo(
        id = "tab-bar-single",
        group = GROUP_NAVIGATION,
        title = "HyperTabBar · 单动作",
        description = "固定直径 48dp 的单动作玻璃按钮，点击触发操作并更新角标。",
        code = """
            HyperTabBar(items = listOf(action), type = "single", selectedIndex = 0, onChanged = { onAction() }, badgeCount = 3) {
                HyperIcon(it.icon, it.label)
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("单动作", "type = single", "48dp 圆形容器，圆角 24dp"),
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
    ),
    ComponentDemo(
        id = "tab-bar-trailing", group = GROUP_NAVIGATION, title = "HyperTabBar · 右侧操作",
        description = "56dp 主导航与 48dp 独立操作垂直居中，点击右侧入口可重复触发。",
        code = """
            Row(verticalAlignment = Alignment.CenterVertically) {
                HyperTabBar(items = tabs, selectedIndex = index, onChanged = onTab) { HyperText(it) }
                HyperTabBar(items = listOf("打开"), type = "single", selectedIndex = null,
                    onChanged = { onOpen() }) { HyperIcon(openIcon, "打开") }
            }
        """.trimIndent(),
        variants = listOf(DemoVariant("默认尺寸", "type = single/multiple", "single 固定直径 48dp，multiple 默认高 56dp，不由调用方覆盖")),
        apiDocumentPaths = listOf("navigation/hyper-tab-bar.md"),
        content = { TrailingActionTabBarDemo() }, fullScreenPreview = true
    ),
    ComponentDemo(
        id = "tab-bar-docked", group = GROUP_NAVIGATION, title = "HyperTabBar · 非悬浮",
        description = "默认高 56dp，全宽贴底，无胶囊或外间距；动作模式支持重复点击、禁用项且不响应拖动。",
        code = """
            HyperTabBar(items = actions, floating = false, selectedIndex = null,
                itemEnabled = { it.enabled }, onChanged = onAction, itemLabel = { it.label }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    HyperIcon(it.icon, null)
                    HyperText(it.label)
                }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("非悬浮", "floating = false", "全宽平直表面，底部安全区由库处理"),
            DemoVariant("重复动作", "selectedIndex = null", "不显示选中块，不响应拖动"),
            DemoVariant("禁用项", "itemEnabled", "最近入口禁用，连续点击其他入口可观察计数")
        ),
        apiDocumentPaths = listOf("navigation/hyper-tab-bar.md"),
        content = { DockedTabBarDemo() }, fullScreenPreview = true
    ),
    ComponentDemo(
        id = "scaffold", group = GROUP_NAVIGATION, title = "HyperScaffold",
        description = "Compose 基础页面宿主，按实际栏高提供正文净空，不依赖 Material。",
        code = "HyperScaffold(bottomBar = { BottomActions() }) { padding -> LazyColumn(contentPadding = padding) { /* 正文 */ } }",
        variants = listOf(DemoVariant("页面插槽", "topBar / bottomBar / floatingActionButton", "统一画布与安全区")),
        apiDocumentPaths = listOf("navigation/hyper-scaffold.md"),
        content = { hyper_ui.docs.ui.ScaffoldDemo() }, fullScreenPreview = true
    )
)
