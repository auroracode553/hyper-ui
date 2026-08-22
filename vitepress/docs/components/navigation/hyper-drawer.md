# HyperDrawer

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/drawer/HyperDrawer.kt`
- 预览：`drawer`

`HyperDrawer` 是四方向结构玻璃抽屉，无遮罩。面板使用 `HyperColors.cardContainer` 不透明基底，并通过公共 `HyperSurfaceDepth` 的 1dp 低对比度主题描边和单层 5dp 阴影建立空间关系；内部未选中项不重复铺底，选中项只增加轻量主题染色。`open` 直接控制面板是否渲染，不执行动画。

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
    defaultSetPadding: Boolean = true,
    drawerContentModifier: Modifier = Modifier,
    drawerContentScrollEnabled: Boolean = true,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    dismissOnClickOutside: Boolean = false,
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
    val Elevation = 5.dp
    val SelectedItemElevation = 1.dp
    val HeaderPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
    val ItemPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
    val ItemMinHeight = 54.dp
    const val MaxWidthFraction = 0.88f
    const val MaxHeightFraction = 0.88f
    const val DrawerZIndex = 9f

    fun contentPadding(position: HyperDrawerPosition): PaddingValues

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
- `defaultSetPadding = true` 时，抽屉按方向应用 `HyperDrawerDefaults.contentPadding(position)` 并避让相邻 `safeDrawing` 系统栏；这是默认行为。
- `defaultSetPadding = false` 时，不注入上述默认 Padding 或系统栏避让，内容可以延伸到完整面板范围。
- `drawerContentModifier` 不负责开关默认策略，而是在默认策略之后追加调用方布局。Header 和 Item 的 `contentModifier` 只管理各自内容排版。
- 左右抽屉默认宽度为 `HyperDrawerDefaults.Width`；上下抽屉默认由内容自然撑高，不再固定为 `320.dp`，并受窗口最大占比保护。
- `drawerContentScrollEnabled = true` 时，普通内容超过最大高度后由面板负责滚动。
- `drawerContent` 包含 `LazyColumn`、`HyperList` 等纵向滚动组件时，必须设置 `drawerContentScrollEnabled = false`，由内层列表独立负责滚动，避免嵌套滚动导致无限高度测量异常。
- `HyperDrawerItem` 默认最小高度由 `HyperDrawerDefaults.ItemMinHeight` 提供，其他尺寸通过 `modifier` 控制。
- 默认面板使用 `HyperColors.cardContainer` 的不透明材质；自定义 `containerColor` 若带 alpha，会与 `HyperColors.pageBackground` 合成为不透明颜色。
- 面板不提供公开描边入口；内部公共深度层按主题生成 1dp 低对比度描边，并与单层 `5.dp` 投影共同建立厚度。
- 未选中 Item 透明显示在同一面板内，选中 Item 使用主题色半透明层和 `1.dp` 轻抬升。
- 抽屉打开与关闭均直接渲染或移除，不执行过渡动画。
- Header/Item 不提供 `title`、`description`、`leadingIcon` 参数。
- `open`、选中项和路由由调用方持有。

<WasmPreview demo="drawer" title="HyperDrawer 交互预览" />
