# HyperPlaybackSpeedScale

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/feedback/HyperPlaybackSpeedScale.kt`
- 预览：`playback_speed_scale`

`HyperPlaybackSpeedScale` 是播放器长按临时加速使用的紧凑横向固定深色玻璃刻度。默认八档时，首尾档位位于轨道左右，中间档位位于轨道上方，当前倍速在下方居中显示；组件在常见手机宽度下约为 `310dp × 59dp`。组件只负责展示候选档位、当前高亮档位和实时倍速；内部轨道统一复用只读 `HyperSlider` 的分段点与三层圆点视觉，长按识别、横向拖动、临时设置播放器速度以及松手恢复仍由调用方处理。

## 公开签名

```kotlin
data class HyperPlaybackSpeedScaleColors(
    val containerColor: Color,
    val trackColor: Color,
    val activeTrackColor: Color,
    val tickColor: Color,
    val selectedTickColor: Color,
    val labelColor: Color,
    val selectedLabelColor: Color,
    val valueColor: Color
)

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

## 默认值与工具

```kotlin
object HyperPlaybackSpeedScaleDefaults {
    val SpeedOptions = listOf(0.25f, 0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 4f)
    val DragStep = 42.dp
    val MaxWidth = 360.dp
    const val WidthFraction = 0.86f
    val Shape: Shape = RoundedCornerShape(percent = 50)
    val Elevation = 6.dp
    val ContentPadding = PaddingValues(horizontal = 12.dp, vertical = 5.dp)
    val TrackHeight = 22.dp
    val TrackStrokeWidth = 3.dp
    val TrackThumbSize = 12.dp
    val TrackMarkerSize = 4.dp
    val LabelWidth = 28.dp
    val LeadingIconSize = 13.dp

    fun closestSpeed(
        targetSpeed: Float,
        speedOptions: List<Float> = SpeedOptions
    ): Float

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        trackColor: Color = Color.Unspecified,
        activeTrackColor: Color = Color.Unspecified,
        tickColor: Color = Color.Unspecified,
        selectedTickColor: Color = Color.Unspecified,
        labelColor: Color = Color.Unspecified,
        selectedLabelColor: Color = Color.Unspecified,
        valueColor: Color = Color.Unspecified
    ): HyperPlaybackSpeedScaleColors
}
```

## 最小用法

```kotlin
var temporarySpeed by remember { mutableStateOf(2f) }

HyperPlaybackSpeedScale(
    selectedSpeed = temporarySpeed,
    modifier = Modifier.align(Alignment.Center)
)
```

## 状态与约束

- `selectedSpeed` 由调用方持有；若值不完全等于某个档位，组件会高亮数值最近的档位，同时底部仍展示原始速度。
- 默认档位覆盖 `0.25x` 到 `4x`。可通过 `speedOptions` 替换，但应保持由小到大且至少包含一个值。
- `DragStep` 是调用方实现横向离散调速时可复用的推荐手势距离，组件自身不拦截指针事件。
- `closestSpeed` 可将初始速度或拖动结果吸附到最近档位。
- 刻度轨道通过 `HyperSlider(readOnly = true)` 渲染，档位列表会映射为显式 `segmentValues`，因此与通用滑块保持同一套轨道、分段点和圆点样式。
- `leadingContent` 为空时显示内置双箭头；传入图标时通过 `LocalContentColor` 接收 `valueColor`。
- 默认先将可用宽度限制到 `360.dp`，再取其 86%，因此空间充足时最终宽度约为 `310.dp`；内部内容高 `49.dp`，加上下内边距后约为 `59dp`。可用 `modifier` 继续约束外部尺寸与位置。
- 默认八档时，`0.25x` 与 `4x` 位于轨道左右，中间六档与分段点对齐排列在轨道上方，避免为所有标签单独占用一整行宽度。
- 组件提供确定进度语义，当前档位会映射为无障碍进度。
- 默认容器固定为深色 `Color(0.10f, 0.11f, 0.14f, 0.96f)`，主要内容固定为近白色，不读取外层 `MaterialTheme` 的明暗模式；强调色仍来自 `HyperThemeConfig`。
- 固定深色玻璃使用 `0.12f` 顶部柔光、`0.12f` 底部折射和一层 `6.dp`、`0.30f` 黑色投影；没有边框或独立高光色入口。

<WasmPreview demo="playback_speed_scale" title="HyperPlaybackSpeedScale 交互预览" />
