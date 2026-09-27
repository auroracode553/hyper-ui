/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/BasicComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui
import hyper_ui.*
import hyper_ui.docs.theme.LocalDocsColorScheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperButton
import hyper_ui.HyperButtonVariant
import hyper_ui.HyperIconButton
import hyper_ui.HyperIconButtonDefaults

@Composable
fun ButtonDemo() {
    var clicks by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                onClick = { clicks += 1 },
                height = 32.dp
            ) {
                HyperText(text = "小按钮")
            }
            HyperButton(onClick = { clicks += 1 }) {
                HyperText(text = "默认按钮")
            }
            HyperButton(
                onClick = {},
                loading = true,
                variant = HyperButtonVariant.Outline
            ) {
                HyperText(text = "处理中")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                onClick = { clicks += 1 },
                variant = HyperButtonVariant.Outline
            ) {
                HyperText(text = "导出")
            }
            HyperButton(
                onClick = { clicks = 0 },
                variant = HyperButtonVariant.Danger
            ) {
                HyperText(text = "删除")
            }
            HyperButton(onClick = { clicks += 1 }, variant = HyperButtonVariant.Tonal) {
                HyperIcon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                HyperText(text = "带图标胶囊")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperIconButton(onClick = { clicks += 1 }) {
                HyperIcon(Icons.Default.Search, contentDescription = "搜索", modifier = Modifier.size(18.dp))
            }
            HyperIconButton(onClick = { clicks += 1 }) {
                HyperIcon(Icons.Default.Notifications, contentDescription = "通知", modifier = Modifier.size(18.dp))
            }
            HyperIconButton(onClick = { clicks = 0 }) {
                HyperIcon(Icons.Default.Delete, contentDescription = "删除", modifier = Modifier.size(18.dp))
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(onClick = {}, enabled = false, variant = HyperButtonVariant.Outline) {
                HyperText("禁用")
            }
            HyperButton(
                onClick = { clicks += 1 },
                height = 32.dp,
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                HyperText(text = "小尺寸 slot", fontSize = 13.sp)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton({ loading = !loading }, variant = HyperButtonVariant.Ghost) { HyperText("切换加载") }
            HyperButton({ clicks += 1 }, loading = loading) { HyperText("提交") }
        }
        HyperText(
            text = "点击次数：$clicks · 按住观察缩放，拖出按钮再松开可取消点击",
            color = LocalDocsColorScheme.current.onSurfaceVariant,
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
            IconButtonVariantLabel(label = "默认玻璃") {
                HyperIconButton(onClick = { selectedAction = "搜索" }) {
                    HyperIcon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "搜索",
                        modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                    )
                }
            }
            IconButtonVariantLabel(label = "主题玻璃") {
                HyperIconButton(
                    onClick = { selectedAction = "通知" },
                    colors = HyperIconButtonDefaults.colors(
                        containerColor = LocalDocsColorScheme.current.primaryContainer.copy(alpha = 0.52f),
                        pressedContainerColor = LocalDocsColorScheme.current.primary.copy(alpha = 0.68f),
                        contentColor = LocalDocsColorScheme.current.primary,
                        pressedContentColor = LocalDocsColorScheme.current.onPrimary
                    )
                ) {
                    HyperIcon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "通知",
                        modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                    )
                }
            }
            IconButtonVariantLabel(label = "危险玻璃") {
                HyperIconButton(
                    onClick = { selectedAction = "删除" },
                    shape = RoundedCornerShape(12.dp),
                    colors = HyperIconButtonDefaults.colors(
                        containerColor = LocalDocsColorScheme.current.errorContainer.copy(alpha = 0.54f),
                        pressedContainerColor = LocalDocsColorScheme.current.error.copy(alpha = 0.7f),
                        contentColor = LocalDocsColorScheme.current.error,
                        pressedContentColor = LocalDocsColorScheme.current.onError
                    )
                ) {
                    HyperIcon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除",
                        modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
                    )
                }
            }
            IconButtonVariantLabel(label = "禁用状态") {
                HyperIconButton(onClick = {}, enabled = false) {
                    HyperIcon(
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
            IconButtonVariantLabel(label = "大尺寸主题") {
                HyperIconButton(
                    onClick = { selectedAction = "媒体控制" },
                    size = 56.dp,
                    colors = HyperIconButtonDefaults.colors(
                        containerColor = LocalDocsColorScheme.current.primary.copy(alpha = 0.64f),
                        pressedContainerColor = LocalDocsColorScheme.current.primaryContainer.copy(alpha = 0.76f),
                        contentColor = LocalDocsColorScheme.current.onPrimaryContainer,
                        pressedContentColor = LocalDocsColorScheme.current.primary
                    )
                ) {
                    HyperIcon(
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
                        containerColor = LocalDocsColorScheme.current.surfaceVariant.copy(alpha = 0.58f),
                        pressedContainerColor = LocalDocsColorScheme.current.secondaryContainer.copy(alpha = 0.72f),
                        contentColor = LocalDocsColorScheme.current.onSurface
                    )
                ) {
                    HyperIcon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "中性操作",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        HyperText(
            text = selectedAction,
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 13.sp
        )
        HyperText(
            text = "按下立即收缩至 0.975、透明度降至 0.92；拖出取消后恢复且不触发操作。",
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
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
        HyperText(
            text = label,
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}
