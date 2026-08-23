# HyperSlideMenu

- 分类：导航组件
- 包名：`hyper_ui`
- 状态模型：受控组件；选中项由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/menu/HyperSlideMenu.kt`
- Preview ID：`slide-menu`

`HyperSlideMenu` 是可横向滚动的同级菜单，适合分类、筛选和标签较多的视图切换。每个项目直接复用 `HyperButton`，因此尺寸、形状、内容间距、中性细描边、`Outline` 强调色描边、按压反馈和禁用态都与按钮保持一致。

## 公开 API

```kotlin
class HyperSlideMenuItemScope {
    val selected: Boolean
    val enabled: Boolean
}

@Composable
fun <T> HyperSlideMenu(
    items: List<T>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperSlideMenuDefaults.ItemGap),
    itemEnabled: (T) -> Boolean = { true },
    selectedTone: HyperButtonTone = HyperButtonTone.Primary,
    unselectedTone: HyperButtonTone = HyperButtonTone.Secondary,
    selectedColors: HyperButtonColors = HyperButtonDefaults.colors(selectedTone),
    unselectedColors: HyperButtonColors = HyperButtonDefaults.colors(unselectedTone),
    selectedBorder: BorderStroke? = HyperButtonDefaults.border(selectedTone),
    unselectedBorder: BorderStroke? = HyperButtonDefaults.border(unselectedTone),
    itemShape: Shape = HyperButtonDefaults.Shape,
    itemContentPadding: PaddingValues = HyperButtonDefaults.ContentPadding,
    itemContent: @Composable HyperSlideMenuItemScope.(item: T) -> Unit
)
```

## 默认值

```kotlin
object HyperSlideMenuDefaults {
    val ItemGap = 8.dp
}
```

单项外观的默认值全部来自 `HyperButtonDefaults`：

| 状态 | 默认按钮配置 |
| --- | --- |
| 选中 | `HyperButtonTone.Primary`、对应颜色与描边 |
| 未选中 | `HyperButtonTone.Secondary`、对应颜色与描边 |
| 形状 | `HyperButtonDefaults.Shape` |
| 内容间距 | `HyperButtonDefaults.ContentPadding` |
| 最小高度、按压和禁用反馈 | 由 `HyperButton` 内部统一处理 |

## 参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `items` | `List<T>` | 是 | 无 | 按顺序显示的菜单值。 |
| `selectedItem` | `T` | 是 | 无 | 调用方持有的选中值，使用 `equals` 比较。 |
| `onSelected` | `(T) -> Unit` | 是 | 无 | 点击可用项目后的回调。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 横向懒列表外壳；组件会填满可用宽度。 |
| `horizontalArrangement` | `Arrangement.Horizontal` | 否 | `spacedBy(ItemGap)` | 项目间距与横向排列。 |
| `itemEnabled` | `(T) -> Boolean` | 否 | `{ true }` | 每项的可用状态。 |
| `selectedTone` | `HyperButtonTone` | 否 | `Primary` | 选中项按钮语气；使用 `Outline` 可显示强调色描边。 |
| `unselectedTone` | `HyperButtonTone` | 否 | `Secondary` | 未选中项按钮语气。 |
| `selectedColors` | `HyperButtonColors` | 否 | `HyperButtonDefaults.colors(selectedTone)` | 选中项按钮颜色。 |
| `unselectedColors` | `HyperButtonColors` | 否 | `HyperButtonDefaults.colors(unselectedTone)` | 未选中项按钮颜色。 |
| `selectedBorder` | `BorderStroke?` | 否 | `HyperButtonDefaults.border(selectedTone)` | 选中项描边；`null` 使用按钮公共中性细描边。 |
| `unselectedBorder` | `BorderStroke?` | 否 | `HyperButtonDefaults.border(unselectedTone)` | 未选中项描边；`null` 使用按钮公共中性细描边。 |
| `itemShape` | `Shape` | 否 | `HyperButtonDefaults.Shape` | 所有菜单按钮的形状。 |
| `itemContentPadding` | `PaddingValues` | 否 | `HyperButtonDefaults.ContentPadding` | 所有菜单按钮的内容间距。 |
| `itemContent` | `@Composable HyperSlideMenuItemScope.(T) -> Unit` | 是 | 无 | 项目内容；作用域提供 `selected` 和 `enabled`。 |

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

## 描边选中态

```kotlin
HyperSlideMenu(
    items = categories,
    selectedItem = selected,
    onSelected = { selected = it },
    selectedTone = HyperButtonTone.Outline
) { item ->
    Text(item)
}
```

## 约束

- 组件不内置 `label`、`count`、`icon` 或业务分类模型。
- 菜单只管理横向排列、选中判断、禁用判断和 `Role.Tab` 语义；单项表面和交互统一由 `HyperButton` 提供。
- `selectedColors`、`unselectedColors`、描边、形状和内容间距直接使用按钮类型，不再维护菜单专属按钮样式。
- 选中状态即时更新，不执行滚动定位或选中动画。

<WasmPreview demo="slide-menu" title="HyperSlideMenu 交互预览" />
