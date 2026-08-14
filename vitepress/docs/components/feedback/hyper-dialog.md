# HyperDialog

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/dialog/HyperDialog.kt`
- 预览：`hyper_dialog`

`HyperDialog` 是模态对话框容器。平台 Dialog 的透明根节点始终铺满可用窗口，实色面板在根节点内部居中，因此标题、输入内容、滚动范围或异步正文变化只会重排面板，不会让平台窗口跟随内容反复改尺寸。Android 宿主在窗口建立后只清除一次系统调暗标记；组件不绘制蒙层。

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
    const val WidthFraction = 0.9f
    const val MaxHeightFraction = 0.7f
    val WindowPadding = PaddingValues(16.dp)
    val Shape: Shape = RoundedCornerShape(20.dp)
    val ContentPadding = PaddingValues(20.dp)
    val ContentSpacing = 16.dp
    val ActionSpacing = 12.dp
    const val ShowScrollIndicator = true
    val ScrollIndicatorWidth = 3.dp
    val ScrollIndicatorContentPadding = 10.dp
    val ScrollIndicatorMinHeight = 32.dp

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
- Dialog 根节点固定铺满可用窗口，面板宽度限制在 `280–360dp`，最大高度为窗口高度的 `70%`。
- 不绘制遮罩；外部点击由透明命中区域处理，不依赖内容尺寸窗口的 outside touch。
- 标题与底部操作区固定，正文超出高度时独立滚动。
- 输入框焦点、输入值和校验状态由调用方管理。

<WasmPreview demo="hyper_dialog" title="HyperDialog 稳定输入预览" />
