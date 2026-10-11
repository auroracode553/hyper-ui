# HyperMenuGroup

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="hyper_menu_list" title="HyperMenuGroup 交互预览" />

## 样式约定

regular 玻璃 radius 18dp，padding 4dp。泛型 items 入口自动在项之间绘制两侧缩进 14dp 的分隔线。

菜单组按内容测量高度；页面滚动由调用方容器承担，较多数据使用 HyperList。

## 状态归属与使用约束

用于少量菜单。items 入口自动分隔；自由 ColumnScope 内容入口可显式用 HyperDivider 或 ListTile.dividerVisible 组合分隔。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperMenuGroup(items = settings) { item ->
    HyperListTile(headlineContent = { HyperText(item.label) }, onClick = { onOpen(item) })
}
```

## HyperMenuGroup API / Props

```kotlin
@Composable
fun <T> HyperMenuGroup(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperMenuGroupDefaults.ContentPadding),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    colors: HyperMenuGroupColors = HyperMenuGroupDefaults.colors(),
    itemContent: @Composable (item: T) -> Unit
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| items | List&lt;T&gt; | 是 | — | 待呈现条目。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperMenuGroupDefaults.ContentPadding) | 内容区修饰符；显式传入时覆盖默认内容留白。 |
| verticalArrangement | Arrangement.Vertical | 否 | Arrangement.spacedBy(0.dp) | 子项的垂直排列方式。 |
| colors | HyperMenuGroupColors | 否 | HyperMenuGroupDefaults.colors() | 各状态配色配置。 |
| itemContent | @Composable (item: T) -&gt; Unit | 是 | — | 条目内容插槽。 |

## HyperMenuGroup API / Props

```kotlin
@Composable
fun HyperMenuGroup(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperMenuGroupDefaults.ContentPadding),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    colors: HyperMenuGroupColors = HyperMenuGroupDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperMenuGroupDefaults.ContentPadding) | 内容区修饰符；显式传入时覆盖默认内容留白。 |
| verticalArrangement | Arrangement.Vertical | 否 | Arrangement.spacedBy(0.dp) | 子项的垂直排列方式。 |
| colors | HyperMenuGroupColors | 否 | HyperMenuGroupDefaults.colors() | 各状态配色配置。 |
| content | @Composable ColumnScope.() -&gt; Unit | 是 | — | 主体内容插槽。 |

## HyperMenuGroupDefaults.colors API / Props

```kotlin
@Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperMenuGroupColors
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 否 | Color.Unspecified | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |

## HyperMenuGroupColors 配置

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 是 | — | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
