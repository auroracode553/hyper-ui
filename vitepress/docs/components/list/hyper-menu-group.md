---
description: 设置页式分组菜单，适合少量入口、开关和选择项。
---

# HyperMenuGroup

设置页式分组菜单，适合少量入口、开关和选择项。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="hyper_menu_list" title="HyperMenuGroup 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperMenuGroup(items = settings) { item ->
    HyperListTile(title = { HyperText(item.label) }, onPressed = { onOpen(item) })
}
```

</details>

## 使用约定

**样式约定**

regular 玻璃 radius 18dp，padding 4dp。泛型 items 入口自动在项之间绘制两侧缩进 14dp 的分隔线。

菜单组按内容测量高度；页面滚动由调用方容器承担，较多数据使用 HyperList。

**状态归属**

用于少量菜单。items 入口自动分隔；自由 ColumnScope 内容入口可显式用 HyperDivider 或 ListTile.dividerVisible 组合分隔。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperMenuGroup · 重载 1

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun <T> HyperMenuGroup(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperMenuGroupDefaults.ContentPadding),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    colors: HyperMenuGroupColors = HyperMenuGroupDefaults.colors(),
    title: String? = null,
    subtitle: String? = null,
    itemContent: @Composable (item: T) -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| items | List&lt;T&gt; | 是 | — | 由调用方提供的选项或列表数据。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperMenuGroupDefaults.ContentPadding) | 内部内容区域的布局修饰符。 |
| verticalArrangement | Arrangement.Vertical | 否 | Arrangement.spacedBy(0.dp) | 子项的垂直排列方式。 |
| colors | HyperMenuGroupColors | 否 | HyperMenuGroupDefaults.colors() | 组件各状态的颜色配置。 |
| title | String? | 否 | null | 组件标题文字或内容。 |
| subtitle | String? | 否 | null | 副标题或辅助说明。 |
| itemContent | @Composable (item: T) -&gt; Unit | 是 | — | 每个数据项的自定义内容。 |

### HyperMenuGroup · 重载 2

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperMenuGroup(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperMenuGroupDefaults.ContentPadding),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    colors: HyperMenuGroupColors = HyperMenuGroupDefaults.colors(),
    title: String? = null,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperMenuGroupDefaults.ContentPadding) | 内部内容区域的布局修饰符。 |
| verticalArrangement | Arrangement.Vertical | 否 | Arrangement.spacedBy(0.dp) | 子项的垂直排列方式。 |
| colors | HyperMenuGroupColors | 否 | HyperMenuGroupDefaults.colors() | 组件各状态的颜色配置。 |
| title | String? | 否 | null | 组件标题文字或内容。 |
| subtitle | String? | 否 | null | 副标题或辅助说明。 |
| content | @Composable ColumnScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperMenuGroupDefaults.colors**

```kotlin
@Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperMenuGroupColors
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 否 | Color.Unspecified | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |

**HyperMenuGroupColors 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 是 | — | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |

</details>

## 共用内容表面

本组件的正文表面复用内部 hyperCardSurface/ContentCard，与 HyperCard 共用无普通描边、画布色阶和双层柔阴影。圆角、间距及已有交互不变；选中态保留完整强调边界，组内行不重复装饰，分隔线继续保留。具体参数见[设计系统基线](../../style-reference.md#内容卡片与导航维护规则)。浮层、操作区和输入控件不使用此内容卡片规则。
