# HyperSwitch

包名：`hyper_ui`。

<WasmPreview demo="switch" title="HyperSwitch 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedTrackColor: Color = Color.Unspecified,
    uncheckedTrackColor: Color = Color.Unspecified,
    checkedThumbColor: Color = Color(1f, 1f, 1f, 1f),
    uncheckedThumbColor: Color = Color.Unspecified
)
```

## 最小用法

```kotlin
var enabled by remember { mutableStateOf(false) }

HyperSwitch(
    checked = enabled,
    onCheckedChange = { enabled = it }
)
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
