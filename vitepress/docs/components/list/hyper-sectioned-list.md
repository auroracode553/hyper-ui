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

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| sections | List&lt;S&gt; | 是 | — | 分组数据。 |
| items | (S) -&gt; List&lt;T&gt; | 是 | — | 由调用方提供的选项或列表数据。 |
| sectionKey | (S) -&gt; Any | 是 | — | 为分组生成稳定键。 |
| itemKey | (S, T) -&gt; Any | 是 | — | 为列表项生成稳定键。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| state | LazyListState | 否 | rememberLazyListState() | 组件使用的状态对象，由调用方提供或记忆。 |
| contentPadding | PaddingValues | 否 | HyperSectionedListDefaults.ContentPadding | 主体内容的内部留白。 |
| firstSectionTopSpacing | Dp | 否 | HyperSectionedListDefaults.FirstSectionTopSpacing | 第一分组顶部的间距。 |
| sectionSpacing | Dp | 否 | HyperSectionedListDefaults.SectionSpacing | 相邻分组之间的间距。 |
| headerBottomSpacing | Dp | 否 | HyperSectionedListDefaults.HeaderBottomSpacing | 分组标题下方间距。 |
| dividerModifier | Modifier | 否 | Modifier.padding( start = HyperSectionedListDefaults.DividerInset ) | 分隔线的布局修饰符。 |
| colors | HyperSectionedListColors | 否 | HyperSectionedListDefaults.colors() | 组件各状态的颜色配置。 |
| headerContent | @Composable (section: S) -&gt; Unit | 是 | — | 每个分组的标题内容。 |
| itemContent | @Composable (section: S, item: T) -&gt; Unit | 是 | — | 每个数据项的自定义内容。 |

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
