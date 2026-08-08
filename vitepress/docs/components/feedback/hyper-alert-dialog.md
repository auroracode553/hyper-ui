# HyperAlertDialog

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/dialog/HyperAlertDialog.kt`
- 预览：`dialog`

`HyperAlertDialog` 是基于 `HyperDialog` 的结构化弹窗。它只提供 title、body、action 三个 slot 区域，不内置确认、取消、危险操作等业务按钮，并继承 `HyperDialog` 的默认不透明卡片面板。

## 公开签名

```kotlin
@Composable
fun HyperAlertDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    titleContent: (@Composable ColumnScope.() -> Unit)? = null,
    bodyContent: (@Composable ColumnScope.() -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
)
```

## 最小用法

```kotlin
HyperAlertDialog(
    visible = visible,
    onDismissRequest = onDismiss,
    titleContent = { Text("确认删除") },
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

- 不存在 `title`、`message`、`confirmText`、`cancelText` 字符串参数。
- 如果项目需要二次确认语义，应该在业务项目中封装项目级 ConfirmDialog。
- 弹窗不渲染遮罩或半透明蒙层。
- 面板背景、圆角和描边由内部 `HyperDialog` 默认值提供；当前 `HyperAlertDialog` 不单独暴露这些样式参数。
- `titleContent` 默认继承 `HyperColors.primaryText`，`bodyContent` 默认继承 `HyperColors.secondaryText`；slot 内显式传入的颜色优先。

<WasmPreview demo="dialog" title="HyperAlertDialog 交互预览" />
