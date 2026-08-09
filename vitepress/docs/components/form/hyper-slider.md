# HyperSlider

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/progress/HyperSlider.kt`
- 预览：`slider`

`HyperSlider` 是可点击、可拖动的受控进度滑块。调用方持有业务值，并通过 `onValueChangeStarted`、`onValueChange`、`onValueChangeFinished` 处理拖动生命周期，适合媒体播放进度、音量和数值范围选择。

## 公开签名

```kotlin
@Composable
fun HyperSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeStarted: (() -> Unit)? = null,
    onValueChangeFinished: (() -> Unit)? = null,
    trackHeight: Dp = HyperSliderDefaults.TrackHeight,
    thumbSize: Dp = HyperSliderDefaults.ThumbSize,
    trackShape: Shape = HyperSliderDefaults.TrackShape,
    thumbShape: Shape = HyperSliderDefaults.ThumbShape,
    colors: HyperSliderColors = HyperSliderDefaults.colors(),
    trackBorder: BorderStroke? = HyperSliderDefaults.trackBorder(),
    thumbBorder: BorderStroke? = HyperSliderDefaults.thumbBorder()
)
```

## 默认值与配置

```kotlin
object HyperSliderDefaults {
    val MinTouchHeight: Dp
    val TrackHeight: Dp
    val ThumbSize: Dp
    val TrackShape: Shape
    val ThumbShape: Shape

    @Composable
    fun colors(...): HyperSliderColors

    @Composable
    fun trackBorder(color: Color = Color.Unspecified): BorderStroke

    @Composable
    fun thumbBorder(color: Color = Color.Unspecified): BorderStroke
}
```

## 最小用法

```kotlin
var position by remember { mutableStateOf(0f) }

HyperSlider(
    value = position,
    onValueChange = { position = it },
    valueRange = 0f..duration,
    onValueChangeStarted = onSeekStart,
    onValueChangeFinished = onSeekFinished
)
```

## 约束

- `valueRange` 的起止值必须是有限值，且结束值必须大于起始值。
- `steps = 0` 表示连续值；大于 0 时会吸附到等距分段。
- 点击轨道会直接定位，拖动过程中持续调用 `onValueChange`。
- 业务值由调用方持有；组件只维护拖动中的纯 UI 状态。
- 默认颜色、轨道、滑块、描边和禁用态均来自 HyperUI；调用方只在确有产品语义时覆盖。

<WasmPreview demo="slider" title="HyperSlider 交互预览" />
