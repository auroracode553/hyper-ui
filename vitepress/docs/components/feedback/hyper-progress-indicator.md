# HyperProgressIndicator

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/progress/HyperProgressIndicators.kt`
- 预览：`progress`

HyperUI 提供线性和圆形进度指示器。`progress` 为 `0f..1f` 表示确定进度，`null` 表示不确定加载。轨道、指示段和线性描边均使用不透明实色，不再叠加玻璃高光。

需要用户点击或拖动进度时，应使用 [HyperSlider](../form/hyper-slider.md)，不要给只读进度指示器叠加调用方手势和样式。

## 公开签名

```kotlin
@Composable
fun HyperLinearProgressIndicator(
    progress: Float?,
    modifier: Modifier = Modifier,
    shape: Shape = HyperProgressIndicatorDefaults.LinearShape,
    colors: HyperProgressIndicatorColors = HyperProgressIndicatorDefaults.colors(),
    trackBorder: BorderStroke? = HyperProgressIndicatorDefaults.linearTrackBorder()
)

@Composable
fun HyperCircularProgressIndicator(
    progress: Float?,
    modifier: Modifier = Modifier,
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
- 线性高度和圆形尺寸使用 `modifier.height(...)`、`modifier.size(...)` 定制；默认值仍由 `HyperProgressIndicatorDefaults` 提供。
- 颜色通过 `HyperProgressIndicatorDefaults.colors(trackColor, indicatorColor)` 配置。
- 默认轨道使用不透明的 `HyperColors.softContainer`；含 alpha 的自定义轨道色和指示色会先与对应实色背景合成。
- 线性轨道描边通过 `trackBorder` 配置；默认来自 `HyperProgressIndicatorDefaults.linearTrackBorder()`，传 `null` 可关闭。
- 圆形进度指示器本身是 stroke 图形，不额外渲染外框。
- `progress` 会被限制在 `0f..1f`。

<WasmPreview demo="progress" title="HyperProgressIndicator 交互预览" />
