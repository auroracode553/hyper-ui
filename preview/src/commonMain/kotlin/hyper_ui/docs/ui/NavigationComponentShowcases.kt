/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/NavigationComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.FolderOpen
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Settings
import hyper_ui.*

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperTabBar
import hyper_ui.HyperDrawer
import hyper_ui.HyperDrawerPlacement
import hyper_ui.HyperTabs
import kotlinx.coroutines.launch

/** 独立材质与 Navbar 使用同一滚动作用域；净空由真实覆盖层尺寸交给滚动内容。 */
@Composable
fun ScrollGradientDemo() {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    var topClearance by remember { mutableStateOf(0.dp) }
    var forceVisible by remember { mutableStateOf(false) }
    HyperNavBarScrollScope(scrollState) {
        HyperBackdrop(modifier = Modifier.fillMaxSize(), background = {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize().background(HyperColors.pageBackground),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = topClearance + 12.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        HyperText("向上滚动观察渐显，回到顶部恢复透明。")
                        HyperText("渐变不采样正文，前景操作保持清晰。")
                        HyperButton(onPressed = { forceVisible = !forceVisible }) {
                            HyperText(if (forceVisible) "恢复自动感知" else "固定显示材质")
                        }
                    }
                }
                items(20) { index ->
                    HyperCard(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            HyperText("灵感 ${index + 1}")
                            HyperText("让内容自然流动，前景操作保持清晰。")
                        }
                    }
                }
            }
        }) {
            HyperScrollGradient(
                scrolledUnder = forceVisible,
                modifier = Modifier.fillMaxWidth().onSizeChanged {
                    topClearance = with(density) { it.height.toDp() }
                }
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(top = PreviewTopSafeArea).height(44.dp).padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HyperText("滚动渐变")
                    HyperButton(onPressed = { coroutineScope.launch { scrollState.animateScrollToItem(0) } }) {
                        HyperText("回到顶部")
                    }
                }
            }
        }
    }
}

private data class DemoNavItem(
    val id: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun DrawerDemo() {
    var open by remember { mutableStateOf(false) }
    var placement by remember { mutableStateOf(HyperDrawerPlacement.end) }
    var selected by remember { mutableStateOf("尚未选择") }
    var favorites by remember { mutableStateOf(true) }
    val filters = placement == HyperDrawerPlacement.start
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperText("右侧抽屉与返回值")
            HyperButton(onPressed = { placement = HyperDrawerPlacement.end; open = true }) { HyperText("打开抽屉") }
            HyperText("返回结果：$selected")
            androidx.compose.foundation.layout.Spacer(Modifier.height(12.dp))
            HyperText("左侧抽屉与固定底部操作区")
            HyperButton(onPressed = { placement = HyperDrawerPlacement.start; open = true }) { HyperText("左侧筛选") }
        }
        HyperDrawer(
            open = open,
            onClose = { open = false },
            placement = placement,
            width = if (filters) 300.dp else 320.dp,
            title = if (filters) "筛选条件" else "选择工作空间",
            footer = if (filters) {
                { HyperButton(onPressed = { open = false }, modifier = Modifier.fillMaxWidth(),
                    type = "filled", colorStyle = "primary") { HyperText("应用筛选") } }
            } else null,
        ) {
            if (filters) {
                HyperCheckbox(favorites, { favorites = it == true }, label = "仅显示收藏")
                HyperCheckbox(false, label = "包含已归档")
                HyperRadio("all", "all", label = "全部时间")
            } else {
                listOf("产品设计", "移动端", "文档站").forEach { workspace ->
                    HyperListTile(title = { HyperText(workspace) },
                        onPressed = { selected = workspace; open = false })
                }
            }
        }
    }
}

@Composable
fun FilterBarDemo() {
    val categories = listOf("全部", "PDF", "Word", "文本", "Markdown", "表格", "演示文稿")
    var selected by remember { mutableStateOf(0) }
    var enabled by remember { mutableStateOf(true) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HyperSwitch(value = enabled, onChanged = { enabled = it }, label = "允许筛选")
        HyperFilterBar(categories, selected, { selected = it }, enabled = enabled,
            itemEnabled = { it != "演示文稿" }) { HyperText(it, maxLines = 1) }
        HyperText("当前选中：${categories[selected]}")
        HyperText("点击分类切换，左右滑动查看更多", color = HyperColors.secondaryText)
    }
}

@Composable
fun SingleTabBarDemo() { TabBarDemo(single = true) }

@Composable
fun MultipleTabBarDemo() { TabBarDemo(single = false) }

