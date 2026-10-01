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

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | Float | 是 | — | 由调用方持有的当前值。 |
| onValueChange | (Float) -&gt; Unit | 是 | — | 值变化时通知调用方更新状态。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| readOnly | Boolean | 否 | false | 是否只展示当前值而禁止修改。 |
| valueRange | ClosedFloatingPointRange&lt;Float&gt; | 否 | 0f..1f | 可操作数值范围。 |
| steps | Int | 否 | 0 | 数值范围内的离散步数。 |
| showSegmentMarkers | Boolean | 否 | false | 是否显示分段刻度标记。 |
| segmentValues | List&lt;Float&gt; | 否 | emptyList() | 需要标出的分段数值。 |
| onValueChangeStarted | (() -&gt; Unit)? | 否 | null | 用户开始滑动时的回调。 |
| onValueChangeFinished | (() -&gt; Unit)? | 否 | null | 用户结束滑动时的回调。 |
| minimumTouchHeight | Dp | 否 | HyperSliderDefaults.MinTouchHeight | 滑块可触摸区域的最小高度。 |
| trackHeight | Dp | 否 | HyperSliderDefaults.TrackHeight | 滑块轨道高度。 |
| thumbSize | Dp | 否 | HyperSliderDefaults.ThumbSize | 滑块手柄的尺寸。 |
| segmentMarkerSize | Dp | 否 | HyperSliderDefaults.SegmentMarkerSize | 分段刻度标记的尺寸。 |
| trackShape | Shape | 否 | HyperSliderDefaults.TrackShape | 滑块轨道形状。 |
| thumbShape | Shape | 否 | HyperSliderDefaults.ThumbShape | 滑块手柄的形状。 |
| colors | HyperSliderColors | 否 | HyperSliderDefaults.colors() | 组件各状态的颜色配置。 |
| trackBorder | BorderStroke? | 否 | null | 进度轨道的边框配置。 |
| thumbBorder | BorderStroke? | 否 | null | 滑块手柄的边框配置。 |

## 最小用法

```kotlin
HyperSlider(value = volume, onValueChange = { volume = it })
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
