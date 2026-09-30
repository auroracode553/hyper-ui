# HyperEmptyState

包名：`hyper_ui`。

<WasmPreview demo="empty_state" title="HyperEmptyState 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    contentModifier: Modifier = Modifier,
    colors: HyperEmptyStateColors = HyperEmptyStateDefaults.colors(),
    iconContent: (@Composable () -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
)
```

## 最小用法

```kotlin
HyperEmptyState(title = "暂无内容", description = "稍后再试", iconContent = { HyperText("○") })
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
