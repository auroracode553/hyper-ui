# HyperDrawer

`HyperDrawer` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

<WasmPreview demo="drawer" title="HyperDrawer 交互预览" />

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

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- 公开 API 仅支持 Android 手机端；Preview 只是文档交互工具。
