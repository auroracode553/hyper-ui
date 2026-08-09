# HyperMenuList

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/list/HyperMenuList.kt`
- 状态归属：调用方提供菜单数据或内容
- Preview ID：`hyper_menu_list`

圆角菜单列表容器，适合少量静态菜单、设置分组和操作入口。它默认带圆角、卡片背景和 1dp 轻描边；数据项入口基于 `Column` 与 `verticalScroll` 一次组合全部项目，并会自动抑制最后一项的 `HyperListItem` 分割线；slot 分组入口不额外添加滚动，适合放在页面级滚动容器中。

## 公开签名

```kotlin
@Composable
fun HyperMenuList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = HyperMenuListDefaults.border(),
    colors: HyperMenuListColors = HyperMenuListDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit
)

@Composable
fun <T> HyperMenuList(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = HyperMenuListDefaults.border(),
    colors: HyperMenuListColors = HyperMenuListDefaults.colors(),
    itemContent: @Composable (item: T) -> Unit
)
```

## 关键公开类型

```kotlin
data class HyperMenuListColors(
    val containerColor: Color
)

object HyperMenuListDefaults {
    val Shape: Shape

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperMenuListColors

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## 参数

| 参数 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `items` | `List<T>` | 必填 | 一次性渲染的菜单数据 |
| `modifier` | `Modifier` | `Modifier` | 根容器修饰符 |
| `contentPadding` | `PaddingValues` | `PaddingValues(0.dp)` | 容器内容内边距 |
| `verticalArrangement` | `Arrangement.Vertical` | 间距 `0.dp` | 条目纵向排列 |
| `border` | `BorderStroke?` | `HyperMenuListDefaults.border()` | 菜单外层描边；传 `null` 可关闭 |
| `colors` | `HyperMenuListColors` | `HyperMenuListDefaults.colors()` | 菜单容器颜色，默认使用 `HyperColors.elevatedContainer` |
| `itemContent` | `@Composable (T) -> Unit` | 必填 | 每项菜单内容 |
| `content` | `@Composable ColumnScope.() -> Unit` | slot 入口必填 | 直接放置 `HyperListItem` 等内容，适合设置分组 |

## 最小用法

```kotlin
@Composable
fun MenuOptions(options: List<String>) {
    HyperMenuList(items = options) { option ->
        HyperListItem(
            headlineContent = { Text(option) },
            dividerVisible = true,
            onClick = { /* 由调用方处理 */ }
        )
    }
}

@Composable
fun SettingsGroup(enabled: Boolean, onEnabledChange: (Boolean) -> Unit) {
    HyperMenuList {
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

- `HyperMenuList` 用于菜单、设置分组和少量操作入口；页面级列表使用 [HyperList](hyper-list.md)。
- `contentPadding` 位于圆角背景和 border 内部；底栏避让等页面级留白应通过外层 `modifier` 或父布局约束实现，避免 border 包住留白区域。
- `items` 数据入口内部自带纵向滚动；放入另一个同方向无界滚动容器前，应明确尺寸约束。
- `items` 数据入口会自动隐藏最后一项的 `HyperListItem` 分割线，调用方只需表达普通行是否需要分割线。
- slot 分组入口不额外添加纵向滚动，适合设置页、详情页等已有页面级滚动的场景。
- slot 分组入口无法推断最后一个子项；使用 `HyperListItem.dividerVisible` 时仍由调用方控制最后一项是否显示。
- 首尾圆角、卡片背景和外层描边由菜单容器处理，不要在每项重复计算外层形状。

## 交互预览

<WasmPreview demo="hyper_menu_list" title="HyperMenuList 交互预览" />
