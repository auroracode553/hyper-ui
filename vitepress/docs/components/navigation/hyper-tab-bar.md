# HyperTabBar

- 分类：导航组件
- 包名：`hyper_ui`
- 状态模型：页面选择和导航由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/navigation/HyperTabBar.kt`、`library/src/main/java/hyper_ui/components/navigation/HyperFloatingTabBar.kt`
- Preview ID：`tab-bar`

`HyperTabBar` 通过 `type` 参数在两种样式间切换：

- **Docked（贴底，默认）**：总高 60dp 的底部标签栏，由 55dp 标签操作区和 5dp 轻量底部留白组成。贴底容器默认只绘制 0.5dp 的低对比度顶部发丝线，不使用阴影或整框描边；深色模式下容器与发丝线采用当前 `HyperColors.pageBackground`，浅色模式保留轻量透明度。两种模式均不叠加玻璃高光或渐变。
- **Floating（悬浮玻璃胶囊）**：56dp 轻薄磨砂底座悬浮于页面（四周留白 20/8/20/12dp）。静止时选中项显示与标签格接近等宽的灰色托盘；按下立即展开为半透明水珠，拖动时按 1:1 跟手并对标签做轻微放大，边缘使用渐进阻力；松手后按释放速度投影并用弹簧吸附到最近标签，再收回为静态托盘。

<WasmPreview demo="tab-bar" title="HyperTabBar 交互预览" />

## 公开 API

```kotlin
enum class HyperTabBarType { Docked, Floating }

enum class HyperTabBarItemLayout { Equal, Packed }

data class HyperTabBarColors(
    val containerColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContentColor: Color
)

data class HyperFloatingTabBarColors(
    val containerColor: Color,
    val indicatorColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContentColor: Color
)

class HyperTabBarItemScope {
    val selected: Boolean
    val enabled: Boolean
    val selectionStrength: Float
}

@Composable
fun HyperTabBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    shape: Shape = HyperTabBarDefaults.Shape,
    topDivider: BorderStroke? = HyperTabBarDefaults.topDivider(),
    colors: HyperTabBarColors = HyperTabBarDefaults.colors(),
    type: HyperTabBarType = HyperTabBarType.Docked,
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
    type: HyperTabBarType = HyperTabBarType.Docked,
    floatingColors: HyperFloatingTabBarColors = HyperFloatingTabBarDefaults.colors(),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperTabBarItemScope.(item: T) -> Unit
)
```

## 默认值

```kotlin
object HyperTabBarDefaults {
    val Height = 55.dp
    val BottomPadding = 5.dp
    val TopDividerThickness = 0.5.dp
    val ItemWidth = 60.dp
    val Shape: Shape = RoundedCornerShape(0.dp)
    val ItemTextStyle: TextStyle
        @Composable get() = HyperTheme.typography.labelSmall

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperTabBarColors

