# HyperToast

`HyperToast` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。 Android 原生 Toast 工具 `hyperToast(context, message)` 仍可单独使用；跨平台可视提示使用受控 `HyperToast`。

<WasmPreview demo="toast" title="HyperToast 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperToast(
    visible: Boolean,
    message: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    durationMillis: Long = 3000L,
    tone: HyperToastTone = HyperToastTone.Neutral,
    leadingContent: (@Composable () -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
)

enum class HyperToastTone { Neutral, Success, Info, Warning, Error }
```

## 最小用法

```kotlin
var visible by remember { mutableStateOf(false) }
HyperToast(
    visible = visible,
    message = "保存成功",
    onDismissRequest = { visible = false },
    tone = HyperToastTone.Success
)
```

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 默认提示宽度随内容收缩，范围为 180–420dp；文案最多三行。`tone` 决定玻璃微染色、描边和默认前导图标，`leadingContent` 可覆盖图标。
- `durationMillis = 0L` 可关闭自动计时；显示状态、关闭回调与操作区由调用方控制。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- Android 原生窗口和系统工具仅在 Android 目标可用；Preview 使用跨平台示例。
