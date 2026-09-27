# HyperUpdateDialog

`HyperUpdateDialog` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

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

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- Android 原生窗口和系统工具仅在 Android 目标可用；Preview 使用跨平台示例。
