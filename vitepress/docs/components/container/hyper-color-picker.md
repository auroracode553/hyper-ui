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

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| selectedId | String | 是 | — | 当前颜色选项 ID，由调用方持有。 |
| onSelected | (HyperColorOption) -&gt; Unit | 是 | — | 选中项变化时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| options | List&lt;HyperColorOption&gt; | 否 | HyperColorPickerDefaults.presetOptions | 可选颜色配置。 |
| colorSize | Dp | 否 | HyperColorPickerDefaults.colorSize | 单个颜色选项的尺寸。 |
| horizontalSpacing | Dp | 否 | HyperColorPickerDefaults.horizontalSpacing | 颜色选项之间的水平间距。 |
| verticalSpacing | Dp | 否 | HyperColorPickerDefaults.verticalSpacing | 颜色选项之间的垂直间距。 |
| labelTopSpacing | Dp | 否 | HyperColorPickerDefaults.labelTopSpacing | 颜色名称与色块之间的间距。 |

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
