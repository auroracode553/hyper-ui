# HyperFilterChip

- 分类：表单组件
- 包名：`hyper_ui`
- 状态模型：受控组件；选中态由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/selection/HyperFilterChip.kt`
- Preview 注册：`preview/src/commonMain/kotlin/hyper_ui/docs/data/FormComponentDemos.kt`
- Preview 交互：`preview/src/commonMain/kotlin/hyper_ui/docs/ui/FormComponentShowcases.kt`

提供横向滚动的筛选标签栏 [HyperFilterChipBar] 与单个筛选标签 [HyperFilterChip]，
适用于“全部 / 分类 A / 分类 B …”这类横向滚动单选过滤场景，例如拦截日志分类、
消息分类、下载状态过滤等。

## 公开 API

```kotlin
@Immutable
data class HyperFilterChipItem<T>(
    val key: T,
    val label: String,
    val count: Int? = null,
    val enabled: Boolean = true
)

@Composable
fun HyperFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    count: Int? = null,
    enabled: Boolean = true,
    selectedColor: Color = Color.Unspecified,
    unselectedColor: Color = Color.Unspecified,
    selectedTextColor: Color = Color.Unspecified,
    unselectedTextColor: Color = Color.Unspecified
)

@Composable
fun <T> HyperFilterChipBar(
    items: List<HyperFilterChipItem<T>>,
    selectedKey: T?,
    onSelected: (T?) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = HyperFilterChipDefaults.BarHorizontalPadding,
        vertical = HyperFilterChipDefaults.BarVerticalPadding
    ),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperFilterChipDefaults.Spacing),
    chip: @Composable (item: HyperFilterChipItem<T>, selected: Boolean) -> Unit = { item, selected ->
        HyperFilterChip(
            selected = selected,
            onClick = { onSelected(item.key) },
            label = item.label,
            count = item.count,
            enabled = item.enabled
        )
    }
)
```

## 关键公开类型

```kotlin
object HyperFilterChipDefaults {
    val Height = 32.dp
    val HorizontalPadding = 14.dp
    val CountSpacing = 4.dp
    val Spacing = 8.dp
    val BarHorizontalPadding = 16.dp
    val BarVerticalPadding = 8.dp
    val LabelFontSize = 13.sp
    val CountFontSize = 12.sp
    const val CountAlpha = 0.7f
}
```

`HyperFilterChipDefaults` 是公开尺寸与样式常量集合；当前组件签名不接收该对象作为配置参数。

## 参数

### HyperFilterChipItem

| 参数 | 类型 | 必填 | 默认值 | 状态归属 / 作用 |
| --- | --- | --- | --- | --- |
| `key` | `T` | 是 | 无 | 项的唯一标识，允许为 nullable 以表达“全部/清除选择”语义。 |
| `label` | `String` | 是 | 无 | 标签显示文案。 |
| `count` | `Int?` | 否 | `null` | 计数，为 `null` 时不显示计数。 |
| `enabled` | `Boolean` | 否 | `true` | 是否可用，为 `false` 时不可点击。 |

### HyperFilterChip

| 参数 | 类型 | 必填 | 默认值 | 状态归属 / 作用 |
| --- | --- | --- | --- | --- |
| `selected` | `Boolean` | 是 | 无 | 调用方持有的当前选中状态。 |
| `onClick` | `() -> Unit` | 是 | 无 | 点击时回调；调用方据此更新选中状态。 |
| `label` | `String` | 是 | 无 | 标签显示文案。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 调整外层布局。 |
| `count` | `Int?` | 否 | `null` | 计数，为 `null` 时不显示计数。 |
| `enabled` | `Boolean` | 否 | `true` | 调用方控制可用态；为 `false` 时不回调。 |
| `selectedColor` | `Color` | 否 | `Color.Unspecified` | 选中背景色；未指定时使用 `HyperColors.accent`。 |
| `unselectedColor` | `Color` | 否 | `Color.Unspecified` | 未选中背景色；未指定时使用 `HyperColors.elevatedContainer`（半透明玻璃托盘）。 |
| `selectedTextColor` | `Color` | 否 | `Color.Unspecified` | 选中文字色；未指定时使用白色。 |
| `unselectedTextColor` | `Color` | 否 | `Color.Unspecified` | 未选中文字色；未指定时使用 `HyperColors.primaryText`。 |

### HyperFilterChipBar

| 参数 | 类型 | 必填 | 默认值 | 状态归属 / 作用 |
| --- | --- | --- | --- | --- |
| `items` | `List<HyperFilterChipItem<T>>` | 是 | 无 | 标签项列表。 |
| `selectedKey` | `T?` | 是 | 无 | 当前选中项的 key，`null` 表示未选中或“全部”。 |
| `onSelected` | `(T?) -> Unit` | 是 | 无 | 选中回调，回传被点击项的 key。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 调整外层布局。 |
| `contentPadding` | `PaddingValues` | 否 | 见公开 API | 标签栏内容内边距。 |
| `horizontalArrangement` | `Arrangement.Horizontal` | 否 | `spacedBy(Spacing)` | 项之间的水平排列。 |
| `chip` | `@Composable (item, selected) -> Unit` | 否 | 渲染 `HyperFilterChip` | 自定义项渲染插槽。 |

## 状态归属

- 组件不会在内部保存业务选中值。
- 调用方必须把 `onSelected` / `onClick` 返回的新值写回自己的状态。
- 组件内部只处理容器色、玻璃高光、文字颜色动画、禁用透明度与横向滚动。
- `HyperFilterChipBar` 通过 `item.key == selectedKey` 判定选中态，不持有选中值本身。

## 最小用法

```kotlin
enum class LogCategory { MaliciousSite, Ad, MaliciousRedirect }

