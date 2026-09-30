# HyperSlideMenu

`HyperSlideMenu` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。 `reveal` 控制展开侧；操作项通过 `HyperSlideAction` 提供。拖动释放时按位置和速度投影吸附。

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

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- 公开 API 仅支持 Android 手机端；Preview 只是文档交互工具。
