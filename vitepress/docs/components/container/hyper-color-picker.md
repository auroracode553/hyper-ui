---
description: 从颜色面板选择颜色，颜色状态由调用方持有。
---

# HyperColorPicker

从颜色面板选择颜色，颜色状态由调用方持有。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="color-picker" title="HyperColorPicker 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
var selectedId by remember { mutableStateOf("ocean_blue") }

HyperColorPicker(
    selectedId = selectedId,
    onSelected = { option -> selectedId = option.id }
)
```

</details>

## 使用约定

**约束**

业务状态由调用方管理，组件只负责 UI 与回调。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperColorPicker

<details class="component-doc__details">
<summary>查看公开签名</summary>

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

</details>

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
