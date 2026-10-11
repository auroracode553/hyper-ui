/** 文件职责：把受控更新状态映射为纯展示数据；不执行下载、检查或安装。 */
package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

internal data class HyperUpdatePresentation(
    val state: HyperUpdateDialogState,
    val title: String,
    val message: String,
    val version: String,
    val release: HyperAppRelease?,
    val icon: ImageVector,
    val color: Color,
    val busy: Boolean = false,
    val hasProgress: Boolean = false,
    val progress: Float? = null,
    val detail: String? = null
)

@Composable
internal fun hyperUpdatePresentation(state: HyperUpdateDialogState, texts: HyperUpdateDialogTexts): HyperUpdatePresentation {
    val primary = HyperColors.accent
    return when (state) {
        HyperUpdateDialogState.Idle -> HyperUpdatePresentation(state, "让体验保持最新", "检查新版本，获取最新功能与体验优化。", "", null, HyperReferenceIcons.Refresh, primary)
        is HyperUpdateDialogState.Checking -> HyperUpdatePresentation(state, texts.checkTitle, texts.checkingMessage,
            state.currentVersionName, null, HyperReferenceIcons.Refresh, primary, busy = true)
        is HyperUpdateDialogState.UpToDate -> HyperUpdatePresentation(state, "已是最新版本", texts.upToDateMessage,
            state.currentVersionName, null, HyperReferenceIcons.Check, HyperColors.success)
        is HyperUpdateDialogState.UpdateAvailable -> HyperUpdatePresentation(state, texts.updateAvailableTitle,
            "为你带来了新的功能与细节优化。", state.currentVersionName, state.release, HyperReferenceIcons.Sparkles, primary)
        is HyperUpdateDialogState.DownloadQueued -> HyperUpdatePresentation(state, texts.downloadQueuedTitle,
            texts.downloadQueuedMessage, state.currentVersionName, state.release, HyperReferenceIcons.Download, primary,
            busy = true, hasProgress = true, detail = texts.downloadNotificationHint)
        is HyperUpdateDialogState.Downloading -> HyperUpdatePresentation(state, "正在下载更新", "新版本正在路上，请保持网络连接。",
            state.currentVersionName, state.release, HyperReferenceIcons.Download, primary, busy = true, hasProgress = true,
            progress = state.progress?.takeIf { it.isFinite() }?.coerceIn(0f, 1f), detail = state.progressText)
        is HyperUpdateDialogState.ReadyToInstall -> HyperUpdatePresentation(state, "更新已准备就绪", "下载已完成，你可以开始安装新版本。",
            state.currentVersionName, state.release, HyperReferenceIcons.PackageCheck, primary)
        is HyperUpdateDialogState.Installing -> HyperUpdatePresentation(state, "正在安装更新", "正在应用新版本，请保持应用开启。",
            state.currentVersionName, state.release, HyperReferenceIcons.PackageCheck, primary, busy = true, hasProgress = true,
            progress = state.progress?.takeIf { it.isFinite() }?.coerceIn(0f, 1f), detail = state.progressText)
        is HyperUpdateDialogState.Completed -> HyperUpdatePresentation(state, "更新完成", "新版本已就绪，继续探索更好的体验。",
            state.currentVersionName, state.release, HyperReferenceIcons.Check, HyperColors.success)
        is HyperUpdateDialogState.Error -> HyperUpdatePresentation(state, texts.errorTitle, state.message,
            state.currentVersionName, null, HyperReferenceIcons.CircleAlert, HyperColors.danger)
    }
}
