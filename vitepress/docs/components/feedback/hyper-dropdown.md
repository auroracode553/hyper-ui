# HyperDropdown

- 分类：反馈组件
- 包名：`hyper_ui`
- 状态模型：受控组件；展开状态由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/menu/HyperDropdown.kt`
- Preview ID：`dropdown`

`HyperDropdown` 是无蒙层的 Popup 菜单容器。面板宽度按最宽菜单项内容自动收缩，并以 220dp 为上限，避免短菜单右侧出现大块空白。组件同时负责浮层定位、参考系统菜单的柔雾面板、滚动、即时按压反馈、危险项语义与点击关闭；描边和阴影复用内部公共 `HyperSurfaceDepth` 浮层强度，文字、图标和业务动作仍由 slot 提供。

## 公开 API

```kotlin
data class HyperDropdownColors(
    val containerColor: Color,
    val contentColor: Color,
    val dangerContentColor: Color,
    val disabledContentColor: Color,
    val pressedContainerColor: Color,
    val dividerColor: Color
)

enum class HyperDropdownItemTone {
    Normal,
    Danger
}

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
        tone: HyperDropdownItemTone = HyperDropdownItemTone.Normal,
        content: @Composable RowScope.() -> Unit
    )

    @Composable
    fun Divider(modifier: Modifier = Modifier)
}
```

## 默认值

```kotlin
object HyperDropdownDefaults {
    val MaxWidth = 220.dp
    val MaxHeight = 432.dp
    val ItemHeight = 48.dp
    val AnchorOffsetY = 52.dp
    val Elevation = 10.dp
    val Shape: Shape = RoundedCornerShape(26.dp)
    val ItemShape: Shape = RoundedCornerShape(16.dp)
    val MenuPadding = PaddingValues(vertical = 8.dp)
    val ItemPadding = PaddingValues(horizontal = 22.dp)
    val DividerPadding = PaddingValues(horizontal = 22.dp, vertical = 6.dp)
    val ItemTextStyle = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    )

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        dangerContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        pressedContainerColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperDropdownColors
}
```

## 容器参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `expanded` | `Boolean` | 是 | 无 | 是否渲染 Popup。 |
| `onDismissRequest` | `() -> Unit` | 是 | 无 | 外部点击、返回或菜单项关闭时请求调用方更新状态。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 菜单面板外壳；默认内容自适应宽度和最大宽度会在其后应用，可用 `width`、`heightIn` 覆盖。 |
| `contentModifier` | `Modifier` | 否 | `Modifier.padding(MenuPadding)` | 面板内部滚动内容的修饰符。 |
| `alignment` | `Alignment` | 否 | `Alignment.TopEnd` | Popup 相对应用窗口的对齐方式。 |
| `offset` | `DpOffset` | 否 | `(0.dp, AnchorOffsetY)` | 在 `alignment` 基础上的密度无关偏移。 |
| `shape` | `Shape` | 否 | `HyperDropdownDefaults.Shape` | 菜单面板形状。 |
| `colors` | `HyperDropdownColors` | 否 | `HyperDropdownDefaults.colors()` | 面板、普通/危险/禁用内容、按压反馈和分割线颜色。 |
| `content` | `@Composable HyperDropdownScope.() -> Unit` | 是 | 无 | 菜单项与分割线 slot。 |

## Item 参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `onClick` | `() -> Unit` | 是 | 无 | 菜单项动作；先执行该回调，再按配置请求关闭。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 48dp 高的整行点击节点。 |
| `contentModifier` | `Modifier` | 否 | `Modifier.padding(ItemPadding)` | 行内 slot 留白。 |
| `enabled` | `Boolean` | 否 | `true` | 控制点击和禁用内容色。 |
| `closeOnClick` | `Boolean` | 否 | `true` | 点击后是否调用 `onDismissRequest`。 |
| `tone` | `HyperDropdownItemTone` | 否 | `Normal` | `Danger` 自动使用主题危险色，适合删除等不可逆操作。 |
| `content` | `@Composable RowScope.() -> Unit` | 是 | 无 | 图标、文字等行内容。 |

`Divider(modifier)` 使用当前 `dividerColor`，并默认添加水平 22dp、垂直 6dp 留白。菜单项通过 `LocalContentColor` 和 `ProvideTextStyle` 提供默认颜色与 18sp 字体；slot 内显式设置的样式仍可覆盖默认值。

## 最小用法

```kotlin
HyperDropdown(
    expanded = expanded,
    onDismissRequest = { expanded = false }
) {
    Item(onClick = onChangeBackground) { Text("更换背景") }
    Item(onClick = onMove) { Text("移动到") }
    Item(
        onClick = onDelete,
        tone = HyperDropdownItemTone.Danger
    ) {
        Text("删除")
    }
}
```

## 约束

- 不存在 `text`、`leadingIcon` 或业务动作参数；内容继续由 slot 注入。
- 菜单不渲染遮罩；`PopupProperties(focusable = true)` 负责外部点击和返回关闭请求。
- 面板默认按最宽菜单项的固有宽度收缩，最大宽 220dp、最大高 432dp，26dp 圆角；纵向超出后在组件内部滚动。
- 默认容器在浅色主题使用 96% 乳白色、深色主题使用 96% 炭灰色；公共深度层以 1dp 低对比度描边和单层 10dp 阴影建立浮层层级。
- 自定义 `containerColor` 的 alpha 会被保留，可让底层内容轻微透出；组件不执行真实背景模糊。
- 菜单项固定 48dp 高，按下时立即显示低对比度背景；禁用态不响应点击，也不显示按压反馈。

<WasmPreview demo="dropdown" title="HyperDropdown 交互预览" />
