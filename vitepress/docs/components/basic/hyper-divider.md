# HyperDivider

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="divider" title="HyperDivider 交互预览" />

## 样式约定

默认 1dp 水平实线。dashed 绘制 4dp 线段和 4dp 间隔；vertical 默认长度 24dp。

## 状态归属与使用约束

长度和缩进不能为负，thickness 必须大于 0。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperDivider(type = "dashed", indent = 14.dp, endIndent = 14.dp)
```

## HyperDivider API / Props

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
