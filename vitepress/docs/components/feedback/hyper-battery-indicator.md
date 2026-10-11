---
description: 显示电量与充电状态，支持百分比文字和自定义配色。
---

# HyperBatteryIndicator

显示电量与充电状态，支持百分比文字和自定义配色。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="battery_indicator" title="HyperBatteryIndicator 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

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

</details>

## 使用约定

**约束**

业务状态由调用方管理，组件只负责 UI 与回调。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperBatteryIndicator

<details class="component-doc__details">
<summary>查看公开签名</summary>

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

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| percentage | Int | 是 | — | 电量百分比，范围为 0 至 100。 |
| charging | Boolean | 是 | — | 当前是否正在充电。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| showPercentage | Boolean | 否 | true | 是否显示电量百分比文字。 |
| contentDescription | String? | 否 | null | 无障碍内容描述。 |
| shape | Shape | 否 | HyperBatteryIndicatorDefaults.Shape | 组件容器的形状。 |
| colors | HyperBatteryIndicatorColors | 否 | HyperBatteryIndicatorDefaults.colors() | 组件各状态的颜色配置。 |
