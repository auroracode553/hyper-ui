# HyperSlideMenu

包名：`hyper_ui`。

<WasmPreview demo="slide_menu" title="HyperSlideMenu 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperSlideMenu(
    reveal: HyperSlideMenuReveal,
    onRevealChange: (HyperSlideMenuReveal) -> Unit,
    modifier: Modifier = Modifier,
    startActions: List<HyperSlideAction> = emptyList(),
    endActions: List<HyperSlideAction> = emptyList(),
    enabled: Boolean = true,
    actionExtent: Dp = 64.dp,
    shape: Shape = RoundedCornerShape(18.dp),
    content: @Composable () -> Unit
)
```

操作项：`HyperSlideAction(label, onClick, containerColor = Color.Unspecified, contentColor = Color(1f, 1f, 1f, 1f), iconContent = null)`；展开值：`Closed / Start / End`。

## 最小用法

```kotlin
var reveal by remember { mutableStateOf(HyperSlideMenuReveal.Closed) }
HyperSlideMenu(reveal, { reveal = it }, endActions = listOf(
    HyperSlideAction("删除", onClick = onDelete)
)) { HyperText("向左滑动") }
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
