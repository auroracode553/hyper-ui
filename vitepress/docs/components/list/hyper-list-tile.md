---
description: 组织图标、标题、说明、元信息和尾部操作，可独立显示或置于分组表面内。
---

# HyperListTile

组织图标、标题、说明、元信息和尾部操作，可独立显示或置于分组表面内。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="list-tile" title="HyperListTile 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperListTile(title = { HyperText("系统设置") }, subtitle = { HyperText("查看配置") }, onPressed = onSettings)
```

</details>

## 使用约定

**样式约定**

type auto/plain/glass。plain 行最小 46dp，padding 14×8dp、radius 12dp；独立 glass 使用 radius 18dp、外 padding 3dp。标题 14sp/600，副标题 12sp、间距 3dp，meta 11sp、间距 4dp。leadingIcon 34dp 块、radius 11dp、icon 18dp；leading 与正文间距 12dp，正文与 trailing 间距 8dp。

**状态归属**

auto 在 Card/MenuGroup/List 等表面内部使用 plain。selected/ enabled 与业务结果由调用方持有。

预览直接展示列表项及其状态，不额外套 HyperCard。弹窗中的只读详情可用 HyperListTile(type = "plain") 的 title/subtitle 插槽展示标签和值，沿用库默认排版与留白，不绘制独立卡片，也不再额外包装容器。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperListTile

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperListTile(
    title: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperListTileDefaults.ContentPadding),
    dividerModifier: Modifier = Modifier.padding(horizontal = 14.dp),
    type: String = "auto",
    selected: Boolean = false,
    enabled: Boolean = true,
    showChevron: Boolean = true,
    dividerVisible: Boolean = false,
    colors: HyperListTileColors = HyperListTileDefaults.colors(),
    onPressed: (() -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    leadingColor: Color = HyperColors.accent,
    leading: (@Composable RowScope.() -> Unit)? = null,
    subtitle: (@Composable ColumnScope.() -> Unit)? = null,
    meta: (@Composable ColumnScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| title | @Composable ColumnScope.() -&gt; Unit | 是 | — | 组件标题文字或内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperListTileDefaults.ContentPadding) | 内部内容区域的布局修饰符。 |
| dividerModifier | Modifier | 否 | Modifier.padding(horizontal = 14.dp) | 分隔线的布局修饰符。 |
| type | String | 否 | &quot;auto&quot; | 组件的视觉或布局形态。 |
| selected | Boolean | 否 | false | 由调用方持有的选中状态。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| showChevron | Boolean | 否 | true | 存在点击回调且没有自定义尾部时，是否显示右箭头。 |
| dividerVisible | Boolean | 否 | false | 是否显示行分隔线。 |
| colors | HyperListTileColors | 否 | HyperListTileDefaults.colors() | 组件各状态的颜色配置。 |
| onPressed | (() -&gt; Unit)? | 否 | null | 点击操作回调；为 null 时不能触发操作。 |
| leadingIcon | ImageVector? | 否 | null | 内置图标块使用的矢量图标；自定义 leading 优先。 |
| leadingColor | Color | 否 | HyperColors.accent | 内置图标及其背景的强调颜色。 |
| leading | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| subtitle | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 副标题或辅助说明。 |
| meta | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 附加信息。 |
| trailing | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 内容末端的自定义区域。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperListTileDefaults.minHeight**

```kotlin
fun minHeight(hasSupportingContent: Boolean): Dp
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| hasSupportingContent | Boolean | 是 | — | 是否包含说明，用于计算行高。 |

**HyperListTileDefaults.colors**

```kotlin
@Composable
    fun colors(
        contentColor: Color = Color.Unspecified,
        supportingColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    )
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| contentColor | Color | 否 | Color.Unspecified | 主要文字与图标颜色。 |
| supportingColor | Color | 否 | Color.Unspecified | 说明文字颜色。 |
| disabledContentColor | Color | 否 | Color.Unspecified | 禁用时的文字与图标颜色。 |
| dividerColor | Color | 否 | Color.Unspecified | 分隔线颜色。 |

**HyperListTileColors 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| contentColor | Color | 是 | — | 主要文字与图标颜色。 |
| supportingColor | Color | 是 | — | 说明文字颜色。 |
| disabledContentColor | Color | 是 | — | 禁用时的文字与图标颜色。 |
| dividerColor | Color | 是 | — | 分隔线颜色。 |

</details>

## 共用内容表面

本组件的正文表面复用内部 hyperCardSurface/ContentCard，与 HyperCard 共用无普通描边、画布色阶和双层柔阴影。圆角、间距及已有交互不变；选中态保留完整强调边界，组内行不重复装饰，分隔线继续保留。具体参数见[设计系统基线](../../style-reference.md#内容卡片与导航维护规则)。浮层、操作区和输入控件不使用此内容卡片规则。
