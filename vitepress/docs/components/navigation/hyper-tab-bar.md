# HyperTabBar

包名：`hyper_ui`。

## 贴底预览

<WasmPreview demo="tab-bar-docked" title="HyperTabBar 贴底预览" />

## 悬浮胶囊预览

<WasmPreview demo="tab-bar-floating" title="HyperTabBar 悬浮胶囊预览" />

两个预览均可点击标签切换选中项；悬浮胶囊还可按住并拖动标签，查看指示托盘的跟手与吸附效果。
贴底预览的底栏背景延伸至手机底边，标签与手势条之间只保留少量间距；悬浮胶囊预览保持底部间距。示例不再绘制内层手机框。

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

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
