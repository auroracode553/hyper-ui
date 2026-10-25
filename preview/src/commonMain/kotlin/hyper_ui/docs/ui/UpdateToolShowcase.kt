package hyper_ui.docs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import hyper_ui.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** 仅模拟 Android 工具的时机和交互，平台实现不进入 Wasm/Desktop 编译链。 */
@Composable
fun UpdateToolDemo() {
    val scope = rememberCoroutineScope()
    val flow = remember(scope) { UpdateDemoController(scope) }
    var pending by remember { mutableStateOf<Job?>(null) }
    var due by remember { mutableStateOf(true) }
    var visible by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("本页为交互模拟，不发起真实更新或安装。") }
    DisposableEffect(flow) { onDispose { pending?.cancel(); flow.dispose() } }
    fun check(wait: Long, manual: Boolean) {
        pending?.cancel()
        if (!manual && !due) { message = "尚未到检查间隔，自动检查跳过。"; return }
        message = if (wait > 0) "等待稳定 ${wait / 1000} 秒后检查…" else "手动检查复用现有流程。"
        pending = scope.launch {
            delay(wait)
            flow.check()
            due = false
            visible = true
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HyperText("统一检查、下载与安装工具")
        HyperText(message, size = "small")
        HyperListTile(title = { HyperText("自动检查已到期") },
            trailing = { HyperSwitch(due, { due = it }) })
        HyperButton(onPressed = { check(5000, false) }) { HyperText("模拟冷启动 · 5 秒") }
        HyperButton(onPressed = { check(2000, false) }) { HyperText("模拟恢复前台 · 2 秒") }
        HyperButton(onPressed = { check(0, true) }) { HyperText("手动检查 / 查看进度") }
        HyperButton(onPressed = { pending?.cancel(); visible = false; message = "检查回调已移除，下载继续。" }) {
            HyperText("模拟进入后台")
        }
        HyperListTile(title = { HyperText("下载总大小未知") },
            trailing = { HyperSwitch(flow.unknownTotal, flow::updateUnknownTotal) })
        if (flow.status == HyperUpdateStatus.downloading) HyperText(flow.downloadProgress.sizeLabel, size = "small")
    }
    if (visible) HyperUpdateDialog(status = flow.status, currentVersion = flow.currentVersion,
        release = flow.release, progress = flow.installProgress, downloadProgress = flow.downloadProgress,
        onCheck = { check(0, true) }, onDownload = flow::download, onInstall = flow::install,
        onRetry = flow::retry, onCancel = flow::cancel, onLater = { visible = false }, onClose = { visible = false })
}
