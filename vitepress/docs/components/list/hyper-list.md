# HyperList

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/list/HyperList.kt`
- 状态归属：调用方提供列表数据
- Preview ID：`hyper_list`

非懒加载列表容器，适合数量较少的静态数据和设置页分组。数据项入口基于 `Column` 与 `verticalScroll` 一次组合全部项目；slot 分组入口不额外添加滚动，适合放在页面级滚动容器中。列表外层默认带 1dp 轻描边。

## 公开签名

```kotlin
@Composable
fun HyperList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = HyperListDefaults.border(),
    colors: HyperListColors = HyperListDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit
)

@Composable
fun <T> HyperList(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = HyperListDefaults.border(),
    colors: HyperListColors = HyperListDefaults.colors(),
    itemContent: @Composable (item: T) -> Unit
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
| `items` | `List<T>` | 必填 | 一次性渲染的数据 |
| `modifier` | `Modifier` | `Modifier` | 根 `Column` 修饰符 |
| `contentPadding` | `PaddingValues` | `PaddingValues(0.dp)` | 滚动内容内边距 |
| `verticalArrangement` | `Arrangement.Vertical` | 间距 `0.dp` | 条目纵向排列 |
| `border` | `BorderStroke?` | `HyperListDefaults.border()` | 列表外层描边；传 `null` 可关闭 |
| `colors` | `HyperListColors` | `HyperListDefaults.colors()` | 列表容器颜色，默认使用 `HyperColors.elevatedContainer` |
| `itemContent` | `@Composable (T) -> Unit` | 必填 | 每项内容 |
| `content` | `@Composable ColumnScope.() -> Unit` | slot 入口必填 | 直接放置 `HyperListItem` 等内容，适合设置分组 |

## 最小用法

```kotlin
@Composable
fun StaticOptions(options: List<String>) {
    HyperList(items = options) { option ->
        HyperListItem(
            headlineContent = { Text(option) },
            onClick = { /* 由调用方处理 */ }
        )
    }
}

@Composable
fun SettingsGroup(enabled: Boolean, onEnabledChange: (Boolean) -> Unit) {
    HyperList {
        HyperListItem(
            headlineContent = { Text("推送通知") },
            supportingContent = { Text("接收重要消息提醒") },
            trailingContent = {
                HyperSwitch(
                    checked = enabled,
                    onCheckedChange = onEnabledChange
                )
            }
        )
    }
}
```

## 约束

- `items` 数据入口内部自带纵向滚动；放入另一个同方向无界滚动容器前，应明确尺寸约束。
- slot 分组入口不额外添加纵向滚动，适合设置页、详情页等已有页面级滚动的场景。
- 项目较多时改用 [HyperLazyList](hyper-lazy-list.md)，避免一次组合所有内容。
- 与 `HyperLazyList` 一样，首尾圆角、卡片背景和外层描边由列表处理。
- 默认描边来自 `HyperListDefaults.border()`，内部使用 `HyperColors.panelBorder`。

## 交互预览

<WasmPreview demo="hyper_list" title="HyperList 交互预览" />
