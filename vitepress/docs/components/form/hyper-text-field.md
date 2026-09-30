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
