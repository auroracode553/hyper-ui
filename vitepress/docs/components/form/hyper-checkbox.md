# HyperCheckbox

`HyperCheckbox` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

<WasmPreview demo="checkbox" title="HyperCheckbox 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedColor: Color = Color.Unspecified,
    uncheckedColor: Color = Color.Unspecified,
    uncheckedBorderColor: Color = Color.Unspecified,
    checkmarkColor: Color = Color(1f, 1f, 1f, 1f)
)
```

## 最小用法

```kotlin
var accepted by remember { mutableStateOf(false) }

HyperCheckbox(
    checked = accepted,
    onCheckedChange = { accepted = it }
)
```

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- 公开 API 仅支持 Android 手机端；Preview 只是文档交互工具。
