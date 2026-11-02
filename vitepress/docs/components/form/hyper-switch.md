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

开关保持 52×32dp，圆形滑块可见直径 22dp，轨道轮廓 1dp；滑块初始偏移 5dp、行程 20dp，逻辑方向随 RTL 反转。关闭色仍由 controlTrack 合成到 surfaceStrong，禁用合成到 surfaceSubtle。

滑块位置、轨道颜色和滑块颜色由同一个临界阻尼弹簧进度驱动，stiffness = 1000、dampingRatio = 1。快速反向切换从当前进度继续，不增加业务回调。按住轨道或标签行时立即显示按压高光；松开或取消后恢复。

滑块固定为玻璃，白色或自定义底色按 1−按压进度淡出；背景与轨道分别录制，模糊 8→0dp、折射边缘 0→5dp、位移 0→10dp，显示色散、高光与内阴影。轨道取样缩放为水平 2/3→3/4、垂直 0→3/4，只作用于取样图像；禁用或 onChanged = null 时保留原禁用配色，不响应按压、不提交回调。checkedTrackColor、uncheckedTrackColor、checkedThumbColor、uncheckedThumbColor 继续覆盖对应颜色。

提供 label 时使用 plain 标签行，不附带卡片背景、外框或阴影；基础示例不额外包裹 HyperCard。

**状态归属**

value 由调用方持有；标签行和开关继续通过原有点击入口在释放后请求切换，每次有效点击只回调一次。按压视觉不修改 value，不新增拖动切换，也不改变命中范围或标签行布局。

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
