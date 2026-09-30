# HyperDialog

包名：`hyper_ui`。

<WasmPreview demo="hyper_dialog" title="HyperDialog 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    shape: Shape = HyperDialogDefaults.Shape,
    colors: HyperDialogColors = HyperDialogDefaults.colors(),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperDialogDefaults.ContentSpacing),
    actionArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperDialogDefaults.ActionSpacing,
        Alignment.End
    ),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    showScrollIndicator: Boolean = HyperDialogDefaults.ShowScrollIndicator,
    actionContent: (@Composable RowScope.() -> Unit)? = null,
    border: BorderStroke? = HyperDialogDefaults.border(),
    content: @Composable ColumnScope.() -> Unit
)
```

## 最小用法

```kotlin
HyperDialog(
    visible = visible,
    onDismissRequest = onDismiss,
    title = "编辑名称",
    actionContent = {
        HyperButton(onClick = onSave) { Text("保存") }
    }
) {
    HyperTextField(
        value = name,
        onValueChange = onNameChange
    )
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
