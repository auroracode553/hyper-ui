---
description: 组合倍速选择与快捷操作，播放行为由调用方执行。
---

# HyperPlaybackSpeedPanel

组合倍速选择与快捷操作，播放行为由调用方执行。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="playback_speed_panel" title="HyperPlaybackSpeedPanel 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
var panelVisible by remember { mutableStateOf(false) }
var playbackSpeed by remember { mutableStateOf(1f) }

HyperPlaybackSpeedPanelOverlay(
    visible = panelVisible,
    value = playbackSpeed,
    onChanged = { speed -> playbackSpeed = speed },
    onClose = { panelVisible = false },
    onCustomPressed = { customDialogVisible = true },
    texts = HyperPlaybackSpeedPanelTexts(
        title = stringResource(R.string.playback_speed),
        customAction = stringResource(R.string.custom)
    )
)
```

</details>

## 使用约定

**约束**

业务状态由调用方管理，组件只负责 UI 与回调。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperPlaybackSpeedPanel

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperPlaybackSpeedPanelOverlay(
    visible: Boolean,
    value: Float,
    onChanged: (Float) -> Unit,
    onClose: () -> Unit,
    onCustomPressed: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    panelModifier: Modifier = Modifier,
    texts: HyperPlaybackSpeedPanelTexts = HyperPlaybackSpeedPanelTexts(),
    colors: HyperPlaybackSpeedPanelColors = HyperPlaybackSpeedPanelDefaults.colors(),
    options: List<Float> = HyperPlaybackSpeedPanelDefaults.MajorSpeeds,
    valueRange: ClosedFloatingPointRange<Float> = HyperPlaybackSpeedPanelDefaults.SliderRange,
    steps: Int = HyperPlaybackSpeedPanelDefaults.SliderSteps,
    defaultSpeed: Float = HyperPlaybackSpeedPanelDefaults.DefaultSpeed,
    shape: Shape = HyperPlaybackSpeedPanelDefaults.Shape,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    close: (@Composable BoxScope.() -> Unit)? = null,
    hintLeading: (@Composable BoxScope.() -> Unit)? = null,
    reset: (@Composable BoxScope.() -> Unit)? = null,
    customLeading: (@Composable BoxScope.() -> Unit)? = null
)

@Composable
fun HyperPlaybackSpeedPanel(
    value: Float,
    onChanged: (Float) -> Unit,
    onClose: () -> Unit,
    onCustomPressed: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    texts: HyperPlaybackSpeedPanelTexts = HyperPlaybackSpeedPanelTexts(),
    colors: HyperPlaybackSpeedPanelColors = HyperPlaybackSpeedPanelDefaults.colors(),
    options: List<Float> = HyperPlaybackSpeedPanelDefaults.MajorSpeeds,
    valueRange: ClosedFloatingPointRange<Float> = HyperPlaybackSpeedPanelDefaults.SliderRange,
    steps: Int = HyperPlaybackSpeedPanelDefaults.SliderSteps,
    defaultSpeed: Float = HyperPlaybackSpeedPanelDefaults.DefaultSpeed,
    shape: Shape = HyperPlaybackSpeedPanelDefaults.Shape,
    leading: (@Composable BoxScope.() -> Unit)? = null,
    close: (@Composable BoxScope.() -> Unit)? = null,
    hintLeading: (@Composable BoxScope.() -> Unit)? = null,
    reset: (@Composable BoxScope.() -> Unit)? = null,
    customLeading: (@Composable BoxScope.() -> Unit)? = null
)
```

</details>

### HyperPlaybackSpeedPanelOverlay

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 是否显示组件，由调用方持有。 |
| value | Float | 是 | — | 由调用方持有的当前值。 |
| onChanged | (Float) -&gt; Unit | 是 | — | 用户修改值或选择项时通知调用方。 |
| onClose | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| onCustomPressed | (() -&gt; Unit)? | 否 | null | 请求打开自定义速度设置。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| panelModifier | Modifier | 否 | Modifier | 播放速度面板的布局修饰符。 |
| texts | HyperPlaybackSpeedPanelTexts | 否 | HyperPlaybackSpeedPanelTexts() | 组件内置文案的配置。 |
| colors | HyperPlaybackSpeedPanelColors | 否 | HyperPlaybackSpeedPanelDefaults.colors() | 组件各状态的颜色配置。 |
| options | List&lt;Float&gt; | 否 | HyperPlaybackSpeedPanelDefaults.MajorSpeeds | 可供选择的选项列表。 |
| valueRange | ClosedFloatingPointRange&lt;Float&gt; | 否 | HyperPlaybackSpeedPanelDefaults.SliderRange | 可操作数值范围。 |
| steps | Int | 否 | HyperPlaybackSpeedPanelDefaults.SliderSteps | 数值范围内的离散步数。 |
| defaultSpeed | Float | 否 | HyperPlaybackSpeedPanelDefaults.DefaultSpeed | 重置操作使用的默认速度。 |
| shape | Shape | 否 | HyperPlaybackSpeedPanelDefaults.Shape | 组件容器的形状。 |
| leading | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| close | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 关闭图标插槽。 |
| hintLeading | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 提示信息的前置图标插槽。 |
| reset | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 重置操作的图标插槽。 |
| customLeading | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 自定义速度操作的前置图标插槽。 |

### HyperPlaybackSpeedPanel

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | Float | 是 | — | 由调用方持有的当前值。 |
| onChanged | (Float) -&gt; Unit | 是 | — | 用户修改值或选择项时通知调用方。 |
| onClose | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| onCustomPressed | (() -&gt; Unit)? | 否 | null | 请求打开自定义速度设置。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| texts | HyperPlaybackSpeedPanelTexts | 否 | HyperPlaybackSpeedPanelTexts() | 组件内置文案的配置。 |
| colors | HyperPlaybackSpeedPanelColors | 否 | HyperPlaybackSpeedPanelDefaults.colors() | 组件各状态的颜色配置。 |
| options | List&lt;Float&gt; | 否 | HyperPlaybackSpeedPanelDefaults.MajorSpeeds | 可供选择的选项列表。 |
| valueRange | ClosedFloatingPointRange&lt;Float&gt; | 否 | HyperPlaybackSpeedPanelDefaults.SliderRange | 可操作数值范围。 |
| steps | Int | 否 | HyperPlaybackSpeedPanelDefaults.SliderSteps | 数值范围内的离散步数。 |
| defaultSpeed | Float | 否 | HyperPlaybackSpeedPanelDefaults.DefaultSpeed | 重置操作使用的默认速度。 |
| shape | Shape | 否 | HyperPlaybackSpeedPanelDefaults.Shape | 组件容器的形状。 |
| leading | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| close | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 关闭图标插槽。 |
| hintLeading | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 提示信息的前置图标插槽。 |
| reset | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 重置操作的图标插槽。 |
| customLeading | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 自定义速度操作的前置图标插槽。 |
