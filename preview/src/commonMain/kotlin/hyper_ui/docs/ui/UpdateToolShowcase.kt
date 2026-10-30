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
    var automaticChecks by remember { mutableStateOf(0) }
    var fail by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(false) }
    var savedApk by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("本页为交互模拟，不发起真实更新或安装。") }
    DisposableEffect(flow) { onDispose { pending?.cancel(); flow.dispose() } }
    LaunchedEffect(flow.status) {
        if (flow.status == HyperUpdateStatus.readyToInstall) savedApk = true
        if (flow.status == HyperUpdateStatus.completed && savedApk) {
            savedApk = false
            message = "模拟已安装版本确认成功，已清理工具记录的 APK。"
        }
    }
    fun check(wait: Long, manual: Boolean) {
        pending?.cancel()
        message = if (wait > 0) "等待稳定 ${wait / 1000} 秒后判断本小时剩余额度。" else "手动检查不占自动额度，已有任务则复用。"
        pending = scope.launch {
            delay(wait)
            if (!manual && automaticChecks >= 2) { message = "本小时自动检查已满 2 次，不发请求。"; return@launch }
            val reused = flow.busy
            flow.check(fail = fail)
            if (!reused && !manual) automaticChecks += 1
            message = if (reused) "已复用当前检查或下载。" else "已发起检查；自动检查无论成败都计数，手动不占额度。"
            visible = true
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HyperText("统一检查、下载与安装工具")
        HyperText(message)
        HyperText("默认 1 小时内最多自动检查 2 次，已用 $automaticChecks / 2")
        HyperButton(onPressed = { automaticChecks = 0; message = "已模拟原窗口满 1 小时，自动额度恢复。" }) {
            HyperText("模拟一小时窗口到期")
        }
        HyperListTile(title = { HyperText("模拟检查失败") },
            trailing = { HyperSwitch(fail, { fail = it }) })
        HyperButton(onPressed = { check(5000, false) }) { HyperText("模拟冷启动 · 5 秒") }
        HyperButton(onPressed = { check(2000, false) }) { HyperText("模拟恢复前台 · 2 秒") }
        HyperButton(onPressed = { check(0, true) }) { HyperText("手动检查 / 查看进度") }
        HyperButton(onPressed = { pending?.cancel(); visible = false; message = "检查回调已移除，下载继续。" }) {
            HyperText("模拟进入后台")
        }
        HyperListTile(title = { HyperText("下载总大小未知") },
            trailing = { HyperSwitch(flow.unknownTotal, flow::updateUnknownTotal) })
        HyperText(if (savedApk) "安装包已保留，等待安装成功后清理。" else "当前没有等待清理的安装包。")
        if (flow.status == HyperUpdateStatus.readyToInstall) {
            HyperButton(onPressed = { visible = false; message = "模拟取消安装，安装包继续保留。" }) {
                HyperText("模拟取消安装")
            }
        }
        if (flow.status == HyperUpdateStatus.downloading) HyperText(flow.downloadProgress.sizeLabel)
    }
    if (visible) HyperUpdateDialog(status = flow.status, currentVersion = flow.currentVersion,
        release = flow.release, progress = flow.installProgress, downloadProgress = flow.downloadProgress,
        onCheck = { check(0, true) }, onDownload = flow::download, onInstall = flow::install,
        onRetry = flow::retry, onCancel = flow::cancel, onLater = { visible = false }, onClose = { visible = false })
}
