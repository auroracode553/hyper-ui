# HyperMenuGroup 与 HyperMenuItem

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/menu/HyperMenuGroup.kt`
- 状态归属：调用方处理点击与尾部内容状态
- Preview ID：`menu_group`

面向设置页的菜单分组容器和菜单行。`HyperMenuGroup` 提供统一卡片背景，`HyperMenuItem` 提供标题、描述、图标、分割线和尾部 slot。

## 公开签名

```kotlin
@Composable
fun HyperMenuGroup(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    content: @Composable ColumnScope.() -> Unit
)

@Composable
fun HyperMenuItem(
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

## 容器参数

| 参数 | 默认值 | 说明 |
| --- | --- | --- |
| `modifier` | `Modifier` | 分组根 `Column` 修饰符 |
| `containerColor` | `Color.Unspecified` | 未指定时使用 `HyperColors.elevatedContainer`（半透明玻璃托盘） |
| `content` | 必填 | 分组内通常放多个 `HyperMenuItem` |

`HyperMenuItem` 的布局参数与 [HyperListItem](hyper-list-item.md) 基本一致，但它设计为直接放在 `HyperMenuGroup` 中。

## 最小用法

```kotlin
HyperMenuGroup {
    HyperMenuItem(
        title = "安全中心",
        description = "密码与登录设备",
        showDivider = true,
        onClick = onSecurityClick
    )
    HyperMenuItem(
        title = "推送通知",
        trailing = {
            HyperSwitch(
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsEnabledChange
            )
        }
    )
}
```

## 约束

- `HyperMenuGroup` 不保存菜单选择或开关状态。
- 分割线由每个 `HyperMenuItem.showDivider` 控制。
- `containerColor` 必须使用 Compose `Color`；组件源码颜色遵循 RGBA 规范。

## 交互预览

<WasmPreview demo="menu_group" title="HyperMenuGroup 交互预览" />
