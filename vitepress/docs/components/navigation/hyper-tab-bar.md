# HyperTabBar

包名：`hyper_ui`。

## 贴底预览

<WasmPreview demo="tab-bar-docked" title="HyperTabBar 贴底预览" />

## 悬浮胶囊预览

<WasmPreview demo="tab-bar-floating" title="HyperTabBar 悬浮胶囊预览" />

两个预览均可点击标签切换选中项；悬浮胶囊还可按住并拖动标签，查看指示托盘的跟手与吸附效果。
贴底预览的底栏背景延伸至手机底边，标签与手势条之间只保留少量间距；悬浮胶囊预览保持底部间距。示例不再绘制内层手机框。

## 公开签名与默认值

```kotlin
@Composable
fun HyperTabBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    shape: Shape = HyperTabBarDefaults.Shape,
    topDivider: BorderStroke? = HyperTabBarDefaults.topDivider(),
    colors: HyperTabBarColors = HyperTabBarDefaults.colors(),
    type: String = "docked",
    floatingColors: HyperFloatingTabBarColors = HyperFloatingTabBarDefaults.colors(),
    content: @Composable RowScope.() -> Unit
)

@Composable
fun <T> HyperTabBar(
    items: List<T>,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemLayout: HyperTabBarItemLayout = HyperTabBarItemLayout.Equal,
    itemSelected: (T) -> Boolean = { false },
    itemSlotAlignment: Alignment = Alignment.Center,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    shape: Shape = HyperTabBarDefaults.Shape,
    topDivider: BorderStroke? = HyperTabBarDefaults.topDivider(),
    colors: HyperTabBarColors = HyperTabBarDefaults.colors(),
    type: String = "docked",
    floatingColors: HyperFloatingTabBarColors = HyperFloatingTabBarDefaults.colors(),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperTabBarItemScope.(item: T) -> Unit
)
```

## Props（参数）

### HyperTabBar（内容 Slot 形式）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| horizontalArrangement | Arrangement.Horizontal | 否 | Arrangement.SpaceBetween | 子项的水平排列方式。 |
| verticalAlignment | Alignment.Vertical | 否 | Alignment.CenterVertically | 子项的垂直对齐方式。 |
| shape | Shape | 否 | HyperTabBarDefaults.Shape | 组件容器的形状。 |
| topDivider | BorderStroke? | 否 | HyperTabBarDefaults.topDivider() | 是否显示顶部边界线。 |
| colors | HyperTabBarColors | 否 | HyperTabBarDefaults.colors() | 组件各状态的颜色配置。 |
| type | String | 否 | &quot;docked&quot; | 组件的视觉或布局形态。 |
| floatingColors | HyperFloatingTabBarColors | 否 | HyperFloatingTabBarDefaults.colors() | 悬浮样式的颜色配置。 |
| content | @Composable RowScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

### HyperTabBar（数据项形式）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| items | List&lt;T&gt; | 是 | — | 由调用方提供的选项或列表数据。 |
| onItemClick | (T) -&gt; Unit | 是 | — | 标签栏单项点击时的回调。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| itemLayout | HyperTabBarItemLayout | 否 | HyperTabBarItemLayout.Equal | 标签栏单项的布局配置。 |
| itemSelected | (T) -&gt; Boolean | 否 | { false } | 判断标签栏单项是否选中。 |
| itemSlotAlignment | Alignment | 否 | Alignment.Center | 标签栏内容 Slot 的对齐方式。 |
| horizontalArrangement | Arrangement.Horizontal | 否 | Arrangement.SpaceBetween | 子项的水平排列方式。 |
| shape | Shape | 否 | HyperTabBarDefaults.Shape | 组件容器的形状。 |
| topDivider | BorderStroke? | 否 | HyperTabBarDefaults.topDivider() | 是否显示顶部边界线。 |
| colors | HyperTabBarColors | 否 | HyperTabBarDefaults.colors() | 组件各状态的颜色配置。 |
| type | String | 否 | &quot;docked&quot; | 组件的视觉或布局形态。 |
| floatingColors | HyperFloatingTabBarColors | 否 | HyperFloatingTabBarDefaults.colors() | 悬浮样式的颜色配置。 |
| itemEnabled | (T) -&gt; Boolean | 否 | { true } | 判断单个选项是否可操作。 |
| itemContent | @Composable HyperTabBarItemScope.(item: T) -&gt; Unit | 是 | — | 每个数据项的自定义内容。 |


## 最小用法

```kotlin
// 贴底样式（默认）
HyperTabBar(
    items = tabs,
    itemSelected = { it.id == selectedTabId },
    onItemClick = { selectedTabId = it.id }
) { item ->
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(item.icon, contentDescription = item.label)
        Text(item.label)
    }
}

// 悬浮玻璃胶囊样式
HyperTabBar(
    items = tabs,
    type = "floating",
    itemSelected = { it.id == selectedTabId },
    onItemClick = { selectedTabId = it.id }
) { item ->
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(item.icon, contentDescription = item.label, modifier = Modifier.size(20.dp))
        Text(item.label, fontSize = 11.sp)
    }
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
