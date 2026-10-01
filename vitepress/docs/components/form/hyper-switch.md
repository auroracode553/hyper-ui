# HyperSwitch

包名：`hyper_ui`。

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

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| checked | Boolean | 是 | — | 由调用方持有的勾选状态。 |
| onCheckedChange | (Boolean) -&gt; Unit | 是 | — | 勾选状态变化时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| checkedTrackColor | Color | 否 | Color.Unspecified | 开启时轨道颜色。 |
| uncheckedTrackColor | Color | 否 | Color.Unspecified | 关闭时轨道颜色。 |
| checkedThumbColor | Color | 否 | Color(1f, 1f, 1f, 1f) | 开启时滑块颜色。 |
| uncheckedThumbColor | Color | 否 | Color.Unspecified | 关闭时滑块颜色。 |

## 最小用法

```kotlin
var enabled by remember { mutableStateOf(false) }

HyperSwitch(
    checked = enabled,
    onCheckedChange = { enabled = it }
)
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
