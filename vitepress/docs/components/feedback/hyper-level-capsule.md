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
