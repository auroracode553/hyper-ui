---
description: 不依赖 Material 的页面布局宿主，按实际栏高提供正文净空。
---

# HyperScaffold

Compose 基础布局实现的页面宿主。统一页面画布、上下栏、正文和悬浮操作；导航的滚动监听与模糊继续由 HyperNavBarPage / HyperNavBarScrollScope 管理。

公开函数位于 hyper_ui 包，使用 import hyper_ui.HyperScaffold 引入。

<WasmPreview demo="scaffold" title="页面布局宿主" />

## 使用方式

```kotlin
HyperScaffold(bottomBar = { BottomActions() }) { contentPadding ->
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = contentPadding) {
        items(rows) { row -> RowContent(row) }
    }
}
```

正文视口保持全屏，contentPadding 只用于 LazyColumn/LazyGrid 的内容内边距；普通滚动列使用 verticalScroll(scrollState).padding(contentPadding)。禁止在滚动容器外设置 Modifier.padding(contentPadding)、固定高度占位或扣减视口高度。上栏与下栏按真实测量高度参与净空；缺少对应栏时提供系统安全区。默认 useSystemSafeArea=false，不为整页补系统安全区；局部控件按需避让一次。悬浮 HyperTabBar 覆盖正文、周围透明，末尾净空采用宿主对整个栏及外边距的实测高度，不能以默认栏高估算。悬浮操作位于下栏或系统安全区上方，距逻辑末端和底部各 16dp。

宿主需要有界的页面宽高；不管理路由、滚动位置或业务状态。Flutter 使用其框架页面宿主，此处是 Android Compose 的基础布局能力。

## 公开 API

```kotlin
@Composable
fun HyperScaffold(
    modifier: Modifier = Modifier,
    containerColor: Color = HyperColors.pageBackground,
    contentWindowInsets: WindowInsets = HyperTheme.systemSafeAreaInsets,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 页面布局约束与外部修饰。 |
| containerColor | Color | 否 | HyperColors.pageBackground | 贯穿页面的画布颜色。 |
| contentWindowInsets | WindowInsets | 否 | HyperTheme.systemSafeAreaInsets（默认零） | 根布局可选系统安全区；顶部操作保护由导航负责。 |
| topBar | Composable () -> Unit | 否 | 空 | 顶部内容，自行处理顶部安全区。 |
| bottomBar | Composable () -> Unit | 否 | 空 | 底部内容，自行处理底部安全区。 |
| floatingActionButton | Composable () -> Unit | 否 | 空 | 逻辑末端的悬浮动作插槽。 |
| content | Composable (PaddingValues) -> Unit | 是 | — | 正文及实际测量得到的净空。 |

根 contentWindowInsets 默认读取 HyperTheme.systemSafeAreaInsets（默认零）。顶部操作使用 HyperNavBar 或现有顶部操作容器 保护一次；不在 Scaffold 根内容另加安全区。覆盖式滚动正文使用 HyperNavBarPage 的测量净空。
