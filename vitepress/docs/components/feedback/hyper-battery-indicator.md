# HyperBatteryIndicator

`HyperBatteryIndicator` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

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

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- 公开 API 仅支持 Android 手机端；Preview 只是文档交互工具。
