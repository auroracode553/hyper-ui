# HyperNavBar

- 分类：导航组件
- 包名：`hyper_ui`
- 状态模型：无内部业务状态；返回、标题和操作由调用方提供
- 源码：`library/src/main/java/hyper_ui/components/navigation/HyperNavBar.kt`
- Preview ID：`nav-bar`

`HyperNavBar` 是三段式顶部导航栏容器：`navigationContent`、`titleContent`、`actionContent`。默认背景透明，直接继承页面底色。

## 公开 API

```kotlin
data class HyperNavBarColors(
    val containerColor: Color,
    val contentColor: Color
)

@Composable
fun HyperNavBar(
    titleContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    colors: HyperNavBarColors = HyperNavBarDefaults.colors(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperNavBarDefaults.ContentGap),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    navigationContent: (@Composable RowScope.() -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
)
```

## 默认值

```kotlin
object HyperNavBarDefaults {
    val MinHeight = 56.dp
    val ContentGap = 8.dp
    val Shape: Shape = RoundedCornerShape(0.dp)
    val TitleTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.SemiBold
        )

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified
    ): HyperNavBarColors
}
```

`colors()` 默认把 `containerColor` 解析为 `Color.Transparent`，把 `contentColor` 解析为 `HyperColors.primaryText`。

## 参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `titleContent` | `@Composable RowScope.() -> Unit` | 是 | 无 | 中间标题区域，自动占用剩余宽度并继承 `TitleTextStyle`。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 整个导航栏的宽高和外部间距。 |
| `colors` | `HyperNavBarColors` | 否 | `HyperNavBarDefaults.colors()` | 容器与三个 slot 的默认内容色。 |
| `horizontalArrangement` | `Arrangement.Horizontal` | 否 | `spacedBy(ContentGap)` | 三个区域之间的横向排列和间距。 |
| `verticalAlignment` | `Alignment.Vertical` | 否 | `CenterVertically` | 三个区域与标题内部 Row 的垂直对齐。 |
| `navigationContent` | `(@Composable RowScope.() -> Unit)?` | 否 | `null` | 左侧导航 slot；组件不内置返回按钮。 |
| `actionContent` | `(@Composable RowScope.() -> Unit)?` | 否 | `null` | 右侧操作 slot。 |

组件固定提供水平 16dp 内边距，默认最小高度为 56dp。

## 最小用法

```kotlin
HyperNavBar(
    navigationContent = {
        HyperIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "返回")
        }
    },
    titleContent = { Text("通知设置") },
    actionContent = {
        HyperIconButton(onClick = onSearch) {
            Icon(Icons.Default.Search, contentDescription = "搜索")
        }
    }
)
```

## 约束

- 不存在 `title`、`onBack` 或 `rightSlot` 参数。
- 标题 slot 占用剩余宽度，但组件不强制文本居中；对齐方式由 slot 内容决定。
- 返回按钮是否出现、图标和导航行为均由调用方控制。
- 自定义高度使用 `modifier.height(...)` 或 `heightIn(...)`；背景色使用 `HyperNavBarDefaults.colors(...)`。

<WasmPreview demo="nav-bar" title="HyperNavBar 交互预览" />
