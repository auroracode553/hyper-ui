# HyperTextField

- 分类：表单组件
- 包名：`hyper_ui`
- 状态模型：受控组件；文本值由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/input/HyperTextField.kt`
- Preview 注册：`preview/src/commonMain/kotlin/hyper_ui/docs/data/FormComponentDemos.kt`
- Preview 交互：`preview/src/commonMain/kotlin/hyper_ui/docs/ui/FormComponentShowcases.kt`

## 公开 API

```kotlin
@Composable
fun HyperTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    minHeight: Dp = 52.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
)
```

## 关键公开类型

无专属配置类型；键盘和显示转换使用 Compose 的标准类型。

## 参数

| 参数 | 类型 | 必填 | 默认值 | 状态归属 / 作用 |
| --- | --- | --- | --- | --- |
| `value` | `String` | 是 | 无 | 调用方持有的当前文本。 |
| `onValueChange` | `(String) -> Unit` | 是 | 无 | 用户编辑时回传新文本；调用方应写回状态。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 应用于包含可选标签和输入框的外层 `Column`。 |
| `label` | `String?` | 否 | `null` | 输入框上方标签；`null` 或空白字符串不显示。 |
| `placeholder` | `String?` | 否 | `null` | 值为空时显示的占位文案；`null` 或空白字符串不显示。 |
| `enabled` | `Boolean` | 否 | `true` | 调用方控制是否允许编辑。 |
| `singleLine` | `Boolean` | 否 | `true` | `true` 为单行；`false` 允许多行输入。 |
| `minHeight` | `Dp` | 否 | `52.dp` | 输入区域的最小高度。 |
| `keyboardOptions` | `KeyboardOptions` | 否 | `KeyboardOptions.Default` | 键盘类型、IME action 等 Compose 标准配置。 |
| `keyboardActions` | `KeyboardActions` | 否 | `KeyboardActions()` | IME 动作回调。 |
| `visualTransformation` | `VisualTransformation` | 否 | `VisualTransformation.None` | 显示转换，例如密码掩码。 |
| `leadingContent` | `@Composable () -> Unit` | 否 | `null` | 输入内容前方的可组合插槽。 |
| `trailingContent` | `@Composable () -> Unit` | 否 | `null` | 输入内容后方的可组合插槽。 |

## 状态归属

- 组件不保存表单值；调用方必须将 `onValueChange` 的值写回 `value` 对应状态。
- 校验结果、错误文案、提交状态和持久化均由调用方管理。
- 组件内部只管理焦点及其视觉反馈。

## 最小用法

```kotlin
var name by remember { mutableStateOf("") }

HyperTextField(
    value = name,
    onValueChange = { name = it },
    label = "昵称",
    placeholder = "请输入昵称"
)
```

多行输入：

```kotlin
HyperTextField(
    value = note,
    onValueChange = { note = it },
    singleLine = false,
    minHeight = 92.dp
)
```

## 约束与行为

- `enabled = false` 时不接受文本编辑，仍需传入 `onValueChange`；容器使用默认背景时透明度降至 `0.72f`，内容整体按 `HyperStyleDefaults.DisabledAlpha` 淡化。
- 输入容器默认使用 `HyperColors.elevatedContainer` 半透明玻璃托盘，叠加 `glassHighlightBrush` 顶部高光（与 `HyperIconButton` 一致）；聚焦时额外叠一层 `accent` 8% 覆盖层并切换描边粗细与颜色。
- `keyboardOptions`、`keyboardActions` 和 `visualTransformation` 使用 Compose 标准类型，不属于 `hyper_ui` 包。
- 组件没有 `isError`、`supportingText` 或自动校验参数；错误状态需要由调用方在组件外组合展示。
- `visualTransformation` 只改变显示，不替调用方保存或保护敏感数据。
- `leadingContent` 与 `trailingContent` 是无接收者插槽，不要按 `RowScope` API 调用。

## 常见误用

```kotlin
// 错误：组件没有 initialValue，也不会自行保存输入。
HyperTextField(
    initialValue = "HyperUI",
    onValueChange = { }
)
```

必须传入 `value`，并在 `onValueChange` 中更新调用方状态。

## 相关 API

- `KeyboardOptions`
- `KeyboardActions`
- `VisualTransformation`

## 交互预览

<WasmPreview demo="text_field" title="HyperTextField 交互预览" />
