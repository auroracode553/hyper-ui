# HyperLevelCapsule

`HyperLevelCapsule` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

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

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- 公开 API 仅支持 Android 手机端；Preview 只是文档交互工具。
