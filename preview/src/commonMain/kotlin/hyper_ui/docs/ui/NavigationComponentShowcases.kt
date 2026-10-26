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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperTabBar
import hyper_ui.HyperDrawer
import hyper_ui.HyperDrawerPlacement
import hyper_ui.HyperTabs

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
            HyperText("右侧抽屉与返回值", size = "small")
            HyperButton(onPressed = { placement = HyperDrawerPlacement.end; open = true }) { HyperText("打开抽屉") }
            HyperText("返回结果：$selected", size = "small")
            androidx.compose.foundation.layout.Spacer(Modifier.height(12.dp))
            HyperText("左侧抽屉与固定底部操作区", size = "small")
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
fun SlideMenuDemo() {
    val categories = listOf("全部", "恶意网址", "广告", "恶意跳转", "打开应用")
    var selected by remember { mutableStateOf("全部") }
    var scrollable by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HyperButton({ scrollable = !scrollable }) {
            HyperText(if (scrollable) "切换等宽" else "切换滚动")
        }
        HyperTabs(categories, categories.indexOf(selected), { selected = categories[it] }, scrollable = scrollable,
            itemEnabled = { it != "打开应用" }) { HyperText(it, maxLines = 1) }
        HyperText("当前选中：$selected", size = "small")
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
    HyperScaffold(
        topBar = { HyperNavBar(type = HyperNavBarDefaults.TypeTitleOnly, title = { HyperText("页面布局") }) },
        bottomBar = {
            HyperTabBar(items = listOf("首页", "设置"), selectedIndex = selected, onChanged = { selected = it },
                floating = false, itemLabel = { it }) { HyperText(it) }
        },
        floatingActionButton = { HyperButton(onPressed = { selected = 0 }, type = "filled") { HyperText("返回首页") } }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            HyperText(if (selected == 0) "首页正文" else "设置正文")
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
