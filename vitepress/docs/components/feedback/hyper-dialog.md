# HyperDialog

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/dialog/HyperDialog.kt`
- 状态归属：调用方提供 `show`、内容状态和提交结果
- Preview ID：`custom_dialog`

无预设正文结构的弹窗容器。内容和底部操作分别通过 `ColumnScope` 与 `RowScope` slot 提供。

## 公开签名

```kotlin
@Composable
fun HyperDialog(
    show: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    minWidth: Dp = HyperDialogDefaults.MinWidth,
    maxWidth: Dp = HyperDialogDefaults.MaxWidth,
    maxHeight: Dp = HyperDialogDefaults.MaxHeight,
    shape: Shape = HyperDialogDefaults.Shape,
    elevation: Dp = HyperDialogDefaults.Elevation,
    containerColor: Color = Color.Unspecified,
    contentPadding: PaddingValues = HyperDialogDefaults.ContentPadding,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperDialogDefaults.ContentSpacing),
    actionsArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperDialogDefaults.ActionSpacing,
        Alignment.End
    ),
    showScrollIndicator: Boolean = HyperDialogDefaults.ShowScrollIndicator,
    actions: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
)
```

## 关键参数

| 参数 | 默认值 | 说明 |
| --- | --- | --- |
| `show` | 必填 | 是否显示；关闭动画结束后移除 Popup |
| `onDismissRequest` | 必填 | 返回键等 Popup 关闭请求 |
| `minWidth` / `maxWidth` | `280.dp` / `340.dp` | 面板宽度约束 |
| `maxHeight` | `480.dp` | 面板最大高度 |
| `elevation` | `8.dp` | 面板阴影高度 |
| `containerColor` | `Color.Unspecified` | 未指定时使用 `HyperColors.elevatedContainer`（半透明玻璃托盘） |
| `contentPadding` | 水平 `24.dp`、垂直 `22.dp` | 面板内边距 |
| `showScrollIndicator` | `true` | 内容溢出时是否显示内部滚动指示条 |
| `actions` | `null` | 固定在可滚动内容下方的操作区 |
| `content` | 必填 | 可滚动内容区 |

## 最小用法

```kotlin
HyperDialog(
    show = showDialog,
    onDismissRequest = { showDialog = false },
    actions = {
        HyperButton(
            text = "取消",
            variant = HyperButtonVariant.Default,
            onClick = { showDialog = false }
        )
        HyperButton(
            text = "保存",
            onClick = {
                onSave(draft)
                showDialog = false
            }
        )
    }
) {
    HyperTextField(
        value = draft,
        onValueChange = { draft = it },
        singleLine = false
    )
}
```

## 默认值 API

```kotlin
object HyperDialogDefaults {
    val MinWidth = 280.dp
    val MaxWidth = 340.dp
    val MaxHeight = 480.dp
    val Shape: Shape = RoundedCornerShape(HyperStyleDefaults.LargeCornerRadius)
    val Elevation = 8.dp
    val ContentPadding = PaddingValues(horizontal = 24.dp, vertical = 22.dp)
    val ContentSpacing = 16.dp
    val ActionSpacing = 12.dp
    const val ShowScrollIndicator = true
    val ScrollIndicatorWidth = 3.dp
    val ScrollIndicatorContentPadding = 10.dp
    val ScrollIndicatorMinHeight = 32.dp
}
```

## 行为与约束

- 弹窗居中显示，使用 300ms 淡入/缩放动画。
- Popup 不因点击外部关闭，并且不渲染 scrim/overlay。
- `onDismissRequest` 不会自动把 `show` 改为 `false`，调用方必须更新状态。
- 长内容在内容区滚动；操作按钮应放入 `actions`，避免随正文滚动。
- 内容使用 `DisableSelection` 隔离外部选择容器，可放输入框。

## 交互预览

<WasmPreview demo="custom_dialog" title="HyperDialog 交互预览" />
