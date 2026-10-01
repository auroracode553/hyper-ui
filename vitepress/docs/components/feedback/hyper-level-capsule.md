# HyperLevelCapsule

包名：`hyper_ui`。

<WasmPreview demo="level_capsule" title="HyperLevelCapsule 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperLevelCapsule(
    progress: Float,
    label: String,
    modifier: Modifier = Modifier,
    shape: Shape = HyperLevelCapsuleDefaults.Shape,
    colors: HyperLevelCapsuleColors = HyperLevelCapsuleDefaults.colors(),
    iconContent: (@Composable () -> Unit)? = null
)
```

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| progress | Float | 是 | — | 由调用方提供的进度值。 |
| label | String | 是 | — | 数值旁显示的标签文字。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| shape | Shape | 否 | HyperLevelCapsuleDefaults.Shape | 组件容器的形状。 |
| colors | HyperLevelCapsuleColors | 否 | HyperLevelCapsuleDefaults.colors() | 组件各状态的颜色配置。 |
| iconContent | (@Composable () -&gt; Unit)? | 否 | null | 图标区域的自定义内容。 |

## 最小用法

```kotlin
HyperLevelCapsule(
    progress = brightness,
    label = "${(brightness * 100).toInt()}%",
    iconContent = {
        Icon(
            painter = painterResource(LucideR.drawable.lucide_ic_sun),
            contentDescription = null
        )
    },
    modifier = Modifier
        .align(Alignment.CenterStart)
        .padding(16.dp)
)
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
