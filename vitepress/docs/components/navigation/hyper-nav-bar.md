---
description: 透明页面导航栏，组织返回入口、标题、副标题和右侧操作。
---

# HyperNavBar

透明页面导航栏，组织返回入口、标题、副标题和右侧操作。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="nav-bar" title="HyperNavBar 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperNavBar(type = "backWithTitle", titleContent = { HyperText("详情") }, onBackClick = onBack)
```

</details>

## 使用约定

**样式约定**

透明导航操作层默认 44dp，水平留白 16dp，标题 16sp/600、行高 1.2、字距 -0.2sp；副标题 11sp、间隔 2dp。固定按钮 38dp、图标 20dp。顶部状态栏另计安全区。

**状态归属**

通过 HyperNavBarPage 组合滚动正文，避免重复计算顶部安全区。固定类型仅使用标题和对应回调；custom 接收插槽。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperNavBar

<details class="component-doc__details">
<summary>查看公开签名</summary>

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
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| type | String | 否 | HyperNavBarDefaults.TypeCustom | 渲染形态；合法值见本页样式约定。 |
| titleContent | (@Composable () -&gt; Unit)? | 否 | null | 标题插槽，继承组件标题样式。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |
| padding | PaddingValues | 否 | HyperNavBarDefaults.ContentPadding | 导航栏内部留白。 |
| spacing | Dp | 否 | HyperNavBarDefaults.ContentGap | 导航栏各区域之间的间距。 |
| titleSpacing | Dp | 否 | HyperNavBarDefaults.TitleGap | 标题与其他区域的间距。 |
| actionSpacing | Dp | 否 | HyperNavBarDefaults.ActionGap | 导航操作项之间的间距。 |
| centerTitle | Boolean | 否 | false | custom 类型是否将标题在导航栏中居中。 |
| onBackClick | () -&gt; Unit | 否 | {} | 固定返回类型的点击回调。 |
| onMoreClick | () -&gt; Unit | 否 | {} | more 类型右侧更多操作的点击回调。 |
| onSaveClick | () -&gt; Unit | 否 | {} | edit 类型右侧保存操作的点击回调。 |
| subtitleContent | (@Composable () -&gt; Unit)? | 否 | null | 副标题插槽。 |
| navigationContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 导航栏左侧导航内容。 |
| trailingContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 尾部插槽。 |
| actions | List&lt;@Composable () -&gt; Unit&gt; | 否 | emptyList() | 导航栏操作项内容。 |
| child | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 导航栏附加子内容。 |

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
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| contentPadding | PaddingValues | 否 | PaddingValues() | 主体内容的内部留白。 |
| navBarSize | String | 否 | "default" | 页面导航栏的尺寸档位。 |
| bottomSafeArea | Boolean | 否 | true | 是否为底部系统安全区留白。 |
| content | @Composable BoxScope.(PaddingValues) -&gt; Unit | 是 | — | 主体内容插槽。 |

### HyperNavBarBackButton

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperNavBarBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "返回"
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| onClick | () -&gt; Unit | 是 | — | 用户点击回调。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| contentDescription | String | 否 | "返回" | 无障碍操作名称。 |
