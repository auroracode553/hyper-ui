# HyperPopover

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="custom_popup" title="HyperPopover 交互预览" />

## 样式约定

触发器附近的内容浮层。默认最大宽 280dp、高不超过窗口 40%，strong 材质 radius 18dp、padding 14dp；锚点间距 6dp、安全边距 12dp。

## 状态归属与使用约束

visible 和正文状态由调用方持有；长内容滚动。标题和操作区由调用方在 content 内组合。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperPopover(visible, { visible = false }, anchorContent = {
    HyperButton({ visible = !visible }) { HyperText("编辑") }
}) { HyperTextField(value, { value = it }) }
```

## HyperPopover API / Props

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

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 调用方持有的可见状态。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭，由调用方修改状态。 |
| anchorContent | @Composable () -&gt; Unit | 是 | — | 触发器插槽；用于定位浮层。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| maxWidth | Dp | 否 | 280.dp | 浮层最大宽度。 |
| content | @Composable ColumnScope.() -&gt; Unit | 是 | — | 主体内容插槽。 |
