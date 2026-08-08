# HyperTextField

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/input/HyperTextField.kt`
- 预览：`text_field`、`search`

`HyperTextField` 是 slot-first 输入框。搜索框、地址栏、页内查找栏和普通表单输入都通过同一个组件组合；UI 库不再提供固定搜索图标或固定清空按钮。

## 公开签名

```kotlin
@Composable
fun HyperTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    inputModifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minHeight: Dp = HyperTextFieldDefaults.MinHeight,
    shape: Shape = HyperTextFieldDefaults.Shape,
    colors: HyperTextFieldColors = HyperTextFieldDefaults.colors(),
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(...),
    contentPadding: PaddingValues = HyperTextFieldDefaults.ContentPadding,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    labelContent: (@Composable ColumnScope.() -> Unit)? = null,
    placeholderContent: (@Composable () -> Unit)? = null,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null
)
```

## 最小用法

```kotlin
HyperTextField(
    value = value,
    onValueChange = { value = it },
    labelContent = { Text("备注") },
    placeholderContent = { Text("写一点说明") },
    supportingContent = { Text("${value.length}/80") },
    singleLine = false,
    minLines = 3,
    maxLines = 5
)
```

## 搜索/地址栏组合

```kotlin
HyperTextField(
    value = keyword,
    onValueChange = { keyword = it },
    placeholderContent = { Text("搜索或输入网址") },
    leadingContent = {
        Icon(Icons.Default.Search, contentDescription = null)
    },
    trailingContent = {
        HyperIconButton(onClick = { keyword = "" }) {
            Icon(Icons.Default.Close, contentDescription = "清空")
        }
    }
)
```

## 约束

- 不存在 `label`、`placeholder`、`errorText` 字符串参数；可见文本全部通过 slot 渲染。
- `inputModifier` 用于传入 `focusRequester` 等需要作用在 `BasicTextField` 上的修饰符。
- 聚焦时不改变输入框容器背景；容器只区分普通、错误和禁用状态。
- 错误态通过 `isError` 和 `supportingContent` 组合表达。

<WasmPreview demo="text_field" title="HyperTextField 交互预览" />
<WasmPreview demo="search" title="HyperTextField 搜索组合预览" />
