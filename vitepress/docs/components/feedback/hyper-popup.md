# HyperPopup

包名：`hyper_ui`。

<WasmPreview demo="custom_popup" title="HyperPopup 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperPopup(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    shape: Shape = HyperPopupDefaults.Shape,
    colors: HyperPopupColors = HyperPopupDefaults.colors(),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperPopupDefaults.ContentSpacing),
    actionArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperPopupDefaults.ActionSpacing,
        Alignment.End
    ),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    showScrollIndicator: Boolean = HyperPopupDefaults.ShowScrollIndicator,
    actionContent: (@Composable RowScope.() -> Unit)? = null,
    border: BorderStroke? = HyperPopupDefaults.border(),
    content: @Composable ColumnScope.() -> Unit
)
```

## 最小用法

```kotlin
HyperPopup(
    visible = visible,
    onDismissRequest = onDismiss,
    title = "编辑备注",
    actionContent = {
        HyperButton(onClick = onCancel) { Text("取消") }
        HyperButton(onClick = onSave) { Text("保存") }
    }
) {
    HyperTextField(
        value = value,
        onValueChange = onValueChange
    )
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
