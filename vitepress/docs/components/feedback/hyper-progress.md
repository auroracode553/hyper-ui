# HyperProgress

包名：`hyper_ui`。

<WasmPreview demo="progress" title="HyperProgress 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperProgress(
    progress: Float? = null,
    modifier: Modifier = Modifier,
    type: String = "linear",
    size: String = "default",
    shape: Shape = HyperProgressDefaults.LinearShape,
    strokeWidth: Dp = HyperProgressDefaults.CircularStrokeWidth,
    colors: HyperProgressColors = HyperProgressDefaults.colors(),
    trackBorder: BorderStroke? = null
)
```

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| progress | Float? | 否 | null | 由调用方提供的进度值。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| type | String | 否 | &quot;linear&quot; | 组件的视觉或布局形态。 |
| size | String | 否 | &quot;default&quot; | 组件尺寸档位。 |
| shape | Shape | 否 | HyperProgressDefaults.LinearShape | 组件容器的形状。 |
| strokeWidth | Dp | 否 | HyperProgressDefaults.CircularStrokeWidth | 圆形进度条的描边宽度。 |
| colors | HyperProgressColors | 否 | HyperProgressDefaults.colors() | 组件各状态的颜色配置。 |
| trackBorder | BorderStroke? | 否 | null | 进度轨道的边框配置。 |

## 最小用法

```kotlin
HyperProgress(type = "linear", progress = progress)
HyperProgress(type = "linear", progress = progress, size = "large")
HyperProgress(type = "linear", progress = null)

HyperProgress(type = "circular", progress = progress)
HyperProgress(type = "circular", progress = progress, size = "small")
HyperProgress(type = "circular", progress = null)
```

`size` 支持 `small`、`default`、`large`，分别调整轨道厚度或圆形指示器直径；`progress = null` 表示不确定进度。

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
