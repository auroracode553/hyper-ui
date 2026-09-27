# HyperColorPicker

`HyperColorPicker` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

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

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- Android 原生窗口和系统工具仅在 Android 目标可用；Preview 使用跨平台示例。
