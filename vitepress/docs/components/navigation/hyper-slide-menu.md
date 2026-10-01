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

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| reveal | HyperSlideMenuReveal | 是 | — | 侧滑展开状态，由调用方持有。 |
| onRevealChange | (HyperSlideMenuReveal) -&gt; Unit | 是 | — | 侧滑展开状态变化时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| startActions | List&lt;HyperSlideAction&gt; | 否 | emptyList() | 向右侧滑时露出的操作项。 |
| endActions | List&lt;HyperSlideAction&gt; | 否 | emptyList() | 向左侧滑时露出的操作项。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| actionExtent | Dp | 否 | 64.dp | 侧滑操作区的展开宽度。 |
| shape | Shape | 否 | RoundedCornerShape(18.dp) | 组件容器的形状。 |
| content | @Composable () -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |


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
