# HyperDropdownMenu

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/menu/HyperDropdownMenu.kt`
- 预览：`dropdown`

`HyperDropdownMenu` 是 Popup 菜单容器。菜单项内容使用 slot 渲染，组件只负责浮层、尺寸、滚动、点击关闭和分割线；菜单面板默认带 1dp 轻描边。

## 公开签名

```kotlin
@Composable
fun HyperDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopEnd,
    offset: DpOffset = DpOffset(0.dp, HyperDropdownMenuDefaults.AnchorOffsetY),
    width: Dp = HyperDropdownMenuDefaults.MenuWidth,
    maxHeight: Dp = HyperDropdownMenuDefaults.MaxHeight,
    shape: Shape = HyperDropdownMenuDefaults.Shape,
    colors: HyperDropdownMenuColors = HyperDropdownMenuDefaults.colors(),
    contentPadding: PaddingValues = HyperDropdownMenuDefaults.MenuPadding,
    border: BorderStroke? = HyperDropdownMenuDefaults.border(),
    content: @Composable HyperDropdownMenuScope.() -> Unit
)

class HyperDropdownMenuScope {
    @Composable
    fun Item(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        closeOnClick: Boolean = true,
        contentPadding: PaddingValues = HyperDropdownMenuDefaults.ItemPadding,
        content: @Composable RowScope.() -> Unit
    )

    @Composable
    fun Divider(modifier: Modifier = Modifier)
}
```

## 关键公开类型

```kotlin
object HyperDropdownMenuDefaults {
    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## 最小用法

```kotlin
HyperDropdownMenu(
    expanded = expanded,
    onDismissRequest = { expanded = false }
) {
    Item(onClick = onOpenDetail) {
        Icon(Icons.Default.Info, contentDescription = null)
        Text("查看详情")
    }
    Divider()
    Item(onClick = onDelete) {
        Icon(Icons.Default.Delete, contentDescription = null)
        Text("删除")
    }
}
```

## 约束

- 不存在 `text`、`leadingIcon`、`textColor` 参数。
- 菜单不渲染遮罩。
- 如果点击菜单项后不希望关闭，设置 `closeOnClick = false`。
- 默认描边来自 `HyperDropdownMenuDefaults.border()`，内部使用 `HyperColors.panelBorder`；如需无边框，传入 `border = null`。

<WasmPreview demo="dropdown" title="HyperDropdownMenu 交互预览" />