@Composable
fun TrailingActionTabBarDemo() {
    var selected by remember { mutableStateOf(0) }
    var actionCount by remember { mutableStateOf(0) }
    val labels = listOf("最近", "列表")
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            HyperText("当前：${labels[selected]} · 打开 $actionCount 次")
        }
        Row(Modifier.fillMaxWidth().padding(HyperTabBarDefaults.FloatingMargin),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically) {
            HyperTabBar(items = labels, selectedIndex = selected, onChanged = { selected = it },
                modifier = Modifier.weight(1f, fill = false), itemLabel = { it },
                margin = androidx.compose.foundation.layout.PaddingValues(0.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    HyperIcon(if (it == labels.first()) Lucide.Info else Lucide.House, it)
                    HyperText(it)
                }
            }
            HyperTabBar(items = listOf("打开"), type = "single", selectedIndex = null,
                onChanged = { actionCount++ }, itemLabel = { it }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    HyperIcon(Lucide.FolderOpen, it)
                    HyperText(it)
                }
            }
        }
    }
}

@Composable
fun DockedTabBarDemo() { TabBarDemo(single = false, floating = false, actions = true) }

@Composable
fun ScaffoldDemo() {
    var selected by remember { mutableStateOf(0) }
    val listState = rememberLazyListState()
    HyperNavBarScrollScope(listState) {
        HyperScaffold(
            topBar = { HyperNavBar(layout = HyperNavBarDefaults.LayoutTitleOnly, title = { HyperText("页面布局") }) },
            bottomBar = {
                HyperTabBar(items = listOf("首页", "设置"), selectedIndex = selected, onChanged = { selected = it },
                    itemLabel = { it }) { HyperText(it) }
            },
            floatingActionButton = { HyperButton(onPressed = { selected = 0 }, type = "filled") { HyperText("返回首页") } }
        ) { padding ->
            // 上下净空跟随列表滚动，示例也不能把栏高 padding 放到视口外。
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 14.dp, end = 14.dp,
                    top = padding.calculateTopPadding() + 12.dp,
                    bottom = padding.calculateBottomPadding() + 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(30) { index ->
                    HyperCard(modifier = Modifier.fillMaxWidth()) {
                        HyperText("${if (selected == 0) "首页正文" else "设置正文"} · ${index + 1}")
                    }
                }
            }
        }
    }
}

@Composable
private fun TabBarDemo(single: Boolean, floating: Boolean = true, actions: Boolean = false) {
    var selectedId by remember { mutableStateOf("home") }
    var count by remember { mutableStateOf(if (actions) 0 else 3) }
    val items = listOf(
        DemoNavItem("home", "首页", Lucide.House),
        DemoNavItem("recent", "最近", Lucide.Info),
        DemoNavItem("notice", "消息", Lucide.Bell),
        DemoNavItem("settings", "设置", Lucide.Settings)
    )
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        if (single) HyperButton({ count = if (count == 0) 3 else 0 }) { HyperText("切换角标") }
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            HyperText(if (actions) "最近动作：${items.first { it.id == selectedId }.label} · $count 次" else "当前页面：${items.first { it.id == selectedId }.label}")
        }
        HyperTabBar(
            items = if (single) listOf(items.first()) else items,
            onChanged = { selectedId = items[it].id; if (single || actions) count++ },
            type = if (single) "single" else "multiple",
            selectedIndex = if (actions) null else if (single) 0 else items.indexOfFirst { it.id == selectedId },
            floating = floating,
            itemEnabled = { !actions || it.id != "recent" },
            itemLabel = { it.label },
            badgeCount = count
        ) { item ->
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(1.dp)) {
                HyperIcon(item.icon, item.label)
                if (!single) HyperText(item.label, maxLines = 1)
            }
        }
    }
}

/** 侧滑操作与分类按钮分开演示，保留受控展开和禁用状态。 */
@Composable
fun SlideActionMenuDemo() {
    // 内容采用共用卡片层级，侧滑操作区的布局与手势维持原样。
    var reveal by remember { mutableStateOf(HyperSlideMenuReveal.Closed) }
    var enabled by remember { mutableStateOf(true) }
    var result by remember { mutableStateOf("拖动列表行或用按钮展开") }
    Column(Modifier.widthIn(max = 440.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton(onPressed = { reveal = HyperSlideMenuReveal.Start }) { HyperText("起始侧") }
            HyperButton(onPressed = { reveal = HyperSlideMenuReveal.End }) { HyperText("结束侧") }
            HyperButton({ enabled = !enabled }) { HyperText(if (enabled) "禁用" else "启用") }
        }
        HyperSlideMenu(reveal, { reveal = it }, enabled = enabled,
            startActions = listOf(HyperSlideAction("置顶", { result = "已置顶" })),
            endActions = listOf(HyperSlideAction("删除", { result = "已删除" }))) {
            HyperListTile(title = { HyperText("侧滑查看操作") },
                subtitle = { HyperText("释放时按位置与速度吸附") })
        }
        HyperText("$reveal · $result", fontSize = 13.sp)
    }
}
