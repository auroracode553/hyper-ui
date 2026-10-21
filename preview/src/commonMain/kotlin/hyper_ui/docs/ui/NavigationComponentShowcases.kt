/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/NavigationComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui
import hyper_ui.*
import hyper_ui.docs.theme.LocalDocsColorScheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperTabBar
import hyper_ui.HyperDrawer
import hyper_ui.HyperDrawerHeader
import hyper_ui.HyperDrawerItem
import hyper_ui.HyperDrawerPlacement
import hyper_ui.HyperTabs

private data class DemoNavItem(
    val id: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

/** 贴底预览只在标签下方留出与手机手势条之间的可见间距。 */
private val DockedTabBarGestureClearance = 8.dp

@Composable
fun DrawerDemo() {
    var drawerOpen by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("首页") }
    var position by remember { mutableStateOf(HyperDrawerPlacement.end) }
    var padding by remember { mutableStateOf(true) }
    var scroll by remember { mutableStateOf(true) }
    Box(Modifier.fillMaxSize()) {
        HyperDrawer(
            open = drawerOpen,
            onClose = { drawerOpen = false },
            placement = position,
            title = "导航",
            footer = { HyperText("固定页脚", size = "small") },
            defaultSetPadding = padding,
            scrollable = scroll,
            child = {
                HyperDrawerHeader(title = { HyperText("HyperUI") },
                    subtitle = { HyperText("选择页面") })
                listOf("首页", "通知", "设置", "关于").forEach { page ->
                    HyperDrawerItem(selected = page == selected,
                        onPressed = { selected = page; drawerOpen = false },
                        title = { HyperText(page) })
                }
            },
            content = {
                Column(Modifier.fillMaxSize().background(HyperColors.pageBackground).padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    HyperText("当前页面：$selected")
                    DrawerPositionSelector(position, { position = it })
                    HyperButton(onPressed = { padding = !padding }) {
                        HyperText(if (padding) "默认留白" else "自定义留白")
                    }
                    HyperButton(onPressed = { scroll = !scroll }) {
                        HyperText(if (scroll) "允许滚动" else "关闭滚动")
                    }
                    HyperButton({ drawerOpen = true }, colorStyle = "primary", type = "filled") { HyperText("打开抽屉") }
                }
            }
        )
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
private fun TabBarDemo(single: Boolean) {
    var selectedId by remember { mutableStateOf("home") }
    var enabled by remember { mutableStateOf(true) }
    var count by remember { mutableStateOf(3) }
    val items = listOf(
        DemoNavItem("home", "首页", HyperReferenceIcons.Home),
        DemoNavItem("recent", "最近", HyperReferenceIcons.Info),
        DemoNavItem("notice", "消息", HyperReferenceIcons.Notifications),
        DemoNavItem("settings", "设置", HyperReferenceIcons.Settings)
    )
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton({ enabled = !enabled }) { HyperText(if (enabled) "禁用" else "启用") }
            if (single) HyperButton({ count = if (count == 0) 3 else 0 }) { HyperText("切换角标") }
        }
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            HyperText("当前页面：${items.first { it.id == selectedId }.label}")
        }
        HyperTabBar(
            items = if (single) listOf(items.first()) else items,
            onChanged = { selectedId = items[it].id; if (single) count++ },
            type = if (single) "single" else "multiple",
            enabled = enabled,
            selectedIndex = if (single) 0 else items.indexOfFirst { it.id == selectedId },
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

@Composable
private fun DrawerBadge(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(LocalDocsColorScheme.current.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun DrawerPositionSelector(
    selected: HyperDrawerPlacement,
    onSelect: (HyperDrawerPlacement) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DrawerPositionButton(
                text = "起始侧",
                selected = selected == HyperDrawerPlacement.start,
                onClick = { onSelect(HyperDrawerPlacement.start) }
            )
            DrawerPositionButton(
                text = "结束侧",
                selected = selected == HyperDrawerPlacement.end,
                onClick = { onSelect(HyperDrawerPlacement.end) }
            )
        }

    }
}

@Composable
private fun DrawerPositionButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    HyperButton(
        onPressed = onClick,
        modifier = Modifier.height(36.dp),
        colorStyle = if (selected) "primary" else null
    , type = "filled") {
        HyperText(
            text = text,
            fontSize = 13.sp
        )
    }
}

private fun HyperDrawerPlacement.label(): String = when (this) {
    HyperDrawerPlacement.start -> "左侧"
    HyperDrawerPlacement.end -> "右侧"
}

/** 侧滑操作与分类按钮分开演示，保留受控展开和禁用状态。 */
@Composable
fun SlideActionMenuDemo() {
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
