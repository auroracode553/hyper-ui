/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/FeedbackComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperAlertDialog
import hyper_ui.HyperButton
import hyper_ui.HyperButtonTone
import hyper_ui.HyperPopup
import hyper_ui.HyperDropdownMenu
import hyper_ui.HyperLinearProgressIndicator
import hyper_ui.HyperCircularProgressIndicator
import hyper_ui.HyperProgressIndicatorDefaults
import hyper_ui.HyperProgressIndicatorDefaults.colors
import hyper_ui.HyperTextField
import hyper_ui.HyperAppRelease
import hyper_ui.HyperUpdateDialog
import hyper_ui.HyperUpdateDialogState

@Composable
fun DropdownMenuDemo() {
    var expanded by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("尚未选择") }

    Box(
        modifier = Modifier
            .widthIn(max = 420.dp)
            .height(180.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "菜单面板：不透明实色",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
            HyperButton(
                onClick = { expanded = true },
                tone = HyperButtonTone.Outline
            ) {
                Text(text = "打开菜单")
            }
            Text(
                text = selectedAction,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        }

        HyperDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            contentModifier = Modifier.padding(vertical = 10.dp),
            alignment = Alignment.TopCenter
        ) {
            Item(onClick = { selectedAction = "标记完成" }) {
                MenuIcon(Icons.Default.Check)
                Text(text = "标记完成")
            }
            Item(
                onClick = { selectedAction = "查看详情" },
                contentModifier = Modifier.padding(horizontal = 24.dp)
            ) {
                MenuIcon(Icons.Default.Info)
                Text(text = "查看详情")
            }
            Divider()
            Item(onClick = { selectedAction = "删除" }) {
                MenuIcon(Icons.Default.Delete)
                Text(
                    text = "删除",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun ProgressDemo() {
    var progress by remember { mutableStateOf(0.42f) }

    Column(
        modifier = Modifier.widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "确定进度 · 实色轨道",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
            HyperLinearProgressIndicator(progress = progress)
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HyperCircularProgressIndicator(progress = progress)
                HyperCircularProgressIndicator(progress = null)
                HyperButton(
                    tone = HyperButtonTone.Outline,
                    onClick = { progress = (progress - 0.1f).coerceAtLeast(0f) }
                ) {
                    Text(text = "减少")
                }
                HyperButton(
                    onClick = { progress = (progress + 0.1f).coerceAtMost(1f) }
                ) {
                    Text(text = "增加")
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "自定义颜色",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            HyperLinearProgressIndicator(
                progress = 0.7f,
                colors = colors(
                    indicatorColor = Color(0.12f, 0.50f, 1f, 1f)
                )
            )
            HyperLinearProgressIndicator(
                progress = null,
                modifier = Modifier.height(8.dp),
                colors = colors(
                    indicatorColor = Color(0.03f, 0.76f, 0.38f, 1f)
                )
            )
            HyperCircularProgressIndicator(
                progress = null,
                modifier = Modifier.size(44.dp),
                strokeWidth = 4.dp,
                colors = HyperProgressIndicatorDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Composable
fun LoadingProgressDemo() {
    Column(
        modifier = Modifier.widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HyperLinearProgressIndicator(progress = null)
        HyperCircularProgressIndicator(progress = null)
    }
}

@Composable
fun DialogDemo() {
    var showDialog by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("等待操作") }
    var dismissOnClickOutside by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.widthIn(max = 420.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperButton(
            tone = HyperButtonTone.Danger,
            onClick = { showDialog = true }
        ) {
            Text(text = "删除数据")
        }
        HyperButton(
            tone = HyperButtonTone.Outline,
            onClick = { dismissOnClickOutside = !dismissOnClickOutside }
        ) {
            Text(text = if (dismissOnClickOutside) "空白关闭：开启" else "空白关闭：关闭")
        }
        Text(
            text = resultText,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
        Text(
            text = "Alert 面板全程不透明",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
    }

    HyperAlertDialog(
        visible = showDialog,
        onDismissRequest = {
            resultText = "已收到关闭请求"
            showDialog = false
        },
        title = "确认删除",
        dismissOnClickOutside = dismissOnClickOutside,
        bodyContent = { DialogBody("删除后无法恢复，是否继续？") },
        actionContent = {
            HyperButton(
                modifier = Modifier.weight(1f),
                tone = HyperButtonTone.Outline,
                onClick = {
                    resultText = "已取消"
                    showDialog = false
                }
            ) {
                Text(text = "取消")
            }
            HyperButton(
                modifier = Modifier.weight(1f),
                tone = HyperButtonTone.Danger,
                onClick = {
                    resultText = "已确认删除"
                    showDialog = false
                }
            ) {
                Text(text = "继续删除")
            }
        }
    )
}

@Composable
fun HyperPopupDemo() {
    var showPopup by remember { mutableStateOf(false) }
    var savedNote by remember { mutableStateOf("默认备注") }
    var draftNote by remember { mutableStateOf(savedNote) }
    var dismissOnClickOutside by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.widthIn(max = 420.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperButton(
            onClick = {
                draftNote = savedNote
                showPopup = true
            }
        ) {
            Text(text = "编辑备注")
        }
        HyperButton(
            tone = HyperButtonTone.Outline,
            onClick = { dismissOnClickOutside = !dismissOnClickOutside }
        ) {
            Text(text = if (dismissOnClickOutside) "空白关闭：开启" else "空白关闭：关闭")
        }
        Text(
            text = "当前备注：$savedNote",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
        Text(
            text = "可缩放预览窗口验证：浮层忽略触发位置并始终相对窗口居中，宽度限制在 280–360dp。",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "基础浮层没有公开定位参数或动画，面板保持不透明且不绘制蒙层。",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }

    HyperPopup(
        visible = showPopup,
        onDismissRequest = { showPopup = false },
        title = "编辑备注",
        dismissOnClickOutside = dismissOnClickOutside,
        actionContent = {
            HyperButton(
                tone = HyperButtonTone.Outline,
                onClick = { showPopup = false }
            ) {
                Text(text = "取消")
            }
            HyperButton(
                onClick = {
                    savedNote = draftNote.ifBlank { "未填写备注" }
                    showPopup = false
                }
            ) {
                Text(text = "保存")
            }
        }
    ) {
        popupContent(draftNote) { draftNote = it }
    }
}

@Composable
fun UpdateDialogDemo() {
    val previewRelease = remember {
        HyperAppRelease(
            versionName = "1.2.0",
            displayName = "文档查看器 1.2.0",
            releaseNotes = "优化大文档加载速度，并修复部分表格预览问题。",
            packageFileName = "app-release.apk",
            packageDownloadUrl = "https://example.com/app-release.apk"
        )
    }
    var state by remember { mutableStateOf<HyperUpdateDialogState>(HyperUpdateDialogState.Idle) }
    var resultText by remember { mutableStateOf("选择一种状态查看弹窗") }

    Column(
        modifier = Modifier.widthIn(max = 520.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                onClick = {
                    state = HyperUpdateDialogState.UpdateAvailable(
                        currentVersionName = "1.0.0",
                        release = previewRelease
                    )
                }
            ) {
                Text("发现更新")
            }
            HyperButton(
                tone = HyperButtonTone.Outline,
                onClick = { state = HyperUpdateDialogState.Checking("1.0.0") }
            ) {
                Text("检查中")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                tone = HyperButtonTone.Outline,
                onClick = { state = HyperUpdateDialogState.UpToDate("1.2.0") }
            ) {
                Text("已是最新")
            }
            HyperButton(
                tone = HyperButtonTone.Outline,
                onClick = {
                    state = HyperUpdateDialogState.Error(
                        currentVersionName = "1.0.0",
                        message = "无法连接更新服务，请稍后重试"
                    )
                }
            ) {
                Text("请求失败")
            }
        }
        Text(
            text = resultText,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
        Text(
            text = "更新弹窗及内部按钮、进度指示器均为实色",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
    }

    HyperUpdateDialog(
        state = state,
        onDismissRequest = {
            state = HyperUpdateDialogState.Idle
            resultText = "已关闭弹窗"
        },
        onRetry = {
            state = HyperUpdateDialogState.Checking("1.0.0")
            resultText = "已请求重试"
        },
        onDownload = {
            state = HyperUpdateDialogState.DownloadQueued(
                currentVersionName = "1.0.0",
                release = previewRelease,
                downloadId = 1001L
            )
            resultText = "已确认下载"
        }
    )
}

@Composable
private fun ColumnScope.popupContent(
    value: String,
    onValueChange: (String) -> Unit
) {
    Text(
        text = "标题固定在顶部，正文内容和输入框在中间区域滚动。",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 13.sp,
        lineHeight = 18.sp
    )
    HyperTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        inputModifier = Modifier.heightIn(min = 88.dp, max = 220.dp),
        placeholderContent = {
            Text(
                text = "请输入备注",
                color = LocalContentColor.current
            )
        },
        singleLine = false,
        minLines = 3,
        maxLines = 6
    )
}

@Composable
private fun DialogBody(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun MenuIcon(imageVector: androidx.compose.ui.graphics.vector.ImageVector) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        tint = LocalContentColor.current,
        modifier = Modifier.size(22.dp)
    )
}
