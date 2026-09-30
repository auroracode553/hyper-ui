# HyperNavBar

包名：`hyper_ui`。

<WasmPreview demo="nav-bar" title="HyperNavBar 交互预览" />

预览手机的时间、信号和电量由文档外壳绘制；Wasm 没有 Android 系统状态栏。示例的滚动视口覆盖整块屏幕，`HyperNavBar` 自行避让模拟状态栏，正文首屏使用 `HyperNavBarPage` 提供的内边距开始布局。上滚时正文可经过透明导航栏并进入状态栏背后。

## 公开签名与默认值

```kotlin
@Composable
fun HyperNavBar(
    titleContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    size: String = "default",
    padding: PaddingValues = PaddingValues(horizontal = 16.dp),
    spacing: Dp = 8.dp,
    titleSpacing: Dp = 2.dp,
    actionSpacing: Dp = 4.dp,
    safeArea: Boolean = true,
    centerTitle: Boolean = false,
    subtitleContent: (@Composable () -> Unit)? = null,
    navigationContent: (@Composable RowScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
    actions: List<@Composable () -> Unit> = emptyList(),
    child: (@Composable BoxScope.() -> Unit)? = null
)

@Composable
fun HyperNavBarPage(
    navBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    navBarSize: String = "default",
    safeArea: Boolean = true,
    bottomSafeArea: Boolean = true,
    content: @Composable BoxScope.(PaddingValues) -> Unit
)
```

默认尺寸为 `default`（44.dp），支持 `small`、`large`；默认左右内边距为 16.dp。`child` 与其他插槽互斥，`trailingContent` 与 `actions` 二选一。

## 最小用法

需要把首屏净空自动交给滚动内容时使用 `HyperNavBarPage`：

```kotlin
HyperNavBarPage(
    navBar = { HyperNavBar(titleContent = { Text("详情") }) },
    navBarSize = "default",
    safeArea = true,
    bottomSafeArea = true
) { immersivePadding ->
    LazyColumn(contentPadding = immersivePadding) { /* 内容 */ }
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
