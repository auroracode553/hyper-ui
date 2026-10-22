---
description: 受控文本输入，支持清空、密码、多行、计数、错误与只读状态。
---

# HyperTextField

受控文本输入，支持清空、密码、多行、计数、错误与只读状态。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="text_field" title="HyperTextField 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperTextField(value, { value = it }, clearable = true, placeholder = { HyperText("请输入") })
```

</details>

## 使用约定

maxLength 和 showWordLimit 当前按 UTF-16 单元截断与计数；emoji 或组合字符可能占多单位。与 Flutter 用户可见字符分段的对齐作为独立专项处理。

**样式约定**

输入框圆角 20dp。small/default/large 字号 14/16/18sp、行高 1.3，横向 padding 14/18/20dp，纵向 7/9/11dp。textarea 上下各多 3dp。支持 text/search/password/textarea/email/url/number/tel。聚焦轮廓 1.5dp，普通 1dp。

Preview 使用参考项目的外部标签分组；已经有外部标题时无需重复传入 label。金额示例保留标签插槽，并同时展示前后缀与内置清空操作。

**状态归属**

文本由调用方持有，selection/composition 和密码临时显隐由组件维护。错误优先于聚焦。showPasswordToggle 默认 false。suffix 与内置密码/清空操作同时显示；清空按钮在空值时保持占位，计数在下方右侧。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperTextField

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperTextField(
    value: String,
    onChanged: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier,
    inputModifier: Modifier = Modifier,
    type: String = "text",
    size: String = "default",
    rows: Int = HyperTextFieldDefaults.TextareaRows,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    clearable: Boolean = false,
    showPasswordToggle: Boolean = false,
    maxLength: Int? = null,
    showWordLimit: Boolean = false,
    shape: Shape = HyperTextFieldDefaults.Shape,
    colors: HyperTextFieldColors = HyperTextFieldDefaults.colors(),
    textStyle: TextStyle = HyperTextFieldDefaults.textStyle(size),
    keyboardOptions: KeyboardOptions = HyperTextFieldDefaults.keyboardOptions(type),
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    slotSpacing: Dp = HyperTextFieldDefaults.SlotSpacing,
    label: (@Composable ColumnScope.() -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    prefix: (@Composable RowScope.() -> Unit)? = null,
    suffix: (@Composable RowScope.() -> Unit)? = null,
    supporting: (@Composable ColumnScope.() -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | String | 是 | — | 由调用方持有的当前值。 |
| onChanged | ((String) -&gt; Unit)? | 否 | null | 用户修改值或选择项时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| inputModifier | Modifier | 否 | Modifier | 输入控件自身的布局和交互修饰符。 |
| type | String | 否 | &quot;text&quot; | 组件的视觉或布局形态。 |
| size | String | 否 | &quot;default&quot; | 组件尺寸档位。 |
| rows | Int | 否 | HyperTextFieldDefaults.TextareaRows | 多行输入的显示行数。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| readOnly | Boolean | 否 | false | 是否只展示当前值而禁止修改。 |
| isError | Boolean | 否 | false | 是否显示错误状态。 |
| clearable | Boolean | 否 | false | 是否在有内容时显示清空操作。 |
| showPasswordToggle | Boolean | 否 | false | 密码类型是否显示显隐操作。 |
| maxLength | Int? | 否 | null | 允许输入的最大字符数。 |
| showWordLimit | Boolean | 否 | false | 是否显示字符数和上限。 |
| shape | Shape | 否 | HyperTextFieldDefaults.Shape | 组件容器的形状。 |
| colors | HyperTextFieldColors | 否 | HyperTextFieldDefaults.colors() | 组件各状态的颜色配置。 |
| textStyle | TextStyle | 否 | HyperTextFieldDefaults.textStyle(size) | 输入文字的排版样式。 |
| keyboardOptions | KeyboardOptions | 否 | HyperTextFieldDefaults.keyboardOptions(type) | 软键盘类型与输入配置。 |
| keyboardActions | KeyboardActions | 否 | KeyboardActions() | 软键盘动作回调。 |
| visualTransformation | VisualTransformation | 否 | VisualTransformation.None | 输入文字的视觉变换方式。 |
| interactionSource | MutableInteractionSource | 否 | remember { MutableInteractionSource() } | 输入控件的交互状态来源。 |
| slotSpacing | Dp | 否 | HyperTextFieldDefaults.SlotSpacing | 输入框各内容 Slot 之间的间距。 |
| label | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 数值旁显示的标签文字。 |
| placeholder | (@Composable () -&gt; Unit)? | 否 | null | 未输入或未选择时的提示内容。 |
| prefix | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 输入框起始端内容。 |
| suffix | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 输入框末端内容。 |
| supporting | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 输入框辅助内容。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperTextFieldDefaults.contentPadding**

```kotlin
fun contentPadding(size: String = "default", multiline: Boolean = false): androidx.compose.foundation.layout.PaddingValues =
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| size | String | 否 | &quot;default&quot; | 组件尺寸档位。 |
| multiline | Boolean | 否 | false | 是否按多行输入增加纵向留白。 |

