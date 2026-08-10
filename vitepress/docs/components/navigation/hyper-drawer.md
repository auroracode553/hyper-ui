# HyperDrawer

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/drawer/HyperDrawer.kt`
- 预览：`drawer`

`HyperDrawer` 是四方向抽屉容器，无遮罩。抽屉面板使用不透明实色卡片背景和实色轻描边，不再叠加玻璃高光；打开与关闭只做滑入滑出动画，不再改变面板透明度。`HyperDrawerHeader` 与 `HyperDrawerItem` 都采用 slot-first API。

## 公开签名

```kotlin
enum class HyperDrawerPosition { Left, Right, Top, Bottom }

@Composable
fun HyperDrawer(
    open: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    drawerModifier: Modifier = Modifier,
    drawerContentModifier: Modifier = Modifier.padding(HyperDrawerDefaults.ContentPadding),
    position: HyperDrawerPosition = HyperDrawerPosition.Left,
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
    drawerModifier = Modifier.width(320.dp),
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
- 抽屉面板、Header 和 Item 的内部内容分别使用 `drawerContentModifier`、`contentModifier`；默认 Modifier 携带原有 PaddingValues，不再暴露 `contentPadding` 参数。
- 默认面板宽高由 `HyperDrawerDefaults.Width`、`Height` 提供，且仍受窗口最大占比保护；不再提供 `drawerWidth`、`drawerHeight` 参数。
- `HyperDrawerItem` 默认最小高度由 `HyperDrawerDefaults.ItemMinHeight` 提供，其他尺寸通过 `modifier` 控制。
- 默认面板背景使用不透明的 `HyperColors.cardContainer`；通过 `HyperDrawerColors` 或 `HyperDrawerDefaults.colors(...)` 传入含 alpha 的容器色时，会先与页面背景合成为实色。
- 抽屉进出场只使用滑动动画，不使用淡入淡出；四个方向的面板均全程不透明。
- Header/Item 不提供 `title`、`description`、`leadingIcon` 参数。
- `open`、选中项和路由由调用方持有。
- 默认描边来自 `HyperDrawerDefaults.border()`，使用合成后的实色轻描边；如需无边框，传入 `border = null`。

<WasmPreview demo="drawer" title="HyperDrawer 交互预览" />
