# HyperPlaybackSpeedPanel

包名：`hyper_ui`。

<WasmPreview demo="playback_speed_panel" title="HyperPlaybackSpeedPanel 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperPlaybackSpeedPanelOverlay(
    visible: Boolean,
    currentSpeed: Float,
    onSpeedChange: (Float) -> Unit,
    onDismissRequest: () -> Unit,
    onCustomSpeedRequest: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    panelModifier: Modifier = Modifier,
    texts: HyperPlaybackSpeedPanelTexts = HyperPlaybackSpeedPanelTexts(),
    colors: HyperPlaybackSpeedPanelColors = HyperPlaybackSpeedPanelDefaults.colors(),
    speedOptions: List<Float> = HyperPlaybackSpeedPanelDefaults.MajorSpeeds,
    valueRange: ClosedFloatingPointRange<Float> = HyperPlaybackSpeedPanelDefaults.SliderRange,
    steps: Int = HyperPlaybackSpeedPanelDefaults.SliderSteps,
    defaultSpeed: Float = HyperPlaybackSpeedPanelDefaults.DefaultSpeed,
    shape: Shape = HyperPlaybackSpeedPanelDefaults.Shape,
    leadingContent: (@Composable BoxScope.() -> Unit)? = null,
    closeContent: (@Composable BoxScope.() -> Unit)? = null,
    hintLeadingContent: (@Composable BoxScope.() -> Unit)? = null,
    resetContent: (@Composable BoxScope.() -> Unit)? = null,
    customActionLeadingContent: (@Composable BoxScope.() -> Unit)? = null
)

@Composable
fun HyperPlaybackSpeedPanel(
    currentSpeed: Float,
    onSpeedChange: (Float) -> Unit,
    onDismissRequest: () -> Unit,
    onCustomSpeedRequest: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    texts: HyperPlaybackSpeedPanelTexts = HyperPlaybackSpeedPanelTexts(),
    colors: HyperPlaybackSpeedPanelColors = HyperPlaybackSpeedPanelDefaults.colors(),
    speedOptions: List<Float> = HyperPlaybackSpeedPanelDefaults.MajorSpeeds,
    valueRange: ClosedFloatingPointRange<Float> = HyperPlaybackSpeedPanelDefaults.SliderRange,
    steps: Int = HyperPlaybackSpeedPanelDefaults.SliderSteps,
    defaultSpeed: Float = HyperPlaybackSpeedPanelDefaults.DefaultSpeed,
    shape: Shape = HyperPlaybackSpeedPanelDefaults.Shape,
    leadingContent: (@Composable BoxScope.() -> Unit)? = null,
    closeContent: (@Composable BoxScope.() -> Unit)? = null,
    hintLeadingContent: (@Composable BoxScope.() -> Unit)? = null,
    resetContent: (@Composable BoxScope.() -> Unit)? = null,
    customActionLeadingContent: (@Composable BoxScope.() -> Unit)? = null
)
```

## Props（参数）

### HyperPlaybackSpeedPanelOverlay

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 是否显示组件，由调用方持有。 |
| currentSpeed | Float | 是 | — | 当前播放速度，由调用方持有。 |
| onSpeedChange | (Float) -&gt; Unit | 是 | — | 速度变化时通知调用方更新状态。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| onCustomSpeedRequest | (() -&gt; Unit)? | 否 | null | 请求打开自定义速度设置。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| panelModifier | Modifier | 否 | Modifier | 播放速度面板的布局修饰符。 |
| texts | HyperPlaybackSpeedPanelTexts | 否 | HyperPlaybackSpeedPanelTexts() | 组件内置文案的配置。 |
| colors | HyperPlaybackSpeedPanelColors | 否 | HyperPlaybackSpeedPanelDefaults.colors() | 组件各状态的颜色配置。 |
| speedOptions | List&lt;Float&gt; | 否 | HyperPlaybackSpeedPanelDefaults.MajorSpeeds | 可选播放速度列表。 |
| valueRange | ClosedFloatingPointRange&lt;Float&gt; | 否 | HyperPlaybackSpeedPanelDefaults.SliderRange | 可操作数值范围。 |
| steps | Int | 否 | HyperPlaybackSpeedPanelDefaults.SliderSteps | 数值范围内的离散步数。 |
| defaultSpeed | Float | 否 | HyperPlaybackSpeedPanelDefaults.DefaultSpeed | 重置操作使用的默认速度。 |
| shape | Shape | 否 | HyperPlaybackSpeedPanelDefaults.Shape | 组件容器的形状。 |
| leadingContent | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| closeContent | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 关闭按钮的自定义内容。 |
| hintLeadingContent | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 提示信息的前置内容。 |
| resetContent | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 重置操作的自定义内容。 |
| customActionLeadingContent | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 自定义速度操作的前置内容。 |

### HyperPlaybackSpeedPanel

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| currentSpeed | Float | 是 | — | 当前播放速度，由调用方持有。 |
| onSpeedChange | (Float) -&gt; Unit | 是 | — | 速度变化时通知调用方更新状态。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| onCustomSpeedRequest | (() -&gt; Unit)? | 否 | null | 请求打开自定义速度设置。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| texts | HyperPlaybackSpeedPanelTexts | 否 | HyperPlaybackSpeedPanelTexts() | 组件内置文案的配置。 |
| colors | HyperPlaybackSpeedPanelColors | 否 | HyperPlaybackSpeedPanelDefaults.colors() | 组件各状态的颜色配置。 |
| speedOptions | List&lt;Float&gt; | 否 | HyperPlaybackSpeedPanelDefaults.MajorSpeeds | 可选播放速度列表。 |
| valueRange | ClosedFloatingPointRange&lt;Float&gt; | 否 | HyperPlaybackSpeedPanelDefaults.SliderRange | 可操作数值范围。 |
| steps | Int | 否 | HyperPlaybackSpeedPanelDefaults.SliderSteps | 数值范围内的离散步数。 |
| defaultSpeed | Float | 否 | HyperPlaybackSpeedPanelDefaults.DefaultSpeed | 重置操作使用的默认速度。 |
| shape | Shape | 否 | HyperPlaybackSpeedPanelDefaults.Shape | 组件容器的形状。 |
| leadingContent | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| closeContent | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 关闭按钮的自定义内容。 |
| hintLeadingContent | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 提示信息的前置内容。 |
| resetContent | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 重置操作的自定义内容。 |
| customActionLeadingContent | (@Composable BoxScope.() -&gt; Unit)? | 否 | null | 自定义速度操作的前置内容。 |

## 最小用法

```kotlin
var panelVisible by remember { mutableStateOf(false) }
var playbackSpeed by remember { mutableStateOf(1f) }

HyperPlaybackSpeedPanelOverlay(
    visible = panelVisible,
    currentSpeed = playbackSpeed,
    onSpeedChange = { speed -> playbackSpeed = speed },
    onDismissRequest = { panelVisible = false },
    onCustomSpeedRequest = { customDialogVisible = true },
    texts = HyperPlaybackSpeedPanelTexts(
        title = stringResource(R.string.playback_speed),
        customAction = stringResource(R.string.custom)
    )
)
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
