# HyperUpdateDialog

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/update/`
- 预览：`update_dialog`

`HyperUpdateDialog` 使用不透明实色面板，统一展示检查中、已是最新、发现新版本、下载已入队和错误状态；内部按钮与进度指示器也不使用透明度。`HyperUpdateChecker` 负责版本判断，但不发起网络请求；调用方通过 `HyperReleaseLoader` 提供 Release 加载实现，并通过 `HyperUpdateRequest.releaseUrl` 传入检查地址。

## 公开签名

```kotlin
data class HyperAppRelease(
    val versionName: String,
    val displayName: String,
    val releaseNotes: String,
    val packageFileName: String,
    val packageDownloadUrl: String,
    val isPrerelease: Boolean = false
)

data class HyperUpdateRequest(
    val currentVersionName: String,
    val releaseUrl: String
)

fun interface HyperReleaseLoader {
    suspend fun load(releaseUrl: String): HyperAppRelease
}

class HyperUpdateChecker(releaseLoader: HyperReleaseLoader) {
    suspend fun check(request: HyperUpdateRequest): HyperUpdateCheckResult
}

object HyperVersionNameComparator {
    fun isNewer(candidate: String, current: String): Boolean
    fun compare(left: String, right: String): Int
}

fun HyperUpdateCheckResult.toDialogState(): HyperUpdateDialogState

sealed interface HyperUpdateDialogState {
    data object Idle : HyperUpdateDialogState
    data class Checking(val currentVersionName: String) : HyperUpdateDialogState
    data class UpToDate(val currentVersionName: String) : HyperUpdateDialogState
    data class UpdateAvailable(
        val currentVersionName: String,
        val release: HyperAppRelease
    ) : HyperUpdateDialogState
    data class DownloadQueued(
        val currentVersionName: String,
        val release: HyperAppRelease,
        val downloadId: Long
    ) : HyperUpdateDialogState
    data class Error(
        val currentVersionName: String,
        val message: String
    ) : HyperUpdateDialogState
}

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
    val downloadNotificationHint: String =
        "下载进度和完成结果可在系统通知或“下载”中查看。",
    val cancelAction: String = "取消",
    val downloadAction: String = "立即下载",
    val closeAction: String = "关闭",
    val retryAction: String = "重试"
)

@Composable
fun HyperUpdateDialog(
    state: HyperUpdateDialogState,
    onDismissRequest: () -> Unit,
    onRetry: () -> Unit,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier,
    texts: HyperUpdateDialogTexts = HyperUpdateDialogTexts(),
    dismissOnClickOutside: Boolean = true
)
```

`HyperUpdateCheckResult` 提供 `UpToDate` 与 `UpdateAvailable`；它们分别携带当前版本，或当前版本加最新 Release。

## 最小用法

```kotlin
val checker = HyperUpdateChecker(
    HyperReleaseLoader { releaseUrl ->
        // 调用方在仓库层访问 releaseUrl，并映射为 HyperAppRelease。
        updateRepository.loadLatestRelease(releaseUrl)
    }
)

val result = checker.check(
    HyperUpdateRequest(
        currentVersionName = currentVersionName,
        releaseUrl = "https://example.com/releases/latest"
    )
)
updateState = result.toDialogState()

HyperUpdateDialog(
    state = updateState,
    onDismissRequest = { updateState = HyperUpdateDialogState.Idle },
    onRetry = ::checkForUpdate,
    onDownload = ::enqueueSystemDownload
)
```

## 状态与约束

- 所有状态由调用方持有；组件不创建 ViewModel，不保存网络或下载状态。
- `HyperUpdateChecker` 只调用注入的 `HyperReleaseLoader` 并比较版本，不依赖 Gitee、GitHub 或任何网络库。
- `releaseUrl` 由调用方传入，其协议、鉴权和 JSON 解析均由调用方负责。
- 版本比较支持可选 `v` 前缀、不同长度的数字段、预发布标识和构建元数据。
- `onDownload` 只表达用户确认；实际下载、权限和安装流程由调用方实现。
- `texts` 可完整替换可见文案；默认提供简体中文。
- 弹窗继承 `HyperAlertDialog` 禁用平台默认宽度、固定全尺寸根节点和屏蔽窗口动画的 Dialog 宿主；面板保持不透明实色。

<WasmPreview demo="update_dialog" title="HyperUpdateDialog 交互预览" />
