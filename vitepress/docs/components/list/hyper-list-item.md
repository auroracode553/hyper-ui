# HyperListItem

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/list/HyperListItem.kt`
- 状态归属：调用方处理点击和尾部内容状态
- Preview：在 `HyperLazyList` 与 `HyperList` 示例中展示

通用列表行布局，支持标题、描述、前置图标、分割线、点击事件和尾部 slot。

## 公开签名

```kotlin
@Composable
fun HyperListItem(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    leadingIcon: ImageVector? = null,
    minHeight: Dp = 68.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    showDivider: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailing: @Composable RowScope.() -> Unit = {}
)
```

## 参数

| 参数 | 默认值 | 说明 |
| --- | --- | --- |
| `title` | 必填 | 主标题 |
| `modifier` | `Modifier` | 整行根 `Column` 修饰符 |
| `description` | `null` | 非空且非空白时显示描述 |
| `leadingIcon` | `null` | 可选前置 `ImageVector` |
| `minHeight` | `68.dp` | 内容行最小高度 |
| `contentPadding` | 水平 `20.dp`、垂直 `12.dp` | 内容内边距 |
| `showDivider` | `false` | 是否在底部显示分割线 |
| `onClick` | `null` | 非空时整行可点击 |
| `trailing` | 空内容 | 尾部 `RowScope`，可放开关、文字或图标按钮 |

## 最小用法

```kotlin
HyperListItem(
    title = "自动同步",
    description = "仅在 Wi-Fi 下同步",
    showDivider = true,
    trailing = {
        HyperSwitch(
            checked = syncEnabled,
            onCheckedChange = onSyncEnabledChange
        )
    }
)
```

## 约束

- `trailing` 中组件的状态仍由调用方持有。
- 放入 `HyperList`/`HyperLazyList` 时，外层列表负责背景与圆角；`HyperListItem` 只负责行内容。
- `showDivider` 不会根据列表位置自动变化。

## 交互预览

`HyperListItem` 在列表文档项中演示：

<WasmPreview demo="lazy_list" title="HyperListItem 交互预览" />
