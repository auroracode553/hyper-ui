/** 文件职责：逐项对齐 Flutter UpdateDialogComponentExample 的分组、状态与弹层交互。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.*
import hyper_ui.core.interaction.hyperNoRippleClickable

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UpdateDialogDemo() {
    val scope = rememberCoroutineScope()
    val controller = remember(scope) { UpdateDemoController(scope) }
    var mandatory by remember { mutableStateOf(false) }
    var dialogOpen by remember { mutableStateOf(false) }
    DisposableEffect(controller) { onDispose { controller.dispose() } }

    fun open(latest: Boolean = false, fail: Boolean = false) {
        if (dialogOpen) return
        if (!controller.busy && (latest || fail || controller.status != HyperUpdateStatus.readyToInstall)) {
            controller.check(latest, fail)
        }
        dialogOpen = true
    }

    Column(Modifier.fillMaxWidth()) {
        HyperText("更新流程", size = "small")
        Spacer(Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperButton(onPressed = if (dialogOpen) null else ({ open() }), colorStyle = "primary", type = "filled") {
                HyperText(if (controller.busy || controller.status == HyperUpdateStatus.readyToInstall) "查看更新进度" else "检查更新")
            }
            HyperButton(onPressed = if (controller.busy || dialogOpen) null else ({ open(latest = true) }), type = "tonal") {
                HyperText("已是最新")
            }
            HyperButton(onPressed = if (controller.busy || dialogOpen) null else ({ open(fail = true) }), type = "tonal") {
                HyperText("失败与重试")
            }
        }
        if (controller.status == HyperUpdateStatus.downloading) {
            Spacer(Modifier.height(12.dp))
            HyperProgress(controller.downloadProgress.fraction)
            Spacer(Modifier.height(8.dp))
            HyperText(controller.downloadProgress.sizeLabel, size = "small")
        }
        Spacer(Modifier.height(20.dp))
        HyperText("状态预览", size = "small")
        HyperText("检查中隐藏版本；其他版本信息不套卡片。标识块 40、图标 20、加载环 30；错误仅保留原因与操作，次操作使用默认描边按钮。", size = "small")
        Spacer(Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            HyperUpdateStatus.entries.forEach { status ->
                UpdatePreviewTag(updateStatusLabel(status), controller.status == status) { controller.preview(status) }
            }
        }
        Spacer(Modifier.height(12.dp))
        HyperListTile(
            title = { HyperText("强制更新") }, subtitle = { HyperText("隐藏稍后与取消入口，安装完成后恢复关闭") },
            trailing = { HyperSwitch(mandatory, if (controller.busy || dialogOpen) null else ({ mandatory = it })) }
        )
        HyperListTile(
            title = { HyperText("总大小未知") }, subtitle = { HyperText("保留下载量与速度，使用不定进度") },
            trailing = { HyperSwitch(controller.unknownTotal, controller::setUnknownTotal) }
        )
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            CompositionLocalProvider(LocalHyperUpdateDialogInlinePreview provides true) {
                UpdatePreviewDialog(controller, mandatory)
            }
        }
        Spacer(Modifier.height(12.dp))
        HyperText("示例任务由页面持有。关闭弹窗后下载继续，重新打开可查看进度；取消按钮终止下载。", size = "small")
    }
    if (dialogOpen) UpdatePreviewDialog(controller, mandatory, onClose = { dialogOpen = false })
}

@Composable
private fun UpdatePreviewDialog(controller: UpdateDemoController, mandatory: Boolean, onClose: (() -> Unit)? = null) {
    HyperUpdateDialog(
        status = controller.status, currentVersion = controller.currentVersion, release = controller.release,
        progress = controller.installProgress,
        downloadProgress = controller.downloadProgress.takeIf { controller.status == HyperUpdateStatus.downloading },
        mandatory = mandatory, onCheck = { controller.check() }, onDownload = controller::download,
        onInstall = controller::install, onRetry = controller::retry,
        onCancel = {
            controller.cancel()
            if (controller.status == HyperUpdateStatus.idle) onClose?.invoke()
        },
        onLater = onClose, onClose = onClose
    )
}

@Composable
private fun UpdatePreviewTag(label: String, selected: Boolean, onPressed: () -> Unit) {
    val color = HyperColors.secondaryText
    val shape = RoundedCornerShape(16.dp)
    val foreground = if (!selected) color else if (color.luminance() < 0.5f)
        Color(1f, 1f, 1f, 1f) else Color(0f, 0f, 0f, 1f)
    Box(Modifier.defaultMinSize(minHeight = 30.dp)
        .hyperNoRippleClickable(onClick = onPressed)
        .background(if (selected) color else hyperGlass.surfaceSubtle, shape)
        .border(1.dp, if (selected) color else HyperColors.fieldBorder, shape)
        .padding(horizontal = 13.dp, vertical = 4.dp), contentAlignment = Alignment.Center) {
        HyperText(label, color = foreground, fontSize = 13.sp, lineHeight = 15.6.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

private fun updateStatusLabel(status: HyperUpdateStatus): String = when (status) {
    HyperUpdateStatus.idle -> "待检查"
    HyperUpdateStatus.checking -> "检查中"
    HyperUpdateStatus.available -> "新版本"
    HyperUpdateStatus.upToDate -> "已是最新"
    HyperUpdateStatus.downloadQueued -> "系统排队"
    HyperUpdateStatus.downloading -> "下载中"
    HyperUpdateStatus.readyToInstall -> "待安装"
    HyperUpdateStatus.installing -> "安装中"
    HyperUpdateStatus.completed -> "已完成"
    HyperUpdateStatus.error -> "失败"
}
