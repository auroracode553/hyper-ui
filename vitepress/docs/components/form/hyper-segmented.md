# HyperSegmented

包名：`hyper_ui`。

<WasmPreview demo="segmented" title="HyperSegmented 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun <T> HyperSegmented(
    items: List<T>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemEnabled: (T) -> Boolean = { true },
    equalWidth: Boolean = true,
    shape: Shape = HyperSegmentedDefaults.Shape,
    itemShape: Shape = HyperSegmentedDefaults.ItemShape,
    colors: HyperSegmentedColors = HyperSegmentedDefaults.colors(),
    containerPadding: PaddingValues = HyperSegmentedDefaults.ContainerPadding,
    itemContentPadding: PaddingValues = HyperSegmentedDefaults.ItemContentPadding,
    itemContent: @Composable HyperSegmentedItemScope.(item: T) -> Unit
)
```

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

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
