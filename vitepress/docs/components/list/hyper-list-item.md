# HyperListItem

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/list/HyperListItem.kt`
- 预览：`lazy_list`

`HyperListItem` 是 slot-first 列表行。它提供 leading、headline、supporting、trailing 四个区域，以及点击、禁用态和分割线。组件会向各 slot 注入默认 `LocalTextStyle` 和 `LocalContentColor`，因此调用方直接写 `Text(...)` 也能得到稳定的标题/描述层级。

## 公开签名

```kotlin
@Composable
fun HyperListItem(
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

## 最小用法

```kotlin
HyperListItem(
    leadingContent = { Icon(Icons.Default.Settings, contentDescription = null) },
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
- `headlineContent` 默认使用 16sp/22sp，`supportingContent` 默认使用 13sp/18sp；调用方显式传入 `style` 或 `fontSize` 时以调用方为准。
- 行点击和 trailing 控件点击是否独立，由调用方在 slot 中组合。
- 放入 `HyperList` 或 `HyperLazyList` 时，列表容器负责外层圆角背景。

<WasmPreview demo="lazy_list" title="HyperListItem 交互预览" />
