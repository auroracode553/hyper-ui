/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/FeedbackComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui
import hyper_ui.*
import hyper_ui.LocalHyperContentColor
import hyper_ui.docs.theme.LocalDocsColorScheme

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import hyper_ui.HyperDialog
import hyper_ui.HyperDropdown
import hyper_ui.HyperEmptyState
import hyper_ui.HyperPopover
import hyper_ui.HyperLevelCapsule
import hyper_ui.HyperLevelCapsuleDefaults
import hyper_ui.HyperBatteryIndicator
import hyper_ui.HyperProgressDefaults
import hyper_ui.HyperTextField
import hyper_ui.HyperUpdateRelease
import hyper_ui.HyperUpdateDownloadProgress
import hyper_ui.HyperUpdateDialog
import hyper_ui.HyperUpdateStatus

@Composable
fun EmptyStateDemo() {
    var descriptionVisible by remember { mutableStateOf(true) }
    var feedback by remember { mutableStateOf("等待操作") }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PreviewSectionLabel("带恢复操作")
        PreviewToggle("显示说明", descriptionVisible) { descriptionVisible = it }
        HyperEmptyState(
            title = "没有找到结果",
            message = if (descriptionVisible) "换一个关键词，或者清除筛选条件后重试。" else null,
            icon = { HyperIcon(HyperReferenceIcons.Search, null) },
            action = { HyperButton(type = "tonal", onPressed = { feedback = "已清除筛选" }) { HyperText("清除筛选") } }
        )
        HyperText(feedback, size = "small", color = HyperColors.secondaryText)
        PreviewSectionLabel("纯提示 · 无操作按钮")
        HyperEmptyState(
            title = "这里还是空的",
            message = "创建第一个项目后，它会出现在这里。",
            icon = { HyperIcon(HyperReferenceIcons.Info, null) }
        )
    }
}

@Composable
fun DropdownMenuDemo() {
    var selected by remember { mutableStateOf<String?>(null) }
    var enabled by remember { mutableStateOf(true) }
    var empty by remember { mutableStateOf(false) }
    var lastEnabled by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton({ enabled = !enabled }) { HyperText(if (enabled) "禁用字段" else "启用字段") }
            HyperButton({ empty = !empty }) { HyperText(if (empty) "恢复选项" else "空列表") }
        }
        HyperButton(type = "tonal", onPressed = { lastEnabled = !lastEnabled }, size = "small") { HyperText("切换末项禁用态") }
        HyperDropdown(
            options = if (empty) emptyList() else listOf(
                HyperOption("compact", "紧凑布局"), HyperOption("standard", "标准布局"),
                HyperOption("comfortable", "舒适布局", enabled = lastEnabled)
            ),
            value = selected, onChanged = { selected = it }, enabled = enabled, label = "布局选择"
        )
        HyperText("当前值：${selected ?: "尚未选择"}", size = "small")
    }
}

