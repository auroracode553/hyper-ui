# HyperTextField

`HyperTextField` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

<WasmPreview demo="text_field" title="HyperTextField 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    inputModifier: Modifier = Modifier,
    type: HyperTextFieldType = HyperTextFieldType.Text,
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
    type = HyperTextFieldType.Textarea,
    rows = 3,
    labelContent = { Text("备注") },
    placeholderContent = { Text("写一点说明") },
    maxlength = 80,
    showWordLimit = true
)
```

`type = Text` 为 68dp 单行输入，`type = Textarea` 使用 `rows` 控制多行高度，`type = Password` 自动使用密码变换并显示显隐操作。`clearable`、`maxlength` 和 `showWordLimit` 由组件统一处理；前后缀仍通过 `startContent` / `endContent` 注入。

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- Android 原生窗口和系统工具仅在 Android 目标可用；Preview 使用跨平台示例。
