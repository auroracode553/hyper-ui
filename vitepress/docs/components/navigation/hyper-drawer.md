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

HyperDrawer(open, { open = false }, title = "导航", child = {
    HyperDrawerItem(title = { HyperText("首页") }, onPressed = onHome)
}) { pageContent() }
```

</details>

## 使用约定

**样式约定**

默认 end 浮动抽屉，常规左右宽 320dp、外侧间距 12dp、radius 30dp、strong 材质、scrim 蒙层。280ms easeOutCubic 淡入与 12% 位移。标题 18sp，正文 padding 16dp，页脚固定。

**状态归属**

调用方持有 open。懒列表关闭 scrollable，避免嵌套滚动；start/end 为逻辑方向，随 RTL 映射。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperDrawer

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperDrawer(
    open: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    drawerModifier: Modifier = Modifier,
    placement: HyperDrawerPlacement = HyperDrawerPlacement.end,
    defaultSetPadding: Boolean = true,
    drawerContentModifier: Modifier = Modifier,
    scrollable: Boolean = true,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    dismissOnClickOutside: Boolean = true,
    title: String? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
    child: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| open | Boolean | 是 | — | 抽屉是否打开，由调用方持有。 |
| onClose | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| drawerModifier | Modifier | 否 | Modifier | 抽屉面板的布局修饰符。 |
| placement | HyperDrawerPlacement | 否 | HyperDrawerPlacement.end | 从逻辑起始侧或结束侧打开抽屉。 |
| defaultSetPadding | Boolean | 否 | true | 是否使用抽屉默认留白。 |
| drawerContentModifier | Modifier | 否 | Modifier | 抽屉内容区域的布局修饰符。 |
| scrollable | Boolean | 否 | true | 是否允许内容滚动。 |
| colors | HyperDrawerColors | 否 | HyperDrawerDefaults.colors() | 组件各状态的颜色配置。 |
| dismissOnClickOutside | Boolean | 否 | true | 点击外部时是否请求关闭。 |
| title | String? | 否 | null | 组件标题文字或内容。 |
| footer | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 底部内容。 |
| child | @Composable ColumnScope.() -&gt; Unit | 是 | — | 组件主体或触发器内容。 |
| content | @Composable BoxScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

### HyperDrawerHeader

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperDrawerHeader(
    title: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperDrawerDefaults.HeaderPadding),
    leading: (@Composable RowScope.() -> Unit)? = null,
    subtitle: (@Composable ColumnScope.() -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| title | @Composable ColumnScope.() -&gt; Unit | 是 | — | 组件标题文字或内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperDrawerDefaults.HeaderPadding) | 内部内容区域的布局修饰符。 |
| leading | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| subtitle | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 副标题或辅助说明。 |

### HyperDrawerItem

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperDrawerItem(
    title: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperDrawerDefaults.ItemPadding),
    selected: Boolean = false,
    enabled: Boolean = true,
    dividerVisible: Boolean = false,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    onPressed: (() -> Unit)? = null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    subtitle: (@Composable ColumnScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| title | @Composable ColumnScope.() -&gt; Unit | 是 | — | 组件标题文字或内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperDrawerDefaults.ItemPadding) | 内部内容区域的布局修饰符。 |
| selected | Boolean | 否 | false | 由调用方持有的选中状态。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| dividerVisible | Boolean | 否 | false | 是否显示行分隔线。 |
| colors | HyperDrawerColors | 否 | HyperDrawerDefaults.colors() | 组件各状态的颜色配置。 |
| onPressed | (() -&gt; Unit)? | 否 | null | 点击操作回调；为 null 时不能触发操作。 |
| leading | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| subtitle | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 副标题或辅助说明。 |
| trailing | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 内容末端的自定义区域。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperDrawerDefaults.contentPadding**

```kotlin
fun contentPadding(placement: HyperDrawerPlacement): PaddingValues
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| placement | HyperDrawerPlacement | 是 | — | 从逻辑起始侧或结束侧打开抽屉。 |

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
