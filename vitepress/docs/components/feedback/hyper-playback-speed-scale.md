---
description: 通过拖动刻度选择播放速度，并将数值变化回传调用方。
---

# HyperPlaybackSpeedScale

通过拖动刻度选择播放速度，并将数值变化回传调用方。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="playback_speed_scale" title="HyperPlaybackSpeedScale 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
var temporarySpeed by remember { mutableStateOf(2f) }

HyperPlaybackSpeedScale(
    value = temporarySpeed,
    modifier = Modifier.align(Alignment.Center)
)
```

</details>

## 使用约定

**约束**

业务状态由调用方管理，组件只负责 UI 与回调。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperPlaybackSpeedScale

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperPlaybackSpeedScale(
    value: Float,
    modifier: Modifier = Modifier,
    options: List<Float> = HyperPlaybackSpeedScaleDefaults.SpeedOptions,
    shape: Shape = HyperPlaybackSpeedScaleDefaults.Shape,
    colors: HyperPlaybackSpeedScaleColors = HyperPlaybackSpeedScaleDefaults.colors(),
    leading: (@Composable () -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | Float | 是 | — | 由调用方持有的当前值。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| options | List&lt;Float&gt; | 否 | HyperPlaybackSpeedScaleDefaults.SpeedOptions | 可供选择的选项列表。 |
| shape | Shape | 否 | HyperPlaybackSpeedScaleDefaults.Shape | 组件容器的形状。 |
| colors | HyperPlaybackSpeedScaleColors | 否 | HyperPlaybackSpeedScaleDefaults.colors() | 组件各状态的颜色配置。 |
| leading | (@Composable () -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