var selected by remember { mutableStateOf<LogCategory?>(null) }
val logs = listOf<LogCategory?>(null) + LogCategory.entries

HyperFilterChipBar(
    items = logs.map { category ->
        HyperFilterChipItem(
            key = category,
            label = category?.name ?: "全部",
            count = if (category == null) 120 else 32
        )
    },
    selectedKey = selected,
    onSelected = { selected = it }
)
```

## 自定义项渲染

```kotlin
HyperFilterChipBar(
    items = items,
    selectedKey = selected,
    onSelected = { selected = it },
    chip = { item, isSelected ->
        HyperFilterChip(
            selected = isSelected,
            onClick = { selected = item.key },
            label = item.label,
            count = item.count,
            selectedColor = HyperColors.success
        )
    }
)
```

## 约束与行为

- 这是无涟漪点击的受控组件，单个标签语义角色为 `Role.Tab`。
- `enabled = false` 时既不切换，也不调用 `onClick` / `onSelected`。
- 选中态默认使用主题色背景与白色文字；未选中态使用 `HyperColors.elevatedContainer` 半透明玻璃托盘 + `glassHighlightBrush` 顶部高光渐变，与 `HyperIconButton` 托盘风格一致；选中/未选中切换动画背景色平滑过渡。
- 标签为胶囊形（`RoundedCornerShape(percent = 50)`），高度来自 `HyperFilterChipDefaults.Height`。
- 标签栏使用 `LazyRow`，适合较多分类场景；项之间默认 `8.dp` 间距。
- 计数文案相对标签文案透明度为 `HyperFilterChipDefaults.CountAlpha`，字号为 `CountFontSize`。
- 当“全部”项的 `key` 为 `null` 时，`selectedKey = null` 会同时命中该项；若列表中存在多个 `key == null` 的项，它们会被同时标记为选中，应避免这种数据。
- 项目颜色规范禁止十六进制硬编码；自定义颜色使用项目允许的 RGBA 写法或 `HyperColors` 令牌。

## 常见误用

```kotlin
// 错误：列表中存在多个 key 为 null 的项，会导致多个项同时被标记为选中。
val items = listOf(
    HyperFilterChipItem<String?>(key = null, label = "全部"),
    HyperFilterChipItem<String?>(key = null, label = "其它")
)
```

```kotlin
// 错误：期望组件自行管理选中态，未把 onSelected 写回状态。
HyperFilterChipBar(
    items = items,
    selectedKey = null,
    onSelected = { }
)
```

## 相关 API

- `HyperFilterChipItem`
- `HyperFilterChipDefaults`
- `HyperThemeConfig`
- `HyperColors.accent`
- `HyperColors.elevatedContainer`
- `HyperColors.disabledContainer`

## 交互预览

<WasmPreview demo="filter_chip" title="HyperFilterChip 交互预览" />
