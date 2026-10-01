# HyperCheckbox

包名：`hyper_ui`。

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

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| checked | Boolean | 是 | — | 由调用方持有的勾选状态。 |
| onCheckedChange | (Boolean) -&gt; Unit | 是 | — | 勾选状态变化时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| checkedColor | Color | 否 | Color.Unspecified | 勾选时的控件颜色。 |
| uncheckedColor | Color | 否 | Color.Unspecified | 未勾选时的控件颜色。 |
| uncheckedBorderColor | Color | 否 | Color.Unspecified | 未勾选时边框颜色。 |
| checkmarkColor | Color | 否 | Color(1f, 1f, 1f, 1f) | 勾选标记颜色。 |

## 最小用法

```kotlin
var accepted by remember { mutableStateOf(false) }

HyperCheckbox(
    checked = accepted,
    onCheckedChange = { accepted = it }
)
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
