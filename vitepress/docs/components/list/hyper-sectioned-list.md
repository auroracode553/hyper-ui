---
description: 按日期或分类组织页面数据，标题与数据行独立懒加载，使用无卡片平铺行并自动管理组内分隔线。
---

# HyperSectionedList

按日期或分类组织页面数据，标题与数据行独立懒加载，使用无卡片平铺行并自动管理组内分隔线。分组之间固定使用 8dp 紧凑间距，不提供间距切换或 sectionSpacing 参数。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="hyper_sectioned_list" title="HyperSectionedList 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import hyper_ui.*

HyperSectionedList(
    modifier = Modifier.fillMaxSize(),
    sections = groups,
    items = { it.entries },
    sectionKey = { "section-${it.id}" },
    itemKey = { group, entry -> "entry-${group.id}-${entry.id}" },
    headerContent = { HyperText(it.title) }
) { _, entry ->
    HyperListTile(title = { HyperText(entry.title) })
}
```

</details>

## 使用约定

**滚动布局**

列表需要有限高度。页面正文使用 Modifier.fillMaxSize()；顶部控件与列表组合时，列表使用 Modifier.weight(1f) 占满剩余视口，不使用固定 340dp 高度或外层 verticalScroll。默认水平留白 0dp，由调用方通过 contentPadding = PaddingValues(horizontal = HyperSpacing.PageHorizontal) 或页面容器统一提供一次。分组和条目 key 必须在整个列表中唯一。选择状态由调用方持有。列表不绘制卡片背景、阴影或分组圆角；配色仅包含 headerContentColor 与 dividerColor。

**约束**

业务状态由调用方管理，组件只负责 UI 与回调。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperSectionedList

<details class="component-doc__details">
<summary>查看公开签名</summary>

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
    headerBottomSpacing: Dp = HyperSectionedListDefaults.HeaderBottomSpacing,
    dividerModifier: Modifier = Modifier,
    colors: HyperSectionedListColors = HyperSectionedListDefaults.colors(),
    headerContent: @Composable (section: S) -> Unit,
    itemContent: @Composable (section: S, item: T) -> Unit
)
```

</details>

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
| headerBottomSpacing | Dp | 否 | HyperSectionedListDefaults.HeaderBottomSpacing | 分组标题下方间距。 |
| dividerModifier | Modifier | 否 | Modifier | 分隔线的布局修饰符，默认无缩进。 |
| colors | HyperSectionedListColors | 否 | HyperSectionedListDefaults.colors() | 组件各状态的颜色配置。 |
| headerContent | @Composable (section: S) -&gt; Unit | 是 | — | 每个分组的标题内容。 |
| itemContent | @Composable (section: S, item: T) -&gt; Unit | 是 | — | 每个数据项的自定义内容。 |
