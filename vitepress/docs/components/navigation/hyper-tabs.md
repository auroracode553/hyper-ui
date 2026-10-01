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

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| items | List&lt;T&gt; | 是 | — | 由调用方提供的选项或列表数据。 |
| selectedItem | T | 是 | — | 当前选中的数据项，由调用方持有。 |
| onSelected | (T) -&gt; Unit | 是 | — | 选中项变化时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| horizontalArrangement | Arrangement.Horizontal | 否 | Arrangement.spacedBy(HyperTabsDefaults.ItemGap) | 子项的水平排列方式。 |
| itemEnabled | (T) -&gt; Boolean | 否 | { true } | 判断单个选项是否可操作。 |
| selectedType | String | 否 | &quot;filled&quot; | 选中项使用的按钮形态。 |
| unselectedType | String | 否 | &quot;tonal&quot; | 未选中项使用的按钮形态。 |
| selectedColors | HyperButtonColors | 否 | HyperButtonDefaults.colors(selectedType) | 选中项的颜色配置。 |
| unselectedColors | HyperButtonColors | 否 | HyperButtonDefaults.colors(unselectedType) | 未选中项的颜色配置。 |
| selectedBorder | BorderStroke? | 否 | HyperButtonDefaults.border(selectedType) | 选中项的边框配置。 |
| unselectedBorder | BorderStroke? | 否 | HyperButtonDefaults.border(unselectedType) | 未选中项的边框配置。 |
| itemShape | Shape | 否 | HyperButtonDefaults.Shape | 单个选项的形状。 |
| itemContentPadding | PaddingValues | 否 | HyperButtonDefaults.ContentPadding | 单个选项的内部留白。 |
| itemContent | @Composable HyperTabsItemScope.(item: T) -&gt; Unit | 是 | — | 每个数据项的自定义内容。 |


## 最小用法

```kotlin
HyperTabs(items, selectedItem, onSelected = { selectedItem = it }) { item -> HyperText(item.label) }
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
