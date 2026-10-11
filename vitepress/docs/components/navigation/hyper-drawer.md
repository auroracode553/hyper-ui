---
description: 从屏幕边缘展开辅助内容，支持标题、可滚动正文和固定操作区。
---

# HyperDrawer

从屏幕边缘展开辅助内容，支持标题、可滚动正文和固定操作区。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="drawer" title="HyperDrawer 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperDrawer(open, { open = false }, title = "导航", drawerContent = {
    HyperDrawerItem(headlineContent = { HyperText("首页") }, onClick = onHome)
}) { pageContent() }
```

</details>

## 使用约定

**样式约定**

默认 Right 浮动抽屉，常规左右宽 320dp、外侧间距 12dp、radius 30dp、strong 材质、scrim 蒙层。280ms easeOutCubic 淡入与 12% 位移。标题 18sp，正文 padding 16dp，页脚固定。

**状态归属**

调用方持有 open。懒列表关闭 drawerContentScrollEnabled，避免嵌套滚动；Left/Right 为逻辑方向，随 RTL 映射。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperDrawer

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperDrawer(
    open: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    drawerModifier: Modifier = Modifier,
    position: HyperDrawerPosition = HyperDrawerPosition.Right,
    defaultSetPadding: Boolean = true,
    drawerContentModifier: Modifier = Modifier,
    drawerContentScrollEnabled: Boolean = true,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    dismissOnClickOutside: Boolean = true,
    title: String? = null,
    footerContent: (@Composable ColumnScope.() -> Unit)? = null,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| open | Boolean | 是 | — | 抽屉是否打开，由调用方持有。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭，由调用方修改状态。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| drawerModifier | Modifier | 否 | Modifier | 抽屉面板的布局修饰符。 |
| position | HyperDrawerPosition | 否 | HyperDrawerPosition.Right | 抽屉从哪一侧进入。 |
| defaultSetPadding | Boolean | 否 | true | 是否使用抽屉默认留白。 |
| drawerContentModifier | Modifier | 否 | Modifier | 抽屉内容区域的布局修饰符。 |
| drawerContentScrollEnabled | Boolean | 否 | true | 抽屉内容是否允许滚动。 |
| colors | HyperDrawerColors | 否 | HyperDrawerDefaults.colors() | 各状态配色配置。 |
| dismissOnClickOutside | Boolean | 否 | true | 点击外部时是否请求关闭。 |
| title | String? | 否 | null | 标题；null 时省略。 |
| footerContent | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 固定页脚插槽。 |
| drawerContent | @Composable ColumnScope.() -&gt; Unit | 是 | — | 抽屉中的内容。 |
| content | @Composable BoxScope.() -&gt; Unit | 是 | — | 主体内容插槽。 |

### HyperDrawerHeader

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperDrawerHeader(
    headlineContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperDrawerDefaults.HeaderPadding),
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| headlineContent | @Composable ColumnScope.() -&gt; Unit | 是 | — | 主标题插槽。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperDrawerDefaults.HeaderPadding) | 内容区修饰符；显式传入时覆盖默认内容留白。 |
| leadingContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 前导插槽。 |
| supportingContent | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 辅助说明插槽。 |

### HyperDrawerItem

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperDrawerItem(
    headlineContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperDrawerDefaults.ItemPadding),
    selected: Boolean = false,
    enabled: Boolean = true,
    dividerVisible: Boolean = false,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| headlineContent | @Composable ColumnScope.() -&gt; Unit | 是 | — | 主标题插槽。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperDrawerDefaults.ItemPadding) | 内容区修饰符；显式传入时覆盖默认内容留白。 |
| selected | Boolean | 否 | false | 调用方持有的选中状态。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| dividerVisible | Boolean | 否 | false | 是否显示行分隔线。 |
| colors | HyperDrawerColors | 否 | HyperDrawerDefaults.colors() | 各状态配色配置。 |
| onClick | (() -&gt; Unit)? | 否 | null | 用户点击回调。 |
| leadingContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 前导插槽。 |
| supportingContent | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 辅助说明插槽。 |
| trailingContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 尾部插槽。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperDrawerDefaults.contentPadding**

```kotlin
fun contentPadding(position: HyperDrawerPosition): PaddingValues
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| position | HyperDrawerPosition | 是 | — | 抽屉从哪一侧进入。 |

**HyperDrawerDefaults.colors**

```kotlin
@Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        supportingColor: Color = Color.Unspecified,
        selectedContainerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperDrawerColors
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 否 | Color.Unspecified | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| contentColor | Color | 否 | Color.Unspecified | 主要文字与图标颜色。 |
| supportingColor | Color | 否 | Color.Unspecified | 说明文字颜色。 |
| selectedContainerColor | Color | 否 | Color.Unspecified | 选中条目的底色。 |
| selectedContentColor | Color | 否 | Color.Unspecified | 选中条目的文字与图标颜色。 |
| disabledContentColor | Color | 否 | Color.Unspecified | 禁用时的文字与图标颜色。 |
| dividerColor | Color | 否 | Color.Unspecified | 分隔线颜色。 |

**HyperDrawerColors 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 是 | — | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| contentColor | Color | 是 | — | 主要文字与图标颜色。 |
| supportingColor | Color | 是 | — | 说明文字颜色。 |
| selectedContainerColor | Color | 是 | — | 选中条目的底色。 |
| selectedContentColor | Color | 是 | — | 选中条目的文字与图标颜色。 |
| disabledContentColor | Color | 是 | — | 禁用时的文字与图标颜色。 |
| dividerColor | Color | 是 | — | 分隔线颜色。 |

</details>
