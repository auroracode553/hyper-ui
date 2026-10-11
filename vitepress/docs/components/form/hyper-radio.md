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

44dp 触摸区、21dp 圆环。选中为 6dp 品牌色环，未选中为 1.5dp 输入轮廓；内部保持 subtle 材质。

**状态归属**

选中值由调用方持有；onChanged 为 null 时只展示。

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
| label | String? | 否 | null | 数值旁显示的标签文字。 |
