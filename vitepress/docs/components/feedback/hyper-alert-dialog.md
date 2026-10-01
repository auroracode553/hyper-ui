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

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 是否显示组件，由调用方持有。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| title | String? | 否 | null | 对话框或弹出层标题。 |
| dismissOnClickOutside | Boolean | 否 | true | 点击外部时是否请求关闭。 |
| bodyContent | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 对话框主体内容。 |
| actionContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 操作按钮或操作区内容，由调用方提供。 |

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