    @Composable
    fun topDivider(color: Color = Color.Unspecified): BorderStroke
}
```

```kotlin
object HyperFloatingTabBarDefaults {
    val Height = 56.dp
    val Margin = PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 12.dp)
    val InnerPadding = 4.dp
    val IndicatorVerticalInset = 4.dp
    val Elevation = 5.dp
    val MaxPillWidth = 112.dp
    val PillGap = 2.dp
    val LensInMillis = 180
    val LensOutMillis = 230
    val DragSlop = 6.dp
    val ProjectionSeconds = 0.09f
    val LensWidthGrowth = 38.dp
    val LensHeightGrowth = 18.dp
    val MaxVelocityWidthGrowth = 14.dp
    val LensScaleGrowth = 0.15f
    val SelectionSpring: SpringSpec<Float>   // 外部选中状态变化时的吸附弹簧
    val ItemTextStyle: TextStyle
        @Composable get() = HyperTheme.typography.labelSmall

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        indicatorColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperFloatingTabBarColors
}
```

## Slot 入口参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `modifier` | `Modifier` | 否 | `Modifier` | 整个标签栏的宽高和外部间距。 |
| `enabled` | `Boolean` | 否 | `true` | 只控制入口提供的默认内容色；slot 内点击逻辑仍由调用方控制。 |
| `horizontalArrangement` | `Arrangement.Horizontal` | 否 | `SpaceBetween` | Row 内容的横向排列。 |
| `verticalAlignment` | `Alignment.Vertical` | 否 | `CenterVertically` | Row 内容的垂直对齐。 |
| `shape` | `Shape` | 否 | `HyperTabBarDefaults.Shape` | 仅 Docked 样式使用。 |
| `topDivider` | `BorderStroke?` | 否 | `HyperTabBarDefaults.topDivider()` | 仅 Docked 样式使用；只绘制顶边，传 `null` 移除。 |
| `colors` | `HyperTabBarColors` | 否 | `HyperTabBarDefaults.colors()` | 仅 Docked 样式使用。 |
| `type` | `HyperTabBarType` | 否 | `Docked` | `Docked` 贴底；`Floating` 悬浮玻璃胶囊容器。 |
| `floatingColors` | `HyperFloatingTabBarColors` | 否 | `HyperFloatingTabBarDefaults.colors()` | 仅 Floating 样式使用。 |
| `content` | `@Composable RowScope.() -> Unit` | 是 | 无 | 完整自定义内容。 |

Docked 入口固定提供水平 16dp 内边距、5dp 底部留白和 `ItemTextStyle`。Floating 入口固定提供 20/8/20/12dp 悬浮留白、4dp 胶囊内边距和 `ItemTextStyle`，不使用发丝线、整框描边或贴底留白。

## Items 入口附加参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `items` | `List<T>` | 是 | 无 | 按顺序渲染的标签项。 |
| `onItemClick` | `(T) -> Unit` | 是 | 无 | 点击可用标签后的回调。 |
| `itemLayout` | `HyperTabBarItemLayout` | 否 | `Equal` | 仅 Docked 样式使用：`Equal` 等分宽度；`Packed` 使用最小项目宽度。 |
| `itemSelected` | `(T) -> Boolean` | 否 | `{ false }` | 选中判定；Floating 下至少应有一项命中，否则指示胶囊隐藏。 |
| `itemSlotAlignment` | `Alignment` | 否 | `Alignment.Center` | 仅 Docked 样式使用。 |
| `horizontalArrangement` | `Arrangement.Horizontal` | 否 | `SpaceBetween` | 仅 Docked 样式使用：`Packed` 模式生效。 |
| `shape` | `Shape` | 否 | `HyperTabBarDefaults.Shape` | 仅 Docked 样式使用。 |
| `topDivider` | `BorderStroke?` | 否 | `HyperTabBarDefaults.topDivider()` | 仅 Docked 样式使用。 |
| `colors` | `HyperTabBarColors` | 否 | `HyperTabBarDefaults.colors()` | 仅 Docked 样式使用。 |
| `type` | `HyperTabBarType` | 否 | `Docked` | `Docked` 贴底；`Floating` 悬浮玻璃胶囊。 |
| `floatingColors` | `HyperFloatingTabBarColors` | 否 | `HyperFloatingTabBarDefaults.colors()` | 仅 Floating 样式使用。 |
| `itemEnabled` | `(T) -> Boolean` | 否 | `{ true }` | 单项可用状态，最终与全局 `enabled` 合并。 |
| `itemContent` | `@Composable HyperTabBarItemScope.(T) -> Unit` | 是 | 无 | 标签内容，作用域提供 `selected`、`enabled` 与 `selectionStrength`。 |

Floating 模式下项目固定等分宽度，忽略 `itemLayout`、`itemSlotAlignment`、`horizontalArrangement`、`shape`、`topDivider` 与 `colors`。其余参数与 Docked 相同。

## Floating 样式行为

- 选中托盘中心与内边距后的等分标签格中心一致，LTR 和 RTL 排列都能对齐；宽度取「标签格宽度 - 2dp」，上限为 `MaxPillWidth`(112dp)，窄屏下不超过格宽。默认高度 48dp，垂直居中。
- 按下时托盘立即吸附到触点标签并展开水珠；拖动位置直接跟随手指，边界采用渐进阻力。松手按 `ProjectionSeconds` 投影释放速度，再由 `SelectionSpring` 吸附到最近标签。
- 内容颜色按 `selectionStrength` 在 `unselectedContentColor` 与 `selectedContentColor` 间连续插值；`selectionStrength` 随指示胶囊位置在 0f~1f 间连续变化。
- 默认选中内容色使用当前 Material 主题的 `primary`，未选中内容保持中性灰；可通过 `floatingColors` 覆盖。
- 静态托盘底色使用独立 `indicatorColor`（浅色中性蓝灰 `0.82f` alpha / 深色白色 `0.14f` alpha）；拖动水珠使用同色低透明度扩张层，调用方可用 `floatingColors` 覆盖。
- 容器在两种主题下均以白色半透明底混合页面：浅色 `0.78f` alpha、深色 `0.30f` alpha，配合克制的顶部柔光与单层 5dp 阴影。
- 拖拽释放会继承归一化速度进行短距离投影，再交给弹簧吸附；普通点击仍由 `onItemClick` 处理。

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
    type = HyperTabBarType.Floating,
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

- 不存在 `HyperTabBarItem`、`selectedItemId` 或 `HyperTabBarConfig`。
- 组件不依赖导航框架，也不会在 `onItemClick` 后自动切换页面。
- Docked 样式：`Equal` 项目等分宽度；`Packed` 项目至少 60dp 宽，内容可继续撑宽。默认容器在浅色模式使用白色 `0.92f` alpha；深色模式直接使用当前 `HyperColors.pageBackground`。`topDivider` 只绘制顶边，不包围容器四周；浅色模式默认使用 0.5dp、黑色 `0.055f` alpha 的低对比度发丝线，深色模式使用页面背景色隐藏灰边。`Height` 表示 55dp 标签操作区，默认底栏总高度为 60dp。
- Floating 样式：需要至少 2 个标签项；项目等分宽度且不提供 Packed 布局。胶囊悬浮留白、高度与阴影已内置于组件，外部间距无需再通过 `modifier.padding(...)` 添加。指示胶囊仅在存在选中项或被按下时可见；无任何选中时隐藏。
- 通常不应使用 `modifier.height(...)` 强制压缩底栏总高度，否则可能挤占内部操作区或底部留白。
- Floating 样式随布局方向排列标签与托盘；slot 模式的选中视觉仍由调用方绘制。
