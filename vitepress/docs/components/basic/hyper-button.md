# HyperButton

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/button/HyperButton.kt`
- 预览：`button`

`HyperButton` 是 slot-first 按钮容器。组件只负责点击、禁用态、tone、颜色、边框、形状和内容排列；按钮里的文字、图标、计数或加载状态全部由调用方通过 `content` slot 渲染。

## 公开签名

```kotlin
enum class HyperButtonTone {
    Primary, Secondary, Tonal, Outline, Plain, Success, Info, Warning, Danger
}

data class HyperButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

@Composable
fun HyperButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tone: HyperButtonTone = HyperButtonTone.Primary,
    colors: HyperButtonColors = HyperButtonDefaults.colors(tone),
    border: BorderStroke? = HyperButtonDefaults.border(tone),
    shape: Shape = HyperButtonDefaults.Shape,
    minHeight: Dp = HyperButtonDefaults.MinHeight,
    contentPadding: PaddingValues = HyperButtonDefaults.ContentPadding,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperButtonDefaults.ContentSpacing,
        Alignment.CenterHorizontally
    ),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
)
```

## 最小用法

```kotlin
HyperButton(onClick = onSave) {
    Icon(Icons.Default.Search, contentDescription = null)
    Text("搜索")
}
```

## 约束

- 不存在 `text`、`leadingIcon`、`trailingIcon` 参数；这些内容必须由调用方放入 `content`。
- `LocalContentColor` 会传递给 slot 内的 `Text` 与 `Icon`。
- `Plain` 与 `Outline` 默认透明背景；`Outline` 默认有 1.dp 边框。

<WasmPreview demo="button" title="HyperButton 交互预览" />
