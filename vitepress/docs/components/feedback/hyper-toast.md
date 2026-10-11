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

HyperToast(visible, "保存成功", { visible = false }, type = "success")
```

</details>

## 使用约定

**样式约定**

strong 材质，宽 180..420dp、radius 18dp，语义色 10/255 合成底面、105/255 描边。图标徽章 30dp、图标 17dp；文字 14sp/500、行高 1.35。默认 2000ms。

**状态归属**

visible 由调用方持有。duration=0 不自动关闭；计时结束只请求关闭。type 支持 neutral/primary/success/info/warning/error。Android 原生 hyperToast 与 Compose 组件分别使用。

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
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    duration: Long = 2000L,
    type: String = "neutral",
    color: Color = Color.Unspecified,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    action: (@Composable RowScope.() -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 是否显示组件，由调用方持有。 |
| message | String | 是 | — | 提示消息文本。 |
| onClose | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| duration | Long | 否 | 2000L | 自动关闭等待时长，单位毫秒；0 不自动关闭。 |
| type | String | 否 | &quot;neutral&quot; | 组件的视觉或布局形态。 |
| color | Color | 否 | Color.Unspecified | 自定义颜色，覆盖默认配色。 |
| actionLabel | String? | 否 | null | 操作按钮文案。 |
| onAction | (() -&gt; Unit)? | 否 | null | 点击提示操作时执行的回调。 |
| leading | (@Composable () -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| action | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 空状态操作内容。 |

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
| context | Context | 是 | — | 用于读取系统电量状态的 Android Context。 |
| message | CharSequence | 是 | — | 提示消息文本。 |
| duration | HyperToastDuration | 否 | HyperToastDuration.Short | 自动关闭等待时长，单位毫秒；0 不自动关闭。 |

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
| context | Context | 是 | — | 用于读取系统电量状态的 Android Context。 |
| messageResource | Int | 是 | — | Android 字符串资源 ID。 |
| duration | HyperToastDuration | 否 | HyperToastDuration.Short | 自动关闭等待时长，单位毫秒；0 不自动关闭。 |
