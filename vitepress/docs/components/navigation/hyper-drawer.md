# HyperDrawer

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/drawer/HyperDrawer.kt`
- 预览：`drawer`

`HyperDrawer` 是四方向抽屉容器，无遮罩。深色模式下抽屉面板和默认描边直接采用当前 `MaterialTheme.colorScheme.background`，与页面保持同色；浅色模式仅保留轻微透明度。面板不叠加玻璃高光或渐变；`open` 直接控制面板是否渲染，打开与关闭均不执行动画。抽屉默认不注入内容间距或系统栏安全区，内容可以使用完整面板范围。

## 公开签名

```kotlin
enum class HyperDrawerPosition { Left, Right, Top, Bottom }

data class HyperDrawerColors(
    val containerColor: Color,
    val contentColor: Color,
    val supportingColor: Color,
    val selectedContainerColor: Color,
    val selectedContentColor: Color,
    val disabledContentColor: Color,
    val dividerColor: Color
)

@Composable
fun HyperDrawer(
    open: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    drawerModifier: Modifier = Modifier,
    position: HyperDrawerPosition = HyperDrawerPosition.Left,
    drawerContentModifier: Modifier = Modifier,
    drawerContentScrollEnabled: Boolean = true,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    dismissOnClickOutside: Boolean = false,
    border: BorderStroke? = HyperDrawerDefaults.border(),
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
)

@Composable
fun HyperDrawerHeader(
    headlineContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperDrawerDefaults.HeaderPadding),
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null
)

@Composable
fun HyperDrawerItem(
    headlineContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperDrawerDefaults.ItemPadding),
    selected: Boolean = false,
    enabled: Boolean = true,
    dividerVisible: Boolean = false,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
)
```

## 关键公开类型

```kotlin
object HyperDrawerDefaults {
    val Width = 320.dp
    val HeaderPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
    val ItemPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
    val ItemMinHeight = 54.dp
    const val MaxWidthFraction = 0.88f
    const val MaxHeightFraction = 0.88f
    const val DrawerZIndex = 9f

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        supportingColor: Color = Color.Unspecified,
        selectedContainerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperDrawerColors

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## 最小用法

```kotlin
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR

HyperDrawer(
    open = open,
    onDismissRequest = { open = false },
    position = HyperDrawerPosition.Left,
    drawerContent = {
        HyperDrawerHeader(
            leadingContent = {
                Icon(
                    painter = painterResource(LucideR.drawable.lucide_ic_menu),
                    contentDescription = null
                )
            },
            headlineContent = { Text("HyperUI") },
            supportingContent = { Text("左侧抽屉") }
        )
        HyperDrawerItem(
            selected = selectedPageId == "home",
            onClick = { selectedPageId = "home" },
            leadingContent = {
                Icon(
                    painter = painterResource(LucideR.drawable.lucide_ic_house),
                    contentDescription = null
                )
            },
            headlineContent = { Text("首页") }
        )
    }
) {
    content()
}
```

## 约束

- 不存在 `scrimColor`，抽屉不渲染遮罩。
- `modifier` 作用于包含页面内容的抽屉根容器；`drawerModifier` 只作用于抽屉面板，使用 `width(...)` 定制左右抽屉、使用 `height(...)` 定制上下抽屉。
- 抽屉面板、Header 和 Item 的内部内容分别使用 `drawerContentModifier`、`contentModifier`。抽屉面板默认不添加任何内容间距；场景间距由调用方通过 `drawerContentModifier` 注入，Header/Item 只管理自身内容排版。
- 左右抽屉默认宽度为 `HyperDrawerDefaults.Width`；上下抽屉默认由内容自然撑高，不再固定为 `320.dp`，并受窗口最大占比保护。
- 四个方向默认都不避让 `safeDrawing`，因此面板内容可以延伸到状态栏或手势导航区域。需要避让时，由调用方在 `drawerContentModifier` 中显式添加 `windowInsetsPadding(...)`。`drawerContentScrollEnabled = true` 时，普通内容超过最大高度后由面板负责滚动。
- `drawerContent` 包含 `LazyColumn`、`HyperList` 等纵向滚动组件时，必须设置 `drawerContentScrollEnabled = false`，由内层列表独立负责滚动，避免嵌套滚动导致无限高度测量异常。
- `HyperDrawerItem` 默认最小高度由 `HyperDrawerDefaults.ItemMinHeight` 提供，其他尺寸通过 `modifier` 控制。
- 默认面板在浅色模式使用白色 `0.96f` alpha；深色模式直接使用当前 `MaterialTheme.colorScheme.background`，不再使用会发灰的深灰透明层。
- 深色默认描边同样使用页面背景色，因此不会出现灰色边界；通过 `HyperDrawerColors`、`HyperDrawerDefaults.colors(...)` 或 `border` 显式传入的颜色仍会保留。
- 抽屉面板和选中项不叠加玻璃高光或渐变。
- 抽屉打开与关闭均直接渲染或移除，不执行滑动、淡入淡出或透明度动画。
- Header/Item 不提供 `title`、`description`、`leadingIcon` 参数。
- `open`、选中项和路由由调用方持有。
- 默认描边来自 `HyperDrawerDefaults.border()`；如需无边框，传入 `border = null`。

<WasmPreview demo="drawer" title="HyperDrawer 交互预览" />
