# HyperSectionedList

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/list/HyperSectionedList.kt`
- 状态归属：调用方提供分组数据与稳定 key，可选持有 `LazyListState`
- Preview ID：`hyper_sectioned_list`

页面级分段懒列表，适合历史记录、最近文件、消息日期分组等动态数据。日期标题和每一条数据都会成为独立懒加载项目；每个非空分组自动形成独立的 16dp 圆角实色卡片，并由组件统一绘制组内分割线。调用方不需要计算首项、中间项、尾项圆角，也不需要判断最后一项是否显示分割线。

## 公开签名

```kotlin
@Composable
fun <S, T> HyperSectionedList(
    sections: List<S>,
    items: (S) -> List<T>,
    sectionKey: (S) -> Any,
    itemKey: (S, T) -> Any,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
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

## 关键公开类型

```kotlin
data class HyperSectionedListColors(
    val headerContentColor: Color,
    val itemContainerColor: Color,
    val dividerColor: Color
)

object HyperSectionedListDefaults {
    val FirstSectionTopSpacing = 0.dp
    val SectionSpacing = 16.dp
    val HeaderBottomSpacing = 8.dp
    val DividerInset = HyperListItemDefaults.DividerInset
    val HeaderTextStyle: TextStyle

    @Composable
    fun colors(
        headerContentColor: Color = Color.Unspecified,
        itemContainerColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperSectionedListColors
}
```

## 参数

| 参数 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `sections` | `List<S>` | 必填 | 已按业务顺序排列的分组集合；空分组自动跳过 |
| `items` | `(S) -> List<T>` | 必填 | 返回指定分组的数据行 |
| `sectionKey` | `(S) -> Any` | 必填 | 分组标题的稳定且全局唯一 key |
| `itemKey` | `(S, T) -> Any` | 必填 | 数据行的稳定且全局唯一 key |
| `modifier` | `Modifier` | `Modifier` | 列表根容器尺寸和外部间距 |
| `state` | `LazyListState` | `rememberLazyListState()` | 懒列表滚动状态 |
| `contentPadding` | `PaddingValues` | `PaddingValues(0.dp)` | `LazyColumn` 内容区内边距 |
| `firstSectionTopSpacing` | `Dp` | `0.dp` | 第一个非空分组标题上方间距 |
| `sectionSpacing` | `Dp` | `16.dp` | 后续分组标题与上一组卡片之间的间距 |
| `headerBottomSpacing` | `Dp` | `8.dp` | 分组标题与本组卡片之间的间距 |
| `dividerModifier` | `Modifier` | 左侧缩进 `16.dp` | 组件自动绘制的组内分割线修饰符 |
| `colors` | `HyperSectionedListColors` | `HyperSectionedListDefaults.colors()` | 标题、卡片和分割线颜色，最终均解析为不透明实色 |
| `headerContent` | `@Composable (S) -> Unit` | 必填 | 分组标题 Slot，默认获得 15sp/20sp 半粗体样式和次要文字色 |
| `itemContent` | `@Composable (S, T) -> Unit` | 必填 | 单行内容 Slot，通常直接使用 `HyperListItem` |

## 最小用法

```kotlin
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
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

## 约束

- `HyperSectionedList` 固定使用一个 `LazyColumn`；标题和每个数据行均保持独立懒加载，不会把整组数据一次性组合进普通 `Column`。
- `sectionKey` 与 `itemKey` 返回值必须在整个列表中彼此唯一、稳定，并满足 Compose 保存滚动位置对 key 类型的要求。推荐分别添加 `section-`、`item-` 前缀。
- 空分组不会渲染标题，也不占据分段间距。
- 组内圆角、背景和分割线由组件统一负责。`itemContent` 使用 `HyperListItem` 时保持默认的 `dividerVisible = false`，避免业务层重复表达视觉规则。
- 分组卡片固定使用 `HyperStyleDefaults.MediumCornerRadius`；需要单一连续卡片或完全自定义 `LazyListScope` 时使用 [`HyperList`](hyper-list.md)。
- `headerContent` 会收到默认 `LocalContentColor` 与 `LocalTextStyle`，调用方仍可在具体内容中显式覆盖。
- 列表容器颜色始终以不透明实色绘制；含 alpha 的自定义颜色会先与对应背景合成。
- 放入另一个同方向无界滚动容器前，应明确尺寸约束，避免嵌套滚动测量异常。

## 交互预览

<WasmPreview demo="hyper_sectioned_list" title="HyperSectionedList 交互预览" />
