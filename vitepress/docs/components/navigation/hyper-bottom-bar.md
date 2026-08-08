# HyperBottomBar

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/navigation/HyperBottomBar.kt`
- 预览：`bottom-bar`

`HyperBottomBar` 是泛型底部栏容器。组件负责底栏面板、横向布局、点击区域和选中/未选中内容色；具体图标、文字、徽标或布局由 item slot 渲染。

## 公开签名

```kotlin
enum class HyperBottomBarItemLayout { Equal, Packed }

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
    border: BorderStroke? = null,
    colors: HyperBottomBarColors = HyperBottomBarDefaults.colors(),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperBottomBarItemScope.(item: T) -> Unit
)
```

## 最小用法

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
- 页面切换和导航由调用方在 `onItemClick` 中完成。
- 单项可用状态由 `itemEnabled` 决定，全局禁用仍使用 `enabled`。
- `HyperBottomBarItemScope` 暴露 `selected` 与 `enabled`，slot 可据此渲染字体、徽标或动画。

<WasmPreview demo="bottom-bar" title="HyperBottomBar 交互预览" />
