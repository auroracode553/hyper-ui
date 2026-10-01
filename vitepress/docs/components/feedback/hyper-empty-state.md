# HyperEmptyState

包名：`hyper_ui`。

<WasmPreview demo="empty_state" title="HyperEmptyState 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    contentModifier: Modifier = Modifier,
    colors: HyperEmptyStateColors = HyperEmptyStateDefaults.colors(),
    iconContent: (@Composable () -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
)
```

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| title | String | 是 | — | 对话框或弹出层标题。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| description | String? | 否 | null | 空状态的说明文字。 |
| contentModifier | Modifier | 否 | Modifier | 内部内容区域的布局修饰符。 |
| colors | HyperEmptyStateColors | 否 | HyperEmptyStateDefaults.colors() | 组件各状态的颜色配置。 |
| iconContent | (@Composable () -&gt; Unit)? | 否 | null | 图标区域的自定义内容。 |
| actionContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 操作按钮或操作区内容，由调用方提供。 |

## 最小用法

```kotlin
HyperEmptyState(title = "暂无内容", description = "稍后再试", iconContent = { HyperText("○") })
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
