/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/ListComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperCheckbox
import hyper_ui.HyperList
import hyper_ui.HyperListDefaults
import hyper_ui.HyperListItem
import hyper_ui.HyperListItemDefaults
import hyper_ui.HyperMenuList
import hyper_ui.HyperRadio
import hyper_ui.HyperSwitch

@Composable
fun HyperMenuListDemo() {
    val items = listOf("系统设置", "通知权限", "同步策略", "安全中心")
    var pushEnabled by remember { mutableStateOf(true) }
    var autoSync by remember { mutableStateOf(false) }
    var performanceMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "菜单列表使用 44dp 单行与 54dp 描述行，外部视觉由 modifier 组合",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
        Box(modifier = Modifier.height(220.dp)) {
            HyperMenuList(items = items) { item ->
                HyperListItem(
                    leadingContent = { ListIcon(iconFor(item)) },
                    headlineContent = { ListTitle(item) },
                    supportingContent = { ListDescription("点击查看配置") },
                    dividerVisible = true,
                    dividerModifier = Modifier.padding(start = 70.dp)
                )
            }
        }

        HyperMenuList {
            HyperListItem(
                leadingContent = { ListIcon(Icons.Default.Notifications) },
                headlineContent = { Text("推送通知") },
                supportingContent = { Text("接收系统消息提醒") },
                dividerVisible = true,
                dividerModifier = Modifier.padding(start = 70.dp),
                trailingContent = {
                    HyperSwitch(
                        checked = pushEnabled,
                        onCheckedChange = { pushEnabled = it }
                    )
                }
            )
            HyperListItem(
                leadingContent = { ListIcon(Icons.Default.Check) },
                headlineContent = { Text("自动同步") },
                supportingContent = { Text("网络可用时自动刷新数据") },
                dividerVisible = true,
                dividerModifier = Modifier.padding(start = 70.dp),
                trailingContent = {
                    HyperCheckbox(
                        checked = autoSync,
                        onCheckedChange = { autoSync = it }
                    )
                }
            )
            HyperListItem(
                leadingContent = { ListIcon(Icons.Default.Settings) },
                headlineContent = { Text("性能模式") },
                supportingContent = { Text("优先保证流畅度") },
                trailingContent = {
                    HyperRadio(
                        selected = performanceMode,
                        onClick = { performanceMode = true }
                    )
                }
            )
        }
    }
}

@Composable
fun HyperListDemo() {
    val items = listOf(
        "系统设置",
        "通知权限",
        "同步策略",
        "安全中心",
        "账户资料",
        "登录设备",
        "隐私权限",
        "数据备份"
    )
    var roundedCorners by remember { mutableStateOf(true) }
    var comfortableContent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (roundedCorners) "轻圆角开启" else "直角容器",
                color = LocalContentColor.current,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            HyperSwitch(
                checked = roundedCorners,
                onCheckedChange = { roundedCorners = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (comfortableContent) "宽松条目间距" else "默认自适应间距",
                color = LocalContentColor.current,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            HyperSwitch(
                checked = comfortableContent,
                onCheckedChange = { comfortableContent = it }
            )
        }

        Box(modifier = Modifier.height(280.dp)) {
            HyperList(
                contentModifier = Modifier.padding(vertical = 4.dp),
                shape = if (roundedCorners) HyperListDefaults.Shape else RectangleShape
            ) {
                itemsIndexed(
                    items = items,
                    key = { _, item -> item },
                    contentType = { _, _ -> "setting-item" }
                ) { index, item ->
                    HyperListItem(
                        contentModifier = if (comfortableContent) {
                            Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        } else {
                            Modifier.padding(HyperListItemDefaults.ContentPadding)
                        },
                        leadingContent = { ListIcon(iconFor(item)) },
                        headlineContent = { ListTitle(item) },
                        supportingContent = { ListDescription("点击查看配置") },
                        dividerVisible = index < items.lastIndex,
                        dividerModifier = Modifier.padding(start = 70.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ListIcon(imageVector: ImageVector) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
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
