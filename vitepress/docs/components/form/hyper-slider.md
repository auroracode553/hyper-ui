# HyperSlider

包名：`hyper_ui`。

<WasmPreview demo="slider" title="HyperSlider 交互预览" />

## 公开签名与默认值

```kotlin
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

## 最小用法

```kotlin
HyperSlider(value = volume, onValueChange = { volume = it })
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
