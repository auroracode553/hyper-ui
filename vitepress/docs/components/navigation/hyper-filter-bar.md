# HyperFilterBar

`HyperFilterBar` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。 此组件是横向分类条；列表侧滑操作请使用 `HyperSlideMenu`。

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

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- 公开 API 仅支持 Android 手机端；Preview 只是文档交互工具。
