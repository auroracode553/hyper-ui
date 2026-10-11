---
description: 组织标题、说明、操作、正文和页脚，支持选中状态。
---

# HyperCard

组织标题、说明、操作、正文和页脚，支持选中状态。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="panel" title="HyperCard 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperCard(title = { HyperText("标题") }, subtitle = { HyperText("说明") }) { HyperText("正文") }
```

</details>

## 使用约定

**样式约定**

卡片 regular 材质、24dp 圆角、12dp 内容间距。标题 14sp/600、副标题 13sp，头部与正文、页脚间距 12dp；selected 使用 strong 与强调色轮廓。

**状态归属**

selected、onPressed 与业务数据由调用方维护；卡片内 ListTile 自动使用 plain。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperCard

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperCard(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperCardDefaults.ContentPadding),
    selected: Boolean = false,
    onPressed: (() -> Unit)? = null,
    colors: HyperCardColors = HyperCardDefaults.colors(selected = selected),
    shape: Shape = HyperCardDefaults.Shape,
    elevation: Dp = HyperCardDefaults.Elevation,
    border: BorderStroke? = HyperCardDefaults.border(selected = selected),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperCardDefaults.ContentSpacing),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    title: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
    child: (@Composable ColumnScope.() -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperCardDefaults.ContentPadding) | 内部内容区域的布局修饰符。 |
| selected | Boolean | 否 | false | 由调用方持有的选中状态。 |
| onPressed | (() -&gt; Unit)? | 否 | null | 点击操作回调；为 null 时不能触发操作。 |
| colors | HyperCardColors | 否 | HyperCardDefaults.colors(selected = selected) | 组件各状态的颜色配置。 |
| shape | Shape | 否 | HyperCardDefaults.Shape | 组件容器的形状。 |
| elevation | Dp | 否 | HyperCardDefaults.Elevation | 容器阴影高度。 |
| border | BorderStroke? | 否 | HyperCardDefaults.border(selected = selected) | 容器边框配置。 |
| verticalArrangement | Arrangement.Vertical | 否 | Arrangement.spacedBy(HyperCardDefaults.ContentSpacing) | 子项的垂直排列方式。 |
| horizontalAlignment | Alignment.Horizontal | 否 | Alignment.Start | 容器内内容的水平对齐方式。 |
| title | (@Composable () -&gt; Unit)? | 否 | null | 组件标题文字或内容。 |
| subtitle | (@Composable () -&gt; Unit)? | 否 | null | 副标题或辅助说明。 |
| leading | (@Composable () -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| actions | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 导航栏操作项内容。 |
| footer | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 底部内容。 |
| child | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 组件主体或触发器内容。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperCardDefaults.colors**

```kotlin
@Composable
    fun colors(containerColor: Color = Color.Unspecified, selected: Boolean = false): HyperCardColors
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 否 | Color.Unspecified | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| selected | Boolean | 否 | false | 由调用方持有的选中状态。 |

**HyperCardDefaults.border**

```kotlin
@Composable
    fun border(color: Color = Color.Unspecified, selected: Boolean = false): BorderStroke =
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| color | Color | 否 | Color.Unspecified | 自定义颜色，覆盖默认配色。 |
| selected | Boolean | 否 | false | 由调用方持有的选中状态。 |

**HyperCardColors 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 是 | — | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |

</details>
