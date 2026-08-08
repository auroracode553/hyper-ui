# HyperDialog

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/dialog/HyperDialog.kt`
- 预览：`custom_dialog`

`HyperDialog` 是基础弹窗容器，只负责居中浮层、尺寸、滚动内容区、底部 action slot 和动画。弹窗不渲染遮罩，面板默认使用 92% 屏宽、不透明卡片背景、20dp 圆角和 1dp 轻描边，避免页面内容透到弹窗内部。

## 公开签名

```kotlin
@Composable
fun HyperDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    minWidth: Dp = HyperDialogDefaults.MinWidth,
    maxWidth: Dp = HyperDialogDefaults.MaxWidth,
    maxHeight: Dp = HyperDialogDefaults.MaxHeight,
    shape: Shape = HyperDialogDefaults.Shape,
    colors: HyperDialogColors = HyperDialogDefaults.colors(),
    contentPadding: PaddingValues = HyperDialogDefaults.ContentPadding,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperDialogDefaults.ContentSpacing),
    actionArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperDialogDefaults.ActionSpacing,
        Alignment.End
    ),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = false,
    showScrollIndicator: Boolean = HyperDialogDefaults.ShowScrollIndicator,
    actionContent: (@Composable RowScope.() -> Unit)? = null,
    border: BorderStroke? = HyperDialogDefaults.border(),
    content: @Composable ColumnScope.() -> Unit
)
```

## 关键公开类型

```kotlin
object HyperDialogDefaults {
    val MinWidth = 280.dp
    const val WidthFraction = 0.92f
    val MaxWidth = 360.dp
    val MaxHeight = 480.dp
    val Shape: Shape = RoundedCornerShape(20.dp)
    val ContentPadding = PaddingValues(20.dp)
    val ContentSpacing = 16.dp
    val ActionSpacing = 12.dp
    const val ShowScrollIndicator = true
    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperDialogColors
    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## 最小用法

```kotlin
HyperDialog(
    visible = visible,
    onDismissRequest = onDismiss,
    actionContent = {
        HyperButton(onClick = onCancel) { Text("取消") }
        HyperButton(onClick = onSave) { Text("保存") }
    }
) {
    Text("编辑备注")
    HyperTextField(
        value = value,
        onValueChange = onValueChange
    )
}
```

## 约束

- 不存在 `show` 和 `actions` 参数；使用 `visible` 与 `actionContent`。
- 不渲染遮罩或半透明蒙层。
- 标题、正文、输入框和按钮都由调用方通过 slots 提供。
- 默认背景来自 `HyperDialogDefaults.colors()`，未指定 `containerColor` 时使用 `HyperColors.cardContainer`，保持不透明卡片效果。
- slot 内容默认继承 `HyperColors.primaryText`，裸 `Text` 在深色模式下也会使用浅色文字；调用方显式传入 `color` 时以调用方为准。
- 默认描边来自 `HyperDialogDefaults.border()`，内部使用 `HyperColors.panelBorder`；如需无边框，传入 `border = null`。

<WasmPreview demo="custom_dialog" title="HyperDialog 交互预览" />
