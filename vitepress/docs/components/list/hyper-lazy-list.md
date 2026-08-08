# HyperLazyList

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/list/HyperLazyList.kt`
- 状态归属：调用方提供列表数据
- Preview ID：`lazy_list`

基于 `LazyColumn` 的懒加载列表，适合数量较多或动态变化的数据。列表自动计算首尾圆角与卡片背景。

## 公开签名

```kotlin
@Composable
fun <T> HyperLazyList(
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    itemContent: @Composable (item: T) -> Unit
)
```

## 参数

| 参数 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `items` | `List<T>` | 必填 | 列表数据 |
| `modifier` | `Modifier` | `Modifier` | `LazyColumn` 修饰符 |
| `key` | `((T) -> Any)?` | `null` | 可选稳定键，动态列表建议提供 |
| `contentPadding` | `PaddingValues` | `PaddingValues(0.dp)` | 列表内容内边距 |
| `verticalArrangement` | `Arrangement.Vertical` | 间距 `0.dp` | 条目纵向排列 |
| `itemContent` | `@Composable (T) -> Unit` | 必填 | 每项内容，只接收当前项目，不接收索引 |

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
            headlineContent = { Text(account) }
        )
    }
}
```

## 约束

- `itemContent` 的参数是项目本身，不是索引。
- 首尾圆角与项目背景由列表自动处理，不要在每项重复计算外层形状。
- 分割线仍由条目内容决定；使用 `HyperListItem.dividerVisible` 时由调用方根据业务数据设置。
- 少量、固定数据可使用 [HyperList](hyper-list.md)。

## 交互预览

<WasmPreview demo="lazy_list" title="HyperLazyList 交互预览" />
