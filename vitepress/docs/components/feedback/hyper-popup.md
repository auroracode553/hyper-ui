# HyperPopup

`HyperPopup` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

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

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- 公开 API 仅支持 Android 手机端；Preview 只是文档交互工具。
