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

## Props（参数）

### HyperDrawer

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| open | Boolean | 是 | — | 抽屉是否打开，由调用方持有。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| drawerModifier | Modifier | 否 | Modifier | 抽屉面板的布局修饰符。 |
| position | HyperDrawerPosition | 否 | HyperDrawerPosition.Left | 抽屉从哪一侧进入。 |
| defaultSetPadding | Boolean | 否 | true | 是否使用抽屉默认留白。 |
| drawerContentModifier | Modifier | 否 | Modifier | 抽屉内容区域的布局修饰符。 |
| drawerContentScrollEnabled | Boolean | 否 | true | 抽屉内容是否允许滚动。 |
| colors | HyperDrawerColors | 否 | HyperDrawerDefaults.colors() | 组件各状态的颜色配置。 |
| dismissOnClickOutside | Boolean | 否 | true | 点击外部时是否请求关闭。 |
| drawerContent | @Composable ColumnScope.() -&gt; Unit | 是 | — | 抽屉中的内容。 |
| content | @Composable BoxScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

### HyperDrawerHeader

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| headlineContent | @Composable ColumnScope.() -&gt; Unit | 是 | — | 列表行主标题内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperDrawerDefaults.HeaderPadding) | 内部内容区域的布局修饰符。 |
| leadingContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| supportingContent | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 标题或输入框下方的辅助内容。 |

### HyperDrawerItem

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| headlineContent | @Composable ColumnScope.() -&gt; Unit | 是 | — | 列表行主标题内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperDrawerDefaults.ItemPadding) | 内部内容区域的布局修饰符。 |
| selected | Boolean | 否 | false | 由调用方持有的选中状态。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| dividerVisible | Boolean | 否 | false | 是否显示行分隔线。 |
| colors | HyperDrawerColors | 否 | HyperDrawerDefaults.colors() | 组件各状态的颜色配置。 |
| onClick | (() -&gt; Unit)? | 否 | null | 用户点击时执行的回调。 |
| leadingContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| supportingContent | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 标题或输入框下方的辅助内容。 |
| trailingContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 内容末端的自定义区域。 |

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
