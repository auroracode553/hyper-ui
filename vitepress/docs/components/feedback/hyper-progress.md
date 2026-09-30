# HyperProgress

包名：`hyper_ui`。

<WasmPreview demo="progress" title="HyperProgress 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperProgress(
    progress: Float? = null,
    modifier: Modifier = Modifier,
    type: String = "linear",
    size: String = "default",
    shape: Shape = HyperProgressDefaults.LinearShape,
    strokeWidth: Dp = HyperProgressDefaults.CircularStrokeWidth,
    colors: HyperProgressColors = HyperProgressDefaults.colors(),
    trackBorder: BorderStroke? = null
)
```

## 最小用法

```kotlin
HyperProgress(type = "linear", progress = progress)
HyperProgress(type = "linear", progress = progress, size = "large")
HyperProgress(type = "linear", progress = null)

HyperProgress(type = "circular", progress = progress)
HyperProgress(type = "circular", progress = progress, size = "small")
HyperProgress(type = "circular", progress = null)
```

`size` 支持 `small`、`default`、`large`，分别调整轨道厚度或圆形指示器直径；`progress = null` 表示不确定进度。

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
