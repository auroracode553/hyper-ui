# HyperPanel

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/panel/HyperPanel.kt`
- 预览：`panel`

`HyperPanel` 是通用 slot 容器，只负责面板视觉与内容排列，不内置点击、标题、图标或业务状态。
默认容器带 1dp 轻描边，白色背景下也能保持面板边界。

## 公开签名

```kotlin
@Composable
fun HyperPanel(
    modifier: Modifier = Modifier,
    colors: HyperPanelColors = HyperPanelDefaults.colors(),
    shape: Shape = HyperPanelDefaults.Shape,
    elevation: Dp = HyperPanelDefaults.Elevation,
    border: BorderStroke? = HyperPanelDefaults.border(),
    clipContent: Boolean = true,
    contentPadding: PaddingValues = HyperPanelDefaults.ContentPadding,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperPanelDefaults.ContentSpacing),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
)
```

## 关键公开类型

```kotlin
object HyperPanelDefaults {
    val Shape: Shape = RoundedCornerShape(HyperStyleDefaults.LargeCornerRadius)
    val Elevation = 0.dp
    val ContentPadding = PaddingValues(20.dp)
    val ContentSpacing = 12.dp

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## 最小用法

```kotlin
HyperPanel(
    colors = HyperPanelDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surface
    )
) {
    Text("系统状态")
    Text("运行正常")
}
```

## 约束

- 点击语义放在调用方外层或内部具体控件，不由 `HyperPanel` 提供。
- 自定义背景色通过 `HyperPanelDefaults.colors(containerColor = ...)` 传入。
- 默认描边来自 `HyperPanelDefaults.border()`，内部使用 `HyperColors.panelBorder`。
- 需要完全无边框面板时显式传入 `border = null`；需要阴影时使用 `elevation`。

<WasmPreview demo="panel" title="HyperPanel 交互预览" />
