# HyperTooltip

包名：`hyper_ui`。

<WasmPreview demo="tooltip" title="HyperTooltip 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperTooltip(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
)
```

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| text | String | 是 | — | 提示文字。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| content | @Composable () -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

## 最小用法

```kotlin
HyperTooltip(text = "提示文本") {
    HyperText("悬停查看")
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
