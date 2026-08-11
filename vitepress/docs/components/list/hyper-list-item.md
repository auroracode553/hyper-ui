# HyperListItem

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/list/HyperListItem.kt`
- 预览：`hyper_list`

`HyperListItem` 是 slot-first 列表行。它提供 leading、headline、supporting、trailing 四个区域，以及点击、禁用态和分割线。内容色、禁用态和分割线均以不透明实色绘制；组件会向各 slot 注入默认 `LocalTextStyle` 和 `LocalContentColor`，因此调用方直接写 `Text(...)` 也能得到稳定的标题/描述层级。

## 公开签名

```kotlin
@Composable
fun HyperListItem(
    headlineContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperListItemDefaults.ContentPadding),
    dividerModifier: Modifier = Modifier.padding(start = HyperListItemDefaults.DividerInset),
    enabled: Boolean = true,
    dividerVisible: Boolean = false,
    colors: HyperListItemColors = HyperListItemDefaults.colors(),
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
)
```

## 最小用法

```kotlin
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR

HyperListItem(
    leadingContent = {
        Icon(
            painter = painterResource(LucideR.drawable.lucide_ic_settings),
            contentDescription = null
        )
    },
    headlineContent = { Text("主题外观") },
    supportingContent = { Text("颜色、圆角和显示密度") },
    trailingContent = {
        HyperSwitch(
            checked = enabled,
            onCheckedChange = { enabled = it }
        )
    }
)
```

## 关键公开类型

```kotlin
object HyperListItemDefaults {
    val SingleLineMinHeight = 52.dp
    val SupportingMinHeight = 56.dp
    val ContentGap = 12.dp
    val TextGap = 3.dp
    val ContentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
    val DividerInset = 16.dp

    fun minHeight(hasSupportingContent: Boolean): Dp
}
```

## 约束

- 不存在 `title`、`description`、`leadingIcon`、`trailing` 参数。
- 组件根据 `supportingContent` 自动选择行高：纯单行项最小 `52.dp`，带说明文字的项最小 `56.dp`；长文本和较大 slot 仍会自然撑高。
- `contentModifier` 默认应用水平 `16.dp`、垂直 `6.dp` 的 `HyperListItemDefaults.ContentPadding`；调用页面无需为单行/双行分别设置高度或 padding。
- `headlineContent` 默认使用 16sp/22sp，`supportingContent` 默认使用 13sp/17sp，两者间距为 `3.dp`；调用方显式传入 `style` 或 `fontSize` 时以调用方为准。
- `dividerVisible = true` 时会绘制分割线；`HyperList` 不解析 Slot 顺序，最后一项由调用方关闭分割线；`HyperMenuList(items)` 会自动隐藏最后一项分割线。
- 分割线默认使用 `HyperListItemDefaults.DividerInset` 缩进；自定义缩进或尺寸使用 `dividerModifier`，不提供 `dividerInset` 数值参数。
- 通过 `HyperListItemColors` 或 `HyperListItemDefaults.colors(...)` 传入含 alpha 的颜色时，会先与父 `HyperList` 或 `HyperMenuList` 的实际容器背景合成为实色；独立使用时按页面背景解析。
- 行点击和 trailing 控件点击是否独立，由调用方在 slot 中组合。
- 放入 `HyperMenuList` 或 `HyperList` 时，父容器负责外层圆角背景与裁剪。

<WasmPreview demo="hyper_list" title="HyperListItem 交互预览" />
