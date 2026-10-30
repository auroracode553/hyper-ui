---
description: 展示确定或不定进度，提供线性与圆环两种形态。
---

# HyperProgress

展示确定或不定进度，提供线性与圆环两种形态。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="value" title="HyperProgress 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperProgress(value, showLabel = true)
HyperProgress(type = "circular", value = null, showLabel = false)
```

</details>

## 使用约定

**样式约定**

linear 高度 4/6/12dp，确定值 220ms easeOutCubic；circular 直径 48/64/80dp、stroke 4/6/8dp。不确定线性段宽 32%、周期 1400ms；圆环占 72%、周期 1100ms。

**状态归属**

调用方持有 value。有限值收敛 0..1；非有限值按不确定处理。showLabel 默认 true，圆环标签 12sp。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperProgress

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperProgress(
    value: Float? = null,
    modifier: Modifier = Modifier,
    type: String = "linear",
    showLabel: Boolean = true,
    shape: Shape = HyperProgressDefaults.LinearShape,
    strokeWidth: Dp = HyperProgressDefaults.CircularStrokeWidth,
    color: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    colors: HyperProgressColors = HyperProgressDefaults.colors(trackColor = backgroundColor, indicatorColor = color),
    trackBorder: BorderStroke? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | Float? | 否 | null | 由调用方持有的当前值。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| type | String | 否 | &quot;linear&quot; | 组件的视觉或布局形态。 |
| showLabel | Boolean | 否 | true | 确定进度时是否显示百分比。 |
| shape | Shape | 否 | HyperProgressDefaults.LinearShape | 组件容器的形状。 |
| strokeWidth | Dp | 否 | HyperProgressDefaults.CircularStrokeWidth | 圆形进度条的描边宽度。 |
| color | Color | 否 | Color.Unspecified | 自定义颜色，覆盖默认配色。 |
| backgroundColor | Color | 否 | Color.Unspecified | 背景轨道颜色。 |
| colors | HyperProgressColors | 否 | HyperProgressDefaults.colors(trackColor = backgroundColor, indicatorColor = color) | 组件各状态的颜色配置。 |
| trackBorder | BorderStroke? | 否 | null | 进度轨道的边框配置。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperProgressDefaults.strokeWidth**

```kotlin
val CircularStrokeWidth: Dp = 6.dp
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| — | — | — | — | 无参数，采用组件统一规格。 |

**HyperProgressDefaults.colors**

```kotlin
@Composable
    fun colors(
        trackColor: Color = Color.Unspecified,
        indicatorColor: Color = Color.Unspecified
    ): HyperProgressColors
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| trackColor | Color | 否 | Color.Unspecified | 背景轨道颜色。 |
| indicatorColor | Color | 否 | Color.Unspecified | 进度指示颜色。 |

**HyperProgressDefaults.linearTrackBorder**

```kotlin
@Composable
    fun linearTrackBorder(color: Color = Color.Unspecified): BorderStroke
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| color | Color | 否 | Color.Unspecified | 自定义颜色，覆盖默认配色。 |

**HyperProgressColors 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| trackColor | Color | 是 | — | 背景轨道颜色。 |
| indicatorColor | Color | 是 | — | 进度指示颜色。 |

</details>
