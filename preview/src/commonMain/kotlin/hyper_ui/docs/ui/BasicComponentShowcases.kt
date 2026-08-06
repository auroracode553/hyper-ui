package hyper_ui.docs.ui

import hyper_ui.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonDemo() {
    var clicks by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                text = "默认按钮",
                onClick = { clicks += 1 },
                variant = HyperButtonVariant.Default
            )
            HyperButton(
                text = "主要按钮",
                onClick = { clicks += 1 },
                variant = HyperButtonVariant.Primary
            )
            HyperButton(
                text = "成功按钮",
                onClick = { clicks += 1 },
                variant = HyperButtonVariant.Success
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                text = "信息按钮",
                onClick = { clicks += 1 },
                variant = HyperButtonVariant.Info
            )
            HyperButton(
                text = "警告按钮",
                onClick = { clicks += 1 },
                variant = HyperButtonVariant.Warning
            )
            HyperButton(
                text = "危险按钮",
                onClick = { clicks = 0 },
                variant = HyperButtonVariant.Danger
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                text = "禁用默认",
                onClick = {},
                enabled = false,
                variant = HyperButtonVariant.Default
            )
            HyperButton(
                text = "禁用主要",
                onClick = {},
                enabled = false
            )
        }
        HyperButton(
            text = "小按钮",
            onClick = { clicks += 1 },
            minHeight = 32.dp,
            horizontalPadding = 10.dp,
            verticalPadding = 5.dp,
            fontSize = 13.sp
        )
        Text(
            text = "点击次数：$clicks",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun IconButtonDemo() {
    var selectedAction by remember { mutableStateOf("未选择操作") }

    Column(
        modifier = Modifier.widthIn(max = 420.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HyperIconButton(
                imageVector = Icons.Default.Search,
                contentDescription = "搜索",
                onClick = { selectedAction = "搜索" },
                backgroundColor = MaterialTheme.colorScheme.surface
            )
            HyperIconButton(
                imageVector = Icons.Default.Notifications,
                contentDescription = "通知",
                onClick = { selectedAction = "通知" },
                tint = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.primaryContainer
            )
            HyperIconButton(
                imageVector = Icons.Default.Delete,
                contentDescription = "删除",
                onClick = { selectedAction = "删除" },
                tint = MaterialTheme.colorScheme.error,
                backgroundColor = MaterialTheme.colorScheme.errorContainer
            )
            HyperIconButton(
                imageVector = Icons.Default.Close,
                contentDescription = "关闭",
                onClick = {},
                enabled = false,
                backgroundColor = MaterialTheme.colorScheme.surface
            )
        }
        Text(
            text = selectedAction,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
    }
}
