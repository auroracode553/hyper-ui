# HyperDialog

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/dialog/HyperDialog.kt`
- 预览：`custom_dialog`

`HyperDialog` 是基础弹窗容器，只负责居中浮层、尺寸、滚动内容区、底部 action slot 和动画。弹窗不渲染遮罩，面板默认带 1dp 轻描边。

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
    elevation: Dp = HyperDialogDefaults.Elevation,
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
- 默认描边来自 `HyperDialogDefaults.border()`，内部使用 `HyperColors.panelBorder`；如需无边框，传入 `border = null`。

<WasmPreview demo="custom_dialog" title="HyperDialog 交互预览" />
