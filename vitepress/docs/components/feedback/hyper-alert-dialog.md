---
description: 用于确认、删除等需要明确结果的操作，提供标题、说明和操作插槽。
---

# HyperAlertDialog

安全区与键盘避让由共用 Dialog 宿主管理，遮罩覆盖整屏；业务不重复包顶部安全区。长消息在受约束的内容区域内滚动，文档预览同样传递模拟窗口 Insets。

用于确认、删除等需要明确结果的操作，提供标题、说明和操作插槽。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="dialog" title="HyperAlertDialog 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperAlertDialog(visible, { visible = false }, title = "确认删除", content = { HyperText("删除后无法恢复") },
    actions = { HyperButton(onDelete, colorStyle = "danger", type = "filled") { HyperText("删除") } })
```

</details>

## 使用约定

**样式约定**

复用 [HyperDialog 的高遮蔽玻璃面板](hyper-dialog.md#使用约定)，圆角 26dp、最大宽 420dp，采用 250ms 淡入与 0.96 → 1 缩放。标题、可滚动正文和固定操作区通过明确插槽组成。

正文默认继承主题主文字色；取消操作省略 type 使用默认描边按钮，危险操作使用 danger。

**状态归属**

调用方持有 visible，确认业务通过 actions 回调处理。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperAlertDialog

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperAlertDialog(
    visible: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    dismissOnClickOutside: Boolean = true,
    content: (@Composable ColumnScope.() -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 是否显示组件，由调用方持有。 |
| onClose | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| title | String? | 否 | null | 组件标题文字或内容。 |
| dismissOnClickOutside | Boolean | 否 | true | 点击外部时是否请求关闭。 |
| content | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 组件主体内容，由调用方提供。 |
| actions | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 导航栏操作项内容。 |
