---
description: 为操作提供简短说明，支持悬停、长按和键盘聚焦。
---

# HyperTooltip

为操作提供简短说明，支持悬停、长按和键盘聚焦。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="tooltip" title="HyperTooltip 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperTooltip("提示") { HyperButton(onSave) { HyperText("保存") } }
```

</details>

## 使用约定

**样式约定**

strong 玻璃 radius 12dp、文字 12sp/1.3、padding 10×7dp。默认最大宽 240dp，锚点间距 8dp，悬停与手机长按等待 500ms，焦点立即展示。

**状态归属**

仅维护临时提示显隐；不截获内容的点击回调。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperTooltip

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperTooltip(
    message: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    hoverDelay: Long = 500L,
    maxWidth: Dp = 240.dp,
    child: @Composable () -> Unit,
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| message | String | 是 | — | 提示消息文本。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| hoverDelay | Long | 否 | 500L | 显示悬浮提示前的等待时间，单位毫秒。 |
| maxWidth | Dp | 否 | 240.dp | 弹层或提示的最大宽度。 |
| child | @Composable () -&gt; Unit | 是 | — | 组件主体或触发器内容。 |
