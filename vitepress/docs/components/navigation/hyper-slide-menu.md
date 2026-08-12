# HyperSlideMenu

- 分类：导航组件
- 包名：`hyper_ui`
- 状态模型：受控组件；选中项由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/menu/HyperSlideMenu.kt`
- Preview ID：`slide-menu`

`HyperSlideMenu` 是可横向滚动的同级菜单，适合分类、筛选和标签较多的视图切换。单项也可通过 `HyperSlideMenuItem` 独立使用。

## 公开 API

```kotlin
data class HyperSlideMenuColors(
    val selectedContainerColor: Color,
    val unselectedContainerColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

class HyperSlideMenuItemScope {
    val selected: Boolean
    val enabled: Boolean
}

@Composable
fun HyperSlideMenuItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperSlideMenuDefaults.ItemContentPadding),
    enabled: Boolean = true,
    shape: Shape = HyperSlideMenuDefaults.Shape,
    colors: HyperSlideMenuColors = HyperSlideMenuDefaults.colors(),
    role: Role = Role.Tab,
    content: @Composable HyperSlideMenuItemScope.() -> Unit
)

@Composable
fun <T> HyperSlideMenu(
    items: List<T>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperSlideMenuDefaults.ItemGap),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperSlideMenuItemScope.(item: T) -> Unit
)
```

## 默认值

```kotlin
object HyperSlideMenuDefaults {
    val MinHeight = 32.dp
    val ItemContentGap = 6.dp
    val ItemGap = 8.dp
    val ItemContentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
    val ItemBorderWidth = 1.dp
    val Shape: Shape = RoundedCornerShape(percent = 50)

    @Composable
    fun colors(
        selectedContainerColor: Color = Color.Unspecified,
        unselectedContainerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperSlideMenuColors
}
```

## HyperSlideMenu 参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `items` | `List<T>` | 是 | 无 | 按顺序显示的菜单值。 |
| `selectedItem` | `T` | 是 | 无 | 调用方持有的选中值，使用 `equals` 比较。 |
| `onSelected` | `(T) -> Unit` | 是 | 无 | 点击可用项目后的回调。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 横向懒列表外壳；组件会填满可用宽度。 |
| `horizontalArrangement` | `Arrangement.Horizontal` | 否 | `spacedBy(ItemGap)` | 项目间距与横向排列。 |
| `itemEnabled` | `(T) -> Boolean` | 否 | `{ true }` | 每项的可用状态。 |
| `itemContent` | `@Composable HyperSlideMenuItemScope.(T) -> Unit` | 是 | 无 | 项目内容，作用域提供 `selected`、`enabled`。 |

## HyperSlideMenuItem 参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `selected` | `Boolean` | 是 | 无 | 当前项目的选中态。 |
| `onClick` | `() -> Unit` | 是 | 无 | 点击回调。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 单项外壳和尺寸。 |
| `contentModifier` | `Modifier` | 否 | `Modifier.padding(ItemContentPadding)` | 单项内部 slot 留白。 |
| `enabled` | `Boolean` | 否 | `true` | 可用态。 |
| `shape` | `Shape` | 否 | `HyperSlideMenuDefaults.Shape` | 单项形状。 |
| `colors` | `HyperSlideMenuColors` | 否 | `HyperSlideMenuDefaults.colors()` | 六种选中、未选中和禁用颜色。 |
| `role` | `Role` | 否 | `Role.Tab` | 点击语义角色。 |
| `content` | `@Composable HyperSlideMenuItemScope.() -> Unit` | 是 | 无 | 文字、计数或图标内容。 |

## 颜色默认来源

| 属性 | 默认来源 |
| --- | --- |
| `selectedContainerColor` | `HyperColors.accent` |
| `unselectedContainerColor` | `HyperColors.elevatedContainer` |
| `selectedContentColor` | `rgba(255, 255, 255, 1f)` |
| `unselectedContentColor` | `HyperColors.primaryText` |
| `disabledContainerColor` | `HyperColors.disabledContainer` |
| `disabledContentColor` | `HyperColors.disabledText` |

## 最小用法

```kotlin
HyperSlideMenu(
    items = categories,
    selectedItem = selected,
    onSelected = { selected = it }
) { item ->
    Text(item)
}
```

## 约束

- 组件不内置 `label`、`count`、`icon` 或业务分类模型。
- 泛型入口内部使用 `HyperSlideMenuItem` 的默认形状与颜色；需要逐项定制外壳时直接组合独立 `HyperSlideMenuItem`。
- 未选中项使用 1dp `HyperColors.fieldBorder` 描边，选中项不绘制该描边。
- 选中状态即时更新，不执行滚动定位或选中动画。

<WasmPreview demo="slide-menu" title="HyperSlideMenu 交互预览" />
