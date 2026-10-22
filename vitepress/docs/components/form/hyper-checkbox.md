---
description: 用于独立复选或多选，支持三态与禁用状态。
---

# HyperCheckbox

用于独立复选或多选，支持三态与禁用状态。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="checkbox" title="HyperCheckbox 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperCheckbox(value, { value = it }, tristate = true)
```

</details>

## 使用约定

**样式约定**

44dp 触摸区、21dp 可见方框、7dp 圆角、1.5dp 轮廓、15dp 勾或横线。

提供 label 时使用 plain 标签行，不附带卡片背景、外框或阴影，保留整行点击。尾随方框只占 21dp，不叠加独立 44dp 触摸空间；普通标签行沿用 46dp 最小高度。组件预览直接展示默认标签行及纯控件，不额外包裹 HyperCard。

**状态归属**

value 为 Boolean?。两态必须非 null；三态按 false → true → null → false 循环。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperCheckbox

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperCheckbox(
    value: Boolean?,
    onChanged: ((Boolean?) -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tristate: Boolean = false,
    checkedColor: Color = Color.Unspecified,
    uncheckedColor: Color = Color.Unspecified,
    uncheckedBorderColor: Color = Color.Unspecified,
    checkmarkColor: Color = Color(1f, 1f, 1f, 1f),
    label: String? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | Boolean? | 是 | — | 由调用方持有的当前值。 |
| onChanged | ((Boolean?) -&gt; Unit)? | 否 | null | 用户修改值或选择项时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| tristate | Boolean | 否 | false | 允许 null 不确定状态，false → true → null → false。 |
| checkedColor | Color | 否 | Color.Unspecified | 勾选时的控件颜色。 |
| uncheckedColor | Color | 否 | Color.Unspecified | 未勾选时的控件颜色。 |
| uncheckedBorderColor | Color | 否 | Color.Unspecified | 未勾选时边框颜色。 |
| checkmarkColor | Color | 否 | Color(1f, 1f, 1f, 1f) | 勾选标记颜色。 |
| label | String? | 否 | null | 无卡片的 plain 标签行，点击由整行处理。 |
