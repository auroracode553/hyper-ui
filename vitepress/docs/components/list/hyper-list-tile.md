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

HyperListTile(headlineContent = { HyperText("系统设置") }, supportingContent = { HyperText("查看配置") }, onClick = onSettings)
```

</details>

## 使用约定

**样式约定**

type auto/plain/glass。plain 行最小 46dp，padding 14×8dp、radius 12dp；独立 glass 使用 radius 18dp、外 padding 4dp。标题 14sp/600，副标题 12sp、间距 3dp，meta 11sp、间距 4dp。leadingIcon 34dp 块、radius 11dp、icon 18dp。

**状态归属**

auto 在 Card/MenuGroup/List 等表面内部使用 plain。selected/ enabled 与业务结果由调用方持有。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperListTile

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperListTile(
    headlineContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperListTileDefaults.ContentPadding),
    dividerModifier: Modifier = Modifier.padding(horizontal = 14.dp),
    type: String = "auto",
    selected: Boolean = false,
    enabled: Boolean = true,
    showChevron: Boolean = true,
    dividerVisible: Boolean = false,
    colors: HyperListTileColors = HyperListTileDefaults.colors(),
    onClick: (() -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    leadingColor: Color = HyperColors.accent,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null,
    metaContent: (@Composable ColumnScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| headlineContent | @Composable ColumnScope.() -&gt; Unit | 是 | — | 主标题插槽。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperListTileDefaults.ContentPadding) | 内容区修饰符；显式传入时覆盖默认内容留白。 |
| dividerModifier | Modifier | 否 | Modifier.padding(horizontal = 14.dp) | 分隔线的布局修饰符。 |
| type | String | 否 | "auto" | 渲染形态；合法值见本页样式约定。 |
| selected | Boolean | 否 | false | 调用方持有的选中状态。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| showChevron | Boolean | 否 | true | 存在点击回调且没有自定义尾部时，是否显示右箭头。 |
| dividerVisible | Boolean | 否 | false | 是否显示行分隔线。 |
| colors | HyperListTileColors | 否 | HyperListTileDefaults.colors() | 各状态配色配置。 |
| onClick | (() -&gt; Unit)? | 否 | null | 用户点击回调。 |
| leadingIcon | ImageVector? | 否 | null | 内置图标块使用的矢量图标；自定义 leadingContent 优先。 |
| leadingColor | Color | 否 | HyperColors.accent | 内置图标及其背景的强调颜色。 |
| leadingContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 前导插槽。 |
| supportingContent | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 辅助说明插槽。 |
| metaContent | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 说明下方的辅助元信息插槽。 |
| trailingContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 尾部插槽。 |

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
