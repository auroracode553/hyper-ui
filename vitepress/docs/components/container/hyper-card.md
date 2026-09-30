# HyperCard

包名：`hyper_ui`。

<WasmPreview demo="panel" title="HyperCard 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperCard(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperCardDefaults.ContentPadding),
    colors: HyperCardColors = HyperCardDefaults.colors(),
    shape: Shape = HyperCardDefaults.Shape,
    elevation: Dp = HyperCardDefaults.Elevation,
    border: BorderStroke? = HyperCardDefaults.border(),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperCardDefaults.ContentSpacing),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
)
```

## 最小用法

```kotlin
HyperCard(
    colors = HyperCardDefaults.colors(
        containerColor = HyperColors.cardContainer
    )
) {
    Text("系统状态")
    Text("运行正常")
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
