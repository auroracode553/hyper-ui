# HyperSlider

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="slider" title="HyperSlider 交互预览" />

## 样式约定

默认值域 0..100，显示数值，44dp 触摸高、6dp 轨道、20dp 白色圆形滑块和 2dp 品牌描边。滑块中心沿完整轨道移动，并在边缘限制可见滑块。旧版中心点与光晕配置已删除，不保留无效颜色入口。

## 状态归属与使用约束

值由调用方持有。steps 仅控制等距吸附，segmentValues 控制标记；0..1 使用场景必须显式传 valueRange=0f..1f。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperSlider(value, { value = it })
```

## HyperSlider API / Props

```kotlin
@Composable
fun HyperSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    showValue: Boolean = true,
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
    thumbBorder: BorderStroke? = HyperSliderDefaults.thumbBorder()
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | Float | 是 | — | 调用方持有的当前值。 |
| onValueChange | (Float) -&gt; Unit | 是 | — | 用户修改值时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| readOnly | Boolean | 否 | false | 只读；保持正常外观并停止修改值。 |
| valueRange | ClosedFloatingPointRange&lt;Float&gt; | 否 | 0f..100f | 可操作数值范围。 |
| showValue | Boolean | 否 | true | 是否显示滑块当前数值。 |
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
| colors | HyperSliderColors | 否 | HyperSliderDefaults.colors() | 各状态配色配置。 |
| trackBorder | BorderStroke? | 否 | null | 进度轨道的边框配置。 |
| thumbBorder | BorderStroke? | 否 | HyperSliderDefaults.thumbBorder() | 滑块手柄的边框配置。 |

## HyperSliderDefaults.colors API / Props

```kotlin
@Composable
    fun colors(
        trackColor: Color = Color.Unspecified,
        activeTrackColor: Color = Color.Unspecified,
        segmentMarkerColor: Color = Color.Unspecified,
        thumbColor: Color = Color.Unspecified,
        disabledTrackColor: Color = Color.Unspecified,
        disabledActiveTrackColor: Color = Color.Unspecified,
        disabledSegmentMarkerColor: Color = Color.Unspecified,
        disabledThumbColor: Color = Color.Unspecified
    ): HyperSliderColors
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| trackColor | Color | 否 | Color.Unspecified | 背景轨道颜色。 |
| activeTrackColor | Color | 否 | Color.Unspecified | 已填充轨道颜色。 |
| segmentMarkerColor | Color | 否 | Color.Unspecified | 刻度点颜色。 |
| thumbColor | Color | 否 | Color.Unspecified | 圆形手柄填充色。 |
| disabledTrackColor | Color | 否 | Color.Unspecified | 禁用时的背景轨道。 |
| disabledActiveTrackColor | Color | 否 | Color.Unspecified | 禁用时的已填充轨道。 |
| disabledSegmentMarkerColor | Color | 否 | Color.Unspecified | 禁用时的刻度点颜色。 |
| disabledThumbColor | Color | 否 | Color.Unspecified | 禁用时的手柄填充色。 |

## HyperSliderDefaults.trackBorder API / Props

```kotlin
@Composable
    fun trackBorder(color: Color = Color.Unspecified): BorderStroke
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| color | Color | 否 | Color.Unspecified | 颜色覆盖。 |

## HyperSliderDefaults.thumbBorder API / Props

```kotlin
@Composable
    fun thumbBorder(color: Color = Color.Unspecified): BorderStroke =
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| color | Color | 否 | Color.Unspecified | 颜色覆盖。 |

## HyperSliderColors 配置

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| trackColor | Color | 是 | — | 背景轨道颜色。 |
| activeTrackColor | Color | 是 | — | 已填充轨道颜色。 |
| segmentMarkerColor | Color | 是 | — | 刻度点颜色。 |
| thumbColor | Color | 是 | — | 圆形手柄填充色。 |
| disabledTrackColor | Color | 是 | — | 禁用时的背景轨道。 |
| disabledActiveTrackColor | Color | 是 | — | 禁用时的已填充轨道。 |
| disabledSegmentMarkerColor | Color | 是 | — | 禁用时的刻度点颜色。 |
| disabledThumbColor | Color | 是 | — | 禁用时的手柄填充色。 |
