# HyperDialog

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/dialog/HyperDialog.kt`
- 预览：`custom_dialog`

`HyperDialog` 是基础弹窗容器，只负责居中浮层、可选固定顶部标题、尺寸、滚动内容区和底部 action slot。点击面板外的空白区域默认通过 `onDismissRequest` 请求关闭，传入 `dismissOnClickOutside = false` 可禁用。面板默认取扣除窗口间距后可用宽度的 90%，限制在 280–360dp，最大高度为 480dp，并在窗口四周保留 16dp 间距。弹窗不渲染遮罩，也不使用显示或关闭动画；面板使用不透明卡片背景和实色描边。

## 公开签名

```kotlin
@Composable
fun HyperDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    title: String? = null,
    modifier: Modifier = Modifier,
    minWidth: Dp = HyperDialogDefaults.MinWidth,
    maxWidth: Dp = HyperDialogDefaults.MaxWidth,
    widthFraction: Float = HyperDialogDefaults.WidthFraction,
    maxHeight: Dp = HyperDialogDefaults.MaxHeight,
    windowPadding: PaddingValues = HyperDialogDefaults.WindowPadding,
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
    dismissOnClickOutside: Boolean = true,
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
    val MaxWidth = 360.dp
    const val WidthFraction = 0.9f
    val MaxHeight = 480.dp
    val WindowPadding = PaddingValues(16.dp)
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

- 不存在 `show` 和 `actions` 参数；使用 `visible` 与 `actionContent`。
- 不渲染遮罩或半透明蒙层。
- `dismissOnClickOutside` 默认为 `true`，点击面板外空白区域会调用 `onDismissRequest`；传入 `false` 后空白区域点击不会请求关闭。
- 标题通过 `title` 属性提供，由组件固定渲染在顶部，不参与正文滚动；`title = null`、空字符串或全空白字符串时不渲染标题槽位，也不预留标题高度。
- 默认背景来自 `HyperDialogDefaults.colors()`，未指定 `containerColor` 时使用 `HyperColors.cardContainer`，保持不透明卡片效果。
- `visible` 直接控制弹窗是否渲染，显示与关闭均不执行动画。
- 通过 `HyperDialogColors` 或 `HyperDialogDefaults.colors(...)` 传入含 alpha 的容器色时，会先与页面背景合成为实色。
- `widthFraction` 必须在 `(0, 1]` 范围内，默认取扣除 `windowPadding` 后可用宽度的 `90%`；结果继续受 `minWidth`、`maxWidth` 约束。`maxHeight` 约束面板高度。
- 默认 `windowPadding` 确保面板与窗口四周至少保留 16dp 间距。窗口不足以容纳默认最小宽度时，以窗口可用宽度为准，不会越界。
- slot 内容默认继承 `HyperColors.primaryText`，裸 `Text` 在深色模式下也会使用浅色文字；调用方显式传入 `color` 时以调用方为准。
- 默认描边来自 `HyperDialogDefaults.border()`，使用合成后的实色轻描边；如需无边框，传入 `border = null`。

<WasmPreview demo="custom_dialog" title="HyperDialog 交互预览" />
