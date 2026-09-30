# HyperTabs

包名：`hyper_ui`。

<WasmPreview demo="tabs" title="HyperTabs 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun <T> HyperTabs(
    items: List<T>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperTabsDefaults.ItemGap),
    itemEnabled: (T) -> Boolean = { true },
    selectedType: String = "filled",
    unselectedType: String = "tonal",
    selectedColors: HyperButtonColors = HyperButtonDefaults.colors(selectedType),
    unselectedColors: HyperButtonColors = HyperButtonDefaults.colors(unselectedType),
    selectedBorder: BorderStroke? = HyperButtonDefaults.border(selectedType),
    unselectedBorder: BorderStroke? = HyperButtonDefaults.border(unselectedType),
    itemShape: Shape = HyperButtonDefaults.Shape,
    itemContentPadding: PaddingValues = HyperButtonDefaults.ContentPadding,
    itemContent: @Composable HyperTabsItemScope.(item: T) -> Unit
)
```

## 最小用法

```kotlin
HyperTabs(items, selectedItem, onSelected = { selectedItem = it }) { item -> HyperText(item.label) }
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
