# HyperNavBar

包名：`hyper_ui`。

<WasmPreview demo="nav-bar" title="HyperNavBar 交互预览" />

导航栏默认完全透明，不绘制背景、模糊、描边或阴影；顶部状态栏避让始终开启，不再提供 `safeArea` 参数。要让滚动正文出现在导航栏后方，使用 `HyperNavBarPage` 叠加页面，而不是把导航栏和正文上下排列。

预览手机的时间、信号和电量由文档外壳绘制；Wasm 没有 Android 系统状态栏，Preview 仅额外模拟顶部间距。示例的滚动视口覆盖整块屏幕，正文首屏使用 `HyperNavBarPage` 提供的内边距开始布局。上滚时正文可经过透明导航栏并进入状态栏背后。

## 公开签名与默认值

```kotlin
@Composable
fun HyperNavBar(
    titleContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    size: String = "default",
    padding: PaddingValues = HyperNavBarDefaults.ContentPadding,
    spacing: Dp = HyperNavBarDefaults.ContentGap,
    titleSpacing: Dp = HyperNavBarDefaults.TitleGap,
    actionSpacing: Dp = HyperNavBarDefaults.ActionGap,
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
    bottomSafeArea: Boolean = true,
    content: @Composable BoxScope.(PaddingValues) -> Unit
)
```

## Props（参数）

### HyperNavBar

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| titleContent | (@Composable () -&gt; Unit)? | 否 | null | 导航栏标题内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| size | String | 否 | &quot;default&quot; | 组件尺寸档位。 |
| padding | PaddingValues | 否 | HyperNavBarDefaults.ContentPadding | 导航栏内部留白。 |
| spacing | Dp | 否 | HyperNavBarDefaults.ContentGap | 导航栏各区域之间的间距。 |
| titleSpacing | Dp | 否 | HyperNavBarDefaults.TitleGap | 标题与其他区域的间距。 |
| actionSpacing | Dp | 否 | HyperNavBarDefaults.ActionGap | 导航操作项之间的间距。 |
| centerTitle | Boolean | 否 | false | 是否将标题在导航栏中居中。 |
| subtitleContent | (@Composable () -&gt; Unit)? | 否 | null | 导航栏副标题内容。 |
| navigationContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 导航栏左侧导航内容。 |
| trailingContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 内容末端的自定义区域。 |
| actions | List&lt;@Composable () -&gt; Unit&gt; | 否 | emptyList() | 导航栏操作项内容。 |
| child | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 导航栏附加子内容。 |

### HyperNavBarPage

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| navBar | @Composable () -&gt; Unit | 是 | — | 页面顶部导航栏内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentPadding | PaddingValues | 否 | PaddingValues() | 主体内容的内部留白。 |
| navBarSize | String | 否 | &quot;default&quot; | 页面导航栏的尺寸档位。 |
| bottomSafeArea | Boolean | 否 | true | 是否为底部系统安全区留白。 |
| content | @Composable BoxScope.(PaddingValues) -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |


默认尺寸为 `default`（44.dp），支持 `small`、`large`；默认左右内边距为 16.dp。`child` 与其他插槽互斥，`trailingContent` 与 `actions` 二选一。

## 最小用法

需要把首屏净空自动交给滚动内容时使用 `HyperNavBarPage`：

```kotlin
HyperNavBarPage(
    navBar = { HyperNavBar(titleContent = { HyperText("详情") }) }
) { immersivePadding ->
    LazyColumn(contentPadding = immersivePadding) { /* 内容 */ }
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
- `HyperNavBar` 固定避让顶部状态栏；`HyperNavBarPage` 固定将相同顶部安全区与导航栏高度计入首屏正文内边距。调用方不要在页面根容器重复添加顶部安全区。
- `HyperNavBarPage` 只提供正文的初始内边距，不缩小正文视口。使用 `LazyColumn(contentPadding = ...)`，或将内边距放在 `verticalScroll` 修饰符之后，让它随正文一起滚走。
- 透明意味着显示后方内容；首屏只有页面底色时，导航栏区域自然呈现页面底色。不要给导航栏或其外层另加背景、模糊或阴影来模拟透明。
- 默认 `bottomSafeArea = true` 仅为正文末尾增加可滚动的底部系统安全区避让，不计入软键盘高度；键盘避让由页面宿主使用 `imePadding` 等方式处理。页面背景和滚动视口仍延伸到手势条区域。
