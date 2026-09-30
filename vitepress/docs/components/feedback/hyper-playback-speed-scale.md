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
