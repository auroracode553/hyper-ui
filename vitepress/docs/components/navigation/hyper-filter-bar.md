# HyperFilterBar

包名：`hyper_ui`。

<WasmPreview demo="filter_bar" title="HyperFilterBar 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun <T> HyperFilterBar(
    items: List<T>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperFilterBarDefaults.ItemGap),
    itemEnabled: (T) -> Boolean = { true },
    selectedType: String = "filled",
    unselectedType: String = "tonal",
    selectedColors: HyperButtonColors = HyperButtonDefaults.colors(selectedType),
    unselectedColors: HyperButtonColors = HyperButtonDefaults.colors(unselectedType),
    selectedBorder: BorderStroke? = HyperButtonDefaults.border(selectedType),
    unselectedBorder: BorderStroke? = HyperButtonDefaults.border(unselectedType),
    itemShape: Shape = HyperButtonDefaults.Shape,
    itemContentPadding: PaddingValues = HyperButtonDefaults.ContentPadding,
    itemContent: @Composable HyperFilterBarItemScope.(item: T) -> Unit
)
```

## 最小用法

```kotlin
HyperFilterBar(items, selectedItem, onSelected = { selectedItem = it }) { item -> HyperText(item.label) }
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
