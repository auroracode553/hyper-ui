---
description: 通过拖动选择范围内数值，支持离散刻度、数值标签与提交回调。
---

# HyperSlider

通过拖动选择范围内数值，支持离散刻度、数值标签与提交回调。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="slider" title="HyperSlider 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperSlider(value, { value = it })
```

</details>

## 使用约定

**样式约定**

默认值域 0..100，显示数值，44dp 触摸高、6dp 轨道、20dp 白色圆形滑块和 2dp 品牌描边。滑块中心沿完整轨道移动，并在边缘限制可见滑块。旧版中心点与光晕配置已删除，不保留无效颜色入口。

**状态归属**

值由调用方持有。divisions 控制等分区间数量，null 为连续，1 只选两端；segmentValues 控制标记。0..1 使用场景传 min = 0f、max = 1f。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperSlider

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperSlider(
    value: Float,
    onChanged: ((Float) -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    min: Float = 0f,
    max: Float = 100f,
    showValue: Boolean = true,
    divisions: Int? = null,
    showSegmentMarkers: Boolean = false,
    segmentValues: List<Float> = emptyList(),
    onChangeStart: ((Float) -> Unit)? = null,
    onChangeEnd: ((Float) -> Unit)? = null,
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

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | Float | 是 | — | 由调用方持有的当前值。 |
| onChanged | ((Float) -&gt; Unit)? | 否 | null | 用户修改值或选择项时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| readOnly | Boolean | 否 | false | 是否只展示当前值而禁止修改。 |
| min | Float | 否 | 0f | 数值范围的下限。 |
| max | Float | 否 | 100f | 数值范围的上限。 |
| showValue | Boolean | 否 | true | 是否显示滑块当前数值。 |
| divisions | Int? | 否 | null | 等分区间数量；null 表示连续，1 表示只选两端。 |
| showSegmentMarkers | Boolean | 否 | false | 是否显示分段刻度标记。 |
| segmentValues | List&lt;Float&gt; | 否 | emptyList() | 需要标出的分段数值。 |
| onChangeStart | ((Float) -&gt; Unit)? | 否 | null | 开始拖动时回传当前值。 |
| onChangeEnd | ((Float) -&gt; Unit)? | 否 | null | 结束拖动时回传最终值。 |
| minimumTouchHeight | Dp | 否 | HyperSliderDefaults.MinTouchHeight | 滑块可触摸区域的最小高度。 |
| trackHeight | Dp | 否 | HyperSliderDefaults.TrackHeight | 滑块轨道高度。 |
| thumbSize | Dp | 否 | HyperSliderDefaults.ThumbSize | 滑块手柄的尺寸。 |
| segmentMarkerSize | Dp | 否 | HyperSliderDefaults.SegmentMarkerSize | 分段刻度标记的尺寸。 |
| trackShape | Shape | 否 | HyperSliderDefaults.TrackShape | 滑块轨道形状。 |
| thumbShape | Shape | 否 | HyperSliderDefaults.ThumbShape | 滑块手柄的形状。 |
| colors | HyperSliderColors | 否 | HyperSliderDefaults.colors() | 组件各状态的颜色配置。 |
| trackBorder | BorderStroke? | 否 | null | 进度轨道的边框配置。 |
| thumbBorder | BorderStroke? | 否 | HyperSliderDefaults.thumbBorder() | 滑块手柄的边框配置。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperSliderDefaults.colors**

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

**HyperSliderDefaults.trackBorder**

```kotlin
@Composable
    fun trackBorder(color: Color = Color.Unspecified): BorderStroke
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| color | Color | 否 | Color.Unspecified | 自定义颜色，覆盖默认配色。 |

**HyperSliderDefaults.thumbBorder**

```kotlin
@Composable
    fun thumbBorder(color: Color = Color.Unspecified): BorderStroke =
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| color | Color | 否 | Color.Unspecified | 自定义颜色，覆盖默认配色。 |

**HyperSliderColors 配置**

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

</details>
