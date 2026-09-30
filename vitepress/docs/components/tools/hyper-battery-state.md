# HyperBatteryState

包名：`hyper_ui`。

`HyperBatteryState` 读取 Android 电量百分比和充电状态。普通代码一次读取，Compose 页面使用订阅版本；Wasm 预览只模拟状态。

<WasmPreview demo="battery_indicator" title="HyperBatteryState 状态模拟" />

## 公开签名

```kotlin
@Immutable
data class HyperBatteryState(
    val percentage: Int,
    val isCharging: Boolean,
    val isAvailable: Boolean
) {
    companion object {
        val Unavailable: HyperBatteryState
    }
}

fun readHyperBatteryState(context: Context): HyperBatteryState

@Composable
fun rememberHyperBatteryState(): State<HyperBatteryState>
```

## Compose 最小用法

```kotlin
val batteryState by rememberHyperBatteryState()

if (batteryState.isAvailable) {
    HyperBatteryIndicator(
        percentage = batteryState.percentage,
        charging = batteryState.isCharging
    )
}
```

## 非 Compose 用法

```kotlin
val batteryState = readHyperBatteryState(context)
if (batteryState.isAvailable) {
    updateBatteryStatus(
        percentage = batteryState.percentage,
        charging = batteryState.isCharging
    )
}
```

## 状态与约束

- `percentage` 会被规范到 `0..100`，并使用系统的 `EXTRA_LEVEL / EXTRA_SCALE` 计算。
- `isCharging` 在系统报告正在充电，或已充满且仍连接电源时为 `true`。
- 系统没有返回有效粘性广播或电量数据时返回 `HyperBatteryState.Unavailable`，此时 `isAvailable = false`。
- `rememberHyperBatteryState` 持有并返回 Compose `State`，广播变化会触发读取该状态的界面重组。
- 工具不申请权限、不持有 Activity Context，也不负责渲染电池图标。
- `readHyperBatteryState` 只读取调用当下状态，不会持续监听。
