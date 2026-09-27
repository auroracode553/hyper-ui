# HyperSectionedList

`HyperSectionedList` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

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
    HyperListItem(
        headlineContent = { Text(history.title) },
        supportingContent = { Text(history.url) },
        onClick = { openHistory(history) }
    )
}
```

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- Android 原生窗口和系统工具仅在 Android 目标可用；Preview 使用跨平台示例。
