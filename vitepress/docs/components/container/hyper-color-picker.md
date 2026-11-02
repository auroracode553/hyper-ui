---
description: 通过连续 HSV 调色板选择颜色，支持点击和拖动取色。
---

# HyperColorPicker

包名 hyper_ui。Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于文档 Preview。

<WasmPreview demo="color-picker" title="HyperColorPicker 交互预览" />

## 使用方式

```kotlin
var color by remember { mutableStateOf(Color(0.25f, 0.47f, 1f, 1f)) }

HyperColorPicker(
    value = color,
    onChanged = { color = it }
)
```

## 取色与状态

上方为 160dp 高的饱和度／明度渐变区：水平方向从低饱和到高饱和，垂直方向从亮到暗。下方 32dp 高的色相条覆盖 0–360 度。两个区域间距 12dp、圆角 10dp，宽度跟随宿主；定位环标记当前选择，下方提供 28dp 颜色样本和只读颜色值。

按下立即取色，拖动期间持续调用 onChanged，松手结束。拖出区域时按边界取值；取消手势不撤销已通知的颜色。颜色由调用方持有，外部更新 value 后定位环和颜色值同步更新，组件不修改或保存应用主题，也不改变深浅色选择。

无彩色保留上次编辑色相，黑色还保留上次编辑饱和度，使拖回有色区域时可以继续调整。首次输入无彩色时色相起点为 0 度，黑色的饱和度起点为 1。颜色运算使用 sRGB；输入 alpha 原样保留，不提供透明度编辑。颜色值显示 #RRGGBB，有透明度时显示 #RRGGBBAA。

enabled = false 时整体透明度为 0.45，保留显示状态，不响应取色、不调用 onChanged。value 不允许 Color.Unspecified。组件不提供预设色块、颜色名称列表或尺寸档位。

## 公开 API

```kotlin
@Composable
fun HyperColorPicker(
    value: Color,
    onChanged: (Color) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | Color | 是 | — | 当前颜色，由调用方持有；不接受 Color.Unspecified。 |
| onChanged | (Color) -&gt; Unit | 是 | — | 按下和拖动取色时返回颜色，保留输入 alpha。 |
| modifier | Modifier | 否 | Modifier | 外部宽度、位置与间距。 |
| enabled | Boolean | 否 | true | 是否允许取色；禁用时保留颜色显示。 |
