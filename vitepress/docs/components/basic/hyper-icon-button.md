# HyperIconButton

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/button/HyperIconButton.kt`
- 预览：`icon_button`

`HyperIconButton` 是固定尺寸的 slot-first 点击容器。它不接收 `ImageVector`；调用方在 `content` slot 中放入任意 `Icon`、进度或状态内容。
默认容器是圆形半透明控制按钮，深色模式下使用亮色玻璃感背景，浅色模式下使用浅黑透明背景。调用方可以通过 `colors` 显式设置普通、按压和禁用颜色，适合播放器控制、工具栏和浮层操作。

## 公开签名

```kotlin
data class HyperIconButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val pressedContainerColor: Color,
    val pressedContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

@Composable
fun HyperIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = HyperIconButtonDefaults.Size,
    shape: Shape = HyperIconButtonDefaults.Shape,
    colors: HyperIconButtonColors = HyperIconButtonDefaults.colors(),
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
)
```

## 关键公开类型

```kotlin
object HyperIconButtonDefaults {
    val Size = 48.dp
    val IconSize = 24.dp
    val Shape: Shape = CircleShape
    const val PressedScale = 0.92f

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        pressedContainerColor: Color = Color.Unspecified,
        pressedContentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperIconButtonColors
}
```

## 最小用法

```kotlin
HyperIconButton(onClick = onSearch) {
    Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "搜索",
        modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
    )
}
```

自定义播放器按钮色：

```kotlin
HyperIconButton(
    onClick = onPlay,
    size = 56.dp,
    colors = HyperIconButtonDefaults.colors(
        containerColor = rgba(255, 255, 255, 0.18f),
        pressedContainerColor = rgba(255, 255, 255, 0.28f),
        contentColor = rgba(255, 255, 255, 1f)
    )
) {
    Icon(Icons.Default.PlayArrow, contentDescription = "播放")
}
```

## 约束

- 不存在 `imageVector`、`contentDescription`、`tint`、`backgroundColor` 参数；这些通过 slot 或 `colors` 表达。
- 默认视觉是无描边半透明圆形按钮；圆形和圆角矩形按钮都通过 `shape` 配置。
- 如需完全透明图标按钮，可把 `containerColor` 与 `pressedContainerColor` 传为透明色。
- `LocalContentColor` 会传递给 slot 内容。
- 按压反馈由组件内部处理，业务状态仍由调用方维护。

<WasmPreview demo="icon_button" title="HyperIconButton 交互预览" />
