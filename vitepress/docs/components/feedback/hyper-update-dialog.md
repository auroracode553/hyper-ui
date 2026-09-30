# HyperUpdateDialog

包名：`hyper_ui`。

<WasmPreview demo="update_dialog" title="HyperUpdateDialog 交互预览" />

## 公开签名与默认值

```kotlin
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

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
