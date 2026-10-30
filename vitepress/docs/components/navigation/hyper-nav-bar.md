---
description: 顶部透明、正文滚入时渐显渐变的页面导航栏。
---

# HyperNavBar

顶部透明、正文滚入时渐显渐变的页面导航栏，组织返回入口、标题、副标题和右侧操作。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="nav-bar" title="HyperNavBar 交互预览" />

## 导航形态

- type = "standard"：默认形态，保留 44dp 操作行、标题、返回和操作插槽；layout 选择 custom / backOnly / titleOnly / backWithTitle / more / edit。
- type = "simple"：只覆盖 HyperTheme.topBarInsets 的真实状态栏/挖孔高度，不生成操作行，不接受标题、操作回调或非默认 layout。

```kotlin
HyperNavBar(type = "simple", scrolledUnder = hostScrolledUnder)
HyperNavBar(type = "standard", layout = "backWithTitle", title = { HyperText("详情") })
```

simple 同样应放在全屏正文上方；首屏顶部避让属于滚动内容，不用固定 Spacer 或视口外 padding。两种形态都只做滚动渐变，不做背景模糊。

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperNavBar(layout = "backWithTitle", title = { HyperText("详情") }, onBackPressed = onBack)
```

</details>

## 使用约定

**样式约定**

透明导航操作层默认 44dp，水平留白 16dp，标题 16sp/600、行高 1.2、字距 -0.2sp；副标题 11sp、间隔 2dp。固定按钮 38dp、图标 20dp。顶部状态栏另计安全区。

正文滚入时以 160ms easeOutCubic 渐显 pageBackground 向下渐隐的渐变，返回顶部恢复透明。standard 范围为状态栏顶部至操作行底部；simple 只覆盖真实状态栏/挖孔高度。两者均不采样正文、不调用 Haze、不做像素模糊，不绘制分隔线；前景保持清晰，纯背景层透传正文手势。

顶部安全区位于背景层内部，只有操作行避让状态栏。Desktop/Wasm 预览通过内部作用域注入 52dp 模拟状态栏 Insets，导航栏与页面正文共用；不在整个导航外层添加顶部 Padding，正文也不重复增加模拟安全区。真机默认读取系统状态栏 Insets。

背景实现复用 [HyperScrollGradient](hyper-scroll-gradient.md)，只绘制渐变，无采样或模糊分支。

**状态归属**

通过 HyperNavBarPage 组合滚动正文，避免重复计算顶部安全区。固定类型仅使用标题和对应回调，不接受 subtitle；副标题及其他插槽使用 custom。

滚动容器持有自身状态，把同一个 ScrollState、LazyListState 或 LazyGridState 交给 HyperNavBarPage.scrollState 即可。库内作用域观察真实的 canScrollBackward，反向布局设置 reverseLayout = true 后读取 canScrollForward；恢复位置、程序滚动、数据缩减和返回顶部都会同步。页面不再声明导航滚动布尔值、derivedStateOf 或手势监听，也不累计位移推测列表位置。HyperNavBar 自动读取作用域，不需要逐页传 scrolledUnder。

作用域传递稳定的派生 State，由 Navbar 读取其值，滚动阈值变化不再使整个正文重组。逻辑页面隐藏或主列表被空状态替代时传 active = false；库停止采用该来源的滚动位置，保留原滚动状态，恢复活动后继续读取真实位置。不要用弹窗内部列表、文本横向滚动或视频时间轴驱动页面导航。

已有普通 Scaffold、固定搜索区或其他正式布局只需用 HyperNavBarScrollScope 包住导航与主视口的共同父级，不改它的 Scaffold、正文约束、padding、Insets、physics 或底栏。Compose 以主容器真实状态显式绑定，不扫描后代，也不由页面累加滚动手势。覆盖式页面继续使用已有 HyperNavBarPage；不能借监听整理转换页面布局或改变滚动范围。

HyperNavBarPage 叠放全屏正文与导航，测量包括附加搜索/筛选行的实际导航高度，将净空交给滚动内容。不要把 padding 放在视口外。已有布局可只用 HyperNavBarScrollScope 绑定主状态；不接管或释放该状态，不需要 HyperBackdrop。非 Compose 内容可显式传 scrolledUnder。

```kotlin
val scrollState = rememberLazyListState()
HyperNavBarPage(scrollState = scrollState, navBar = {
    HyperNavBar(
        layout = "titleOnly",
        title = { HyperText("详情") }
    )
}) { padding ->
    LazyColumn(state = scrollState, contentPadding = padding) { /* 正文 */ }
}
```

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperNavBar

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperNavBar(
    type: String = HyperNavBarDefaults.TypeStandard,
    layout: String = HyperNavBarDefaults.LayoutCustom,
    title: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    padding: PaddingValues = HyperNavBarDefaults.ContentPadding,
    spacing: Dp = HyperNavBarDefaults.ContentGap,
    titleSpacing: Dp = HyperNavBarDefaults.TitleGap,
    actionSpacing: Dp = HyperNavBarDefaults.ActionGap,
    centerTitle: Boolean = false,
    scrolledUnder: Boolean = false,
    onBackPressed: (() -> Unit)? = null,
    onMorePressed: (() -> Unit)? = null,
    onSavePressed: (() -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    leading: (@Composable RowScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    actions: List<@Composable () -> Unit> = emptyList(),
    child: (@Composable BoxScope.() -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| type | String | 否 | HyperNavBarDefaults.TypeStandard | standard 显示标准操作行；simple 只适配真实状态栏/挖孔，不生成标题、操作或 44dp 工具栏。 |
| layout | String | 否 | HyperNavBarDefaults.LayoutCustom | 组件的视觉或布局形态。 |
| title | (@Composable () -&gt; Unit)? | 否 | null | 组件标题文字或内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| padding | PaddingValues | 否 | HyperNavBarDefaults.ContentPadding | 导航栏内部留白。 |
| spacing | Dp | 否 | HyperNavBarDefaults.ContentGap | 导航栏各区域之间的间距。 |
| titleSpacing | Dp | 否 | HyperNavBarDefaults.TitleGap | 标题与其他区域的间距。 |
| actionSpacing | Dp | 否 | HyperNavBarDefaults.ActionGap | 导航操作项之间的间距。 |
| centerTitle | Boolean | 否 | false | 是否将标题在导航栏中居中。 |
| scrolledUnder | Boolean | 否 | false | 默认使用库内滚动作用域；true 强制显示材质，无作用域时可手动控制。 |
| onBackPressed | (() -&gt; Unit)? | 否 | null | 点击返回操作时通知调用方。 |
| onMorePressed | (() -&gt; Unit)? | 否 | null | 点击更多操作时通知调用方。 |
| onSavePressed | (() -&gt; Unit)? | 否 | null | 点击保存操作时通知调用方。 |
| subtitle | (@Composable () -&gt; Unit)? | 否 | null | 副标题或辅助说明。 |
| leading | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| trailing | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 内容末端的自定义区域。 |
| actions | List&lt;@Composable () -&gt; Unit&gt; | 否 | emptyList() | 导航栏操作项内容。 |
| child | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 组件主体或触发器内容。 |

### HyperNavBarPage

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperNavBarPage(
    navBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    bottomSafeArea: Boolean = true,
    scrollState: ScrollableState? = null,
    reverseLayout: Boolean = false,
    active: Boolean = true,
    content: @Composable BoxScope.(PaddingValues) -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| navBar | @Composable () -&gt; Unit | 是 | — | 页面顶部导航栏内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentPadding | PaddingValues | 否 | PaddingValues() | 主体内容的内部留白。 |
| bottomSafeArea | Boolean | 否 | true | 是否为底部系统安全区留白。 |
| scrollState | ScrollableState? | 否 | null | 主滚动容器已有状态；库自动监听并供 Navbar 读取，支持 ScrollState、LazyListState、LazyGridState。 |
| reverseLayout | Boolean | 否 | false | 反向主列表，改用 canScrollForward 判断内容是否经过导航。 |
| active | Boolean | 否 | true | 该主滚动来源是否活动；隐藏页或无主列表的空状态传 false，保留原滚动位置。 |
| content | @Composable BoxScope.(PaddingValues) -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

### HyperNavBarScrollScope

```kotlin
@Composable
fun HyperNavBarScrollScope(
    scrollState: ScrollableState,
    reverseLayout: Boolean = false,
    active: Boolean = true,
    content: @Composable () -> Unit
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| scrollState | ScrollableState | 是 | — | 唯一主滚动状态，库内观察；不接管其生命周期。 |
| reverseLayout | Boolean | 否 | false | 按反向列表读取滚动边界。 |
| active | Boolean | 否 | true | 只采用活动主视口的真实状态；暂停来源不改变控制器、布局或滚动范围。 |
| content | @Composable () -&gt; Unit | 是 | — | 导航与主滚动容器的共同父级内容；不需要正文采样源。 |

```kotlin
val listState = rememberLazyListState()
HyperNavBarScrollScope(scrollState = listState, active = items.isNotEmpty()) {
    // 同一主状态交给导航作用域与列表，净空只进入列表内容。
    HyperScaffold(topBar = { HyperNavBar(title = { HyperText("下载") }) }) { padding ->
        LazyColumn(state = listState, modifier = Modifier.fillMaxSize(), contentPadding = padding) {
            items(items) { item -> HyperText(item.title) }
        }
    }
}
```

### HyperNavBarBackButton

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperNavBarBackButton(
    onPressed: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    contentDescription: String = "返回"
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| onPressed | (() -&gt; Unit)? | 否 | null | 点击操作回调；为 null 时不能触发操作。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentDescription | String | 否 | &quot;返回&quot; | 无障碍内容描述。 |

## 顶部布局

根主题 useSystemSafeArea=false 不取消顶部交互保护。标准 HyperNavBar 内部读取窗口 Insets；自定义固定顶栏直接使用 Compose 的 windowInsetsPadding(HyperTheme.topBarInsets)，只作用于操作区，不给整页增加留白。HyperNavBarPage 的首屏净空应放进滚动内容。
