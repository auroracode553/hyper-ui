# HyperSegmented

- 分类：表单组件
- 包名：`hyper_ui`
- 状态模型：受控组件；选中项由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/selection/HyperSegmented.kt`
- Preview ID：`segmented`

`HyperSegmented` 是等宽分段控制器。外层使用不透明浅色容器，选中项使用实色面板、轻描边和阴影形成参考图中的抬升效果；状态即时切换，不执行动画。

## 公开 API

```kotlin
data class HyperSegmentedColors(
    val containerColor: Color,
    val selectedItemColor: Color,
    val unselectedItemColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledItemColor: Color,
    val disabledContentColor: Color,
    val selectedBorderColor: Color
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
    val MinHeight = 40.dp
    val SelectedElevation = 2.dp
    val SelectedBorderWidth = 1.dp
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
        disabledContentColor: Color = Color.Unspecified,
        selectedBorderColor: Color = Color.Unspecified
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
| `colors` | `HyperSegmentedColors` | 否 | `HyperSegmentedDefaults.colors()` | 容器、项目、内容和选中描边颜色。 |
| `containerPadding` | `PaddingValues` | 否 | `ContainerPadding` | 外层容器到各分段的内部留白。 |
| `itemContentPadding` | `PaddingValues` | 否 | `ItemContentPadding` | 每个分段内部 slot 的留白。 |
| `itemContent` | `@Composable HyperSegmentedItemScope.(T) -> Unit` | 是 | 无 | 分段内容；作用域提供 `selected`、`enabled`。 |

## 颜色属性

| 属性 | 默认来源 | 作用 |
| --- | --- | --- |
| `containerColor` | `HyperColors.fieldContainer` | 外层不透明浅灰容器。 |
| `selectedItemColor` | `HyperColors.cardContainer` | 选中分段实色背景。 |
| `unselectedItemColor` | `Color.Transparent` | 未选中分段透出外层容器。 |
| `selectedContentColor` | `HyperColors.primaryText` | 选中内容色。 |
| `unselectedContentColor` | `HyperColors.secondaryText` | 未选中内容色。 |
| `disabledItemColor` | `HyperColors.disabledContainer` | 禁用分段背景。 |
| `disabledContentColor` | `HyperColors.disabledText` | 禁用内容色。 |
| `selectedBorderColor` | `HyperColors.fieldBorder` | 选中分段的 1dp 描边。 |

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
- 组件不绘制遮罩，也不执行颜色、阴影或位移动画。

<WasmPreview demo="segmented" title="HyperSegmented 交互预览" />
