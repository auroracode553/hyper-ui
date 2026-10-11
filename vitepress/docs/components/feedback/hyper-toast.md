# HyperToast

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="toast" title="HyperToast 交互预览" />

## 样式约定

strong 材质，宽 180..420dp、radius 18dp，语义色 10/255 合成底面、105/255 描边。图标徽章 30dp、图标 17dp；文字 14sp/500、行高 1.35。默认 2000ms。

## 状态归属与使用约束

visible 由调用方持有。durationMillis=0 不自动关闭；计时结束只请求关闭。tone 支持 Neutral/Primary/Success/Info/Warning/Error。Android 原生 hyperToast 与 Compose 组件分别使用。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperToast(visible, "保存成功", { visible = false }, tone = HyperToastTone.Success)
```

## HyperToast API / Props

```kotlin
@Composable
fun HyperToast(
    visible: Boolean,
    message: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    durationMillis: Long = 2000L,
    tone: HyperToastTone = HyperToastTone.Neutral,
    leadingContent: (@Composable () -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 调用方持有的可见状态。 |
| message | String | 是 | — | 提示消息文本。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭，由调用方修改状态。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| durationMillis | Long | 否 | 2000L | 自动关闭等待时间；0 表示不自动关闭。 |
| tone | HyperToastTone | 否 | HyperToastTone.Neutral | 提示的语义色类型。 |
| leadingContent | (@Composable () -&gt; Unit)? | 否 | null | 前导插槽。 |
| actionContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 操作区插槽。 |

## hyperToast API / Props

```kotlin
fun hyperToast(
    context: Context,
    message: CharSequence,
    duration: HyperToastDuration = HyperToastDuration.Short
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| context | Context | 是 | — | 可覆盖的 context 配置；默认表达式见此表。 |
| message | CharSequence | 是 | — | 提示消息文本。 |
| duration | HyperToastDuration | 否 | HyperToastDuration.Short | 可覆盖的 duration 配置；默认表达式见此表。 |

## hyperToast API / Props

```kotlin
fun hyperToast(
    context: Context,
    messageResource: Int,
    duration: HyperToastDuration = HyperToastDuration.Short
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| context | Context | 是 | — | 可覆盖的 context 配置；默认表达式见此表。 |
| messageResource | Int | 是 | — | 可覆盖的 messageResource 配置；默认表达式见此表。 |
| duration | HyperToastDuration | 否 | HyperToastDuration.Short | 可覆盖的 duration 配置；默认表达式见此表。 |
