# HyperCheckbox

包名：`hyper_ui`。

<WasmPreview demo="checkbox" title="HyperCheckbox 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedColor: Color = Color.Unspecified,
    uncheckedColor: Color = Color.Unspecified,
    uncheckedBorderColor: Color = Color.Unspecified,
    checkmarkColor: Color = Color(1f, 1f, 1f, 1f)
)
```

## 最小用法

```kotlin
var accepted by remember { mutableStateOf(false) }

HyperCheckbox(
    checked = accepted,
    onCheckedChange = { accepted = it }
)
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
