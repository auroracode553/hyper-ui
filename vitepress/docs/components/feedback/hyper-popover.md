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

HyperPopover(visible, { visible = false }, child = {
    HyperButton({ visible = !visible }) { HyperText("编辑") }
}) { HyperTextField(value, { value = it }) }
```

</details>

## 使用约定

**样式约定**

触发器附近的内容浮层。默认内容宽 180dp，maxWidth = 280.dp 只限制上限，短操作菜单不会默认撑到 280dp。复杂内容可传 width = 240.dp 等显式宽度；最终宽度为 min(width ?: 180dp, maxWidth, 窗口安全区可用宽度)。高不超过窗口 40%，strong 材质 radius 18dp、padding 14dp；锚点间距 6dp、安全边距 12dp。

Compose 与 Flutter 共用补充说明、文件夹操作和 240dp 较宽内容预览；文件夹操作展示“删除文件夹 / 详情 / 重命名”，选择后收起并显示结果。

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
    onClose: () -> Unit,
    child: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    maxWidth: Dp = 280.dp,
    width: Dp? = null,
    content: @Composable ColumnScope.() -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 是否显示组件，由调用方持有。 |
| onClose | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| child | @Composable () -&gt; Unit | 是 | — | 组件主体或触发器内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| maxWidth | Dp | 否 | 280.dp | 浮层宽度上限，不决定默认内容宽度。 |
| width | Dp? | 否 | null | 期望内容宽度；省略时采用 180dp，显式值仍受 maxWidth 和窗口安全区约束。 |
| content | @Composable ColumnScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |
