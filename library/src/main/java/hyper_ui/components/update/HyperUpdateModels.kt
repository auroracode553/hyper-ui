/** 文件职责：定义通用应用更新检查的输入、Release 信息、检查结果与弹窗状态。 */
package hyper_ui

import androidx.compose.runtime.Immutable

@Immutable
data class HyperAppRelease(
    val versionName: String,
    val displayName: String,
    val releaseNotes: String,
    val packageFileName: String,
    val packageDownloadUrl: String,
    val isPrerelease: Boolean = false
)

@Immutable
data class HyperUpdateRequest(
    val currentVersionName: String,
    val releaseUrl: String
)

fun interface HyperReleaseLoader {
    suspend fun load(releaseUrl: String): HyperAppRelease
}

sealed interface HyperUpdateCheckResult {
    val currentVersionName: String

    @Immutable
    data class UpToDate(
        override val currentVersionName: String
    ) : HyperUpdateCheckResult

    @Immutable
    data class UpdateAvailable(
        override val currentVersionName: String,
        val release: HyperAppRelease
    ) : HyperUpdateCheckResult
}

sealed interface HyperUpdateDialogState {
    data object Idle : HyperUpdateDialogState

    @Immutable
    data class Checking(
        val currentVersionName: String
    ) : HyperUpdateDialogState

    @Immutable
    data class UpToDate(
        val currentVersionName: String
    ) : HyperUpdateDialogState

    @Immutable
    data class UpdateAvailable(
        val currentVersionName: String,
        val release: HyperAppRelease
    ) : HyperUpdateDialogState

    @Immutable
    data class DownloadQueued(
        val currentVersionName: String,
        val release: HyperAppRelease,
        val downloadId: Long
    ) : HyperUpdateDialogState

    @Immutable
    data class Error(
        val currentVersionName: String,
        val message: String
    ) : HyperUpdateDialogState
}

@Immutable
data class HyperUpdateDialogTexts(
    val checkTitle: String = "检查更新",
    val updateAvailableTitle: String = "发现新版本",
    val downloadQueuedTitle: String = "已开始下载",
    val errorTitle: String = "检查更新失败",
    val checkingMessage: String = "正在请求最新版本…",
    val upToDateMessage: String = "当前已是最新版本",
    val currentVersionLabel: String = "当前版本",
    val latestVersionLabel: String = "最新版本",
    val packageFileLabel: String = "更新包",
    val prereleaseWarning: String = "这是预发布版本，请确认后再安装。",
    val releaseNotesLabel: String = "更新说明",
    val emptyReleaseNotes: String = "暂无更新说明",
    val downloadQueuedMessage: String = "更新包已交给系统下载管理器。",
    val downloadNotificationHint: String = "下载进度和完成结果可在系统通知或“下载”中查看。",
    val cancelAction: String = "取消",
    val downloadAction: String = "立即下载",
    val closeAction: String = "关闭",
    val retryAction: String = "重试"
)
