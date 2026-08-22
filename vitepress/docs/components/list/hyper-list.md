# HyperList

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/list/HyperList.kt`
- 状态归属：调用方提供项目 Slot，可选持有 `LazyListState`
- Preview ID：`hyper_list`

页面级连续懒加载列表，适合列表页、消息页、记录流和分页内容。`HyperList` 固定使用 `LazyColumn`，只负责一个连续的不透明实色卡片背景、12dp 轻圆角、可选边框和滚动状态；项目结构完全由调用方通过 `LazyListScope` Slot 描述。需要按日期或类别形成多个独立圆角卡片时使用 [`HyperSectionedList`](hyper-sectioned-list.md)。

## 公开签名

```kotlin
@Composable
fun HyperList(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = HyperListDefaults.ContentPadding,
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
    val ContentPadding: PaddingValues = PaddingValues(0.dp)

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperListColors

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## 参数

| 参数 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `modifier` | `Modifier` | `Modifier` | 列表根容器修饰符 |
| `contentModifier` | `Modifier` | `Modifier` | 列表容器内部的布局修饰符 |
| `state` | `LazyListState` | `rememberLazyListState()` | 懒列表滚动状态 |
| `contentPadding` | `PaddingValues` | `HyperListDefaults.ContentPadding` | `LazyColumn` 的可滚动内容间距，可用于沉浸式页面首屏净空 |
| `verticalArrangement` | `Arrangement.Vertical` | 间距 `0.dp` | 条目纵向排列 |
| `shape` | `Shape` | `HyperListDefaults.Shape` | 容器与内容裁剪形状，默认 12dp 轻圆角 |
| `border` | `BorderStroke?` | `null` | 可选同形边框 |
| `colors` | `HyperListColors` | `HyperListDefaults.colors()` | 默认使用不透明的 `HyperColors.cardContainer` |
| `content` | `LazyListScope.() -> Unit` | 必填 | 使用 `item`、`items`、`itemsIndexed` 等标准懒列表 Slot 描述项目 |

## 最小用法

```kotlin
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import hyper_ui.*

@Composable
fun AccountList(accounts: List<Account>) {
    HyperList {
        items(
            items = accounts,
            key = { account -> account.id },
            contentType = { "account" }
        ) { account ->
            HyperListItem(
                headlineContent = { Text(account.name) },
                dividerVisible = account != accounts.lastOrNull()
            )
        }
    }
}
```

异构或分组内容直接组合多个 Slot：

```kotlin
HyperList(state = listState) {
    item(key = "overview", contentType = "header") {
        Text("概览")
    }
    items(accounts, key = { it.id }, contentType = { "account" }) { account ->
        HyperListItem(headlineContent = { Text(account.name) })
    }
}
```

## 约束

- `HyperList` 始终使用 `LazyColumn`，不提供关闭懒加载或切换普通 `Column` 的参数。
- 多个分组需要各自的圆角、背景和自动末项分割线时使用 [HyperSectionedList](hyper-sectioned-list.md)，不要在每个 `HyperListItem` 上重复拼接分组样式。
- 数据量很少且不需要独立滚动时，直接使用 Compose `Column`；设置分组和少量操作入口使用 [HyperMenuList](hyper-menu-list.md)。
- 容器颜色始终以不透明实色绘制；含 alpha 的自定义颜色会先与页面背景合成。
- 随条目一起滚动的首尾留白使用 `contentPadding`；列表节点内部布局使用 `contentModifier`，页面级外部留白使用 `modifier` 或父布局约束。
- `HyperList` 不解析 Slot 内容，最后一项分割线由调用方通过 `HyperListItem.dividerVisible` 控制。
- 大量或动态数据应提供稳定 `key`；结构不同的项目建议提供 `contentType`。
- 放入另一个同方向无界滚动容器前，应明确尺寸约束，避免嵌套滚动测量异常。

## 交互预览

<WasmPreview demo="hyper_list" title="HyperList 交互预览" />