/** 受控 Toast 的语义色、长文案和操作区预览。 */
@Composable
fun ToastDemo() {
    var message by remember { mutableStateOf("保存成功") }
    var visible by remember { mutableStateOf(false) }
    var duration by remember { mutableStateOf(3000L) }
    var type by remember { mutableStateOf("success") }
    Column(Modifier.widthIn(max = 420.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton(onPressed = { message = "保存成功"; type = "success"; duration = 3000L; visible = true }, colorStyle = "primary", type = "filled") {
                HyperText("成功")
            }
            HyperButton(onPressed = { message = "请检查输入内容"; type = "warning"; duration = 3000L; visible = true }) { HyperText("警告") }
            HyperButton(onPressed = { message = "网络连接失败"; type = "error"; duration = 3000L; visible = true }) { HyperText("错误") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton(onPressed = { message = "操作已完成，这是一条较长提示"; type = "info"; duration = 6000L; visible = true }) { HyperText("长提示") }
            HyperButton(onPressed = { message = "已复制到剪贴板"; type = "neutral"; duration = 3000L; visible = true }) { HyperText("默认") }
        }
        Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.BottomCenter) {
            HyperToast(visible, message, onClose = { visible = false }, duration = duration, type = type,
                action = {
                    HyperButton(onPressed = { visible = false }, size = "small") {
                        HyperText("关闭")
                    }
                })
        }
        HyperText(if (visible) "显示中：${duration / 1000} 秒后关闭" else "提示已关闭", fontSize = 13.sp)
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
                HyperText(
                    text = "确定进度 · 实色轨道",
                    color = LocalDocsColorScheme.current.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                HyperText(
                    text = "${(progress * 100).toInt()}%",
                    color = LocalDocsColorScheme.current.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
            HyperProgress(type = "linear", value = progress)
            HyperProgress(type = "linear", value = progress, size = "small")
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HyperProgress(type = "circular", value = progress, size = "small")
                HyperProgress(type = "circular", value = null)
                HyperButton(
                    onPressed = { progress = (progress - 0.1f).coerceAtLeast(0f) }
                ) {
                    HyperText(text = "减少")
                }
                HyperButton(
                    onPressed = { progress = (progress + 0.1f).coerceAtMost(1f) },
                    colorStyle = "primary", type = "filled",
                ) {
                    HyperText(text = "增加")
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperText(
                text = "自定义颜色",
                color = LocalDocsColorScheme.current.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            HyperProgress(
                type = "linear",
                value = 0.7f,
                colors = HyperProgressDefaults.colors(
                    indicatorColor = Color(0.12f, 0.50f, 1f, 1f)
                )
            )
            HyperProgress(
                type = "linear",
                value = null,
                modifier = Modifier.height(8.dp),
                colors = HyperProgressDefaults.colors(
                    indicatorColor = Color(0.03f, 0.76f, 0.38f, 1f)
                )
            )
            HyperProgress(
                type = "circular",
                value = null,
                modifier = Modifier.size(24.dp),
                strokeWidth = 4.dp,
                colors = HyperProgressDefaults.colors(
                    indicatorColor = LocalDocsColorScheme.current.primary
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
                    HyperIcon(
                        icon = HyperReferenceIcons.Info,
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
                onPressed = { progress = (progress - 0.1f).coerceAtLeast(0f) }
            ) {
                HyperText("降低")
            }
            HyperButton(
                onPressed = { progress = (progress + 0.1f).coerceAtMost(1f) },
                colorStyle = "primary", type = "filled",
            ) {
                HyperText("提高")
            }
            HyperButton(
                onPressed = { showIcon = !showIcon }
            ) {
                HyperText(if (showIcon) "隐藏图标" else "显示图标")
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
                onPressed = { percentage = (percentage - 10).coerceAtLeast(0) }
            ) {
                HyperText("减少电量")
            }
            HyperButton(
                onPressed = { percentage = (percentage + 10).coerceAtMost(100) },
                colorStyle = "primary", type = "filled",
            ) {
                HyperText("增加电量")
            }
            HyperButton(
                onPressed = { charging = !charging }
            ) {
                HyperText(if (charging) "停止充电" else "开始充电")
            }
        }
        HyperText(
            text = "模拟 Android 电池工具输出：常规、低电量与充电状态。",
            color = LocalDocsColorScheme.current.onSurfaceVariant,
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
        HyperProgress(type = "linear", value = null)
        HyperProgress(type = "circular", value = null)
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
        HyperButton(onPressed = { visible = true }, colorStyle = "primary", type = "filled") {
            HyperText("显示 Dialog")
        }
        HyperText(
            text = "禁用平台默认宽度；面板在稳定的全尺寸 Dialog 根节点内居中。",
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }

    HyperDialog(
        visible = visible,
        onClose = { visible = false },
        title = "稳定输入",
        actions = {
            HyperButton(onPressed = { visible = false }, colorStyle = "primary", type = "filled") {
                HyperText("确定")
            }
        }
    ) {
        HyperTextField(
            value = draft,
            onChanged = { draft = it },
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
            colorStyle = "danger", type = "filled",
            onPressed = {
                showLongContent = false
                showDialog = true
            }
        ) {
            HyperText(text = "删除数据")
        }
        HyperButton(
            onPressed = {
                showLongContent = true
                showDialog = true
            }
        ) {
            HyperText(text = "查看长内容")
        }
        HyperButton(
            onPressed = { dismissOnClickOutside = !dismissOnClickOutside }
        ) {
            HyperText(text = if (dismissOnClickOutside) "空白关闭：开启" else "空白关闭：关闭")
        }
        HyperText(
            text = resultText,
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 13.sp
        )
        HyperText(
            text = "Alert 居中淡入，关闭时从当前状态淡出",
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 13.sp
        )
    }

    HyperAlertDialog(
        visible = showDialog,
        onClose = {
            resultText = "已收到关闭请求"
            showDialog = false
        },
        title = if (showLongContent) "长内容对话框" else "确认删除",
        dismissOnClickOutside = dismissOnClickOutside,
        content = {
            if (showLongContent) {
                repeat(18) { index ->
                    HyperText(
                        text = "第 ${index + 1} 项可滚动内容",
                        modifier = Modifier.fillMaxWidth(),
                        color = LocalDocsColorScheme.current.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                }
            } else {
                DialogBody("删除后无法恢复，是否继续？")
            }
        },
        actions = {
            HyperButton(
                onPressed = {
                    resultText = "已取消"
                    showDialog = false
                }
            ) {
                HyperText(text = "取消")
            }
            HyperButton(
                colorStyle = "danger", type = "filled",
                onPressed = {
                    resultText = "已确认删除"
                    showDialog = false
                }
            ) {
                HyperText(text = "继续删除")
            }
        }
    )
}

@Composable
fun HyperPopoverDemo() {
    var visible by remember { mutableStateOf(false) }
    var savedNote by remember { mutableStateOf("默认备注") }
    var draft by remember { mutableStateOf(savedNote) }
    var longContent by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HyperButton({ longContent = !longContent }) { HyperText("切换长内容") }
        HyperPopover(
            visible = visible, onClose = { visible = false },
            child = {
                HyperButton({ draft = savedNote; visible = !visible }, colorStyle = "primary", type = "filled") { HyperText("编辑备注") }
            }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                HyperText("编辑备注", fontWeight = FontWeight.SemiBold)
                HyperTextField(draft, { draft = it }, placeholder = { HyperText("填写备注") })
                if (longContent) repeat(12) { HyperText("补充说明 ${it + 1}", size = "small") }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    HyperButton({ visible = false }, size = "small") { HyperText("取消") }
                    HyperButton({ savedNote = draft; visible = false }, size = "small", colorStyle = "primary", type = "filled") { HyperText("保存") }
                }
            }
        }
        HyperText("当前备注：$savedNote", size = "small")
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UpdateDialogDemo() {
    val release = remember { HyperUpdateRelease("1.2.0", releaseDate = "2026-10-11", sizeLabel = "32 MB",
        highlights = listOf("优化大文档加载速度", "修复部分表格预览问题")) }
    var status by remember { mutableStateOf(HyperUpdateStatus.idle) }
    var mandatory by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0.42f) }
    val stages = listOf("发现更新" to HyperUpdateStatus.available, "检查中" to HyperUpdateStatus.checking,
        "系统排队" to HyperUpdateStatus.downloadQueued, "下载中" to HyperUpdateStatus.downloading,
        "准备安装" to HyperUpdateStatus.readyToInstall, "安装中" to HyperUpdateStatus.installing,
        "完成" to HyperUpdateStatus.completed, "最新" to HyperUpdateStatus.upToDate, "错误提示" to HyperUpdateStatus.error)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HyperButton({ mandatory = !mandatory }) { HyperText(if (mandatory) "必要更新：开启" else "必要更新：关闭") }
        HyperSlider(progress, { progress = it }, min = 0f, max = 1f, showValue = false)
        HyperText("模拟进度 ${(progress * 100).toInt()}%，检查/下载/安装状态 2.5 秒后模拟完成。", size = "small")
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            stages.forEach { (label, value) -> HyperButton({ status = value }, size = "small") { HyperText(label) } }
        }
    }
    androidx.compose.runtime.LaunchedEffect(status) {
        val shown = status
        if (shown in setOf(HyperUpdateStatus.checking, HyperUpdateStatus.downloading, HyperUpdateStatus.installing)) {
            kotlinx.coroutines.delay(2500)
            status = when (shown) {
                HyperUpdateStatus.checking -> HyperUpdateStatus.available
                HyperUpdateStatus.downloading -> HyperUpdateStatus.readyToInstall
                else -> HyperUpdateStatus.completed
            }
        }
    }
    HyperUpdateDialog(
        status = status,
        currentVersion = if (status in setOf(HyperUpdateStatus.completed, HyperUpdateStatus.upToDate)) "1.2.0" else "1.0.0",
        release = release,
        progress = progress.takeIf { status != HyperUpdateStatus.downloadQueued },
        downloadProgress = if (status == HyperUpdateStatus.downloading) HyperUpdateDownloadProgress(
            (32_000_000 * progress).toLong(), 32_000_000, 2_000_000.0) else null,
        message = "无法连接更新服务，请稍后重试".takeIf { status == HyperUpdateStatus.error },
        mandatory = mandatory,
        onClose = { status = HyperUpdateStatus.idle },
        onCancel = { status = HyperUpdateStatus.idle },
        onLater = { status = HyperUpdateStatus.idle },
        onCheck = { status = HyperUpdateStatus.checking },
        onRetry = { status = HyperUpdateStatus.checking },
        onDownload = { status = HyperUpdateStatus.downloading },
        onInstall = { status = HyperUpdateStatus.installing }
    )
    if (status == HyperUpdateStatus.installing) {
        HyperButton({ status = HyperUpdateStatus.completed }) { HyperText("宿主模拟完成更新") }
    }
}

@Composable
private fun ColumnScope.popupContent(
    value: String,
    onValueChange: (String) -> Unit
) {
    HyperText(
        text = "标题固定在顶部，正文内容和输入框在中间区域滚动。",
        color = LocalDocsColorScheme.current.onSurfaceVariant,
        fontSize = 13.sp,
        lineHeight = 18.sp
    )
    HyperTextField(
        value = value,
        onChanged = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        inputModifier = Modifier.heightIn(min = 88.dp, max = 220.dp),
        placeholder = {
            HyperText(
                text = "请输入备注",
                color = LocalHyperContentColor.current
            )
        },
        type = "textarea",
        rows = 3
    )
}

@Composable
private fun DialogBody(text: String) {
    HyperText(
        text = text,
        color = LocalDocsColorScheme.current.onSurfaceVariant,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )
}
