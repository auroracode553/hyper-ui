# HyperProgressIndicator

包名：`hyper_ui`。

<WasmPreview demo="progress" title="HyperProgressIndicator 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperLinearProgressIndicator(
    progress: Float? = null,
    modifier: Modifier = Modifier,
    size: String = "default",
    shape: Shape = HyperProgressIndicatorDefaults.LinearShape,
    colors: HyperProgressIndicatorColors = HyperProgressIndicatorDefaults.colors(),
    trackBorder: BorderStroke? = null
)

@Composable
fun HyperCircularProgressIndicator(
    progress: Float? = null,
    modifier: Modifier = Modifier,
    size: String = "default",
    strokeWidth: Dp = HyperProgressIndicatorDefaults.CircularStrokeWidth,
    colors: HyperProgressIndicatorColors = HyperProgressIndicatorDefaults.colors()
)
```

## 最小用法

```kotlin
HyperLinearProgressIndicator(progress = progress)
HyperLinearProgressIndicator(progress = progress, size = "large")
HyperLinearProgressIndicator(progress = null)

HyperCircularProgressIndicator(progress = progress)
HyperCircularProgressIndicator(progress = progress, size = "small")
HyperCircularProgressIndicator(progress = null)
```

`size` 支持 `small`、`default`、`large`，分别调整轨道厚度或圆形指示器直径；`progress = null` 表示不确定进度。

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
