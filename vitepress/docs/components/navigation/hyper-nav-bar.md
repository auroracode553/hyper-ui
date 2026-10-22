---
description: 顶部透明、正文滚入时渐显毛玻璃的页面导航栏。
---

# HyperNavBar

顶部透明、正文滚入时渐显毛玻璃的页面导航栏，组织返回入口、标题、副标题和右侧操作。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="nav-bar" title="HyperNavBar 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperNavBar(type = "backWithTitle", title = { HyperText("详情") }, onBackPressed = onBack)
```

</details>

## 使用约定

**样式约定**

透明导航操作层默认 44dp，水平留白 16dp，标题 16sp/600、行高 1.2、字距 -0.2sp；副标题 11sp、间隔 2dp。固定按钮 38dp、图标 20dp。顶部状态栏另计安全区。

scrolledUnder 为 true 时，组件用 160ms 过渡显示 20dp 背景模糊和主题 pageBackground 画布色，范围为状态栏顶部至导航底部；蒙版将模糊采样与底色一起向下淡出至完全透明，不绘制底部分隔线。材质层不占位、不拦截触摸，导航底部之外不绘制；为 false 时恢复透明。文字与按钮保持清晰，不参与背景采样。

顶部安全区位于背景层内部，只有操作行避让状态栏。Desktop/Wasm 预览通过内部作用域注入 52dp 模拟状态栏 Insets，导航栏与页面正文共用；不在整个导航外层添加顶部 Padding，正文也不重复增加模拟安全区。真机默认读取系统状态栏 Insets。

**状态归属**

通过 HyperNavBarPage 组合滚动正文，避免重复计算顶部安全区。固定类型仅使用标题和对应回调，不接受 subtitle；副标题及其他插槽使用 custom。

滚动状态由页面管理：LazyColumn 使用 rememberLazyListState，根据 canScrollBackward 设置 scrolledUnder；反向列表使用 canScrollForward。HyperNavBarPage 负责把正文注册为背景采样源。独立使用 HyperNavBar 时需要由 HyperBackdrop 提供背景源；没有采样源时仍显示渐变着色，不模糊导航自身。

```kotlin
val scrollState = rememberLazyListState()
HyperNavBarPage(navBar = {
    HyperNavBar(
        type = "titleOnly",
        title = { HyperText("详情") },
        scrolledUnder = scrollState.canScrollBackward
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
    type: String = HyperNavBarDefaults.TypeCustom,
    title: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    size: String = "default",
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
| type | String | 否 | HyperNavBarDefaults.TypeCustom | 组件的视觉或布局形态。 |
| title | (@Composable () -&gt; Unit)? | 否 | null | 组件标题文字或内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| size | String | 否 | &quot;default&quot; | 组件尺寸档位。 |
| padding | PaddingValues | 否 | HyperNavBarDefaults.ContentPadding | 导航栏内部留白。 |
| spacing | Dp | 否 | HyperNavBarDefaults.ContentGap | 导航栏各区域之间的间距。 |
| titleSpacing | Dp | 否 | HyperNavBarDefaults.TitleGap | 标题与其他区域的间距。 |
| actionSpacing | Dp | 否 | HyperNavBarDefaults.ActionGap | 导航操作项之间的间距。 |
| centerTitle | Boolean | 否 | false | 是否将标题在导航栏中居中。 |
| scrolledUnder | Boolean | 否 | false | 正文是否滚入导航区域，驱动透明与渐变毛玻璃的切换。 |
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
    navBarSize: String = "default",
    bottomSafeArea: Boolean = true,
    content: @Composable BoxScope.(PaddingValues) -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| navBar | @Composable () -&gt; Unit | 是 | — | 页面顶部导航栏内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentPadding | PaddingValues | 否 | PaddingValues() | 主体内容的内部留白。 |
| navBarSize | String | 否 | &quot;default&quot; | 页面导航栏的尺寸档位。 |
| bottomSafeArea | Boolean | 否 | true | 是否为底部系统安全区留白。 |
| content | @Composable BoxScope.(PaddingValues) -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

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
