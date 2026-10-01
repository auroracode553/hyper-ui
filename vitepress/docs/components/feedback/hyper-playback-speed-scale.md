# HyperPlaybackSpeedScale

包名：`hyper_ui`。

<WasmPreview demo="playback_speed_scale" title="HyperPlaybackSpeedScale 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperPlaybackSpeedScale(
    selectedSpeed: Float,
    modifier: Modifier = Modifier,
    speedOptions: List<Float> = HyperPlaybackSpeedScaleDefaults.SpeedOptions,
    shape: Shape = HyperPlaybackSpeedScaleDefaults.Shape,
    colors: HyperPlaybackSpeedScaleColors = HyperPlaybackSpeedScaleDefaults.colors(),
    leadingContent: (@Composable () -> Unit)? = null
)
```

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| selectedSpeed | Float | 是 | — | 当前选中的播放速度。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| speedOptions | List&lt;Float&gt; | 否 | HyperPlaybackSpeedScaleDefaults.SpeedOptions | 可选播放速度列表。 |
| shape | Shape | 否 | HyperPlaybackSpeedScaleDefaults.Shape | 组件容器的形状。 |
| colors | HyperPlaybackSpeedScaleColors | 否 | HyperPlaybackSpeedScaleDefaults.colors() | 组件各状态的颜色配置。 |
| leadingContent | (@Composable () -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |

## 最小用法

```kotlin
var temporarySpeed by remember { mutableStateOf(2f) }

HyperPlaybackSpeedScale(
    selectedSpeed = temporarySpeed,
    modifier = Modifier.align(Alignment.Center)
)
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