**HyperTextFieldDefaults.textStyle**

```kotlin
@Composable
    fun textStyle(size: String = "default"): TextStyle
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| size | String | 否 | &quot;default&quot; | 组件尺寸档位。 |

**HyperTextFieldDefaults.keyboardOptions**

```kotlin
fun keyboardOptions(type: String = "text"): KeyboardOptions
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| type | String | 否 | &quot;text&quot; | 组件的视觉或布局形态。 |

**HyperTextFieldDefaults.colors**

```kotlin
@Composable
    fun colors(
        containerColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        errorContainerColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        contentColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        placeholderColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        labelColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        supportingColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        errorColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        cursorColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        disabledContainerColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
        disabledContentColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified
    ): HyperTextFieldColors
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | androidx.compose.ui.graphics.Color | 否 | androidx.compose.ui.graphics.Color.Unspecified | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| errorContainerColor | androidx.compose.ui.graphics.Color | 否 | androidx.compose.ui.graphics.Color.Unspecified | 错误态的输入表面背景色。 |
| contentColor | androidx.compose.ui.graphics.Color | 否 | androidx.compose.ui.graphics.Color.Unspecified | 主要文字与图标颜色。 |
| placeholderColor | androidx.compose.ui.graphics.Color | 否 | androidx.compose.ui.graphics.Color.Unspecified | 输入占位文字颜色。 |
| labelColor | androidx.compose.ui.graphics.Color | 否 | androidx.compose.ui.graphics.Color.Unspecified | 输入标签颜色。 |
| supportingColor | androidx.compose.ui.graphics.Color | 否 | androidx.compose.ui.graphics.Color.Unspecified | 说明文字颜色。 |
| errorColor | androidx.compose.ui.graphics.Color | 否 | androidx.compose.ui.graphics.Color.Unspecified | 错误轮廓与说明颜色。 |
| cursorColor | androidx.compose.ui.graphics.Color | 否 | androidx.compose.ui.graphics.Color.Unspecified | 光标与聚焦轮廓颜色。 |
| disabledContainerColor | androidx.compose.ui.graphics.Color | 否 | androidx.compose.ui.graphics.Color.Unspecified | 禁用时的表面背景色。 |
| disabledContentColor | androidx.compose.ui.graphics.Color | 否 | androidx.compose.ui.graphics.Color.Unspecified | 禁用时的文字与图标颜色。 |

**HyperTextFieldColors 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 是 | — | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| errorContainerColor | Color | 是 | — | 错误态的输入表面背景色。 |
| contentColor | Color | 是 | — | 主要文字与图标颜色。 |
| placeholderColor | Color | 是 | — | 输入占位文字颜色。 |
| labelColor | Color | 是 | — | 输入标签颜色。 |
| supportingColor | Color | 是 | — | 说明文字颜色。 |
| errorColor | Color | 是 | — | 错误轮廓与说明颜色。 |
| cursorColor | Color | 是 | — | 光标与聚焦轮廓颜色。 |
| disabledContainerColor | Color | 是 | — | 禁用时的表面背景色。 |
| disabledContentColor | Color | 是 | — | 禁用时的正文颜色；默认沿用正常文字色。 |

</details>

onChanged 为 null 时只读。业务从外部修改 value 后组件同步文本，保持可用的光标与组合区间。Flutter 对应受控用法使用同名的 value/onChanged，controller 属于 Flutter 的原生编辑入口。
