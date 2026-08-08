package hyper_ui.docs.ui

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperBottomBar
import hyper_ui.HyperButton
import hyper_ui.HyperButtonTone
import hyper_ui.HyperDrawer
import hyper_ui.HyperDrawerHeader
import hyper_ui.HyperDrawerItem
import hyper_ui.HyperDrawerPosition
import hyper_ui.HyperIconButton
import hyper_ui.HyperIconButtonDefaults
import hyper_ui.HyperPanel
import hyper_ui.HyperPanelDefaults
import hyper_ui.HyperTopBar
import hyper_ui.docs.theme.DocsBorder

private data class DemoNavItem(
    val id: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun TopBarDemo() {
    var showBack by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.widthIn(max = 640.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HyperPanel(
            colors = HyperPanelDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            HyperTopBar(
                navigationContent = if (showBack) {
                    {
                        TopBarIconButton(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            onClick = {}
                        )
                    }
                } else {
                    null
                },
                titleContent = {
                    Text(
                        text = if (showBack) "可返回页面" else "一级页面",
                        fontSize = 28.sp,
                        lineHeight = 34.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actionContent = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TopBarIconButton(
                            imageVector = Icons.Default.Search,
                            contentDescription = "搜索",
                            onClick = {}
                        )
                        TopBarIconButton(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "更多",
                            onClick = {}
                        )
                    }
                }
            )
            HyperButton(
                onClick = { showBack = !showBack },
                tone = HyperButtonTone.Tonal
            ) {
                Text(text = if (showBack) "隐藏返回 slot" else "显示返回 slot")
            }
        }
    }
}

@Composable
fun DrawerDemo() {
    var open by remember { mutableStateOf(false) }
    var selectedPageId by remember { mutableStateOf("home") }
    var drawerPosition by remember { mutableStateOf(HyperDrawerPosition.Left) }
    val items = listOf(
        DemoNavItem("home", "首页", Icons.Default.Home),
        DemoNavItem("notice", "通知", Icons.Default.Notifications),
        DemoNavItem("settings", "设置", Icons.Default.Settings),
        DemoNavItem("about", "关于", Icons.Default.Info)
    )

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
            dismissOnClickOutside = true,
            drawerContent = {
                HyperDrawerHeader(
                    leadingContent = {
                        DrawerBadge {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    },
                    headlineContent = {
                        Text(
                            text = "HyperUI",
                            fontSize = 20.sp,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    supportingContent = {
                        Text(
                            text = "${drawerPosition.label()}抽屉",
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                )
                items.forEachIndexed { index, item ->
                    HyperDrawerItem(
                        selected = selectedPageId == item.id,
                        dividerVisible = index == 2,
                        onClick = {
                            selectedPageId = item.id
                            open = false
                        },
                        leadingContent = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        headlineContent = {
                            Text(
                                text = item.label,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "${drawerPosition.label()}抽屉示例 · 当前选中: $selectedPageId",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                DrawerPositionSelector(
                    selected = drawerPosition,
                    onSelect = { drawerPosition = it }
                )
                HyperButton(onClick = { open = true }) {
                    Text(text = "打开${drawerPosition.label()}抽屉")
                }
            }
        }
    }
}

@Composable
fun BottomBarDemo() {
    var selectedItemId by remember { mutableStateOf("home") }
    val bottomItems = listOf(
        DemoNavItem("home", "首页", Icons.Default.Home),
        DemoNavItem("recent", "最近", Icons.Default.Info),
        DemoNavItem("settings", "设置", Icons.Default.Settings)
    )
    val selectedTitle = bottomItems.first { it.id == selectedItemId }.label

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
                        text = "HyperBottomBar 只负责容器、布局、点击和选中颜色；图标文字由 item slot 决定。",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
                HyperBottomBar(
                    items = bottomItems,
                    itemSelected = { it.id == selectedItemId },
                    onItemClick = { item -> selectedItemId = item.id }
                ) { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = LocalContentColor.current,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = item.label,
                            color = LocalContentColor.current,
                            fontSize = 12.sp,
                            lineHeight = 14.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBarIconButton(
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    HyperIconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
        )
    }
}

@Composable
private fun DrawerBadge(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        content()
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
        onClick = onClick,
        minHeight = 36.dp,
        tone = if (selected) HyperButtonTone.Primary else HyperButtonTone.Outline
    ) {
        Text(
            text = text,
            fontSize = 13.sp
        )
    }
}

private fun HyperDrawerPosition.label(): String = when (this) {
    HyperDrawerPosition.Left -> "左侧"
    HyperDrawerPosition.Right -> "右侧"
    HyperDrawerPosition.Top -> "顶部"
    HyperDrawerPosition.Bottom -> "底部"
}
