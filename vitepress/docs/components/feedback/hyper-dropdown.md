# HyperDropdown

- 分类：反馈组件
- 包名：`hyper_ui`
- 状态模型：受控组件；展开状态由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/menu/HyperDropdown.kt`
- Preview ID：`dropdown`

`HyperDropdown` 是无蒙层的 Popup 菜单容器。组件负责浮层定位、实色面板、滚动、菜单项点击关闭与分割线；文字、图标和业务动作由 slot 提供。

## 公开 API

```kotlin
data class HyperDropdownColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContentColor: Color,
    val dividerColor: Color
)

@Composable
fun HyperDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperDropdownDefaults.MenuPadding),
    alignment: Alignment = Alignment.TopEnd,
    offset: DpOffset = DpOffset(0.dp, HyperDropdownDefaults.AnchorOffsetY),
    shape: Shape = HyperDropdownDefaults.Shape,
    colors: HyperDropdownColors = HyperDropdownDefaults.colors(),
    border: BorderStroke? = HyperDropdownDefaults.border(),
    content: @Composable HyperDropdownScope.() -> Unit
)

class HyperDropdownScope {
    @Composable
    fun Item(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        contentModifier: Modifier = Modifier.padding(HyperDropdownDefaults.ItemPadding),
        enabled: Boolean = true,
        closeOnClick: Boolean = true,
        content: @Composable RowScope.() -> Unit
    )

    @Composable
    fun Divider(modifier: Modifier = Modifier)
}
```

## 默认值

```kotlin
object HyperDropdownDefaults {
    val MenuWidth = 184.dp
    val MaxHeight = 420.dp
    val ItemHeight = 48.dp
    val AnchorOffsetY = 52.dp
    val Shape: Shape = RoundedCornerShape(20.dp)
    val MenuPadding = PaddingValues(vertical = 8.dp)
    val ItemPadding = PaddingValues(horizontal = 20.dp)

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperDropdownColors

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## 容器参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `expanded` | `Boolean` | 是 | 无 | 是否渲染 Popup。 |
| `onDismissRequest` | `() -> Unit` | 是 | 无 | 外部点击、返回或菜单项关闭时请求调用方更新状态。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 菜单面板外壳；默认尺寸会在其后应用，可用 `width`、`heightIn` 覆盖。 |
| `contentModifier` | `Modifier` | 否 | `Modifier.padding(MenuPadding)` | 面板内部滚动内容的修饰符。 |
| `alignment` | `Alignment` | 否 | `Alignment.TopEnd` | Popup 相对应用窗口的对齐方式。 |
| `offset` | `DpOffset` | 否 | `(0.dp, AnchorOffsetY)` | 在 `alignment` 基础上的密度无关偏移。 |
| `shape` | `Shape` | 否 | `HyperDropdownDefaults.Shape` | 菜单面板形状。 |
| `colors` | `HyperDropdownColors` | 否 | `HyperDropdownDefaults.colors()` | 面板、内容、禁用内容和分割线颜色。 |
| `border` | `BorderStroke?` | 否 | `HyperDropdownDefaults.border()` | 菜单描边；传 `null` 移除。 |
| `content` | `@Composable HyperDropdownScope.() -> Unit` | 是 | 无 | 菜单项与分割线 slot。 |

## Item 参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `onClick` | `() -> Unit` | 是 | 无 | 菜单项动作；先执行该回调，再按配置请求关闭。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 48dp 高的整行点击节点。 |
| `contentModifier` | `Modifier` | 否 | `Modifier.padding(ItemPadding)` | 行内 slot 留白。 |
| `enabled` | `Boolean` | 否 | `true` | 控制点击和禁用内容色。 |
| `closeOnClick` | `Boolean` | 否 | `true` | 点击后是否调用 `onDismissRequest`。 |
| `content` | `@Composable RowScope.() -> Unit` | 是 | 无 | 图标、文字等行内容。 |

`Divider(modifier)` 使用当前 `dividerColor`，并固定添加水平 20dp、垂直 6dp 留白。

## 最小用法

```kotlin
HyperDropdown(
    expanded = expanded,
    onDismissRequest = { expanded = false }
) {
    Item(onClick = onOpenDetail) { Text("查看详情") }
    Divider()
    Item(onClick = onDelete) { Text("删除") }
}
```

## 约束

- 不存在 `text`、`leadingIcon`、`textColor` 或 `contentPadding` 参数。
- 菜单不渲染遮罩；`PopupProperties(focusable = true)` 负责外部点击和返回关闭请求。
- 面板默认宽 184dp、最大高 420dp，超出后在组件内部纵向滚动。
- 组件会将面板、内容、禁用内容、分割线和默认描边与实色背景合成，避免底层页面透出。

<WasmPreview demo="dropdown" title="HyperDropdown 交互预览" />
