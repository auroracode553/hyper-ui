# HyperPopover

包名：`hyper_ui`。

<WasmPreview demo="custom_popup" title="HyperPopover 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperPopover(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    shape: Shape = HyperPopoverDefaults.Shape,
    colors: HyperPopoverColors = HyperPopoverDefaults.colors(),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperPopoverDefaults.ContentSpacing),
    actionArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperPopoverDefaults.ActionSpacing,
        Alignment.End
    ),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    showScrollIndicator: Boolean = HyperPopoverDefaults.ShowScrollIndicator,
    actionContent: (@Composable RowScope.() -> Unit)? = null,
    border: BorderStroke? = HyperPopoverDefaults.border(),
    content: @Composable ColumnScope.() -> Unit
)
```

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 是否显示组件，由调用方持有。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| title | String? | 否 | null | 对话框或弹出层标题。 |
| shape | Shape | 否 | HyperPopoverDefaults.Shape | 组件容器的形状。 |
| colors | HyperPopoverColors | 否 | HyperPopoverDefaults.colors() | 组件各状态的颜色配置。 |
| horizontalAlignment | Alignment.Horizontal | 否 | Alignment.Start | 容器内内容的水平对齐方式。 |
| verticalArrangement | Arrangement.Vertical | 否 | Arrangement.spacedBy(HyperPopoverDefaults.ContentSpacing) | 子项的垂直排列方式。 |
| actionArrangement | Arrangement.Horizontal | 否 | Arrangement.spacedBy( HyperPopoverDefaults.ActionSpacing, Alignment.End ) | 操作区的排列方式。 |
| dismissOnBackPress | Boolean | 否 | true | 按返回键时是否请求关闭。 |
| dismissOnClickOutside | Boolean | 否 | true | 点击外部时是否请求关闭。 |
| showScrollIndicator | Boolean | 否 | HyperPopoverDefaults.ShowScrollIndicator | 内容滚动时是否显示滚动提示。 |
| actionContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 操作按钮或操作区内容，由调用方提供。 |
| border | BorderStroke? | 否 | HyperPopoverDefaults.border() | 容器边框配置。 |
| content | @Composable ColumnScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

## 最小用法

```kotlin
HyperPopover(
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

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
