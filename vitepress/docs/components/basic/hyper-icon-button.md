# HyperIconButton

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/button/HyperIconButton.kt`
- 预览：`icon_button`

`HyperIconButton` 是固定尺寸的 slot-first 点击容器。它不接收 `ImageVector`；调用方在 `content` slot 中放入任意 `Icon`、进度或状态内容。
默认容器带 1dp 轻描边，白色或近白色背景下也能看清边界。

## 公开签名

```kotlin
data class HyperIconButtonColors(
    val containerColor: Color,
    val contentColor: Color,
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
    border: BorderStroke? = HyperIconButtonDefaults.border(),
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
)
```

## 关键公开类型

```kotlin
object HyperIconButtonDefaults {
    val Size = 40.dp
    val IconSize = 22.dp
    val Shape: Shape = CircleShape
    val BorderWidth = 1.dp

    @Composable
    fun border(
        color: Color = Color.Unspecified
    ): BorderStroke
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

## 约束

- 不存在 `imageVector`、`contentDescription`、`tint`、`backgroundColor` 参数；这些通过 slot 或 `colors` 表达。
- 圆形和圆角矩形按钮都通过 `shape` 配置。
- 默认描边来自 `HyperIconButtonDefaults.border()`，内部使用 `HyperColors.fieldBorder`。
- 如需完全透明无描边图标按钮，可显式传入 `border = null`。
- `LocalContentColor` 会传递给 slot 内容。

<WasmPreview demo="icon_button" title="HyperIconButton 交互预览" />
