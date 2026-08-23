# HyperDialog

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/dialog/HyperDialog.kt`
- Android 宿主：`library/src/main/java/hyper_ui/components/dialog/HyperDialogHost.kt`
- 预览：`hyper_dialog`

`HyperDialog` 基于 `androidx.compose.ui.window.Dialog` 承载对话框内容。宿主设置 `usePlatformDefaultWidth = false`，让 Dialog 使用稳定的全宽 Window；内部全尺寸根节点固定首帧测量，并把 280–360dp 面板居中，避免平台默认 `WRAP_CONTENT` Window 在首次布局时产生位置变化。Android 宿主另外应用空动画样式，屏蔽真实的 enter、exit、show、hide 窗口过渡。返回键与外部点击行为由公开参数控制。

## 问题原因与实现方向

本组件曾在 `usePlatformDefaultWidth` 使用默认值 `true` 时出现面板由上向下进入的视觉效果。该效果不是 `HyperFloatingPanel` 的 Compose 位移或补间动画；默认宽度会让 Dialog Window 使用 `WRAP_CONTENT`，首次显示时窗口测量与居中定位可能形成可见的位置变化。

当前实现遵循以下方向：

- `usePlatformDefaultWidth = false` 不是动画开关；它把 Dialog Window 宽度改为 `MATCH_PARENT`，用于消除平台默认宽度带来的首帧窗口重新定位。
- Dialog 内容使用稳定的全尺寸根节点，面板在根节点内居中；不能只设置 `usePlatformDefaultWidth = false` 后继续让面板直接作为根内容，否则平台的全宽约束会影响面板宽度与命中区域。
- 全尺寸根节点已经覆盖 Dialog Window，因此 `dismissOnClickOutside` 由面板后方的背景命中层处理；面板自身注册命中区域，空白点击不会穿透。
- Android 的 `HyperDialogNoWindowAnimation` 只负责屏蔽真实的窗口 enter、exit、show、hide 过渡，是测量稳定方案之外的第二道保护。
- 组件继续使用 Compose `Dialog`，不改用 `Popup`。

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
- `DialogProperties` 固定使用 `usePlatformDefaultWidth = false`；全尺寸根节点负责稳定窗口测量，面板使用扣除 16dp 窗口边距后的 `280–360dp` 宽度边界。
- Android 平台窗口在首次显示前绑定 `HyperDialogNoWindowAnimation`，显式清除进入、退出、显示、隐藏动画。
- `dismissOnBackPress` 传给 `DialogProperties`；`dismissOnClickOutside` 由全尺寸根节点的背景命中层处理，面板自身不会把点击穿透到关闭层。
- `HyperDialog` 不使用 `Popup`；`HyperPopup` 与模态 Dialog 保持独立实现和职责。
- 根节点通过 `BoxWithConstraints` 读取固定窗口约束，只计算面板可用边界，不反向修改 Window 尺寸。
- 标题与底部操作区固定，正文超出高度时独立滚动。
- 输入框焦点、输入值和校验状态由调用方管理。

<WasmPreview demo="hyper_dialog" title="HyperDialog 交互预览" />
