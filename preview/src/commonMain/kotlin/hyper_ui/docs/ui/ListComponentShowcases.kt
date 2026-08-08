package hyper_ui.docs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperCheckbox
import hyper_ui.HyperLazyList
import hyper_ui.HyperList
import hyper_ui.HyperListItem
import hyper_ui.HyperMenuGroup
import hyper_ui.HyperMenuGroupDefaults
import hyper_ui.HyperMenuItem
import hyper_ui.HyperRadioButton
import hyper_ui.HyperSwitch

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
                leadingContent = { ListIcon(iconFor(item)) },
                headlineContent = { ListTitle(item) },
                supportingContent = { ListDescription("点击查看配置") },
                dividerVisible = item != items.last(),
                dividerInset = 70.dp
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
                leadingContent = { ListIcon(iconFor(item)) },
                headlineContent = { ListTitle(item) },
                supportingContent = { ListDescription("点击查看配置") },
                dividerVisible = item != items.last(),
                dividerInset = 70.dp
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
        HyperMenuGroup(
            colors = HyperMenuGroupDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            HyperMenuItem(
                leadingContent = { ListIcon(Icons.Default.Lock) },
                headlineContent = { ListTitle("安全中心") },
                supportingContent = { ListDescription("登录保护和设备管理") },
                dividerVisible = true,
                dividerInset = 70.dp,
                onClick = { selectedMenu = "安全中心" },
                trailingContent = {
                    Text(
                        text = "已开启",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            )
            HyperMenuItem(
                leadingContent = { ListIcon(Icons.Default.Settings) },
                headlineContent = { ListTitle("主题外观") },
                supportingContent = { ListDescription("颜色、圆角和显示密度") },
                dividerVisible = true,
                dividerInset = 70.dp,
                onClick = { selectedMenu = "主题外观" }
            )
            HyperMenuItem(
                leadingContent = { ListIcon(Icons.Default.Info) },
                headlineContent = { ListTitle("关于应用") },
                dividerVisible = true,
                dividerInset = 70.dp,
                onClick = { selectedMenu = "关于应用" }
            )
            HyperMenuItem(
                leadingContent = { ListIcon(Icons.Default.Notifications) },
                headlineContent = { ListTitle("推送通知") },
                supportingContent = { ListDescription("接收系统消息提醒") },
                dividerVisible = true,
                dividerInset = 70.dp,
                trailingContent = {
                    HyperSwitch(
                        checked = pushEnabled,
                        onCheckedChange = { pushEnabled = it }
                    )
                }
            )
            HyperMenuItem(
                leadingContent = { ListIcon(Icons.Default.Check) },
                headlineContent = { ListTitle("自动同步") },
                supportingContent = { ListDescription("网络可用时自动刷新数据") },
                dividerVisible = true,
                dividerInset = 70.dp,
                trailingContent = {
                    HyperCheckbox(
                        checked = autoSync,
                        onCheckedChange = { autoSync = it }
                    )
                }
            )
            HyperMenuItem(
                leadingContent = { ListIcon(Icons.Default.Settings) },
                headlineContent = { ListTitle("均衡模式") },
                supportingContent = { ListDescription("平衡性能与续航") },
                dividerVisible = true,
                dividerInset = 70.dp,
                trailingContent = {
                    HyperRadioButton(
                        selected = mode == "balanced",
                        onClick = { mode = "balanced" }
                    )
                }
            )
            HyperMenuItem(
                leadingContent = { ListIcon(Icons.Default.Star) },
                headlineContent = { ListTitle("性能模式") },
                supportingContent = { ListDescription("优先保证流畅度") },
                trailingContent = {
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

@Composable
private fun ListIcon(imageVector: ImageVector) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ListTitle(text: String) {
    Text(
        text = text,
        color = LocalContentColor.current,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun ListDescription(text: String) {
    Text(
        text = text,
        color = LocalContentColor.current,
        fontSize = 14.sp,
        lineHeight = 19.sp
    )
}

private fun iconFor(item: String): ImageVector = when (item) {
    "系统设置" -> Icons.Default.Settings
    "通知权限" -> Icons.Default.Notifications
    "同步策略" -> Icons.Default.Check
    else -> Icons.Default.Lock
}
