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

## 约束

- 不存在 `title`、`description`、`leadingIcon`、`trailing` 参数。
- 默认最小高度为 `HyperListItemDefaults.MinHeight`；外部尺寸由 `modifier` 控制，内部内容间距由组件统一维护。
- `headlineContent` 默认使用 16sp/22sp，`supportingContent` 默认使用 13sp/18sp；调用方显式传入 `style` 或 `fontSize` 时以调用方为准。
- `dividerVisible = true` 时会绘制分割线；放入 `HyperList(items)` 或 `HyperMenuList(items)` 的最后一项时，父列表会自动隐藏该分割线。
- 分割线默认使用 `HyperListItemDefaults.DividerInset` 缩进；自定义缩进或尺寸使用 `dividerModifier`，不提供 `dividerInset` 数值参数。
- 通过 `HyperListItemColors` 或 `HyperListItemDefaults.colors(...)` 传入含 alpha 的颜色时，会先与列表卡片背景合成为实色。
- 行点击和 trailing 控件点击是否独立，由调用方在 slot 中组合。
- 放入 `HyperMenuList` 时，菜单容器负责外层圆角背景；放入 `HyperList` 时保持页面列表的无圆角平铺效果。

<WasmPreview demo="hyper_list" title="HyperListItem 交互预览" />
