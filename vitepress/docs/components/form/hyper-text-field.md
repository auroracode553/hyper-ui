# HyperTextField

包名：`hyper_ui`。

<WasmPreview demo="text_field" title="HyperTextField 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    inputModifier: Modifier = Modifier,
    type: String = "text",
    size: String = "default",
    rows: Int = HyperTextFieldDefaults.TextareaRows,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    clearable: Boolean = false,
    showPasswordToggle: Boolean = true,
    maxlength: Int? = null,
    showWordLimit: Boolean = false,
    shape: Shape = HyperTextFieldDefaults.Shape,
    colors: HyperTextFieldColors = HyperTextFieldDefaults.colors(),
    textStyle: TextStyle = HyperTheme.typography.bodyMedium.copy(
        fontSize = 14.sp,
        lineHeight = 19.sp
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    slotSpacing: Dp = HyperTextFieldDefaults.SlotSpacing,
    labelContent: (@Composable ColumnScope.() -> Unit)? = null,
    placeholderContent: (@Composable () -> Unit)? = null,
    startContent: (@Composable RowScope.() -> Unit)? = null,
    endContent: (@Composable RowScope.() -> Unit)? = null,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null
)
```

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | String | 是 | — | 由调用方持有的当前值。 |
| onValueChange | (String) -&gt; Unit | 是 | — | 值变化时通知调用方更新状态。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| inputModifier | Modifier | 否 | Modifier | 输入控件自身的布局和交互修饰符。 |
| type | String | 否 | &quot;text&quot; | 组件的视觉或布局形态。 |
| size | String | 否 | &quot;default&quot; | 组件尺寸档位。 |
| rows | Int | 否 | HyperTextFieldDefaults.TextareaRows | 多行输入的显示行数。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| readOnly | Boolean | 否 | false | 是否只展示当前值而禁止修改。 |
| isError | Boolean | 否 | false | 是否显示错误状态。 |
| clearable | Boolean | 否 | false | 是否在有内容时显示清空操作。 |
| showPasswordToggle | Boolean | 否 | true | 密码类型是否显示显隐操作。 |
| maxlength | Int? | 否 | null | 允许输入的最大字符数。 |
| showWordLimit | Boolean | 否 | false | 是否显示字符数和上限。 |
| shape | Shape | 否 | HyperTextFieldDefaults.Shape | 组件容器的形状。 |
| colors | HyperTextFieldColors | 否 | HyperTextFieldDefaults.colors() | 组件各状态的颜色配置。 |
| textStyle | TextStyle | 否 | HyperTheme.typography.bodyMedium.copy( fontSize = 14.sp, lineHeight = 19.sp ) | 输入文字的排版样式。 |
| keyboardOptions | KeyboardOptions | 否 | KeyboardOptions.Default | 软键盘类型与输入配置。 |
| keyboardActions | KeyboardActions | 否 | KeyboardActions() | 软键盘动作回调。 |
| visualTransformation | VisualTransformation | 否 | VisualTransformation.None | 输入文字的视觉变换方式。 |
| interactionSource | MutableInteractionSource | 否 | remember { MutableInteractionSource() } | 输入控件的交互状态来源。 |
| slotSpacing | Dp | 否 | HyperTextFieldDefaults.SlotSpacing | 输入框各内容 Slot 之间的间距。 |
| labelContent | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 输入框标签内容。 |
| placeholderContent | (@Composable () -&gt; Unit)? | 否 | null | 输入框占位内容。 |
| startContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 输入框起始端的自定义内容。 |
| endContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 输入框末端的自定义内容。 |
| supportingContent | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 标题或输入框下方的辅助内容。 |

## 最小用法

```kotlin
HyperTextField(
    value = value,
    onValueChange = { value = it },
    type = "textarea",
    rows = 3,
    labelContent = { Text("备注") },
    placeholderContent = { Text("写一点说明") },
    maxlength = 80,
    showWordLimit = true
)
```

`type = "text"` 为单行输入，`type = "textarea"` 使用 `rows` 控制多行高度，`type = "password"` 自动使用密码变换并显示显隐操作。`size` 支持 `small`、`default`、`large`；`clearable`、`maxlength` 和 `showWordLimit` 由组件统一处理。

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
