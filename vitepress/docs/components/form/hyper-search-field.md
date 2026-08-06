# HyperSearchField

- 分类：表单组件
- 包名：`hyper_ui`
- 状态模型：受控组件；搜索文本由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/input/HyperSearchField.kt`
- Preview 注册：`preview/src/commonMain/kotlin/hyper_ui/docs/data/FormComponentDemos.kt`
- Preview 交互：`preview/src/commonMain/kotlin/hyper_ui/docs/ui/FormComponentShowcases.kt`

## 公开 API

```kotlin
@Composable
fun HyperSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索",
    enabled: Boolean = true
)
```

## 关键公开类型

无专属配置类型。

## 参数

| 参数 | 类型 | 必填 | 默认值 | 状态归属 / 作用 |
| --- | --- | --- | --- | --- |
| `value` | `String` | 是 | 无 | 调用方持有的当前搜索文本。 |
| `onValueChange` | `(String) -> Unit` | 是 | 无 | 编辑或点击内置清空按钮时回传新文本。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 调整输入框外层布局。 |
| `placeholder` | `String` | 否 | `"搜索"` | 值为空时显示的占位文案。 |
| `enabled` | `Boolean` | 否 | `true` | 控制文本输入是否可编辑。当前实现不把该值传给内置清空按钮。 |

## 状态归属

- 组件不保存搜索文本；调用方必须将 `onValueChange` 的值写回状态。
- 搜索触发、过滤、网络请求、防抖和结果列表均由调用方管理。
- 组件内部只管理焦点、搜索图标和非空值时的清空按钮。

## 最小用法

```kotlin
var keyword by remember { mutableStateOf("") }

HyperSearchField(
    value = keyword,
    onValueChange = { keyword = it },
    placeholder = "搜索组件"
)
```

## 约束与行为

- 输入固定为单行。
- 当 `value` 非空时显示清空按钮；点击后调用 `onValueChange("")`。
- 组件没有 `onSearch`、`keyboardOptions`、`keyboardActions`、自定义前后插槽或多行参数。
- `enabled = false` 会禁用文本编辑；但当前源码中的内置清空按钮仍使用默认 `enabled = true`。若禁用态必须完全不可变，调用方应在回调中拒绝更新，或在值为空后再禁用。
- 组件不会自动执行过滤、提交或防抖。

## 常见误用

```kotlin
// 错误：HyperSearchField 没有 onSearch 参数。
HyperSearchField(
    value = keyword,
    onValueChange = { keyword = it },
    onSearch = { search(keyword) }
)
```

应在调用方观察 `keyword`，或在外部操作按钮、IME 逻辑中触发搜索。

## 相关 API

- `HyperTextField`：需要键盘动作、显示转换、前后插槽或多行输入时使用。
- `HyperIconButton`：当前内置清空按钮使用该组件。

## 交互预览

<WasmPreview demo="search" title="HyperSearchField 交互预览" />
