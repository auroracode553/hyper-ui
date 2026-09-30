# HyperAlertDialog

包名：`hyper_ui`。

<WasmPreview demo="dialog" title="HyperAlertDialog 交互预览" />

## 公开签名与默认值

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
            type = "danger"
        ) { Text("删除") }
    }
)
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
