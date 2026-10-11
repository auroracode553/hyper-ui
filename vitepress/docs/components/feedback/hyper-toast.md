---
description: 展示短暂操作反馈，支持语义色调、图标和附加操作。
---

# HyperToast

展示短暂操作反馈，支持语义色调、图标和附加操作。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="toast" title="HyperToast 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperToast(visible, "保存成功", { visible = false }, tone = HyperToastTone.Success)
```

</details>

## 使用约定

**样式约定**

strong 材质，宽 180..420dp、radius 18dp，语义色 10/255 合成底面、105/255 描边。图标徽章 30dp、图标 17dp；文字 14sp/500、行高 1.35。默认 2000ms。

**状态归属**

visible 由调用方持有。durationMillis=0 不自动关闭；计时结束只请求关闭。tone 支持 Neutral/Primary/Success/Info/Warning/Error。Android 原生 hyperToast 与 Compose 组件分别使用。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperToast

<details class="component-doc__details">
<summary>查看公开签名</summary>

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

</details>

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

### hyperToast · 重载 1

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
fun hyperToast(
    context: Context,
    message: CharSequence,
    duration: HyperToastDuration = HyperToastDuration.Short
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| context | Context | 是 | — | 用于显示 Android 原生 Toast 的上下文。 |
| message | CharSequence | 是 | — | 提示消息文本。 |
| duration | HyperToastDuration | 否 | HyperToastDuration.Short | Android 原生 Toast 的显示时长。 |

### hyperToast · 重载 2

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
fun hyperToast(
    context: Context,
    messageResource: Int,
    duration: HyperToastDuration = HyperToastDuration.Short
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| context | Context | 是 | — | 用于显示 Android 原生 Toast 的上下文。 |
| messageResource | Int | 是 | — | Android 字符串资源 ID。 |
| duration | HyperToastDuration | 否 | HyperToastDuration.Short | Android 原生 Toast 的显示时长。 |
