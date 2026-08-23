# HyperBatteryIndicator

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/feedback/HyperBatteryIndicator.kt`
- 充电图标：Lucide Android `zap`
- 预览：`battery_indicator`

`HyperBatteryIndicator` 是紧凑系统状态电池组件。电量百分比显示在电池内部，电量按比例填充；充电时使用充电色并在电池右侧展示独立闪电图标。组件保持纯渲染；Android 项目可通过 [HyperBatteryState 工具](../tools/hyper-battery-state.md) 获取系统状态。

## 公开签名

```kotlin
data class HyperBatteryIndicatorColors(
    val containerColor: Color,
    val levelColor: Color,
    val lowLevelColor: Color,
    val chargingLevelColor: Color,
    val percentageColor: Color,
    val terminalColor: Color,
    val chargingIconColor: Color
)

@Composable
fun HyperBatteryIndicator(
    percentage: Int,
    charging: Boolean,
    modifier: Modifier = Modifier,
    showPercentage: Boolean = true,
    contentDescription: String? = null,
    shape: Shape = HyperBatteryIndicatorDefaults.Shape,
    colors: HyperBatteryIndicatorColors = HyperBatteryIndicatorDefaults.colors()
)
```

## 默认值

```kotlin
object HyperBatteryIndicatorDefaults {
    val BodyWidth = 38.dp
    val Height = 18.dp
    val Shape: Shape = RoundedCornerShape(4.dp)
    val LevelShape: Shape = RoundedCornerShape(2.dp)
    val BodyInset = 2.dp
    val Elevation = 1.dp
    val TerminalWidth = 2.dp
    val TerminalHeight = 8.dp
    val TerminalShape: Shape = RoundedCornerShape(percent = 50)
    val TerminalElevation = 1.dp
    val TerminalSpacing = 1.dp
    val ElementSpacing = 4.dp
    val ChargingIconWidth = 14.dp
    val ChargingIconHeight = 14.dp
    const val LowLevelThreshold = 20

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        levelColor: Color = Color.Unspecified,
        lowLevelColor: Color = Color.Unspecified,
        chargingLevelColor: Color = Color.Unspecified,
        percentageColor: Color = Color.Unspecified,
        terminalColor: Color = Color.Unspecified,
        chargingIconColor: Color = Color.Unspecified
    ): HyperBatteryIndicatorColors
}
```

## 最小用法

```kotlin
val batteryState by rememberHyperBatteryState()

if (batteryState.isAvailable) {
    HyperBatteryIndicator(
        percentage = batteryState.percentage,
        charging = batteryState.isCharging,
        contentDescription = "电量 ${batteryState.percentage}%"
    )
}
```

## 状态与约束

- `percentage` 由调用方提供并自动限制在 `0..100`；组件本身不会注册 Android 电池广播。
- `charging = true` 时使用 `chargingLevelColor`，并通过 Lucide Android 的 `zap` VectorDrawable 把闪电显示在电池端子右侧，而非覆盖内部百分比。
- 非充电且电量不高于 `LowLevelThreshold` 时使用 `lowLevelColor`。
- `showPercentage = false` 可隐藏内部数字，但仍保留电量填充与进度语义。
- 建议调用方传入本地化的 `contentDescription`，其中包含电量和充电状态。
- 默认电池主体为 `38×18dp`；Lucide 充电图标出现后组件整体宽度会自然增加。
- 主体和端子均使用无描边的连续玻璃面，内部预留 `2.dp` 让电量填充与壳体自然分层；信息组件仅使用 `1.dp` 轻抬升，不呈现为按钮。

<WasmPreview demo="battery_indicator" title="HyperBatteryIndicator 交互预览" />
