# HyperAlertDialog

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/dialog/HyperAlertDialog.kt`
- 预览：`dialog`

`HyperAlertDialog` 是基于 `HyperDialog` 的结构化弹窗。它通过可选 `title` 属性固定渲染顶部标题，只提供 body、action 两个 slot 区域，不内置确认、取消、危险操作等业务按钮。组件继承 `HyperDialog` 禁用平台默认宽度、固定全尺寸根节点和屏蔽窗口动画的宿主方案；正文超出可用高度时可滚动，标题与底部操作区保持固定。点击面板外是否请求关闭由 `dismissOnClickOutside` 控制。面板固定使用 UI 库标准实色描边，不暴露描边配置。

## 公开签名

```kotlin
@Composable
fun HyperAlertDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    dismissOnClickOutside: Boolean = true,
    bodyContent: (@Composable ColumnScope.() -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
)
```

## 最小用法

```kotlin
HyperAlertDialog(
    visible = visible,
    onDismissRequest = onDismiss,
    title = "确认删除",
    bodyContent = { Text("删除后无法恢复，是否继续？") },
    actionContent = {
        HyperButton(onClick = onDismiss) { Text("取消") }
        HyperButton(
            onClick = onDelete,
            tone = HyperButtonTone.Danger
        ) { Text("删除") }
    }
)
```

## 约束

- 不存在 `titleContent`、`message`、`confirmText`、`cancelText` 参数。
- 标题通过 `title` 属性提供，固定在顶部，不参与 `bodyContent` 的滚动；`title = null`、空字符串或全空白字符串时不渲染标题槽位，也不预留标题高度。
- 如果项目需要二次确认语义，应该在业务项目中封装项目级 ConfirmDialog。
- 弹窗继承 `HyperDialog` 的稳定全尺寸 Dialog 根节点与无窗口动画宿主，不使用 `Popup`。
- `dismissOnClickOutside` 默认为 `true`；该参数由内部全尺寸 Dialog 根节点的背景命中层处理。
- 面板宽度边界、内容间距、不透明实色背景、圆角和描边均由内部 `HyperDialog` 标准样式提供；高度服从平台窗口约束，`HyperAlertDialog` 不暴露 `border` 参数。
- `bodyContent` 默认继承 `HyperColors.secondaryText`；slot 内显式传入的颜色优先。

<WasmPreview demo="dialog" title="HyperAlertDialog 交互预览" />
