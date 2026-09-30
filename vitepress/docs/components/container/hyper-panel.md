# HyperPanel

包名：`hyper_ui`。

<WasmPreview demo="panel" title="HyperPanel 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperPanel(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperPanelDefaults.ContentPadding),
    colors: HyperPanelColors = HyperPanelDefaults.colors(),
    shape: Shape = HyperPanelDefaults.Shape,
    elevation: Dp = HyperPanelDefaults.Elevation,
    border: BorderStroke? = HyperPanelDefaults.border(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperPanelDefaults.ContentSpacing),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
)
```

## 最小用法

```kotlin
HyperPanel(
    colors = HyperPanelDefaults.colors(
        containerColor = HyperColors.cardContainer
    )
) {
    Text("系统状态")
    Text("运行正常")
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
