# HyperTabBar

`HyperTabBar` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

<WasmPreview demo="tab-bar" title="HyperTabBar 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperTabBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    shape: Shape = HyperTabBarDefaults.Shape,
    topDivider: BorderStroke? = HyperTabBarDefaults.topDivider(),
    colors: HyperTabBarColors = HyperTabBarDefaults.colors(),
    type: String = "docked",
    floatingColors: HyperFloatingTabBarColors = HyperFloatingTabBarDefaults.colors(),
    content: @Composable RowScope.() -> Unit
)

@Composable
fun <T> HyperTabBar(
    items: List<T>,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemLayout: HyperTabBarItemLayout = HyperTabBarItemLayout.Equal,
    itemSelected: (T) -> Boolean = { false },
    itemSlotAlignment: Alignment = Alignment.Center,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    shape: Shape = HyperTabBarDefaults.Shape,
    topDivider: BorderStroke? = HyperTabBarDefaults.topDivider(),
    colors: HyperTabBarColors = HyperTabBarDefaults.colors(),
    type: String = "docked",
    floatingColors: HyperFloatingTabBarColors = HyperFloatingTabBarDefaults.colors(),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperTabBarItemScope.(item: T) -> Unit
)
```

## 最小用法

```kotlin
// 贴底样式（默认）
HyperTabBar(
    items = tabs,
    itemSelected = { it.id == selectedTabId },
    onItemClick = { selectedTabId = it.id }
) { item ->
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(item.icon, contentDescription = item.label)
        Text(item.label)
    }
}

// 悬浮玻璃胶囊样式
HyperTabBar(
    items = tabs,
    type = "floating",
    itemSelected = { it.id == selectedTabId },
    onItemClick = { selectedTabId = it.id }
) { item ->
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(item.icon, contentDescription = item.label, modifier = Modifier.size(20.dp))
        Text(item.label, fontSize = 11.sp)
    }
}
```

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- 公开 API 仅支持 Android 手机端；Preview 只是文档交互工具。
