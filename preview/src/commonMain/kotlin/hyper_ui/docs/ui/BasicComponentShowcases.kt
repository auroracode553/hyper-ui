/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/BasicComponentShowcases 可复用界面组件及交互封装。 */
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
import androidx.compose.material.icons.filled.PlayArrow
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
        modifier = Modifier.widthIn(max = 640.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButtonVariantLabel(label = "默认圆形") {
                HyperIconButton(onClick = { selectedAction = "搜索" }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "搜索",
                        modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                    )
                }
            }
            IconButtonVariantLabel(label = "主题实色") {
                HyperIconButton(
                    onClick = { selectedAction = "通知" },
                    colors = HyperIconButtonDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        pressedContainerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.primary,
                        pressedContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "通知",
                        modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                    )
                }
            }
            IconButtonVariantLabel(label = "危险圆角") {
                HyperIconButton(
                    onClick = { selectedAction = "删除" },
                    shape = RoundedCornerShape(12.dp),
                    colors = HyperIconButtonDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        pressedContainerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.error,
                        pressedContentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除",
                        modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                    )
                }
            }
            IconButtonVariantLabel(label = "禁用状态") {
                HyperIconButton(onClick = {}, enabled = false) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "关闭",
                        modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButtonVariantLabel(label = "大尺寸主要") {
                HyperIconButton(
                    onClick = { selectedAction = "媒体控制" },
                    size = 56.dp,
                    colors = HyperIconButtonDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        pressedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        pressedContentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "媒体控制",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            IconButtonVariantLabel(label = "大尺寸中性") {
                HyperIconButton(
                    onClick = { selectedAction = "中性操作" },
                    size = 56.dp,
                    colors = HyperIconButtonDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        pressedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "中性操作",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        Text(
            text = selectedAction,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
    }
}

@Composable
private fun IconButtonVariantLabel(
    label: String,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        content()
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}
