# HyperRadio

包名：`hyper_ui`。

<WasmPreview demo="radio" title="HyperRadio 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperRadio(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedColor: Color = Color.Unspecified,
    unselectedColor: Color = Color.Unspecified,
    unselectedBorderColor: Color = Color.Unspecified,
    innerDotColor: Color = Color.Unspecified
)
```

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| selected | Boolean | 是 | — | 由调用方持有的选中状态。 |
| onClick | (() -&gt; Unit)? | 是 | — | 用户点击时执行的回调。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| selectedColor | Color | 否 | Color.Unspecified | 选中时的控件颜色。 |
| unselectedColor | Color | 否 | Color.Unspecified | 未选中时的控件颜色。 |
| unselectedBorderColor | Color | 否 | Color.Unspecified | 未选中时边框颜色。 |
| innerDotColor | Color | 否 | Color.Unspecified | 单选圆点的颜色。 |

## 最小用法

```kotlin
var mode by remember { mutableStateOf("balanced") }

HyperRadio(
    selected = mode == "balanced",
    onClick = { mode = "balanced" }
)
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
