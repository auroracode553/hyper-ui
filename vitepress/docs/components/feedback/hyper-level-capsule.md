# HyperLevelCapsule

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/progress/HyperLevelCapsule.kt`
- 预览：`level_capsule`

`HyperLevelCapsule` 是竖向连续比例反馈组件，适合播放器亮度、音量等短时状态提示。组件只渲染比例和文案，不处理手势、窗口亮度、系统音量、显示时机或自动隐藏。

## 公开签名

```kotlin
data class HyperLevelCapsuleColors(
    val containerColor: Color,
    val progressColor: Color,
    val labelColor: Color,
    val borderColor: Color
)

@Composable
fun HyperLevelCapsule(
    progress: Float,
    label: String,
    modifier: Modifier = Modifier,
    shape: Shape = HyperLevelCapsuleDefaults.Shape,
    colors: HyperLevelCapsuleColors = HyperLevelCapsuleDefaults.colors()
)
```

## 默认值

```kotlin
object HyperLevelCapsuleDefaults {
    val Width = 40.dp
    val Height = 140.dp
    val Shape: Shape = RoundedCornerShape(percent = 50)
    val BorderWidth = 1.dp
    val ContentInset = 2.dp

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        progressColor: Color = Color.Unspecified,
        labelColor: Color = Color.Unspecified,
        borderColor: Color = Color.Unspecified
    ): HyperLevelCapsuleColors
}
```

## 最小用法

```kotlin
HyperLevelCapsule(
    progress = brightness,
    label = "${(brightness * 100).toInt()}%",
    modifier = Modifier
        .align(Alignment.CenterStart)
        .padding(16.dp)
)
```

## 状态与约束

- `progress` 由调用方持有，组件会将其限制到 `0f..1f`，并从底部向上填充。
- `label` 由调用方格式化；组件按单行居中显示，不内置百分比、亮度或音量文案。
- 手势监听、显示/隐藏、延时关闭以及系统亮度/音量更新均属于调用方业务。
- 默认尺寸为 `40×140dp`；使用 `modifier.width(...)` 和 `modifier.height(...)` 覆盖。
- 默认容器为半透明黑色胶囊、填充和描边为白色、文字使用 `HyperColors.accent`。
- 组件提供确定进度语义，便于无障碍服务读取当前比例。

<WasmPreview demo="level_capsule" title="HyperLevelCapsule 交互预览" />
