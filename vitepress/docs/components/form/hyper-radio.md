---
description: 用于互斥单选，选中项由调用方统一管理。
---

# HyperRadio

用于互斥单选，选中项由调用方统一管理。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="radio" title="HyperRadio 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperRadio(value = "option", groupValue = selectedValue, onChanged = { selectedValue = it })
```

</details>

## 使用约定

**样式约定**

圆环为 21dp。无标签且 onChanged 非 null 的独立控件占 44dp 触摸区；onChanged 为 null 的展示控件只占圆环尺寸。提供 label 时使用 plain 列表行，由整行承载点击，圆环不再叠加独立触摸留白；单行文字采用列表行默认排版与 46dp 最小高度。选中为 6dp 品牌色环，未选中为 1.5dp 输入轮廓；内部保持 subtle 材质。

**状态归属**

选中值由调用方持有；onChanged 为 null 时只展示。

组合 HyperListTile 时，如点击由整行处理，leading/trailing 中的 HyperRadio 应省略 onChanged，让圆环只展示。无需在应用中覆盖控件大小或列表行内边距。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperRadio

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun <T> HyperRadio(
    value: T,
    groupValue: T?,
    onChanged: ((T) -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedColor: Color = Color.Unspecified,
    unselectedColor: Color = Color.Unspecified,
    unselectedBorderColor: Color = Color.Unspecified,
    innerDotColor: Color = Color.Unspecified,
    label: String? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | T | 是 | — | 由调用方持有的当前值。 |
| groupValue | T? | 是 | — | 当前单选组的选中值。 |
| onChanged | ((T) -&gt; Unit)? | 否 | null | 用户修改值或选择项时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| selectedColor | Color | 否 | Color.Unspecified | 选中时的控件颜色。 |
| unselectedColor | Color | 否 | Color.Unspecified | 未选中时的控件颜色。 |
| unselectedBorderColor | Color | 否 | Color.Unspecified | 未选中时边框颜色。 |
| innerDotColor | Color | 否 | Color.Unspecified | 单选圆点的颜色。 |
| label | String? | 否 | null | 使用 plain 列表行展示标签，点击由整行处理。 |

### HyperRadioDefaults

| 属性 | 类型 | 值 | 用途 |
| --- | --- | --- | --- |
| TouchTargetSize | Dp | 44.dp | 无标签独立交互控件的触摸区。 |
| OuterSize | Dp | 21.dp | 圆环及展示控件尺寸。 |
| InnerDotSize | Dp | 9.dp | 选中圆环内部尺寸。 |
| BorderWidth | Dp | 1.5.dp | 未选中轮廓宽度。 |
