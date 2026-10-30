---
description: 紧凑、可左右滑动的受控分类筛选栏。
---

# HyperFilterBar

横向分类筛选使用独立紧凑选项，不绘制整条胶囊底板。适合文档类型、日志分类、工作表等数量可变的分类入口。

包名 hyper_ui · Android 手机端。

<WasmPreview demo="filter-bar" title="横向分类筛选栏" />

## 使用方式

```kotlin
val categories = listOf("全部", "PDF", "Word", "文本", "Markdown")
var selected by remember { mutableIntStateOf(0) }
HyperFilterBar(items = categories, selectedIndex = selected, onChanged = { selected = it }) {
    HyperText(it, maxLines = 1)
}
```

每项最小高度 32dp、最小宽度 48dp，水平/垂直内边距 12/6dp，项间距 6dp。文字为 13sp、行高 1.4；选中 600、普通 500，图标继承 16dp。文字缩放可自然撑高，不通过固定整栏高度裁切内容。选中底色使用主题 selection，普通项背景透明。

选中索引与业务筛选由调用方持有，组件仅管理内部横向滚动。索引必须在 items 范围内；重复点击当前项不回调。整栏和单项可以分别禁用；外部切换或恢复筛选时，组件将不完整可见的选中项滚入视口，已经完整可见的选项不重置滚动位置。背景过渡为 160ms easeOutCubic。

页面将筛选栏放在 HyperNavBarPage 的导航前景，由宿主测量导航与筛选总高度；测量净空只进入主列表 contentPadding，不能将筛选栏高度作为视口外固定占位。少量固定分段切换使用 [HyperTabs](hyper-tabs.md)。

## 公开 API

```kotlin
@Composable
fun <T> HyperFilterBar(
    items: List<T>,
    selectedIndex: Int,
    onChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperFilterBarItemScope.(T) -> Unit,
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| items | List&lt;T&gt; | 是 | — | 分类数据，不能为空。 |
| selectedIndex | Int | 是 | — | 当前选中项的索引，从 0 开始。 |
| onChanged | (Int) -&gt; Unit | 是 | — | 用户选择其他可用项时通知业务。 |
| modifier | Modifier | 否 | Modifier | 栏的外部布局。 |
| enabled | Boolean | 否 | true | 整栏是否可操作。 |
| itemEnabled | (T) -&gt; Boolean | 否 | { true } | 单项是否可操作。 |
| itemContent | Composable HyperFilterBarItemScope.(T) -&gt; Unit | 是 | — | 分类内容；作用域提供 selected 和 enabled。 |

| 默认项 | 类型 | 值 | 用途 |
| --- | --- | --- | --- |
| HyperFilterBarDefaults.MinHeight | Dp | 32dp | 单项最小高度。 |
| HyperFilterBarDefaults.ItemSpacing | Dp | 6dp | 项间距。 |
| HyperFilterBarDefaults.ItemPadding | PaddingValues | 水平 12dp / 垂直 6dp | 单项内容留白。 |
