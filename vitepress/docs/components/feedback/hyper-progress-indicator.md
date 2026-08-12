# HyperProgressIndicator

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/progress/HyperProgressIndicators.kt`
- 预览：`progress`

HyperUI 提供线性和圆形进度指示器。`progress` 为 `0f..1f` 表示确定进度，`null` 表示不确定加载。轨道、指示段和线性描边均使用不透明实色，所有进度状态均即时渲染，不执行补间、旋转或循环动画。

需要用户点击或拖动进度时，应使用 [HyperSlider](../form/hyper-slider.md)，不要给只读进度指示器叠加调用方手势和样式。

## 公开签名

```kotlin
data class HyperProgressIndicatorColors(
    val trackColor: Color,
    val indicatorColor: Color
)

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
    val LinearHeight = 4.dp
    val LinearShape: Shape = RoundedCornerShape(percent = 50)
    val CircularSize = 32.dp
    val CircularStrokeWidth = 3.dp
    const val IndeterminateSegmentFraction = 0.36f
    const val CircularIndeterminateSweepFraction = 0.26f

    @Composable
    fun colors(
        trackColor: Color = Color.Unspecified,
        indicatorColor: Color = Color.Unspecified
    ): HyperProgressIndicatorColors

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
- `progress = null` 时线性组件显示居中的静态指示段，圆形组件显示静态弧段；语义仍保持 `Indeterminate`。

<WasmPreview demo="progress" title="HyperProgressIndicator 交互预览" />
