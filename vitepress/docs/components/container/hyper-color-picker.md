# HyperColorPicker

包名：`hyper_ui`。

<WasmPreview demo="color-picker" title="HyperColorPicker 交互预览" />

## 公开签名与默认值

```kotlin
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HyperColorPicker(
    selectedId: String,
    onSelected: (HyperColorOption) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    options: List<HyperColorOption> = HyperColorPickerDefaults.presetOptions,
    colorSize: Dp = HyperColorPickerDefaults.colorSize,
    horizontalSpacing: Dp = HyperColorPickerDefaults.horizontalSpacing,
    verticalSpacing: Dp = HyperColorPickerDefaults.verticalSpacing,
    labelTopSpacing: Dp = HyperColorPickerDefaults.labelTopSpacing
)
```

## 最小用法

```kotlin
var selectedId by remember { mutableStateOf("ocean_blue") }

HyperColorPicker(
    selectedId = selectedId,
    onSelected = { option -> selectedId = option.id }
)
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
