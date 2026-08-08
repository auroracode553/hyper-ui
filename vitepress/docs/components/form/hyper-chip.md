# HyperChip

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/selection/HyperChip.kt`
- 预览：`chip`

`HyperChip` 是 slot-first 标签容器，适合只读标签、可点击标签、筛选项和状态标签。`HyperChipRow` 只负责横向滚动布局与选中判断，chip 内内容由调用方渲染。

## 公开签名

```kotlin
@Composable
fun HyperChip(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = HyperChipDefaults.Shape,
    minHeight: Dp = HyperChipDefaults.MinHeight,
    contentPadding: PaddingValues = HyperChipDefaults.ContentPadding,
    colors: HyperChipColors = HyperChipDefaults.colors(),
    role: Role = Role.Tab,
    content: @Composable HyperChipScope.() -> Unit
)

@Composable
fun <T> HyperChipRow(
    items: List<T>,
    selectedItem: T?,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = HyperChipDefaults.RowPadding,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperChipDefaults.RowGap),
    chipContent: @Composable HyperChipScope.(item: T) -> Unit
)
```

## 最小用法

```kotlin
HyperChipRow(
    items = categories,
    selectedItem = selected,
    onSelected = { selected = it }
) { item ->
    Text(item)
}
```

## 约束

- 不存在 `label`、`count`、`HyperFilterChipItem` 数据模型。
- 需要计数、图标或复杂内容时直接放入 `content`。
- `HyperChipScope` 暴露 `selected` 和 `enabled`，便于 slot 根据状态渲染。

<WasmPreview demo="chip" title="HyperChip 交互预览" />
