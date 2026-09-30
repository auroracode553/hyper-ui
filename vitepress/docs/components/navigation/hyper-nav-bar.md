# HyperNavBar

`HyperNavBar` 位于 `hyper_ui` 包，按 Flutter `HyNavBar` 的透明导航栏行为实现。

<WasmPreview demo="nav-bar" title="HyperNavBar 交互预览" />

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
```

导航栏默认尺寸为 `default`（44.dp），也支持 `small` 与 `large`，不含顶部安全区；默认左右内边距为 16.dp。它不绘制背景、模糊、描边或阴影。`child` 接管整行布局，不能和其他插槽同时使用。`trailingContent` 与 `actions` 二选一。

## 页面容器

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

## 全面屏滚动

滚动页面由调用方使用 `Box` 或 `Scaffold` 管理：把顶部安全区与 44.dp 导航栏高度加入滚动内容的首屏 `contentPadding`，再将 `HyperNavBar` 作为顶部叠加层。这样首屏内容会避让导航栏，滚动后可以进入透明导航栏和状态栏后方。

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；组件不持有导航、网络或业务状态。
- 返回按钮由调用方通过 `navigationContent` 注入；Compose Multiplatform 组件不直接读取宿主导航栈。
- 标题默认使用 16sp、半粗体；副标题默认使用 11sp。
- Android 原生系统栏配置由宿主处理，Preview 使用跨平台示例。
