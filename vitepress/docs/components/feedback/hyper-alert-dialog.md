---
description: 用于确认、删除等需要明确结果的操作，提供标题、说明和操作插槽。
---

# HyperAlertDialog

用于确认、删除等需要明确结果的操作，提供标题、说明和操作插槽。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="dialog" title="HyperAlertDialog 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperAlertDialog(visible, { visible = false }, title = "确认删除", bodyContent = { HyperText("删除后无法恢复") },
    actionContent = { HyperButton(onDelete, type = "danger") { HyperText("删除") } })
```

</details>

## 使用约定

**样式约定**

复用 HyperDialog 的 26dp 圆角、420dp 最大宽与 250ms 淡入与 0.96 → 1 缩放。标题、可滚动正文、固定操作区通过明确插槽组成。

正文默认继承主题主文字色；取消操作可使用 ghost 按钮，危险操作使用 danger。

**状态归属**

调用方持有 visible，确认业务通过 actionContent 回调处理。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperAlertDialog

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperAlertDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    dismissOnClickOutside: Boolean = true,
    bodyContent: (@Composable ColumnScope.() -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 调用方持有的可见状态。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭，由调用方修改状态。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| title | String? | 否 | null | 标题；null 时省略。 |
| dismissOnClickOutside | Boolean | 否 | true | 点击外部时是否请求关闭。 |
| bodyContent | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 对话框主体内容。 |
| actionContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 操作区插槽。 |
