---
description: 在触发点附近展示补充内容，自动处理边界和上下翻转。
---

# HyperPopover

在触发点附近展示补充内容，自动处理边界和上下翻转。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="custom_popup" title="HyperPopover 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperPopover(visible, { visible = false }, anchorContent = {
    HyperButton({ visible = !visible }) { HyperText("编辑") }
}) { HyperTextField(value, { value = it }) }
```

</details>

## 使用约定

**样式约定**

触发器附近的内容浮层。默认最大宽 280dp、高不超过窗口 40%，strong 材质 radius 18dp、padding 14dp；锚点间距 6dp、安全边距 12dp。

**状态归属**

visible 和正文状态由调用方持有；长内容滚动。标题和操作区由调用方在 content 内组合。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperPopover

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperPopover(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    anchorContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    maxWidth: Dp = 280.dp,
    content: @Composable ColumnScope.() -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 调用方持有的可见状态。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭，由调用方修改状态。 |
| anchorContent | @Composable () -&gt; Unit | 是 | — | 触发器插槽；用于定位浮层。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| maxWidth | Dp | 否 | 280.dp | 浮层最大宽度。 |
| content | @Composable ColumnScope.() -&gt; Unit | 是 | — | 主体内容插槽。 |
