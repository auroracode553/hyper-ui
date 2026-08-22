# HyperImmersiveNavBar

- 分类：导航组件
- 包名：`hyper_ui`
- 状态模型：无内部业务状态；滚动状态、按钮事件和系统栏图标明暗由调用方管理
- 源码：`library/src/main/java/hyper_ui/components/navigation/HyperImmersiveNavBar.kt`
- Preview ID：`immersive-nav-bar`

`HyperImmersiveNavBar` 是复用 `HyperNavBar` 的沉浸式页面布局：导航栏固定在最上层并保持透明，且不绘制描边和阴影；首屏内容自动从“状态栏安全区 + 导航栏”下方开始，向上滚动后内容可以进入导航栏与状态栏后方。

它没有给 `HyperNavBar` 增加一个表面化的 `immersive` 布尔值，因为单独的导航栏无法控制兄弟节点的滚动起点和绘制层级。该组件负责精确测量并把顶部净空交给滚动容器，原 `HyperNavBar` 继续只负责三段式导航视觉和交互。

## 公开 API

```kotlin
@Composable
fun HyperImmersiveNavBar(
    titleContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    navBarModifier: Modifier = Modifier,
    colors: HyperNavBarColors = HyperNavBarDefaults.colors(),
    windowInsets: WindowInsets = WindowInsets.statusBars,
    contentPadding: PaddingValues = HyperImmersiveNavBarDefaults.ContentPadding,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperNavBarDefaults.ContentGap),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    navigationContent: (@Composable RowScope.() -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null,
    headerContent: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable BoxScope.(PaddingValues) -> Unit
)

object HyperImmersiveNavBarDefaults {
    val ContentPadding: PaddingValues = PaddingValues(0.dp)
}
```

## 参数

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| `titleContent` | `@Composable RowScope.() -> Unit` | 是 | 无 | 固定导航栏的标题区域。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 整个沉浸式页面容器的尺寸和外部修饰。 |
| `navBarModifier` | `Modifier` | 否 | `Modifier` | 只作用于内部 `HyperNavBar`，在默认状态栏避让之后应用。 |
| `colors` | `HyperNavBarColors` | 否 | `HyperNavBarDefaults.colors()` | 复用 `HyperNavBar` 颜色；默认容器色为透明。 |
| `windowInsets` | `WindowInsets` | 否 | `WindowInsets.statusBars` | 导航操作层需要避让的系统安全区；测量结果会包含该高度。 |
| `contentPadding` | `PaddingValues` | 否 | `PaddingValues(0.dp)` | 调用方额外内容间距；组件只在其顶部叠加实测导航区高度。 |
| `horizontalArrangement` | `Arrangement.Horizontal` | 否 | `spacedBy(HyperNavBarDefaults.ContentGap)` | 内部三段导航内容的横向排列。 |
| `verticalAlignment` | `Alignment.Vertical` | 否 | `CenterVertically` | 内部三段导航内容的垂直对齐。 |
| `navigationContent` | `(@Composable RowScope.() -> Unit)?` | 否 | `null` | 固定的左侧导航 slot。 |
| `actionContent` | `(@Composable RowScope.() -> Unit)?` | 否 | `null` | 固定的右侧操作 slot。 |
| `headerContent` | `(@Composable ColumnScope.() -> Unit)?` | 否 | `null` | 导航栏下方的固定搜索、筛选或说明区；其高度自动计入首屏顶部净空。 |
| `content` | `@Composable BoxScope.(PaddingValues) -> Unit` | 是 | 无 | 页面内容；参数是合并后的沉浸式内容间距。 |

## LazyColumn 用法

必须把 `immersivePadding` 用作列表的 `contentPadding`，顶部净空才会随列表一起滚走：

```kotlin
HyperImmersiveNavBar(
    navigationContent = {
        HyperIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "返回")
        }
    },
    titleContent = { Text("笔记详情") },
    actionContent = {
        HyperIconButton(onClick = onMore) {
            Icon(Icons.Default.MoreVert, contentDescription = "更多")
        }
    },
    contentPadding = PaddingValues(bottom = bottomBarClearance)
) { immersivePadding ->
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = immersivePadding
    ) {
        items(notes) { note -> NoteCard(note) }
    }
}
```

## 普通滚动内容用法

`verticalScroll` 应放在 `padding(immersivePadding)` 之前，使 Padding 成为可滚动内容的一部分：

```kotlin
HyperImmersiveNavBar(
    titleContent = { Text("详情") }
) { immersivePadding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(immersivePadding)
    ) {
        DetailContent()
    }
}
```

## 约束

- 宿主窗口需要启用 edge-to-edge，内容才能实际绘制到系统状态栏后方；组件不会修改 Activity Window。
- 状态栏图标的浅色/深色外观由调用方根据背景管理，组件不读取业务内容颜色。
- 不要把 `immersivePadding` 作为 `LazyColumn` 外层 Modifier 的固定 Padding，否则列表视口仍会被永久限制在导航栏下方。
- 导航栏位于内容上层，其可见按钮区域会优先处理触摸；滚动内容的状态仍完全归调用方。
- 若不需要系统状态栏避让，可显式传入零值 `WindowInsets`；默认行为适合 Android edge-to-edge 页面。

<WasmPreview demo="immersive-nav-bar" title="HyperImmersiveNavBar 交互预览" />
