# HyperTabBar

- 分类：导航组件
- 包名：`hyper_ui`
- 状态模型：页面选择和导航由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/navigation/HyperTabBar.kt`
- Preview ID：`tab-bar`

`HyperTabBar` 是默认总高 60dp 的底部标签栏，由 55dp 标签操作区和 5dp 轻量底部留白组成。深色模式下容器和默认描边直接采用当前 `MaterialTheme.colorScheme.background`，与页面保持同色；浅色模式保留轻量透明度。两种模式均不叠加玻璃高光或渐变。

## 公开 API

```kotlin
enum class HyperTabBarItemLayout { Equal, Packed }

data class HyperTabBarColors(
    val containerColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContentColor: Color
)

class HyperTabBarItemScope {
    val selected: Boolean
    val enabled: Boolean
}

@Composable
fun HyperTabBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    shape: Shape = HyperTabBarDefaults.Shape,
    border: BorderStroke? = HyperTabBarDefaults.border(),
    colors: HyperTabBarColors = HyperTabBarDefaults.colors(),
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
    border: BorderStroke? = HyperTabBarDefaults.border(),
    colors: HyperTabBarColors = HyperTabBarDefaults.colors(),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperTabBarItemScope.(item: T) -> Unit
)
```

## 默认值

```kotlin
object HyperTabBarDefaults {
    val Height = 55.dp
    val BottomPadding = 5.dp
    val ItemWidth = 60.dp
    val Shape: Shape = RoundedCornerShape(0.dp)
    val ItemTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.labelSmall

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperTabBarColors

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## Slot 入口参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `modifier` | `Modifier` | 否 | `Modifier` | 整个标签栏的宽高和外部间距。 |
| `enabled` | `Boolean` | 否 | `true` | 只控制 Slot 入口提供的默认内容色；slot 内点击逻辑仍由调用方控制。 |
| `horizontalArrangement` | `Arrangement.Horizontal` | 否 | `SpaceBetween` | Row 内容的横向排列。 |
| `verticalAlignment` | `Alignment.Vertical` | 否 | `CenterVertically` | Row 内容的垂直对齐。 |
| `shape` | `Shape` | 否 | `HyperTabBarDefaults.Shape` | 标签栏形状。 |
| `border` | `BorderStroke?` | 否 | `HyperTabBarDefaults.border()` | 描边；传 `null` 移除。 |
| `colors` | `HyperTabBarColors` | 否 | `HyperTabBarDefaults.colors()` | 容器与三种内容状态色。 |
| `content` | `@Composable RowScope.() -> Unit` | 是 | 无 | 完整自定义内容。 |

Slot 入口固定提供水平 16dp 内边距、5dp 底部留白和 `ItemTextStyle`。

## Items 入口附加参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `items` | `List<T>` | 是 | 无 | 按顺序渲染的标签项。 |
| `onItemClick` | `(T) -> Unit` | 是 | 无 | 点击可用标签后的回调。 |
| `itemLayout` | `HyperTabBarItemLayout` | 否 | `Equal` | `Equal` 等分宽度；`Packed` 使用最小项目宽度。 |
| `itemSelected` | `(T) -> Boolean` | 否 | `{ false }` | 选中判定。 |
| `itemSlotAlignment` | `Alignment` | 否 | `Alignment.Center` | 每项内部 slot 的对齐。 |
| `horizontalArrangement` | `Arrangement.Horizontal` | 否 | `SpaceBetween` | 仅 `Packed` 模式使用；`Equal` 内部固定从起点等分。 |
| `itemEnabled` | `(T) -> Boolean` | 否 | `{ true }` | 单项可用状态，最终与全局 `enabled` 合并。 |
| `itemContent` | `@Composable HyperTabBarItemScope.(T) -> Unit` | 是 | 无 | 标签内容，作用域提供 `selected`、`enabled`。 |

其余 `modifier`、`enabled`、`shape`、`border`、`colors` 与 Slot 入口相同。

## 最小用法

```kotlin
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
```

## 约束

- 不存在 `HyperTabBarItem`、`selectedItemId` 或 `HyperTabBarConfig`。
- 组件不依赖导航框架，也不会在 `onItemClick` 后自动切换页面。
- `Equal` 项目等分宽度；`Packed` 项目至少 60dp 宽，内容可继续撑宽。
- 默认容器在浅色模式使用白色 `0.92f` alpha；深色模式直接使用当前 `MaterialTheme.colorScheme.background`，不再使用会发灰的白色透明层。
- 深色默认描边同样使用页面背景色，因此不会出现灰色边界；显式传入 `border` 或 `containerColor` 时仍按调用方颜色绘制，并保留其 alpha。
- 容器只绘制普通颜色背景和可选描边，不叠加玻璃高光或渐变。
- `HyperTabBarDefaults.Height` 表示 55dp 标签操作区；默认底栏总高度为 60dp，包含 5dp 的 `BottomPadding`。
- 通常不应使用 `modifier.height(...)` 强制压缩底栏总高度，否则可能挤占内部操作区或底部留白。

<WasmPreview demo="tab-bar" title="HyperTabBar 交互预览" />
