# HyperEmptyState

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/feedback/HyperEmptyState.kt`

`HyperEmptyState` 是页面级空数据状态。组件直接使用 `HyperPanel` 承载标题、可选说明、图标 Slot 和操作 Slot，不自行绘制面板样式，也不持有加载、筛选、重试或导航等业务状态。

## 公开 API

```kotlin
@Immutable
data class HyperEmptyStateColors(
    val iconContentColor: Color,
    val titleColor: Color,
    val descriptionColor: Color
)

@Composable
fun HyperEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    panelModifier: Modifier = Modifier,
    colors: HyperEmptyStateColors = HyperEmptyStateDefaults.colors(),
    iconContent: (@Composable () -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
)

object HyperEmptyStateDefaults {
    val HorizontalPadding: Dp // 24.dp
    val PanelMaxWidth: Dp // 520.dp
    val ActionSpacing: Dp // 8.dp

    @Composable
    fun colors(
        iconContentColor: Color = Color.Unspecified,
        titleColor: Color = Color.Unspecified,
        descriptionColor: Color = Color.Unspecified
    ): HyperEmptyStateColors
}
```

## 参数

| 参数 | 默认值 | 说明 |
| --- | --- | --- |
| `title` | 必填 | 空状态主文案 |
| `modifier` | `Modifier` | 页面级占位区域；组件会填满此区域并居中卡片 |
| `description` | `null` | 可选辅助说明；空白字符串不会渲染 |
| `panelModifier` | `Modifier` | 内部 `HyperPanel` 外壳修饰符；面板默认最大宽度 520dp |
| `colors` | `HyperEmptyStateDefaults.colors()` | 图标、标题和说明的前景色 |
| `iconContent` | `null` | 可选图标 Slot；默认内容色为主题强调色，图标资源由调用方提供 |
| `actionContent` | `null` | 可选水平操作 Slot；点击行为和结果状态由调用方处理 |

## 最小调用

```kotlin
HyperEmptyState(
    title = "暂无历史记录",
    description = "浏览过的页面会显示在这里",
    iconContent = {
        Icon(
            painter = painterResource(R.drawable.ic_history),
            contentDescription = null,
            modifier = Modifier.size(44.dp)
        )
    }
)
```

需要重试操作时由调用方注入按钮：

```kotlin
HyperEmptyState(
    title = "没有找到文件",
    actionContent = {
        HyperButton(onClick = onRefresh) {
            Text("重新扫描")
        }
    }
)
```

## 使用约束

- 仅用于数据为空或筛选无结果，不用于加载中和错误态；加载反馈使用进度组件，错误处理由页面根据业务决定。
- 此组件的 `iconContent` 资源由调用方提供；HyperUI 内部的 Lucide 默认图标不会为 `HyperEmptyState` 注入默认内容。
- `modifier` 应获得明确的可用高度，例如页面根布局的 `fillMaxSize()` 或 `Column` 中的 `weight(1f)`。
- 组件只渲染自身面板，不添加遮罩或蒙层。
- 面板容器、内间距、内容间距、形状、描边和投影均使用 `HyperPanel` 默认值；`HyperEmptyState` 不维护第二套面板样式。

## Preview

<WasmPreview demo="empty_state" title="HyperEmptyState 交互预览" />
