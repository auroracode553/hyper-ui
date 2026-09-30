# HyperBatteryIndicator

包名：`hyper_ui`。

<WasmPreview demo="battery_indicator" title="HyperBatteryIndicator 交互预览" />

## 公开签名与默认值

```kotlin
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

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
