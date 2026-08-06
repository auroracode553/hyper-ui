# HyperDrawer

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/drawer/HyperDrawer.kt`
- 状态归属：调用方提供打开状态、方向和条目选中态
- Preview ID：`drawer`

在业务内容上方显示左、右、上或下方向的抽屉面板。组件不绘制遮罩。

## 公开类型

```kotlin
enum class HyperDrawerPosition {
    Left,
    Right,
    Top,
    Bottom
}
```

## `HyperDrawer` 公开签名

```kotlin
@Composable
fun HyperDrawer(
    open: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    position: HyperDrawerPosition = HyperDrawerPosition.Left,
    drawerWidth: Dp = HyperDrawerDefaults.Width,
    drawerHeight: Dp = HyperDrawerDefaults.Height,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp),
    containerColor: Color = Color.Unspecified,
    scrimColor: Color = Color.Transparent,
    dismissOnClickOutside: Boolean = false,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
)
```

| 参数 | 默认值 | 说明 |
| --- | --- | --- |
| `open` | 必填 | 是否显示抽屉，由调用方持有 |
| `onDismissRequest` | 必填 | 外部点击等关闭请求 |
| `position` | `Left` | 弹出方向 |
| `drawerWidth` | `320.dp` | 左右抽屉请求宽度，最多占容器宽度 88% |
| `drawerHeight` | `320.dp` | 上下抽屉请求高度，最多占容器高度 88% |
| `contentPadding` | 垂直 `16.dp` | 抽屉内容内边距 |
| `containerColor` | `Color.Unspecified` | 未指定时使用 `HyperColors.cardContainer` |
| `scrimColor` | `Color.Transparent` | 仅为源码兼容保留，当前实现不读取该值，也不绘制遮罩 |
| `dismissOnClickOutside` | `false` | 为 `true` 时，点击抽屉外透明区域请求关闭 |
| `drawerContent` | 必填 | 抽屉面板内容 |
| `content` | 必填 | 页面业务内容 |

## 辅助组件

```kotlin
@Composable
fun HyperDrawerHeader(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    leadingIcon: ImageVector? = null
)

@Composable
fun HyperDrawerItem(
    title: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    description: String? = null,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    showDivider: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailing: @Composable RowScope.() -> Unit = {}
)
```

## 最小用法

```kotlin
var open by remember { mutableStateOf(false) }

HyperDrawer(
    open = open,
    onDismissRequest = { open = false },
    dismissOnClickOutside = true,
    drawerContent = {
        HyperDrawerHeader(title = "HyperUI")
        HyperDrawerItem(
            title = "首页",
            selected = true,
            onClick = { open = false }
        )
    }
) {
    HyperButton(
        text = "打开抽屉",
        onClick = { open = true }
    )
}
```

## 默认值

```kotlin
object HyperDrawerDefaults {
    val Width = 320.dp
    val Height = 320.dp
    val ItemMinHeight = 54.dp

    @Deprecated("HyperDrawer no longer renders a scrim; kept only for source compatibility.")
    val ScrimColor = Color.Transparent

    const val MaxWidthFraction = 0.88f
    const val MaxHeightFraction = 0.88f
    const val AnimationMillis = 240

    @Deprecated("HyperDrawer no longer renders a scrim; kept only for source compatibility.")
    const val ScrimZIndex = 8f

    const val DrawerZIndex = 9f
}
```

`ScrimColor` 和 `ScrimZIndex` 已废弃，仅用于源码兼容。

## 约束

- 应将 `HyperDrawer` 放在页面根容器附近，使 `content` 与 `drawerContent` 共享覆盖范围。
- 不要通过 `scrimColor` 尝试添加蒙层；该参数无效果。
- `HyperDrawerItem.selected` 和 `open` 都由调用方更新。

## 交互预览

<WasmPreview demo="drawer" title="HyperDrawer 交互预览" />
