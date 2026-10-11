# HyperTooltip

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="tooltip" title="HyperTooltip 交互预览" />

## 样式约定

strong 玻璃 radius 12dp、文字 12sp/1.3、padding 10×7dp。默认最大宽 240dp，锚点间距 8dp，悬停与手机长按等待 500ms，焦点立即展示。

## 状态归属与使用约束

仅维护临时提示显隐；不截获内容的点击回调。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperTooltip("提示") { HyperButton(onSave) { HyperText("保存") } }
```

## HyperTooltip API / Props

```kotlin
@Composable
fun HyperTooltip(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    hoverDelayMillis: Long = 500L,
    maxWidth: Dp = 240.dp,
    content: @Composable () -> Unit,
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| text | String | 是 | — | 提示文字。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| hoverDelayMillis | Long | 否 | 500L | 悬停等待时长；手机长按固定 500ms。 |
| maxWidth | Dp | 否 | 240.dp | 浮层最大宽度。 |
| content | @Composable () -&gt; Unit | 是 | — | 主体内容插槽。 |
