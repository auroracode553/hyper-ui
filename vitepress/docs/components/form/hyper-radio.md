# HyperRadio

包名：`hyper_ui`。

<WasmPreview demo="radio" title="HyperRadio 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperRadio(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedColor: Color = Color.Unspecified,
    unselectedColor: Color = Color.Unspecified,
    unselectedBorderColor: Color = Color.Unspecified,
    innerDotColor: Color = Color.Unspecified
)
```

## 最小用法

```kotlin
var mode by remember { mutableStateOf("balanced") }

HyperRadio(
    selected = mode == "balanced",
    onClick = { mode = "balanced" }
)
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
