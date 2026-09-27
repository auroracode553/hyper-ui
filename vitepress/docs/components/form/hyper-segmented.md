# HyperSegmented

`HyperSegmented` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

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

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- Android 原生窗口和系统工具仅在 Android 目标可用；Preview 使用跨平台示例。
