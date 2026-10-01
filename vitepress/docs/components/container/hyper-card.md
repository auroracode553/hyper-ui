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

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperCardDefaults.ContentPadding) | 内部内容区域的布局修饰符。 |
| colors | HyperCardColors | 否 | HyperCardDefaults.colors() | 组件各状态的颜色配置。 |
| shape | Shape | 否 | HyperCardDefaults.Shape | 组件容器的形状。 |
| elevation | Dp | 否 | HyperCardDefaults.Elevation | 容器阴影高度。 |
| border | BorderStroke? | 否 | HyperCardDefaults.border() | 容器边框配置。 |
| verticalArrangement | Arrangement.Vertical | 否 | Arrangement.spacedBy(HyperCardDefaults.ContentSpacing) | 子项的垂直排列方式。 |
| horizontalAlignment | Alignment.Horizontal | 否 | Alignment.Start | 容器内内容的水平对齐方式。 |
| content | @Composable ColumnScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

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
