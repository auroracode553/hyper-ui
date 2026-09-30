# HyperToast

包名：`hyper_ui`。

<WasmPreview demo="toast" title="HyperToast 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperToast(
    visible: Boolean,
    message: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    durationMillis: Long = 3000L,
    tone: HyperToastTone = HyperToastTone.Neutral,
    leadingContent: (@Composable () -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
)

enum class HyperToastTone { Neutral, Success, Info, Warning, Error }
```

## 最小用法

```kotlin
var visible by remember { mutableStateOf(false) }
HyperToast(
    visible = visible,
    message = "保存成功",
    onDismissRequest = { visible = false },
    tone = HyperToastTone.Success
)
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
