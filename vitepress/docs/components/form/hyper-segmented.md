# HyperSegmented

- 分类：表单组件
- 包名：`hyper_ui`
- 状态模型：受控组件；选中项由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/selection/HyperSegmented.kt`
- Preview ID：`segmented`

`HyperSegmented` 是等宽分段控制器。组件只负责低抬升轨道、等宽布局、选中状态与 Tab 语义；每个分段直接复用 `HyperButton`，不再维护独立按钮绘制实现。

## 公开 API

```kotlin
data class HyperSegmentedColors(
    val containerColor: Color,
    val selectedItemColor: Color,
    val unselectedItemColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledItemColor: Color,
    val disabledContentColor: Color
)

class HyperSegmentedItemScope {
    val selected: Boolean
    val enabled: Boolean
}

@Composable
fun <T> HyperSegmented(
    items: List<T>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemEnabled: (T) -> Boolean = { true },
    shape: Shape = HyperSegmentedDefaults.Shape,
    itemShape: Shape = HyperSegmentedDefaults.ItemShape,
    colors: HyperSegmentedColors = HyperSegmentedDefaults.colors(),
    containerPadding: PaddingValues = HyperSegmentedDefaults.ContainerPadding,
    itemContentPadding: PaddingValues = HyperSegmentedDefaults.ItemContentPadding,
    itemContent: @Composable HyperSegmentedItemScope.(item: T) -> Unit
)
```

## 默认值

```kotlin
object HyperSegmentedDefaults {
    val ContainerElevation = 1.dp
    val ContainerPadding = PaddingValues(3.dp)
    val ItemContentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    val Shape: Shape = RoundedCornerShape(5.dp)
    val ItemShape: Shape = RoundedCornerShape(4.dp)

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        selectedItemColor: Color = Color.Unspecified,
        unselectedItemColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledItemColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperSegmentedColors
}
```

## 参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `items` | `List<T>` | 是 | 无 | 按列表顺序渲染的分段；各项按 `equals` 比较。 |
| `selectedItem` | `T` | 是 | 无 | 调用方持有的当前选中项。 |
| `onSelected` | `(T) -> Unit` | 是 | 无 | 点击可用分段后的回调。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 控制整个分段控制器的宽高和外部间距。 |
| `enabled` | `Boolean` | 否 | `true` | 全局可用状态。 |
| `itemEnabled` | `(T) -> Boolean` | 否 | `{ true }` | 单项可用状态；最终状态为 `enabled && itemEnabled(item)`。 |
| `shape` | `Shape` | 否 | `HyperSegmentedDefaults.Shape` | 外层容器形状。 |
| `itemShape` | `Shape` | 否 | `HyperSegmentedDefaults.ItemShape` | 每个分段的形状。 |
| `colors` | `HyperSegmentedColors` | 否 | `HyperSegmentedDefaults.colors()` | 轨道、项目与内容颜色。 |
| `containerPadding` | `PaddingValues` | 否 | `ContainerPadding` | 外层容器到各分段的内部留白。 |
| `itemContentPadding` | `PaddingValues` | 否 | `ItemContentPadding` | 每个分段内部 slot 的留白。 |
| `itemContent` | `@Composable HyperSegmentedItemScope.(T) -> Unit` | 是 | 无 | 分段内容；作用域提供 `selected`、`enabled`。 |

## 颜色属性

| 属性 | 默认来源 | 作用 |
| --- | --- | --- |
| `containerColor` | 浅色白色 `0.48f` / 深色白色 `0.14f` | 外层半透明玻璃轨道。 |
| `selectedItemColor` | `HyperColors.accent` | 选中分段的 `HyperButton` 容器色。 |
| `unselectedItemColor` | `HyperColors.cardContainer` | 未选中分段的 `HyperButton` 容器色。 |
| `selectedContentColor` | 白色不透明 | 选中按钮内容色。 |
| `unselectedContentColor` | `HyperColors.primaryText` | 未选中按钮内容色。 |
| `disabledItemColor` | `HyperColors.softContainer` | 禁用按钮背景。 |
| `disabledContentColor` | `HyperColors.secondaryText` | 禁用按钮内容色。 |

## 最小用法

```kotlin
val periods = listOf("Daily", "Weekly", "Monthly", "Yearly")
var selectedPeriod by remember { mutableStateOf("Yearly") }

HyperSegmented(
    items = periods,
    selectedItem = selectedPeriod,
    onSelected = { selectedPeriod = it }
) { period ->
    Text(period)
}
```

## 约束

- 组件不持有选择状态；`onSelected` 不会自动改变 `selectedItem`。
- 各项等分当前可用宽度，不提供横向滚动；项目很多时应改用 `HyperSlideMenu`。
- `items` 应使用按相等性可唯一识别的值；重复值会同时呈现选中语义。
- `itemContent` 负责文字、图标和业务标签，组件不内置字符串模型。
- 每项由 `HyperButton` 提供最小高度、实色表面、禁用态和点击行为；`HyperSegmented` 传入 `Role.Tab` 并补充 `selected` 语义。
- 分段按钮不绘制边框，也不执行颜色、阴影或位移动画。

<WasmPreview demo="segmented" title="HyperSegmented 交互预览" />
