# HyperIconButton

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/button/HyperIconButton.kt`
- 预览：`icon_button`

`HyperIconButton` 是固定尺寸的 slot-first 点击容器。它不接收 `ImageVector`；调用方在 `content` slot 中放入任意 `Icon`、进度或状态内容。

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
    border: BorderStroke? = null,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
)
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
- `LocalContentColor` 会传递给 slot 内容。

<WasmPreview demo="icon_button" title="HyperIconButton 交互预览" />
