# HyperNavBar

包名：`hyper_ui`。

<WasmPreview demo="nav-bar" title="HyperNavBar 返回布局交互预览" />

导航栏默认完全透明，不绘制背景、模糊、描边或阴影；顶部状态栏避让始终开启，不再提供 `safeArea` 参数。要让滚动正文出现在导航栏后方，使用 `HyperNavBarPage` 叠加页面，而不是把导航栏和正文上下排列。

预览手机的时间、信号和电量由文档外壳绘制；Wasm 没有 Android 系统状态栏，Preview 仅额外模拟顶部间距。预览中可切换仅返回、仅标题、返回与标题，以及带更多或保存操作的布局。仅标题布局不显示返回按钮；只有更多与编辑布局显示右侧按钮。点击顶部操作会在正文显示反馈。示例的滚动视口覆盖整块屏幕，正文首屏使用 `HyperNavBarPage` 提供的内边距开始布局。上滚时正文可经过透明导航栏并进入状态栏背后。

## 公开签名与默认值

```kotlin
@Composable
fun HyperNavBar(
    type: String = HyperNavBarDefaults.TypeCustom,
    titleContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    size: String = "default",
    padding: PaddingValues = HyperNavBarDefaults.ContentPadding,
    spacing: Dp = HyperNavBarDefaults.ContentGap,
    titleSpacing: Dp = HyperNavBarDefaults.TitleGap,
    actionSpacing: Dp = HyperNavBarDefaults.ActionGap,
    centerTitle: Boolean = false,
    onBackClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
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

@Composable
fun HyperNavBarBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "返回"
)
```

## Props（参数）

### HyperNavBar

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| type | String | 否 | HyperNavBarDefaults.TypeCustom | 固定布局类型：custom、backOnly、titleOnly、backWithTitle、more、edit。 |
| titleContent | (@Composable () -&gt; Unit)? | 否 | null | 导航栏标题内容；固定标题类型使用此插槽。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| size | String | 否 | &quot;default&quot; | 组件尺寸档位。 |
| padding | PaddingValues | 否 | HyperNavBarDefaults.ContentPadding | 导航栏内部留白。 |
| spacing | Dp | 否 | HyperNavBarDefaults.ContentGap | 导航栏各区域之间的间距。 |
| titleSpacing | Dp | 否 | HyperNavBarDefaults.TitleGap | 标题与其他区域的间距。 |
| actionSpacing | Dp | 否 | HyperNavBarDefaults.ActionGap | 导航操作项之间的间距。 |
| centerTitle | Boolean | 否 | false | custom 类型是否将标题在导航栏中居中。 |
| onBackClick | () -&gt; Unit | 否 | {} | 固定返回类型的点击回调。 |
| onMoreClick | () -&gt; Unit | 否 | {} | `more` 类型右侧更多操作的点击回调。 |
| onSaveClick | () -&gt; Unit | 否 | {} | `edit` 类型右侧保存操作的点击回调。 |
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

### HyperNavBarBackButton

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| onClick | () -&gt; Unit | 是 | — | 请求返回，由页面调用方处理导航。 |
| modifier | Modifier | 否 | Modifier | 返回按钮的外部修饰符。 |
| contentDescription | String | 否 | &quot;返回&quot; | 无障碍操作名称。 |


默认 `type` 为 `custom`。固定类型只接受标题内容和对应回调；需要完整插槽、居中标题或自定义操作时使用 `type = HyperNavBarDefaults.TypeCustom`。默认尺寸为 `default`（44.dp），支持 `small`、`large`；默认左右内边距为 16.dp。`child` 与其他插槽互斥，`trailingContent` 与 `actions` 二选一。

## 最小用法

需要把首屏净空自动交给滚动内容时使用 `HyperNavBarPage`：

```kotlin
HyperNavBarPage(
    navBar = {
        HyperNavBar(
            type = HyperNavBarDefaults.TypeBackWithTitle,
            titleContent = { HyperText("详情") },
            onBackClick = onBack
        )
    }
) { immersivePadding ->
    LazyColumn(contentPadding = immersivePadding) { /* 内容 */ }
}
```

## 常见返回布局

`HyperNavBarBackButton` 使用 `<` 形矢量图标，自动适应 RTL 方向。调用方通过 `onClick` 接入页面导航；按钮本身不保存导航状态。

```kotlin
// 仅返回：不传标题内容。
HyperNavBar(
    type = HyperNavBarDefaults.TypeBackOnly,
    onBackClick = onBack
)

// 仅标题：不传返回回调。
HyperNavBar(
    type = HyperNavBarDefaults.TypeTitleOnly,
    titleContent = { HyperText("今日灵感") }
)

// 返回与标题：固定布局类型不需要手写图标插槽。
HyperNavBar(
    type = HyperNavBarDefaults.TypeBackWithTitle,
    titleContent = { HyperText("今日灵感") },
    onBackClick = onBack
)

// 更多操作：右侧标准按钮由 type 生成。
HyperNavBar(
    type = HyperNavBarDefaults.TypeMore,
    titleContent = { HyperText("消息") },
    onBackClick = onBack,
    onMoreClick = onMore
)

// 编辑页：保存动作由调用方处理。
HyperNavBar(
    type = HyperNavBarDefaults.TypeEdit,
    titleContent = { HyperText("编辑资料") },
    onBackClick = onBack,
    onSaveClick = onSave
)

// 自定义：固定类型之外的布局统一使用 custom。
HyperNavBar(
    type = HyperNavBarDefaults.TypeCustom,
    titleContent = { HyperText("创作空间") },
    navigationContent = { HyperNavBarBackButton(onClick = onBack) },
    trailingContent = { HyperText("分享") }
)
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
- `HyperNavBar` 固定避让顶部状态栏；`HyperNavBarPage` 固定将相同顶部安全区与导航栏高度计入首屏正文内边距。调用方不要在页面根容器重复添加顶部安全区。
- `HyperNavBarPage` 只提供正文的初始内边距，不缩小正文视口。使用 `LazyColumn(contentPadding = ...)`，或将内边距放在 `verticalScroll` 修饰符之后，让它随正文一起滚走。
- 透明意味着显示后方内容；首屏只有页面底色时，导航栏区域自然呈现页面底色。不要给导航栏或其外层另加背景、模糊或阴影来模拟透明。
- 默认 `bottomSafeArea = true` 仅为正文末尾增加可滚动的底部系统安全区避让，不计入软键盘高度；键盘避让由页面宿主使用 `imePadding` 等方式处理。页面背景和滚动视口仍延伸到手势条区域。
