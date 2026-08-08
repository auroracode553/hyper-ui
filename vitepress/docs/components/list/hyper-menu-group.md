# HyperMenuGroup

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/menu/HyperMenuGroup.kt`
- 预览：`menu_group`

`HyperMenuGroup` 是菜单分组容器，默认带 1dp 轻描边。`HyperMenuItem` 与 `HyperListItem` 使用同一套 slot-first 行模型，适合放在设置页、弹出菜单面板或侧栏分组中。

## 公开签名

```kotlin
@Composable
fun HyperMenuGroup(
    modifier: Modifier = Modifier,
    colors: HyperMenuGroupColors = HyperMenuGroupDefaults.colors(),
    shape: Shape = HyperMenuGroupDefaults.Shape,
    elevation: Dp = HyperMenuGroupDefaults.Elevation,
    border: BorderStroke? = HyperMenuGroupDefaults.border(),
    content: @Composable ColumnScope.() -> Unit
)

@Composable
fun HyperMenuItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minHeight: Dp = HyperListItemDefaults.MinHeight,
    contentPadding: PaddingValues = HyperListItemDefaults.ContentPadding,
    dividerVisible: Boolean = false,
    dividerInset: Dp = HyperListItemDefaults.DividerInset,
    colors: HyperListItemColors = HyperListItemDefaults.colors(),
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    headlineContent: @Composable ColumnScope.() -> Unit,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
)
```

## 关键公开类型

```kotlin
object HyperMenuGroupDefaults {
    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## 最小用法

```kotlin
HyperMenuGroup {
    HyperMenuItem(
        leadingContent = { Icon(Icons.Default.Settings, null) },
        headlineContent = { Text("主题外观") },
        supportingContent = { Text("颜色、圆角和显示密度") },
        trailingContent = {
            HyperSwitch(
                checked = enabled,
                onCheckedChange = { enabled = it }
            )
        }
    )
}
```

## 约束

- 分组不保存选择、开关或点击状态。
- 菜单项不提供 `title`、`description`、`leadingIcon` 字符串/图标参数。
- `dividerVisible` 与 `dividerInset` 由调用方按数据位置决定。
- 分组默认描边来自 `HyperMenuGroupDefaults.border()`，内部使用 `HyperColors.panelBorder`；菜单项本身不额外套外框。
- 如需无边框分组，传入 `border = null`。

<WasmPreview demo="menu_group" title="HyperMenuGroup 交互预览" />
