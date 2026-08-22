# HyperLevelCapsule

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/progress/HyperLevelCapsule.kt`
- 预览：`level_capsule`

`HyperLevelCapsule` 是竖向连续比例反馈组件，适合播放器亮度、音量等短时状态提示。组件使用半透明分层、柔光边缘和主题色填充形成柔性玻璃风格，并提供可选图标插槽。组件不处理手势、窗口亮度、系统音量、显示时机或自动隐藏。

## 公开签名

```kotlin
data class HyperLevelCapsuleColors(
    val containerColor: Color,
    val progressColor: Color,
    val labelColor: Color,
    val iconColor: Color
)

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

## 默认值

```kotlin
object HyperLevelCapsuleDefaults {
    val Width = 52.dp
    val Height = 156.dp
    val Shape: Shape = RoundedCornerShape(percent = 50)
    val ContentInset = 3.dp
    val ContentSpacing = 6.dp
    val IconSize = 20.dp
    val Elevation = 8.dp

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        progressColor: Color = Color.Unspecified,
        labelColor: Color = Color.Unspecified,
        iconColor: Color = Color.Unspecified
    ): HyperLevelCapsuleColors
}
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

## 状态与约束

- `progress` 由调用方持有，组件会将其限制到 `0f..1f`，并从底部向上填充。
- `label` 由调用方格式化；组件按单行居中显示，不内置百分比、亮度或音量文案。
- `iconContent` 可为空；传入时会在 `20.dp` 图标区域内、文案上方显示，并通过 `LocalContentColor` 接收 `iconColor`。
- 手势监听、显示/隐藏、延时关闭以及系统亮度/音量更新均属于调用方业务。
- 默认尺寸为 `52×156dp`；使用 `modifier.width(...)` 和 `modifier.height(...)` 覆盖。
- 默认容器在明暗主题中都以白色半透明基底呈现，填充使用主题强调色；宽柔光、底部弱阴影和单层 `8.dp` 投影形成厚度。
- 组件不再暴露高光色或边框色，也不绘制顶边亮线；材质光源由库统一维护，颜色 API 只表达容器、填充和内容语义。
- 组件提供确定进度语义，便于无障碍服务读取当前比例。

<WasmPreview demo="level_capsule" title="HyperLevelCapsule 交互预览" />
