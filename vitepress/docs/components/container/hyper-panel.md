# HyperPanel

`HyperPanel` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

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

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- 公开 API 仅支持 Android 手机端；Preview 只是文档交互工具。
