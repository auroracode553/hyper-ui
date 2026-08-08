# HyperBottomBar

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/navigation/HyperBottomBar.kt`
- 预览：`bottom-bar`

`HyperBottomBar` 是底部栏容器，默认带 1dp 轻描边。组件负责底栏面板与横向布局；调用方可以直接传入完整内容 slot，也可以使用泛型 items 入口让组件统一处理单项点击、选中/未选中内容色和禁用状态。

## 公开签名

```kotlin
enum class HyperBottomBarItemLayout { Equal, Packed }

@Composable
fun HyperBottomBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = HyperBottomBarDefaults.Height,
    contentHeight: Dp = HyperBottomBarDefaults.ContentHeight,
    contentPadding: PaddingValues = HyperBottomBarDefaults.ContentPadding,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    shape: Shape = HyperBottomBarDefaults.Shape,
    border: BorderStroke? = HyperBottomBarDefaults.border(),
    colors: HyperBottomBarColors = HyperBottomBarDefaults.colors(),
    content: @Composable RowScope.() -> Unit
)

@Composable
fun <T> HyperBottomBar(
    items: List<T>,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemLayout: HyperBottomBarItemLayout = HyperBottomBarItemLayout.Equal,
    itemSelected: (T) -> Boolean = { false },
    height: Dp = HyperBottomBarDefaults.Height,
    contentHeight: Dp = HyperBottomBarDefaults.ContentHeight,
    contentPadding: PaddingValues = HyperBottomBarDefaults.ContentPadding,
    itemWidth: Dp = HyperBottomBarDefaults.ItemWidth,
    itemSlotAlignment: Alignment = Alignment.Center,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    shape: Shape = HyperBottomBarDefaults.Shape,
    border: BorderStroke? = HyperBottomBarDefaults.border(),
    colors: HyperBottomBarColors = HyperBottomBarDefaults.colors(),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperBottomBarItemScope.(item: T) -> Unit
)
```

## 关键公开类型

```kotlin
object HyperBottomBarDefaults {
    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## 最小用法

完整内容 slot：

```kotlin
HyperBottomBar(
    contentPadding = PaddingValues(horizontal = 16.dp)
) {
    HyperIconButton(onClick = onBack) {
        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
    }
    Spacer(modifier = Modifier.weight(1f))
    HyperIconButton(onClick = onMore) {
        Icon(Icons.Default.MoreVert, contentDescription = "更多")
    }
}
```

泛型 items：

```kotlin
HyperBottomBar(
    items = bottomItems,
    itemSelected = { it.id == selectedItemId },
    onItemClick = { selectedItemId = it.id }
) { item ->
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(item.icon, contentDescription = item.label)
        Text(item.label)
    }
}
```

## 约束

- 不存在 `HyperBottomBarItem`、`selectedItemId`、`HyperBottomBarConfig`。
- 泛型 items 入口的页面切换和导航由调用方在 `onItemClick` 中完成。
- 完整内容 slot 只提供底栏外壳和默认内容色；点击、选中、禁用与内部布局由调用方自行组合。
- 单项可用状态由 `itemEnabled` 决定，全局禁用仍使用 `enabled`。
- `HyperBottomBarItemScope` 暴露 `selected` 与 `enabled`，slot 可据此渲染字体、徽标或动画。
- 默认描边来自 `HyperBottomBarDefaults.border()`，内部使用 `HyperColors.panelBorder`；如需无边框，传入 `border = null`。

<WasmPreview demo="bottom-bar" title="HyperBottomBar 交互预览" />
