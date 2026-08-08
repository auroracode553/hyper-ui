# HyperProgressIndicator

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/progress/HyperProgressIndicators.kt`
- 预览：`progress`

HyperUI 提供线性和圆形进度指示器。`progress` 为 `0f..1f` 表示确定进度，`null` 表示不确定加载。

## 公开签名

```kotlin
@Composable
fun HyperLinearProgressIndicator(
    progress: Float?,
    modifier: Modifier = Modifier,
    height: Dp = HyperProgressIndicatorDefaults.LinearHeight,
    shape: Shape = HyperProgressIndicatorDefaults.LinearShape,
    colors: HyperProgressIndicatorColors = HyperProgressIndicatorDefaults.colors()
)

@Composable
fun HyperCircularProgressIndicator(
    progress: Float?,
    modifier: Modifier = Modifier,
    size: Dp = HyperProgressIndicatorDefaults.CircularSize,
    strokeWidth: Dp = HyperProgressIndicatorDefaults.CircularStrokeWidth,
    colors: HyperProgressIndicatorColors = HyperProgressIndicatorDefaults.colors()
)
```

## 最小用法

```kotlin
HyperLinearProgressIndicator(progress = progress)
HyperLinearProgressIndicator(progress = null)

HyperCircularProgressIndicator(progress = progress)
HyperCircularProgressIndicator(progress = null)
```

## 约束

- 不再提供 `HyperProgressBar` 或 `HyperLoadingProgress`。
- 颜色通过 `HyperProgressIndicatorDefaults.colors(trackColor, indicatorColor)` 配置。
- `progress` 会被限制在 `0f..1f`。

<WasmPreview demo="progress" title="HyperProgressIndicator 交互预览" />
