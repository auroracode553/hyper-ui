# HyperLazyList

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/list/HyperLazyList.kt`
- 状态归属：调用方提供列表数据
- Preview ID：`lazy_list`

基于 `LazyColumn` 的页面级懒加载列表，适合列表页、消息页、记录流和大量动态数据。它提供同构数据入口和异构内容 DSL 入口，默认使用整页平铺背景，不添加圆角、玻璃高光或外层描边；同构数据入口会自动抑制最后一项的 `HyperListItem` 分割线。

## 公开签名

```kotlin
@Composable
fun <T> HyperLazyList(
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = null,
    colors: HyperLazyListColors = HyperLazyListDefaults.colors(),
    itemContent: @Composable (item: T) -> Unit
)

@Composable
fun HyperLazyList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = null,
    colors: HyperLazyListColors = HyperLazyListDefaults.colors(),
    content: LazyListScope.() -> Unit
)
```

## 关键公开类型

```kotlin
data class HyperLazyListColors(
    val containerColor: Color
)

object HyperLazyListDefaults {
    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperLazyListColors

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## 参数

| 参数 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `items` | `List<T>` | 必填 | 列表数据 |
| `modifier` | `Modifier` | `Modifier` | `LazyColumn` 修饰符 |
| `key` | `((T) -> Any)?` | `null` | 可选稳定键，动态列表建议提供 |
| `contentPadding` | `PaddingValues` | `PaddingValues(0.dp)` | 列表内容内边距 |
| `verticalArrangement` | `Arrangement.Vertical` | 间距 `0.dp` | 条目纵向排列 |
| `border` | `BorderStroke?` | `null` | 页面列表默认无外层描边；需要矩形描边时可传 `HyperLazyListDefaults.border()` |
| `colors` | `HyperLazyListColors` | `HyperLazyListDefaults.colors()` | 列表容器颜色，默认使用 `HyperColors.cardContainer` |
| `itemContent` | `@Composable (T) -> Unit` | 必填 | 每项内容，只接收当前项目，不接收索引 |

DSL 入口额外接收 `state` 与 `LazyListScope.content`，适合异构条目、分组标题、分页加载占位等内容；其余视觉参数含义与数据入口一致。

## 最小用法

```kotlin
import androidx.compose.runtime.Composable
import hyper_ui.*

@Composable
fun AccountList(accounts: List<String>) {
    HyperLazyList(
        items = accounts,
        key = { account -> account }
    ) { account ->
        HyperListItem(
            headlineContent = { Text(account) },
            dividerVisible = true
        )
    }
}
```

异构内容可使用 DSL 入口：

```kotlin
HyperLazyList(state = listState) {
    item {
        HyperListItem(headlineContent = { Text("概览") })
    }
    items(accounts, key = { it.id }) { account ->
        HyperListItem(headlineContent = { Text(account.name) })
    }
}
```

## 约束

- `HyperLazyList` 是列表页容器，不做首尾圆角，也不默认添加外层描边。
- `itemContent` 的参数是项目本身，不是索引。
- 同构数据入口会自动隐藏最后一项的 `HyperListItem` 分割线，调用方只需表达普通行是否需要分割线。
- 需要混合多种条目或由外部持有滚动状态时，使用 `LazyListScope` DSL 入口。
- DSL 入口无法推断最后一个子项；使用 `HyperListItem.dividerVisible` 时仍由调用方控制最后一项是否显示。
- 需要圆角菜单、设置分组或少量操作入口时，使用 [HyperMenuList](hyper-menu-list.md)。
- 放入另一个同方向无界滚动容器前，应明确尺寸约束。

## 交互预览

<WasmPreview demo="lazy_list" title="HyperLazyList 交互预览" />
