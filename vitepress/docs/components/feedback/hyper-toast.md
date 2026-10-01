# HyperToast

包名：`hyper_ui`。

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

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 是否显示组件，由调用方持有。 |
| message | String | 是 | — | 提示消息文本。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| durationMillis | Long | 否 | 3000L | 提示自动消失前的持续时间，单位毫秒。 |
| tone | HyperToastTone | 否 | HyperToastTone.Neutral | 提示的语义色类型。 |
| leadingContent | (@Composable () -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| actionContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 操作按钮或操作区内容，由调用方提供。 |

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

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
