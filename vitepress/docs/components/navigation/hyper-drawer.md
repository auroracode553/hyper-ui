# HyperDrawer

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/drawer/HyperDrawer.kt`
- 预览：`drawer`

`HyperDrawer` 是四方向抽屉容器，无遮罩。`HyperDrawerHeader` 与 `HyperDrawerItem` 都采用 slot-first API。

## 公开签名

```kotlin
enum class HyperDrawerPosition { Left, Right, Top, Bottom }

@Composable
fun HyperDrawer(
    open: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    position: HyperDrawerPosition = HyperDrawerPosition.Left,
    drawerWidth: Dp = HyperDrawerDefaults.Width,
    drawerHeight: Dp = HyperDrawerDefaults.Height,
    contentPadding: PaddingValues = HyperDrawerDefaults.ContentPadding,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    dismissOnClickOutside: Boolean = false,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
)

@Composable
fun HyperDrawerHeader(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = HyperDrawerDefaults.HeaderPadding,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    headlineContent: @Composable ColumnScope.() -> Unit,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null
)

@Composable
fun HyperDrawerItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    dividerVisible: Boolean = false,
    minHeight: Dp = HyperDrawerDefaults.ItemMinHeight,
    contentPadding: PaddingValues = HyperDrawerDefaults.ItemPadding,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    headlineContent: @Composable ColumnScope.() -> Unit,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
)
```

## 最小用法

```kotlin
HyperDrawer(
    open = open,
    onDismissRequest = { open = false },
    position = HyperDrawerPosition.Left,
    drawerContent = {
        HyperDrawerHeader(
            leadingContent = { Icon(Icons.Default.Menu, null) },
            headlineContent = { Text("HyperUI") },
            supportingContent = { Text("左侧抽屉") }
        )
        HyperDrawerItem(
            selected = selectedPageId == "home",
            onClick = { selectedPageId = "home" },
            leadingContent = { Icon(Icons.Default.Home, null) },
            headlineContent = { Text("首页") }
        )
    }
) {
    content()
}
```

## 约束

- 不存在 `scrimColor`，抽屉不渲染遮罩。
- Header/Item 不提供 `title`、`description`、`leadingIcon` 参数。
- `open`、选中项和路由由调用方持有。

<WasmPreview demo="drawer" title="HyperDrawer 交互预览" />
