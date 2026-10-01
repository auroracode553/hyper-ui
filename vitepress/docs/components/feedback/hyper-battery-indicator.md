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

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| percentage | Int | 是 | — | 电量百分比，范围为 0 至 100。 |
| charging | Boolean | 是 | — | 当前是否正在充电。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| showPercentage | Boolean | 否 | true | 是否显示电量百分比文字。 |
| contentDescription | String? | 否 | null | 无障碍内容描述。 |
| shape | Shape | 否 | HyperBatteryIndicatorDefaults.Shape | 组件容器的形状。 |
| colors | HyperBatteryIndicatorColors | 否 | HyperBatteryIndicatorDefaults.colors() | 组件各状态的颜色配置。 |

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
