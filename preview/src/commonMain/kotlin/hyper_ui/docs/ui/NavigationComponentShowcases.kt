package hyper_ui.docs.ui

import hyper_ui.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.docs.theme.DocsBorder

@Composable
fun TopBarDemo() {
    var showBack by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.widthIn(max = 640.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 场景 1：无后退按钮（onBack 为 null）
        HyperPanel(containerColor = MaterialTheme.colorScheme.surface) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "onBack = null → 不显示后退按钮",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
                HyperTopBar(title = "无返回按钮")
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
            }
        }

        // 场景 2：可交互切换后退按钮的显示/隐藏
        HyperPanel(containerColor = MaterialTheme.colorScheme.surface) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = if (showBack)
                        "onBack = {} → 显示后退按钮"
                    else
                        "onBack = null → 不显示后退按钮",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
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
                HyperButton(
                    text = if (showBack) "隐藏后退按钮" else "显示后退按钮",
                    onClick = { showBack = !showBack },
                    minHeight = 36.dp,
                    horizontalPadding = 12.dp,
                    fontSize = 13.sp
                )
            }
        }

        // 场景 3：完整示例（有后退按钮 + 右侧操作）
        HyperPanel(containerColor = MaterialTheme.colorScheme.surface) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "onBack = {} → 显示后退按钮（右侧双操作）",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
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
            }
        }

        // 说明卡片
        HyperPanel(containerColor = MaterialTheme.colorScheme.surface) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "后退按钮显示逻辑",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "HyperTopBar 的后退按钮不是自动显示的，而是通过 onBack 参数由调用方控制：",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                Text(
                    text = "• onBack = null（默认值）→ 不显示后退按钮\n• onBack 传入回调函数 → 显示后退按钮",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                Text(
                    text = "组件不内置任何导航逻辑（如 popBackStack），后退点击行为完全由调用方在 onBack 中自行实现。",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun DrawerDemo() {
    var open by remember { mutableStateOf(false) }
    var selectedPageId by remember { mutableStateOf("home") }
    var drawerPosition by remember { mutableStateOf(HyperDrawerPosition.Left) }
    var dismissOnClickOutside by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .widthIn(max = 440.dp)
            .height(420.dp)
            .clip(RoundedCornerShape(28.dp))
            .border(width = 1.dp, color = DocsBorder, shape = RoundedCornerShape(28.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        HyperDrawer(
            open = open,
            onDismissRequest = { open = false },
            position = drawerPosition,
            dismissOnClickOutside = dismissOnClickOutside,
            drawerContent = {
                HyperDrawerHeader(
                    title = "HyperUI",
                    description = "${drawerPosition.label()}抽屉",
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(HyperColors.pageBackground)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "${drawerPosition.label()}抽屉示例 · 当前选中: $selectedPageId",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (dismissOnClickOutside) "点击抽屉外部可关闭" else "仅点击菜单项可关闭",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                DrawerPositionSelector(
                    selected = drawerPosition,
                    onSelect = { drawerPosition = it }
                )
                HyperButton(
                    text = "打开${drawerPosition.label()}抽屉",
                    onClick = { open = true }
                )
            }
        }
    }
}

@Composable
fun BottomBarDemo() {
    var selectedItemId by remember { mutableStateOf("home") }
    val bottomItems = listOf(
        HyperBottomBarItem("home", "首页", Icons.Default.Home),
        HyperBottomBarItem("recent", "最近", Icons.Default.Info),
        HyperBottomBarItem("settings", "设置", Icons.Default.Settings)
    )
    val selectedTitle = when (selectedItemId) {
        "home" -> "首页"
        "recent" -> "最近"
        else -> "设置"
    }
    val bottomBarConfig = HyperBottomBarConfig(
        height = 72.dp,
        contentHeight = 64.dp,
        horizontalPadding = 28.dp,
        backgroundAlpha = 0.90f
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 360.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .height(430.dp)
                .clip(RoundedCornerShape(28.dp))
                .border(width = 1.dp, color = DocsBorder, shape = RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "$selectedTitle 内容区",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "HyperBottomBar 只渲染底栏并发出点击事件，页面状态由调用方维护。",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
                HyperBottomBar(
                    items = bottomItems,
                    selectedItemId = selectedItemId,
                    config = bottomBarConfig,
                    onItemClick = { item -> selectedItemId = item.id }
                )
            }
        }
        Text(
            text = "说明：组件不内置页面切换逻辑；如需跳转，请在 onItemClick 中由调用方调用自己的导航系统。",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun DrawerPositionSelector(
    selected: HyperDrawerPosition,
    onSelect: (HyperDrawerPosition) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DrawerPositionButton(
                text = "左侧",
                selected = selected == HyperDrawerPosition.Left,
                onClick = { onSelect(HyperDrawerPosition.Left) }
            )
            DrawerPositionButton(
                text = "右侧",
                selected = selected == HyperDrawerPosition.Right,
                onClick = { onSelect(HyperDrawerPosition.Right) }
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DrawerPositionButton(
                text = "顶部",
                selected = selected == HyperDrawerPosition.Top,
                onClick = { onSelect(HyperDrawerPosition.Top) }
            )
            DrawerPositionButton(
                text = "底部",
                selected = selected == HyperDrawerPosition.Bottom,
                onClick = { onSelect(HyperDrawerPosition.Bottom) }
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
        text = text,
        onClick = onClick,
        minHeight = 36.dp,
        horizontalPadding = 12.dp,
        fontSize = 13.sp,
        variant = if (selected) HyperButtonVariant.Primary else HyperButtonVariant.Default
    )
}

private fun HyperDrawerPosition.label(): String = when (this) {
    HyperDrawerPosition.Left -> "左侧"
    HyperDrawerPosition.Right -> "右侧"
    HyperDrawerPosition.Top -> "顶部"
    HyperDrawerPosition.Bottom -> "底部"
}
