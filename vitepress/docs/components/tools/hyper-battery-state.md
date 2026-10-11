---
description: Android 电池状态读取工具；浏览器 Preview 使用可交互模拟。
---

# HyperBatteryState

Android 电池状态读取工具；浏览器 Preview 使用可交互模拟。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="battery_indicator" title="HyperBatteryState 状态模拟" />

## 使用方式

<details class="component-doc__details">
<summary>Compose 最小调用</summary>

```kotlin
val batteryState by rememberHyperBatteryState()

if (batteryState.isAvailable) {
    HyperBatteryIndicator(
        percentage = batteryState.percentage,
        charging = batteryState.isCharging
    )
}
```

</details>

<details class="component-doc__details">
<summary>非 Compose 用法</summary>

```kotlin
val batteryState = readHyperBatteryState(context)
if (batteryState.isAvailable) {
    updateBatteryStatus(
        percentage = batteryState.percentage,
        charging = batteryState.isCharging
    )
}
```

</details>

## 使用约定

**状态与约束**

- `percentage` 会被规范到 `0..100`，并使用系统的 `EXTRA_LEVEL / EXTRA_SCALE` 计算。
- `isCharging` 在系统报告正在充电，或已充满且仍连接电源时为 `true`。
- 系统没有返回有效粘性广播或电量数据时返回 `HyperBatteryState.Unavailable`，此时 `isAvailable = false`。
- `rememberHyperBatteryState` 持有并返回 Compose `State`，广播变化会触发读取该状态的界面重组。
- 工具不申请权限、不持有 Activity Context，也不负责渲染电池图标。
- `readHyperBatteryState` 只读取调用当下状态，不会持续监听。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperBatteryState

<details class="component-doc__details">
<summary>查看公开签名</summary>

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

</details>

### HyperBatteryState 返回字段

| 字段 | 类型 | 作用 |
| --- | --- | --- |
| percentage | Int | 电量百分比，范围为 0 至 100。 |
| isCharging | Boolean | 是否正在充电或已充满且连接电源。 |
| isAvailable | Boolean | 系统是否提供有效电量数据。 |
| Unavailable | HyperBatteryState | 无法读取电量时使用的状态。 |

### readHyperBatteryState

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| context | Context | 是 | — | 用于读取系统电量状态的 Android Context。 |
