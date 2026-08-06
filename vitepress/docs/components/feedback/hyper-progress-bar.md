# HyperProgressBar 与 HyperLoadingProgress

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/progress/HyperProgressBar.kt`
- 状态归属：调用方提供确定进度；不确定动画由组件管理
- Preview ID：`progress`

支持 `0f..1f` 确定进度和 `null` 不确定加载状态。

## 公开签名

```kotlin
@Composable
fun HyperProgressBar(
    progress: Float?,
    modifier: Modifier = Modifier,
    height: Dp = HyperProgressBarDefaults.Height,
    shape: Shape = HyperProgressBarDefaults.Shape,
    trackColor: Color = Color.Unspecified,
    progressColor: Color = Color.Unspecified
)

@Composable
fun HyperLoadingProgress(
    modifier: Modifier = Modifier,
    height: Dp = HyperProgressBarDefaults.Height,
    shape: Shape = HyperProgressBarDefaults.Shape,
    trackColor: Color = Color.Unspecified,
    progressColor: Color = Color.Unspecified
)
```

`HyperLoadingProgress(...)` 等价于 `HyperProgressBar(progress = null, ...)`。

## 参数

| 参数 | 默认值 | 说明 |
| --- | --- | --- |
| `progress` | 必填 | `null` 为不确定进度；非空值会限制到 `0f..1f` |
| `modifier` | `Modifier` | 根进度轨道修饰符 |
| `height` | `4.dp` | 进度条高度 |
| `shape` | 50% 圆角 | 轨道和进度形状 |
| `trackColor` | `Color.Unspecified` | 未指定时使用 `HyperColors.softContainer` |
| `progressColor` | `Color.Unspecified` | 未指定时使用 `HyperColors.accent` |

## 最小用法

```kotlin
HyperProgressBar(progress = 0.65f)

HyperLoadingProgress(
    modifier = Modifier.width(240.dp)
)
```

## 默认值 API

```kotlin
object HyperProgressBarDefaults {
    val Height = 4.dp
    val Shape: Shape = RoundedCornerShape(percent = 50)
    const val ProgressAnimationMillis = 180
    const val IndeterminateAnimationMillis = 1100
    const val IndeterminateSegmentFraction = 0.36f
}
```

## 行为与约束

- `progress < 0f` 显示为 0，`progress > 1f` 显示为 1。
- 确定进度变化使用 180ms 线性动画。
- 组件会设置正确的确定/不确定进度语义。

## 交互预览

<WasmPreview demo="progress" title="HyperProgressBar 交互预览" />
