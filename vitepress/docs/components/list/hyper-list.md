# HyperList

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/list/HyperList.kt`
- 状态归属：调用方提供列表数据
- Preview ID：`hyper_list`

页面级列表容器，适合列表页、消息页、记录流和动态数据。`HyperList` 默认使用不透明实色卡片背景、12dp 轻圆角、无玻璃高光、无外层描边的平铺列表样式，并会按容器形状裁剪滚动内容；调用方通过 `lazyLoading` 参数决定使用 `LazyColumn` 懒加载，还是使用普通 `Column + verticalScroll` 一次组合全部项目。数据入口会自动抑制最后一项的 `HyperListItem` 分割线。

## 公开签名

```kotlin
@Composable
fun <T> HyperList(
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    lazyLoading: Boolean = true,
    lazyListState: LazyListState = rememberLazyListState(),
    scrollState: ScrollState = rememberScrollState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    shape: Shape = HyperListDefaults.Shape,
    border: BorderStroke? = null,
    colors: HyperListColors = HyperListDefaults.colors(),
    itemContent: @Composable (item: T) -> Unit
)

@Composable
fun HyperList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    shape: Shape = HyperListDefaults.Shape,
    border: BorderStroke? = null,
    colors: HyperListColors = HyperListDefaults.colors(),
    content: LazyListScope.() -> Unit
)
```

## 关键公开类型

```kotlin
data class HyperListColors(
    val containerColor: Color
)

object HyperListDefaults {
    val Shape: Shape

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperListColors

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## 参数

| 参数 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `items` | `List<T>` | 必填 | 列表数据 |
| `modifier` | `Modifier` | `Modifier` | 列表根容器修饰符 |
| `key` | `((T) -> Any)?` | `null` | 可选稳定键，仅在 `lazyLoading = true` 时传给懒列表 |
| `lazyLoading` | `Boolean` | `true` | `true` 使用 `LazyColumn`；`false` 使用普通 `Column + verticalScroll` |
| `lazyListState` | `LazyListState` | `rememberLazyListState()` | 懒加载模式的滚动状态 |
| `scrollState` | `ScrollState` | `rememberScrollState()` | 普通列表模式的滚动状态 |
| `contentPadding` | `PaddingValues` | `PaddingValues(0.dp)` | 列表内容内边距 |
| `verticalArrangement` | `Arrangement.Vertical` | 间距 `0.dp` | 条目纵向排列 |
| `shape` | `Shape` | `HyperListDefaults.Shape` | 列表容器与内容裁剪形状，默认 12dp 轻圆角 |
| `border` | `BorderStroke?` | `null` | 页面列表默认无外层描边；需要同形描边时可传 `HyperListDefaults.border()` |
| `colors` | `HyperListColors` | `HyperListDefaults.colors()` | 列表容器颜色，默认使用不透明的 `HyperColors.cardContainer` |
| `itemContent` | `@Composable (T) -> Unit` | 必填 | 每项内容，只接收当前项目，不接收索引 |

DSL 入口接收 `state` 与 `LazyListScope.content`，固定使用 `LazyColumn`，适合异构条目、分组标题、分页加载占位等内容。

## 最小用法

```kotlin
import androidx.compose.runtime.Composable
import hyper_ui.*

@Composable
fun AccountList(accounts: List<String>) {
    HyperList(
        items = accounts,
        key = { account -> account },
        lazyLoading = true
    ) { account ->
        HyperListItem(
            headlineContent = { Text(account) },
            dividerVisible = true
        )
    }
}
```

少量数据可关闭懒加载：

```kotlin
HyperList(
    items = accounts,
    lazyLoading = false
) { account ->
    HyperListItem(
        headlineContent = { Text(account.name) },
        dividerVisible = true
    )
}
```

异构内容可使用 DSL 入口：

```kotlin
HyperList(state = listState) {
    item {
        HyperListItem(headlineContent = { Text("概览") })
    }
    items(accounts, key = { it.id }) { account ->
        HyperListItem(headlineContent = { Text(account.name) })
    }
}
```

## 约束

- `HyperList` 是页面级列表容器，默认使用 12dp 轻圆角并裁剪内容，不默认添加外层描边。
- 容器颜色始终以不透明实色绘制；含 alpha 的自定义颜色会先与页面背景合成，不会透出下层内容。
- `contentPadding` 位于列表容器内部，会使用列表背景绘制；底栏避让等页面级留白应通过外层 `modifier` 或父布局约束实现。
- 数据入口会自动隐藏最后一项的 `HyperListItem` 分割线，调用方只需表达普通行是否需要分割线。
- `lazyLoading = true` 适合大量数据、分页和动态列表；`key` 只在该模式下生效。
- `lazyLoading = false` 会一次组合全部项目，适合少量稳定数据或需要普通 `Column` 行为的场景。
- DSL 入口固定使用 `LazyListScope`，无法推断最后一个子项；使用 `HyperListItem.dividerVisible` 时仍由调用方控制最后一项是否显示。
- 需要圆角菜单、设置分组或少量操作入口时，使用 [HyperMenuList](hyper-menu-list.md)。
- 放入另一个同方向无界滚动容器前，应明确尺寸约束。

## 交互预览

<WasmPreview demo="hyper_list" title="HyperList 交互预览" />
