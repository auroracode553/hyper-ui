# HyperConfirmDialog

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/dialog/HyperConfirmDialog.kt`
- 状态归属：调用方提供 `show` 并处理确认/取消
- Preview ID：`dialog`

基于 `HyperDialog` 的固定确认弹窗，提供标题、消息、取消和确认按钮。

## 公开签名

```kotlin
@Composable
fun HyperConfirmDialog(
    show: Boolean,
    title: String,
    message: String,
    confirmText: String = "确定",
    cancelText: String = "取消",
    onConfirm: () -> Unit,
    onCancel: () -> Unit
)
```

## 参数

| 参数 | 默认值 | 说明 |
| --- | --- | --- |
| `show` | 必填 | 是否显示 |
| `title` | 必填 | 标题 |
| `message` | 必填 | 消息；空白字符串不会显示正文区域 |
| `confirmText` | `"确定"` | 主按钮文字 |
| `cancelText` | `"取消"` | 次按钮文字 |
| `onConfirm` | 必填 | 确认按钮回调 |
| `onCancel` | 必填 | 取消按钮和 Popup 关闭请求共用的回调 |

## 最小用法

```kotlin
HyperConfirmDialog(
    show = showDeleteDialog,
    title = "确认删除",
    message = "删除后无法恢复，是否继续？",
    confirmText = "删除",
    onConfirm = {
        showDeleteDialog = false
        onDelete()
    },
    onCancel = { showDeleteDialog = false }
)
```

## 行为与约束

- `show = false` 时立即返回，不创建弹窗内容。
- `onConfirm` 和 `onCancel` 都不会自动修改 `show`；调用方必须关闭状态。
- 确认按钮使用 `Primary`，取消按钮使用 `Default`。
- 消息区域最大高度为 `360.dp`，超出后内部滚动。
- 弹窗继承 `HyperDialog` 的无遮罩约束。

## 交互预览

<WasmPreview demo="dialog" title="HyperConfirmDialog 交互预览" />
