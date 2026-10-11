---
description: 用于分隔相邻内容，支持实线、虚线和横竖方向。
---

# HyperDivider

用于分隔相邻内容，支持实线、虚线和横竖方向。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="divider" title="HyperDivider 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperDivider(type = "dashed", indent = 14.dp, endIndent = 14.dp)
```

</details>

## 使用约定

**样式约定**

默认 1dp 水平实线。dashed 绘制 4dp 线段和 4dp 间隔；vertical 默认长度 24dp。

**状态归属**

长度和缩进不能为负，thickness 必须大于 0。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperDivider

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperDivider(
    modifier: Modifier = Modifier,
    color: Color = HyperColors.divider,
    thickness: Dp = 1.dp,
    type: String = "solid",
    orientation: String = "horizontal",
    indent: Dp = 0.dp,
    endIndent: Dp = 0.dp,
    length: Dp? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| color | Color | 否 | HyperColors.divider | 颜色覆盖。 |
| thickness | Dp | 否 | 1.dp | 分隔线厚度。 |
| type | String | 否 | "solid" | 渲染形态；合法值见本页样式约定。 |
| orientation | String | 否 | "horizontal" | horizontal 或 vertical。 |
| indent | Dp | 否 | 0.dp | 起始侧缩进。 |
| endIndent | Dp | 否 | 0.dp | 结束侧缩进。 |
| length | Dp? | 否 | null | 分隔线长度；水平默认填满、竖向默认 24dp。 |
