# HyperAlertDialog

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/dialog/HyperAlertDialog.kt`
- 预览：`dialog`

`HyperAlertDialog` 是基于 `HyperDialog` 的结构化弹窗。它通过可选 `title` 属性固定渲染顶部标题，只提供 body、action 两个 slot 区域，不内置确认、取消、危险操作等业务按钮。面板全程不透明，显示与关闭均不执行动画；点击面板外的空白区域默认请求关闭，也可通过 `dismissOnClickOutside = false` 禁用。

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
- 弹窗不渲染遮罩或半透明蒙层。
- `dismissOnClickOutside` 默认为 `true`；设为 `false` 后，点击面板外空白区域不会调用 `onDismissRequest`。
- 面板尺寸、窗口间距、不透明实色背景、圆角和实色描边由内部 `HyperDialog` 默认值提供；当前 `HyperAlertDialog` 不单独暴露这些参数。
- `bodyContent` 默认继承 `HyperColors.secondaryText`；slot 内显式传入的颜色优先。

<WasmPreview demo="dialog" title="HyperAlertDialog 交互预览" />
