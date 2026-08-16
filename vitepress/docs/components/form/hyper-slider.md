# HyperSlider

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/progress/HyperSlider.kt`
- 预览：`slider`

`HyperSlider` 是可点击、可拖动的受控滑块，适合媒体播放进度、音量和数值范围选择。轨道可以保持连续，也可以显示等距或业务指定的分段点；滑块圆点统一使用“主题柔光环 + 外圆 + 中心点”三层视觉。

## 公开签名

```kotlin
@Immutable
data class HyperSliderColors(
    val trackColor: Color,
    val activeTrackColor: Color,
    val segmentMarkerColor: Color,
    val thumbColor: Color,
    val thumbCenterColor: Color,
    val thumbHaloColor: Color,
    val disabledTrackColor: Color,
    val disabledActiveTrackColor: Color,
    val disabledSegmentMarkerColor: Color,
    val disabledThumbColor: Color,
    val disabledThumbCenterColor: Color,
    val disabledThumbHaloColor: Color
)

@Composable
fun HyperSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    showSegmentMarkers: Boolean = false,
    segmentValues: List<Float> = emptyList(),
    onValueChangeStarted: (() -> Unit)? = null,
    onValueChangeFinished: (() -> Unit)? = null,
    minimumTouchHeight: Dp = HyperSliderDefaults.MinTouchHeight,
    trackHeight: Dp = HyperSliderDefaults.TrackHeight,
    thumbSize: Dp = HyperSliderDefaults.ThumbSize,
    segmentMarkerSize: Dp = HyperSliderDefaults.SegmentMarkerSize,
    trackShape: Shape = HyperSliderDefaults.TrackShape,
    thumbShape: Shape = HyperSliderDefaults.ThumbShape,
    colors: HyperSliderColors = HyperSliderDefaults.colors(),
    trackBorder: BorderStroke? = null,
    thumbBorder: BorderStroke? = null
)
```

## 默认值与配置

```kotlin
object HyperSliderDefaults {
    val MinTouchHeight = 40.dp
    val TrackHeight = 6.dp
    val ThumbSize = 18.dp
    val SegmentMarkerSize = 5.dp
    const val ThumbHaloScale = 1.85f
    const val ThumbCenterScale = 0.52f
    val TrackShape: Shape = RoundedCornerShape(percent = 50)
    val ThumbShape: Shape = CircleShape

    @Composable
    fun colors(
        trackColor: Color = Color.Unspecified,
        activeTrackColor: Color = Color.Unspecified,
        segmentMarkerColor: Color = Color.Unspecified,
        thumbColor: Color = Color.Unspecified,
        thumbCenterColor: Color = Color.Unspecified,
        thumbHaloColor: Color = Color.Unspecified,
        disabledTrackColor: Color = Color.Unspecified,
        disabledActiveTrackColor: Color = Color.Unspecified,
        disabledSegmentMarkerColor: Color = Color.Unspecified,
        disabledThumbColor: Color = Color.Unspecified,
        disabledThumbCenterColor: Color = Color.Unspecified,
        disabledThumbHaloColor: Color = Color.Unspecified
    ): HyperSliderColors

    @Composable
    fun trackBorder(color: Color = Color.Unspecified): BorderStroke

    @Composable
    fun thumbBorder(color: Color = Color.Unspecified): BorderStroke
}
```

## 连续进度条

```kotlin
HyperSlider(
    value = position,
    onValueChange = { position = it },
    valueRange = 0f..duration,
    onValueChangeStarted = onSeekStart,
    onValueChangeFinished = onSeekFinished,
    showSegmentMarkers = false,
    trackHeight = 3.dp,
    thumbSize = 12.dp
)
```

`steps = 0` 且 `showSegmentMarkers = false` 是默认配置，适合视频播放进度。

## 分段滑块

```kotlin
HyperSlider(
    value = speed,
    onValueChange = { speed = it },
    valueRange = 0.25f..4f,
    steps = 74,
    showSegmentMarkers = true,
    segmentValues = listOf(0.25f, 1f, 2f, 3f, 4f)
)
```

- `steps` 控制等距吸附，不负责决定分段点是否可见。
- `showSegmentMarkers = true` 且 `segmentValues` 为空时，组件根据 `steps` 展示全部等距停靠点。
- `segmentValues` 非空时，只显示范围内的指定业务主刻度；它不会改变 `steps` 的吸附规则。
- `readOnly = true` 保留正常颜色、分段点和进度语义，但不会接收点击、拖动或无障碍改值，适合长按反馈等只读场景。
- `enabled = false` 使用整套禁用颜色；它与 `readOnly` 的视觉语义不同。

## 约束

- `valueRange` 的起止值必须有限，且结束值必须大于起始值。
- `steps`、`minimumTouchHeight`、`trackHeight` 和 `segmentMarkerSize` 不能小于零，`thumbSize` 必须大于零。
- `segmentValues` 中的非有限值和范围外值会被忽略，重复值会合并并排序。
- 点击轨道会直接定位，拖动过程中持续调用 `onValueChange`。
- 业务值、拖动预览和最终提交均由调用方持有；组件只维护拖动中的纯 UI 状态。
- 自定义 `activeTrackColor` 时，未单独指定的中心点和柔光环会自动继承该颜色。

<WasmPreview demo="slider" title="HyperSlider 交互预览" />
