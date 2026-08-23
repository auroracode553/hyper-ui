/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/FeedbackComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
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
import hyper_ui.HyperDialog
import hyper_ui.HyperDropdown
import hyper_ui.HyperDropdownDefaults
import hyper_ui.HyperDropdownItemTone
import hyper_ui.HyperEmptyState
import hyper_ui.HyperPopup
import hyper_ui.HyperLinearProgressIndicator
import hyper_ui.HyperCircularProgressIndicator
import hyper_ui.HyperLevelCapsule
import hyper_ui.HyperLevelCapsuleDefaults
import hyper_ui.HyperBatteryIndicator
import hyper_ui.HyperProgressIndicatorDefaults
import hyper_ui.HyperProgressIndicatorDefaults.colors
import hyper_ui.HyperTextField
import hyper_ui.HyperAppRelease
import hyper_ui.HyperUpdateDialog
import hyper_ui.HyperUpdateDialogState

@Composable
fun EmptyStateDemo() {
    var showDescription by remember { mutableStateOf(true) }
    var feedback by remember { mutableStateOf("等待操作") }

    Column(
        modifier = Modifier
            .widthIn(max = 520.dp)
            .heightIn(min = 360.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton(
                tone = HyperButtonTone.Outline,
                onClick = { showDescription = !showDescription }
            ) {
                Text(if (showDescription) "隐藏说明" else "显示说明")
            }
            Text(
                text = feedback,
                modifier = Modifier.align(Alignment.CenterVertically),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        }
        HyperEmptyState(
            title = "暂无历史记录",
            modifier = Modifier.weight(1f),
            description = if (showDescription) "浏览过的页面会显示在这里" else null,
            iconContent = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(44.dp)
                )
            },
            actionContent = {
                HyperButton(onClick = { feedback = "已请求重新加载" }) {
                    Text("重新加载")
                }
            }
        )
    }
}

@Composable
fun DropdownMenuDemo() {
    var expanded by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("尚未选择") }
    var desktopEnabled by remember { mutableStateOf(true) }
    var useCoolTint by remember { mutableStateOf(false) }
    var showDivider by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .widthIn(max = 420.dp)
            .height(400.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "内容自适应宽度 · 纯文字菜单",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HyperButton(
                    onClick = { expanded = true },
                    tone = HyperButtonTone.Outline
                ) {
                    Text(text = "打开菜单")
                }
                HyperButton(
                    onClick = { useCoolTint = !useCoolTint },
                    tone = HyperButtonTone.Tonal
                ) {
                    Text(text = if (useCoolTint) "恢复默认色" else "冷色面板")
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HyperButton(
                    onClick = { desktopEnabled = !desktopEnabled },
                    tone = HyperButtonTone.Outline
                ) {
                    Text(text = if (desktopEnabled) "禁用桌面项" else "启用桌面项")
                }
                HyperButton(
                    onClick = { showDivider = !showDivider },
                    tone = HyperButtonTone.Tonal
                ) {
                    Text(text = if (showDivider) "隐藏分隔线" else "显示分隔线")
                }
            }
            Text(
                text = selectedAction,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        }

        HyperDropdown(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            alignment = Alignment.TopCenter,
            colors = if (useCoolTint) {
                HyperDropdownDefaults.colors(
                    containerColor = Color(0.92f, 0.96f, 1f, 0.96f),
                    contentColor = Color(0.08f, 0.11f, 0.16f, 1f),
                    disabledContentColor = Color(0.08f, 0.11f, 0.16f, 0.38f),
                    pressedContainerColor = Color(0f, 0.12f, 0.24f, 0.07f)
                )
            } else {
                HyperDropdownDefaults.colors()
            }
        ) {
            Item(onClick = { selectedAction = "已选择：更换背景" }) {
                Text(text = "更换背景")
            }
            Item(onClick = { selectedAction = "已选择：设为私密" }) {
                Text(text = "设为私密")
            }
            Item(onClick = { selectedAction = "已选择：移动到" }) {
                Text(text = "移动到")
            }
            Item(
                onClick = { selectedAction = "已选择：设置提醒（菜单保持展开）" },
                closeOnClick = false
            ) {
                Text(text = "设置提醒")
            }
            if (showDivider) {
                Divider()
            }
            Item(
                onClick = { selectedAction = "已选择：发送到桌面" },
                enabled = desktopEnabled
            ) {
                Text(text = "发送到桌面")
            }
            Item(
                onClick = { selectedAction = "已选择：删除" },
                tone = HyperDropdownItemTone.Danger
            ) {
                Text(text = "删除")
            }
        }
    }
}

