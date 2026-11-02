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

开关保持 52×32dp、28dp 圆形滑块、四周留白 2dp 与 20dp 行程；逻辑方向随 RTL 反转。轨道复用 Button 的玻璃表面，默认开启色为主题 accent、alpha 浅色 0.74/深色 0.64；关闭为白色玻璃、alpha 浅色 0.46/深色 0.12。无实体边框，外缘使用共用 0.5dp 柔和高光；模糊 8dp、折射边缘 12dp、位移 16dp、阴影半径 6dp。显式颜色覆盖仍生效，外部背景与玻璃轨道独立录制，不采样滑块自身。

位置与按压进度使用 dampingRatio 1、stiffness 1000、阈值 0.001 的弹簧。按下或位置距目标至少 0.025 时保持扩张，横纵轴目标均为 1.5，分别使用 dampingRatio 0.6/0.7、stiffness 250；接近目标后才回弹。位置采样速度以 0.5/300 弹簧平滑，再用 v/50 的横向除法与纵向乘法产生速度形变，系数分别为 0.75/0.25、各截断至 ±0.2。只变换滑块绘制层，不缩放整条开关、不降低整体透明度，也不裁掉超出轨道的弹性部分。

白色或自定义滑块底色的 alpha 乘以 0.92−0.72×按压进度，静止为轻透白色、按住仍保留 0.2 的底面，确保拖动时镜片清晰。独立背景与轨道组合采样；首个 Clamp 模糊不扩展采样画布，模糊 8→0dp、折射边缘 0→5dp、位移 0→10dp。启用折射时七波段色散强度固定为 1。轨道取样缩放为水平 2/3→3/4、垂直 0→3/4，只作用于取样图像。Ambient 高光以轮廓法线和 45° 方向计算黑白强度，基础 alpha 0.38、宽 0.5/1.5dp、遮罩模糊 0.25/1.5dp，随按压进度淡入；内阴影位于其上，半径与向下偏移均为 4dp×进度、黑色 alpha 0.15×进度。滑块外阴影半径 4dp、向下 4/6dp、黑色 alpha 浅色 0.08/深色 0.18，与共用玻璃深度一致。禁用或 onChanged=null 时使用静态弱化配色，无按压、拖动或业务回调；颜色覆盖继续生效。

提供 label 时使用 plain 标签行，不附带卡片背景、外框或阴影；基础示例不额外包裹 HyperCard。

**状态归属**

value 由调用方持有。点击释放后请求取反；按住不松手后仍可开始横向拖动，按 20dp 行程及 RTL 方向连续更新临时目标，允许停在中间或反向拖动，松手以目标是否达到 0.5 请求切换一次。拖动期间不提交业务状态，取消或纵向滚动接管时恢复受控值，不提交。标签行继续保留整行点击入口，拖动不会叠加点击回调；禁用不交互。

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
