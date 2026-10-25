package hyper_ui

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
fun rememberHyperUpdateController(configurationJson: String): HyperUpdateController {
    val configuration = remember(configurationJson) { HyperUpdateConfig.fromJson(configurationJson) }
    return rememberHyperUpdateController(configuration)
}

/** 页面根节点持有工具；离开 Composition 关闭观察，系统下载和下载记录继续保留。 */
@Composable
fun rememberHyperUpdateController(configuration: HyperUpdateConfig): HyperUpdateController {
    val context = LocalContext.current.applicationContext
    val controller = remember(context, configuration) { HyperUpdateController(context, configuration) }
    DisposableEffect(controller) { onDispose { controller.close() } }
    return controller
}

/** 自动生命周期接入和统一更新窗口；在 Activity 的稳定根节点调用一次。 */
@Composable
fun HyperUpdateHost(controller: HyperUpdateController, visible: Boolean = true) {
    val context = LocalContext.current
    val activity = remember(context) { context.updateActivity() }
    DisposableEffect(controller, activity) {
        val application = activity.application
        val callbacks = object : Application.ActivityLifecycleCallbacks {
            override fun onActivityResumed(current: Activity) { if (current === activity) controller.foreground(true) }
            override fun onActivityPaused(current: Activity) { if (current === activity) controller.foreground(false) }
            override fun onActivityCreated(current: Activity, state: Bundle?) = Unit
            override fun onActivityStarted(current: Activity) = Unit
            override fun onActivityStopped(current: Activity) = Unit
            override fun onActivitySaveInstanceState(current: Activity, state: Bundle) = Unit
            override fun onActivityDestroyed(current: Activity) = Unit
        }
        application.registerActivityLifecycleCallbacks(callbacks)
        // 稳定页面宿主首次进入后即登记；随后的真实 pause/resume 回调修正状态。
        controller.foreground(true)
        onDispose { application.unregisterActivityLifecycleCallbacks(callbacks); controller.foreground(false) }
    }
    LaunchedEffect(controller) { controller.pageReady() }
    if (visible && controller.isForeground && controller.dialogVisible) {
        val state = controller.state
        HyperUpdateDialog(status = state.status, currentVersion = controller.currentVersionName,
            release = state.version?.let { HyperUpdateRelease(it,
                sizeLabel = state.totalBytes?.let { size -> String.format(Locale.ROOT, "%.1f MB", size / 1_000_000.0) },
                highlights = listOfNotNull(state.notes.takeIf(String::isNotBlank))) },
            downloadProgress = HyperUpdateDownloadProgress(state.downloadedBytes, state.totalBytes, state.bytesPerSecond),
            message = state.message ?: if (state.status == HyperUpdateStatus.readyToInstall)
                "安装包已下载，点击后由系统确认安装。" else null,
            onCheck = { controller.check() }, onDownload = controller::download,
            onInstall = controller::install, onRetry = controller::retry,
            onCancel = if (controller.canCancel) controller::cancelDownload else null,
            onLater = controller::dismiss, onClose = controller::dismiss)
    }
}

private fun Context.updateActivity(): Activity = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.updateActivity()
    else -> error("HyperUpdateHost 必须位于 Activity 页面中")
}
