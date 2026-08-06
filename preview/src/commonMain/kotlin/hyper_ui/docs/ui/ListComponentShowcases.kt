package hyper_ui.docs.ui

import hyper_ui.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HyperListDemo() {
    val items = listOf("系统设置", "通知权限", "同步策略", "安全中心")

    Box(
        modifier = Modifier
            .widthIn(max = 560.dp)
            .height(280.dp)
    ) {
        HyperList(items = items) { item ->
            HyperListItem(
                title = item,
                description = "点击查看配置",
                leadingIcon = when (item) {
                    "系统设置" -> Icons.Default.Settings
                    "通知权限" -> Icons.Default.Notifications
                    "同步策略" -> Icons.Default.Check
                    else -> Icons.Default.Lock
                },
                showDivider = item != items.last()
            )
        }
    }
}

@Composable
fun LazyListDemo() {
    val items = listOf("系统设置", "通知权限", "同步策略", "安全中心")

    Box(
        modifier = Modifier
            .widthIn(max = 560.dp)
            .height(280.dp)
    ) {
        HyperLazyList(items = items) { item ->
            HyperListItem(
                title = item,
                description = "点击查看配置",
                leadingIcon = when (item) {
                    "系统设置" -> Icons.Default.Settings
                    "通知权限" -> Icons.Default.Notifications
                    "同步策略" -> Icons.Default.Check
                    else -> Icons.Default.Lock
                },
                showDivider = item != items.last()
            )
        }
    }
}

@Composable
fun MenuDemo() {
    var selectedMenu by remember { mutableStateOf("尚未选择菜单") }
    var pushEnabled by remember { mutableStateOf(true) }
    var autoSync by remember { mutableStateOf(false) }
    var mode by remember { mutableStateOf("balanced") }

    Column(
        modifier = Modifier.widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperMenuGroup(containerColor = MaterialTheme.colorScheme.surface) {
            HyperMenuItem(
                title = "安全中心",
                description = "登录保护和设备管理",
                leadingIcon = Icons.Default.Lock,
                showDivider = true,
                onClick = { selectedMenu = "安全中心" },
                trailing = {
                    Text(
                        text = "已开启",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            )
            HyperMenuItem(
                title = "主题外观",
                description = "颜色、圆角和显示密度",
                leadingIcon = Icons.Default.Settings,
                showDivider = true,
                onClick = { selectedMenu = "主题外观" }
            )
            HyperMenuItem(
                title = "关于应用",
                leadingIcon = Icons.Default.Info,
                showDivider = true,
                onClick = { selectedMenu = "关于应用" }
            )
            HyperMenuItem(
                title = "推送通知",
                description = "接收系统消息提醒",
                leadingIcon = Icons.Default.Notifications,
                showDivider = true,
                trailing = {
                    HyperSwitch(
                        checked = pushEnabled,
                        onCheckedChange = { pushEnabled = it }
                    )
                }
            )
            HyperMenuItem(
                title = "自动同步",
                description = "网络可用时自动刷新数据",
                leadingIcon = Icons.Default.Check,
                showDivider = true,
                trailing = {
                    HyperCheckbox(
                        checked = autoSync,
                        onCheckedChange = { autoSync = it }
                    )
                }
            )
            HyperMenuItem(
                title = "均衡模式",
                description = "平衡性能与续航",
                leadingIcon = Icons.Default.Settings,
                showDivider = true,
                trailing = {
                    HyperRadioButton(
                        selected = mode == "balanced",
                        onClick = { mode = "balanced" }
                    )
                }
            )
            HyperMenuItem(
                title = "性能模式",
                description = "优先保证流畅度",
                leadingIcon = Icons.Default.Star,
                trailing = {
                    HyperRadioButton(
                        selected = mode == "performance",
                        onClick = { mode = "performance" }
                    )
                }
            )
        }

        Text(
            text = selectedMenu,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
    }
}
