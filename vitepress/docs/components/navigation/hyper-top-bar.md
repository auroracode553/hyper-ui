# HyperTopBar

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/navigation/HyperTopBar.kt`
- 预览：`topbar`

`HyperTopBar` 是三段式顶部栏容器：`navigationContent`、`titleContent`、`actionContent`。组件不内置返回按钮、标题文本或导航逻辑。

## 公开签名

```kotlin
@Composable
fun HyperTopBar(
    modifier: Modifier = Modifier,
    minHeight: Dp = HyperTopBarDefaults.MinHeight,
    contentPadding: PaddingValues = HyperTopBarDefaults.ContentPadding,
    colors: HyperTopBarColors = HyperTopBarDefaults.colors(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperTopBarDefaults.ContentGap),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    navigationContent: (@Composable RowScope.() -> Unit)? = null,
    titleContent: @Composable RowScope.() -> Unit,
    actionContent: (@Composable RowScope.() -> Unit)? = null
)
```

## 最小用法

```kotlin
HyperTopBar(
    navigationContent = {
        HyperIconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "返回")
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

- 不存在 `title`、`onBack`、`rightSlot` 参数。
- 返回按钮是否出现、图标内容和点击行为都由调用方控制。
- `LocalContentColor` 会传递给三个 slot。

<WasmPreview demo="topbar" title="HyperTopBar 交互预览" />
