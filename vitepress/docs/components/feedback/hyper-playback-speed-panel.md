# HyperPlaybackSpeedPanel

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/feedback/HyperPlaybackSpeedPanel.kt`
- 默认值与格式工具：`library/src/main/java/hyper_ui/components/feedback/HyperPlaybackSpeedPanelDefaults.kt`
- 预览：`playback_speed_panel`

`HyperPlaybackSpeedPanel` 是固定深色、受控的播放速度设置面板；`HyperPlaybackSpeedPanelOverlay` 在其外层补充全屏点击关闭区域。两者不读取外层应用的明暗模式，适合直接覆盖在视频画面上。组件只分发速度、关闭和自定义速度事件，不持有播放器或输入框业务状态。

## 公开签名

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

## 文案与颜色

```kotlin
data class HyperPlaybackSpeedPanelTexts(
    val title: String = "播放速度",
    val realtimeHint: String = "拖动调节 · 实时生效",
    val customAction: String = "自定义",
    val sliderContentDescription: String = "播放速度滑块",
    val closeContentDescription: String = "关闭播放速度面板",
    val resetContentDescription: String = "恢复默认速度"
)

data class HyperPlaybackSpeedPanelColors(
    val scrimColor: Color,
    val containerTopColor: Color,
    val containerBottomColor: Color,
    val panelBorderColor: Color,
    val contentColor: Color,
    val supportingContentColor: Color,
    val accentColor: Color,
    val trackColor: Color,
    val segmentMarkerColor: Color
)
```

`HyperPlaybackSpeedPanelDefaults.colors()` 默认使用深色纵向渐变、白色内容和低透明白色轨道；只有强调色读取 `HyperThemeConfig`。所有颜色都可逐项覆盖。

## 速度默认值与工具

```kotlin
object HyperPlaybackSpeedPanelDefaults {
    const val SliderMinimum = 0.25f
    const val SliderMaximum = 4f
    const val SliderStep = 0.05f
    const val SliderSteps = 74
    const val DefaultSpeed = 1f
    const val CustomMinimum = 0.1f
    const val CustomMaximum = 8f

    val SliderRange = SliderMinimum..SliderMaximum
    val MajorSpeeds = listOf(0.25f, 1f, 2f, 3f, 4f)
    val Shape: Shape = RoundedCornerShape(26.dp)
    val MaxWidth = 680.dp
    const val WidthFraction = 0.92f
    val Elevation = 24.dp
    val ContentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp)
    val ContentSpacing = 8.dp
    val OverlayHorizontalPadding = 16.dp
    val SliderTouchHeight = 46.dp
    val SliderTrackHeight = 7.dp
    val SliderThumbSize = 24.dp
    val SliderMarkerSize = 5.dp
    val SpeedLabelWidth = 44.dp
    val SpeedLabelHeight = 30.dp

    @Composable
    fun colors(
        scrimColor: Color = Color.Unspecified,
        containerTopColor: Color = Color.Unspecified,
        containerBottomColor: Color = Color.Unspecified,
        panelBorderColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        supportingContentColor: Color = Color.Unspecified,
        accentColor: Color = Color.Unspecified,
        trackColor: Color = Color.Unspecified,
        segmentMarkerColor: Color = Color.Unspecified
    ): HyperPlaybackSpeedPanelColors

    fun formatCompact(speed: Float): String
    fun formatFixed(speed: Float): String
    fun normalizeInput(value: String): String
}
```

`formatCompact` 移除无意义的末尾零，`formatFixed` 固定两位小数，`normalizeInput` 只保留一个小数点和最多两位小数。输入范围校验与错误提示仍由调用方负责。

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

## 状态与约束

- `visible`、`currentSpeed` 及自定义速度弹窗状态全部由调用方持有；组件状态即时渲染，不执行动画。
- `onSpeedChange` 会在滑块拖动、预设档位点击和重置时调用。组件不直接操作播放器。
- `onCustomSpeedRequest` 为 `null` 时不显示自定义入口；非空时只回调事件，不内置文本输入弹窗。
- `valueRange` 必须由有限数值组成且结束值大于起始值，`steps` 不能小于零。超出范围或非有限的预设档位会被过滤。
- 面板默认最大宽度为 `680.dp`、占可用宽度的 92%；覆盖层默认提供 16dp 水平安全间距。
- `modifier` 控制覆盖层，`panelModifier` 控制覆盖层内的面板；单独使用 `HyperPlaybackSpeedPanel` 时通过其 `modifier` 控制尺寸与位置。
- 五个图标插槽均通过 `LocalContentColor` 接收当前语义色；为空时使用不依赖外部图标库的内置 Canvas 图形。

<WasmPreview demo="playback_speed_panel" title="HyperPlaybackSpeedPanel 交互预览" />
