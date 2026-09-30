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
