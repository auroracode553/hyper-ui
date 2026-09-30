# HyperTooltip

包名：`hyper_ui`。

<WasmPreview demo="tooltip" title="HyperTooltip 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperTooltip(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
)
```

## 最小用法

```kotlin
HyperTooltip(text = "提示文本") {
    HyperText("悬停查看")
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