/** Android-only hyperToast 的跨平台交互模拟。 */
@Composable
fun ToastDemo() {
    var message by remember { mutableStateOf("点击按钮模拟 Toast 反馈") }
    var duration by remember { mutableStateOf("Short") }

    Column(
        modifier = Modifier.widthIn(max = 420.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(onClick = {
                message = "保存成功"
                duration = "Short"
            }) {
                Text("短提示")
            }
            HyperButton(
                onClick = {
                    message = "操作已完成，这是一条较长提示"
                    duration = "Long"
                },
                tone = HyperButtonTone.Outline
            ) {
                Text("长提示")
            }
        }
        Box(
            modifier = Modifier
                .widthIn(min = 220.dp)
                .background(
                    color = MaterialTheme.colorScheme.inverseSurface,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                textAlign = TextAlign.Center
            )
        }
        Text(
            text = "模拟时长：HyperToastDuration.$duration",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
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
fun LevelCapsuleDemo() {
    var progress by remember { mutableStateOf(0.56f) }
    var showIcon by remember { mutableStateOf(true) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HyperLevelCapsule(
            progress = progress,
            label = "${(progress * 100).toInt()}%",
            iconContent = if (showIcon) {
                {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else null
        )
        HyperLevelCapsule(
            progress = progress,
            label = "${(progress * 100).toInt()}%",
            modifier = Modifier
                .width(48.dp)
                .height(160.dp),
            colors = HyperLevelCapsuleDefaults.colors(
                progressColor = Color(1f, 0.78f, 0.18f, 1f),
                labelColor = Color(0.12f, 0.35f, 0.88f, 1f)
            )
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                tone = HyperButtonTone.Outline,
                onClick = { progress = (progress - 0.1f).coerceAtLeast(0f) }
            ) {
                Text("降低")
            }
            HyperButton(
                onClick = { progress = (progress + 0.1f).coerceAtMost(1f) }
            ) {
                Text("提高")
            }
            HyperButton(
                tone = HyperButtonTone.Outline,
                onClick = { showIcon = !showIcon }
            ) {
                Text(if (showIcon) "隐藏图标" else "显示图标")
            }
        }
    }
}

@Composable
fun BatteryIndicatorDemo() {
    var percentage by remember { mutableStateOf(68) }
    var charging by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.widthIn(max = 520.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HyperBatteryIndicator(
                percentage = percentage,
                charging = charging,
                contentDescription = "电量 $percentage%"
            )
            HyperBatteryIndicator(percentage = 16, charging = false)
            HyperBatteryIndicator(percentage = 82, charging = true)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                tone = HyperButtonTone.Outline,
                onClick = { percentage = (percentage - 10).coerceAtLeast(0) }
            ) {
                Text("减少电量")
            }
            HyperButton(
                onClick = { percentage = (percentage + 10).coerceAtMost(100) }
            ) {
                Text("增加电量")
            }
            HyperButton(
                tone = HyperButtonTone.Outline,
                onClick = { charging = !charging }
            ) {
                Text(if (charging) "停止充电" else "开始充电")
            }
        }
        Text(
            text = "模拟 Android 电池工具输出：常规、低电量与充电状态。",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
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
fun HyperDialogDemo() {
    var visible by remember { mutableStateOf(false) }
    var draft by remember { mutableStateOf("输入内容验证窗口稳定") }

    Column(
        modifier = Modifier.widthIn(max = 420.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperButton(onClick = { visible = true }) {
            Text("显示 Dialog")
        }
        Text(
            text = "禁用平台默认宽度；面板在稳定的全尺寸 Dialog 根节点内居中。",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }

    HyperDialog(
        visible = visible,
        onDismissRequest = { visible = false },
        title = "稳定输入",
        actionContent = {
            HyperButton(onClick = { visible = false }) {
                Text("确定")
            }
        }
    ) {
        HyperTextField(
            value = draft,
            onValueChange = { draft = it },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DialogDemo() {
    var showDialog by remember { mutableStateOf(false) }
    var showLongContent by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("等待操作") }
    var dismissOnClickOutside by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.widthIn(max = 420.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperButton(
            tone = HyperButtonTone.Danger,
            onClick = {
                showLongContent = false
                showDialog = true
            }
        ) {
            Text(text = "删除数据")
        }
        HyperButton(
            tone = HyperButtonTone.Outline,
            onClick = {
                showLongContent = true
                showDialog = true
            }
        ) {
            Text(text = "查看 70% 高度长内容")
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
            text = "Alert 打开时直接居中，无顶部位移或显示动画",
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
        title = if (showLongContent) "长内容对话框" else "确认删除",
        dismissOnClickOutside = dismissOnClickOutside,
        bodyContent = {
            if (showLongContent) {
                repeat(18) { index ->
                    Text(
                        text = "第 ${index + 1} 项可滚动内容",
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                }
            } else {
                DialogBody("删除后无法恢复，是否继续？")
            }
        },
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
            text = "可缩放预览窗口验证：浮层始终相对窗口居中，宽度限制在 280–360dp，最大高度为窗口的 70%。",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "基础浮层由 Popup 位置提供器居中；模态任务应使用独立的 HyperDialog。",
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
