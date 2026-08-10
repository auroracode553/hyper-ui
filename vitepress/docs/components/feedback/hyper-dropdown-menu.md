# HyperDropdownMenu

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/menu/HyperDropdownMenu.kt`
- 预览：`dropdown`

`HyperDropdownMenu` 是 Popup 菜单容器。菜单项内容使用 slot 渲染，组件只负责浮层、尺寸、滚动、点击关闭和分割线；菜单面板、菜单项内容、禁用态和分割线均使用不透明实色，避免页面内容透到菜单内部。

## 公开签名

```kotlin
@Composable
fun HyperDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperDropdownMenuDefaults.MenuPadding),
    alignment: Alignment = Alignment.TopEnd,
    offset: DpOffset = DpOffset(0.dp, HyperDropdownMenuDefaults.AnchorOffsetY),
    shape: Shape = HyperDropdownMenuDefaults.Shape,
    colors: HyperDropdownMenuColors = HyperDropdownMenuDefaults.colors(),
    border: BorderStroke? = HyperDropdownMenuDefaults.border(),
    content: @Composable HyperDropdownMenuScope.() -> Unit
)

class HyperDropdownMenuScope {
    @Composable
    fun Item(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        contentModifier: Modifier = Modifier.padding(HyperDropdownMenuDefaults.ItemPadding),
        enabled: Boolean = true,
        closeOnClick: Boolean = true,
        content: @Composable RowScope.() -> Unit
    )

    @Composable
    fun Divider(modifier: Modifier = Modifier)
}
```

## 关键公开类型

```kotlin
object HyperDropdownMenuDefaults {
    val MenuWidth = 184.dp
    val MaxHeight = 420.dp
    val ItemHeight = 48.dp
    val AnchorOffsetY = 52.dp
    val Shape: Shape = RoundedCornerShape(20.dp)
    val MenuPadding = PaddingValues(vertical = 8.dp)
    val ItemPadding = PaddingValues(horizontal = 20.dp)
    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperDropdownMenuColors
    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke
}
```

## 最小用法

```kotlin
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR

HyperDropdownMenu(
    expanded = expanded,
    onDismissRequest = { expanded = false }
) {
    Item(onClick = onOpenDetail) {
        Icon(
            painter = painterResource(LucideR.drawable.lucide_ic_info),
            contentDescription = null
        )
        Text("查看详情")
    }
    Divider()
    Item(onClick = onDelete) {
        Icon(
            painter = painterResource(LucideR.drawable.lucide_ic_trash_2),
            contentDescription = null
        )
        Text("删除")
    }
}
```

## 约束

- 不存在 `text`、`leadingIcon`、`textColor` 参数。
- 菜单面板与菜单项的内部布局分别通过各自的 `contentModifier` 控制；默认 Modifier 携带原有 `MenuPadding`、`ItemPadding`，不提供重复的 `contentPadding` 参数。
- 默认宽度和最大高度分别为 `HyperDropdownMenuDefaults.MenuWidth`、`MaxHeight`；自定义面板尺寸使用 `modifier.width(...)`、`modifier.heightIn(...)`。
- 菜单不渲染遮罩。
- 如果点击菜单项后不希望关闭，设置 `closeOnClick = false`。
- 默认背景来自 `HyperDropdownMenuDefaults.colors()`，未指定 `containerColor` 时使用 `HyperColors.cardContainer`，保持不透明卡片效果。
- 通过 `HyperDropdownMenuColors` 或 `HyperDropdownMenuDefaults.colors(...)` 传入含 alpha 的颜色时，组件会先与菜单实色背景合成后再绘制。
- 默认描边来自 `HyperDropdownMenuDefaults.border()`，使用合成后的实色轻描边；如需无边框，传入 `border = null`。

<WasmPreview demo="dropdown" title="HyperDropdownMenu 交互预览" />
