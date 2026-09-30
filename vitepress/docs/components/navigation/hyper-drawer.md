# HyperDrawer

包名：`hyper_ui`。

<WasmPreview demo="drawer" title="HyperDrawer 交互预览" />

抽屉直接占用预览手机的内容视口，打开后可查看完整面板与页面内容。

## 公开签名与默认值

```kotlin
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
    dismissOnClickOutside: Boolean = true,
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

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
