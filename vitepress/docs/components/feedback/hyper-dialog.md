---
description: 模态内容容器，提供标题、正文、操作区和受控关闭。
---

# HyperDialog

模态内容容器，提供标题、正文、操作区和受控关闭。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="hyper_dialog" title="HyperDialog 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperDialog(visible, { visible = false }, title = "确认") { HyperText("正文") }
```

</details>

## 使用约定

**样式约定**

最大宽 420dp、窗口外间距 24dp、radius 26dp、strong 材质。默认 padding 18dp，标题 20sp/600，正文间隔 10dp，操作区距离 18dp、按钮间隔 8dp；250ms 淡入与 0.96 → 1 缩放。

**状态归属**

visible 由调用方持有，滚动正文与固定 actions 分开。系统返回和外部点击按配置请求关闭。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperDialog

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperDialog(
    visible: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    maxWidth: androidx.compose.ui.unit.Dp = HyperDialogDefaults.MaxWidth,
    title: String? = null,
    shape: Shape = HyperDialogDefaults.Shape,
    colors: HyperDialogColors = HyperDialogDefaults.colors(),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperDialogDefaults.ContentSpacing),
    actionArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperDialogDefaults.ActionSpacing,
        Alignment.End
    ),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    showScrollIndicator: Boolean = HyperDialogDefaults.ShowScrollIndicator,
    actions: (@Composable RowScope.() -> Unit)? = null,
    border: BorderStroke? = HyperDialogDefaults.border(),
    contentPadding: androidx.compose.foundation.layout.PaddingValues = HyperDialogDefaults.ContentPadding,
    content: @Composable ColumnScope.() -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 是否显示组件，由调用方持有。 |
| onClose | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| maxWidth | androidx.compose.ui.unit.Dp | 否 | HyperDialogDefaults.MaxWidth | 弹层或提示的最大宽度。 |
| title | String? | 否 | null | 组件标题文字或内容。 |
| shape | Shape | 否 | HyperDialogDefaults.Shape | 组件容器的形状。 |
| colors | HyperDialogColors | 否 | HyperDialogDefaults.colors() | 组件各状态的颜色配置。 |
| horizontalAlignment | Alignment.Horizontal | 否 | Alignment.Start | 容器内内容的水平对齐方式。 |
| verticalArrangement | Arrangement.Vertical | 否 | Arrangement.spacedBy(HyperDialogDefaults.ContentSpacing) | 子项的垂直排列方式。 |
| actionArrangement | Arrangement.Horizontal | 否 | Arrangement.spacedBy( HyperDialogDefaults.ActionSpacing, Alignment.End ) | 操作区的排列方式。 |
| dismissOnBackPress | Boolean | 否 | true | 按返回键时是否请求关闭。 |
| dismissOnClickOutside | Boolean | 否 | true | 点击外部时是否请求关闭。 |
| showScrollIndicator | Boolean | 否 | HyperDialogDefaults.ShowScrollIndicator | 内容滚动时是否显示滚动提示。 |
| actions | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 导航栏操作项内容。 |
| border | BorderStroke? | 否 | HyperDialogDefaults.border() | 容器边框配置。 |
| contentPadding | androidx.compose.foundation.layout.PaddingValues | 否 | HyperDialogDefaults.ContentPadding | 主体内容的内部留白。 |
| content | @Composable ColumnScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperDialogDefaults.colors**

```kotlin
@Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperDialogColors
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 否 | Color.Unspecified | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |

**HyperDialogDefaults.border**

```kotlin
@Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| color | Color | 否 | Color.Unspecified | 自定义颜色，覆盖默认配色。 |

**HyperDialogColors 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 是 | — | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |

</details>
