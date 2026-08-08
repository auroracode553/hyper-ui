# HyperProgressIndicator

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/progress/HyperProgressIndicators.kt`
- 预览：`progress`

HyperUI 提供线性和圆形进度指示器。`progress` 为 `0f..1f` 表示确定进度，`null` 表示不确定加载。线性进度轨道默认带轻描边，白底下仍能看清轨道范围。

## 公开签名

```kotlin
@Composable
fun HyperLinearProgressIndicator(
    progress: Float?,
    modifier: Modifier = Modifier,
    height: Dp = HyperProgressIndicatorDefaults.LinearHeight,
    shape: Shape = HyperProgressIndicatorDefaults.LinearShape,
    colors: HyperProgressIndicatorColors = HyperProgressIndicatorDefaults.colors(),
    trackBorder: BorderStroke? = HyperProgressIndicatorDefaults.linearTrackBorder()
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

## 关键公开类型

```kotlin
object HyperProgressIndicatorDefaults {
    @Composable
    fun linearTrackBorder(color: Color = Color.Unspecified): BorderStroke
}
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
- 线性轨道描边通过 `trackBorder` 配置；默认来自 `HyperProgressIndicatorDefaults.linearTrackBorder()`，传 `null` 可关闭。
- 圆形进度指示器本身是 stroke 图形，不额外渲染外框。
- `progress` 会被限制在 `0f..1f`。

<WasmPreview demo="progress" title="HyperProgressIndicator 交互预览" />
