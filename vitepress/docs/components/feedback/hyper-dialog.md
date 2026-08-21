# HyperDialog

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/dialog/HyperDialog.kt`
- 预览：`hyper_dialog`

`HyperDialog` 是基于 `androidx.compose.ui.window.Dialog` 的标准模态对话框容器。平台负责背景调暗、焦点、返回键、外部点击、系统安全策略和窗口过渡；组件不创建自定义 Window、不清除 dim flag，也不在显示后修改窗口尺寸或动画。HyperUI 只负责实色面板、标题、滚动正文和操作区。

## 公开签名

```kotlin
data class HyperDialogColors(
    val containerColor: Color
)

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

## 默认配置

```kotlin
object HyperDialogDefaults {
    val MinWidth = 280.dp
    val MaxWidth = 360.dp
    val Shape: Shape = RoundedCornerShape(20.dp)
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

- 用于需要用户处理的模态确认、表单、权限说明和警告；临时浮层使用 `HyperPopup`。
- 面板只使用稳定的 `280–360dp` 宽度边界；高度直接服从平台 Dialog 的可用约束。
- 标准 Dialog scrim 由平台绘制；HyperUI 不覆盖或清除平台调暗属性。
- `dismissOnClickOutside` 和 `dismissOnBackPress` 直接传给 `DialogProperties`，不使用透明全屏命中层模拟。
- `HyperDialog` 不使用 `Popup`；`HyperPopup` 与模态 Dialog 保持独立实现和职责。
- 不使用 `BoxWithConstraints`、窗口百分比或内容反向计算 Dialog 尺寸，避免 `WRAP_CONTENT` 窗口产生循环测量。
- 标题与底部操作区固定，正文超出高度时独立滚动。
- 输入框焦点、输入值和校验状态由调用方管理。

<WasmPreview demo="hyper_dialog" title="HyperDialog 标准模态预览" />
