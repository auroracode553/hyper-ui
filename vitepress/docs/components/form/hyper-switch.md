---
description: 即时切换二元设置，当前值与变化回调由调用方提供。
---

# HyperSwitch

即时切换二元设置，当前值与变化回调由调用方提供。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="switch" title="HyperSwitch 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperSwitch(value, { value = it })
```

</details>

## 使用约定

**样式约定**

开关 52×32dp，圆形滑块实际可见 22dp，轨道轮廓 1dp；160ms selectionCurve。关闭色为 controlTrack 合成到 surfaceStrong，禁用合成到 surfaceSubtle。

提供 label 时使用 plain 标签行，不附带卡片背景、外框或阴影；基础示例不额外包裹 HyperCard。

**状态归属**

value 由调用方持有；禁用时不派发回调。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperSwitch

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperSwitch(
    value: Boolean,
    onChanged: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedTrackColor: Color = Color.Unspecified,
    uncheckedTrackColor: Color = Color.Unspecified,
    checkedThumbColor: Color = Color(1f, 1f, 1f, 1f),
    uncheckedThumbColor: Color = Color.Unspecified,
    label: String? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | Boolean | 是 | — | 由调用方持有的当前值。 |
| onChanged | ((Boolean) -&gt; Unit)? | 否 | null | 用户修改值或选择项时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| checkedTrackColor | Color | 否 | Color.Unspecified | 开启时轨道颜色。 |
| uncheckedTrackColor | Color | 否 | Color.Unspecified | 关闭时轨道颜色。 |
| checkedThumbColor | Color | 否 | Color(1f, 1f, 1f, 1f) | 开启时滑块颜色。 |
| uncheckedThumbColor | Color | 否 | Color.Unspecified | 关闭时滑块颜色。 |
| label | String? | 否 | null | 无卡片的 plain 标签行，标签与开关均可操作。 |
