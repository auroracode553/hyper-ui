# HyperSwitch

`HyperSwitch` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

<WasmPreview demo="switch" title="HyperSwitch 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedTrackColor: Color = Color.Unspecified,
    uncheckedTrackColor: Color = Color.Unspecified,
    checkedThumbColor: Color = Color(1f, 1f, 1f, 1f),
    uncheckedThumbColor: Color = Color.Unspecified
)
```

## 最小用法

```kotlin
var enabled by remember { mutableStateOf(false) }

HyperSwitch(
    checked = enabled,
    onCheckedChange = { enabled = it }
)
```

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 轨道为 52×32dp，滑块为 24dp，触摸区域为 52×44dp；关闭态使用主题中性底色及细描边，明暗主题自动适配。
- `checkedTrackColor`、`uncheckedTrackColor` 和滑块颜色可覆盖默认配色；禁用态使用弱化的主题颜色。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- 公开 API 仅支持 Android 手机端；Preview 只是文档交互工具。
