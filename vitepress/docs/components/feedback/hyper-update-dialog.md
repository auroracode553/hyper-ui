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

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| state | HyperUpdateDialogState | 是 | — | 组件使用的状态对象，由调用方提供或记忆。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| onRetry | () -&gt; Unit | 是 | — | 用户触发重试时的回调。 |
| onDownload | () -&gt; Unit | 是 | — | 用户触发下载时的回调。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| texts | HyperUpdateDialogTexts | 否 | HyperUpdateDialogTexts() | 组件内置文案的配置。 |
| dismissOnClickOutside | Boolean | 否 | true | 点击外部时是否请求关闭。 |

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
