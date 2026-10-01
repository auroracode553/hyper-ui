# HyperSegmentedControl

包名：`hyper_ui`。

<WasmPreview demo="segmented" title="HyperSegmentedControl 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun <T> HyperSegmentedControl(
    items: List<T>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemEnabled: (T) -> Boolean = { true },
    equalWidth: Boolean = true,
    shape: Shape = HyperSegmentedControlDefaults.Shape,
    itemShape: Shape = HyperSegmentedControlDefaults.ItemShape,
    colors: HyperSegmentedControlColors = HyperSegmentedControlDefaults.colors(),
    containerPadding: PaddingValues = HyperSegmentedControlDefaults.ContainerPadding,
    itemContentPadding: PaddingValues = HyperSegmentedControlDefaults.ItemContentPadding,
    itemContent: @Composable HyperSegmentOptionScope.(item: T) -> Unit
)
```

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| items | List&lt;T&gt; | 是 | — | 由调用方提供的选项或列表数据。 |
| selectedItem | T | 是 | — | 当前选中的数据项，由调用方持有。 |
| onSelected | (T) -&gt; Unit | 是 | — | 选中项变化时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| itemEnabled | (T) -&gt; Boolean | 否 | { true } | 判断单个选项是否可操作。 |
| equalWidth | Boolean | 否 | true | 各分段是否等宽。 |
| shape | Shape | 否 | HyperSegmentedControlDefaults.Shape | 组件容器的形状。 |
| itemShape | Shape | 否 | HyperSegmentedControlDefaults.ItemShape | 单个选项的形状。 |
| colors | HyperSegmentedControlColors | 否 | HyperSegmentedControlDefaults.colors() | 组件各状态的颜色配置。 |
| containerPadding | PaddingValues | 否 | HyperSegmentedControlDefaults.ContainerPadding | 容器内部留白。 |
| itemContentPadding | PaddingValues | 否 | HyperSegmentedControlDefaults.ItemContentPadding | 单个选项的内部留白。 |
| itemContent | @Composable HyperSegmentOptionScope.(item: T) -&gt; Unit | 是 | — | 每个数据项的自定义内容。 |

## 最小用法

```kotlin
val periods = listOf("Daily", "Weekly", "Monthly", "Yearly")
var selectedPeriod by remember { mutableStateOf("Yearly") }

HyperSegmentedControl(
    items = periods,
    selectedItem = selectedPeriod,
    onSelected = { selectedPeriod = it }
) { period ->
    Text(period)
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
