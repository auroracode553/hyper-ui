# HyperSectionedList

包名：`hyper_ui`。

<WasmPreview demo="hyper_sectioned_list" title="HyperSectionedList 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun <S, T> HyperSectionedList(
    sections: List<S>,
    items: (S) -> List<T>,
    sectionKey: (S) -> Any,
    itemKey: (S, T) -> Any,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = HyperSectionedListDefaults.ContentPadding,
    firstSectionTopSpacing: Dp = HyperSectionedListDefaults.FirstSectionTopSpacing,
    sectionSpacing: Dp = HyperSectionedListDefaults.SectionSpacing,
    headerBottomSpacing: Dp = HyperSectionedListDefaults.HeaderBottomSpacing,
    dividerModifier: Modifier = Modifier.padding(
        start = HyperSectionedListDefaults.DividerInset
    ),
    colors: HyperSectionedListColors = HyperSectionedListDefaults.colors(),
    headerContent: @Composable (section: S) -> Unit,
    itemContent: @Composable (section: S, item: T) -> Unit
)
```

## 最小用法

```kotlin
import androidx.compose.foundation.layout.PaddingValues
import hyper_ui.HyperText
import androidx.compose.ui.unit.dp
import hyper_ui.*

HyperSectionedList(
    sections = historyGroups,
    items = { group -> group.histories },
    sectionKey = { group -> "history-section-${group.date}" },
    itemKey = { _, history -> "history-item-${history.id}" },
    contentPadding = PaddingValues(horizontal = 10.dp),
    headerContent = { group -> Text(group.title) }
) { _, history ->
    HyperListTile(
        headlineContent = { Text(history.title) },
        supportingContent = { Text(history.url) },
        onClick = { openHistory(history) }
    )
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
