package hyper_ui.docs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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
import hyper_ui.HyperButton
import hyper_ui.HyperButtonDefaults
import hyper_ui.HyperButtonTone
import hyper_ui.HyperIconButton
import hyper_ui.HyperIconButtonDefaults

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
                onClick = { clicks += 1 },
                tone = HyperButtonTone.Outline
            ) {
                Text(text = "轮廓")
            }
            HyperButton(onClick = { clicks += 1 }) {
                Text(text = "主要")
            }
            HyperButton(
                onClick = { clicks += 1 },
                tone = HyperButtonTone.Tonal
            ) {
                Text(text = "弱强调")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                onClick = { clicks += 1 },
                tone = HyperButtonTone.Secondary
            ) {
                Text(text = "次要")
            }
            HyperButton(
                onClick = { clicks += 1 },
                tone = HyperButtonTone.Success
            ) {
                Text(text = "成功")
            }
            HyperButton(
                onClick = { clicks = 0 },
                tone = HyperButtonTone.Danger
            ) {
                Text(text = "危险")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                onClick = { clicks += 1 }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(text = "搜索")
            }
            HyperButton(
                onClick = {},
                enabled = false,
                tone = HyperButtonTone.Outline
            ) {
                Text(text = "禁用")
            }
        }
        HyperButton(
            onClick = { clicks += 1 },
            minHeight = 32.dp,
            contentPadding = HyperButtonDefaults.ContentPadding
        ) {
            Text(
                text = "小尺寸 slot",
                fontSize = 13.sp
            )
        }
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
                onClick = { selectedAction = "搜索" },
                colors = HyperIconButtonDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                )
            }
            HyperIconButton(
                onClick = { selectedAction = "通知" },
                colors = HyperIconButtonDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "通知",
                    modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                )
            }
            HyperIconButton(
                onClick = { selectedAction = "删除" },
                shape = RoundedCornerShape(12.dp),
                colors = HyperIconButtonDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除",
                    modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                )
            }
            HyperIconButton(
                onClick = {},
                enabled = false,
                colors = HyperIconButtonDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "关闭",
                    modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                )
            }
        }
        Text(
            text = selectedAction,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
    }
}
