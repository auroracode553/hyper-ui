# HyperTopBar

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/navigation/HyperTopBar.kt`
- 预览：`topbar`

`HyperTopBar` 是三段式顶部栏容器：`navigationContent`、`titleContent`、`actionContent`。组件不内置返回按钮、标题文本或导航逻辑，标题 slot 默认继承 HyperUI 提供的标题字重和字号。默认容器透明，直接继承所在页面的背景，不额外绘制白色顶部色块。

## 公开签名

```kotlin
@Composable
fun HyperTopBar(
    titleContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    colors: HyperTopBarColors = HyperTopBarDefaults.colors(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperTopBarDefaults.ContentGap),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    navigationContent: (@Composable RowScope.() -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
)
```

## 最小用法

```kotlin
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR

HyperTopBar(
    navigationContent = {
        HyperIconButton(onClick = onBack) {
            Icon(
                painter = painterResource(LucideR.drawable.lucide_ic_arrow_left),
                contentDescription = "返回"
            )
        }
    },
    titleContent = { Text("通知设置") },
    actionContent = {
        HyperIconButton(onClick = onSearch) {
            Icon(
                painter = painterResource(LucideR.drawable.lucide_ic_search),
                contentDescription = "搜索"
            )
        }
    }
)
```

## 约束

- 不存在 `title`、`onBack`、`rightSlot` 参数。
- 默认最小高度为 `HyperTopBarDefaults.MinHeight`；自定义高度通过 `modifier.height(...)` 或 `modifier.heightIn(...)` 表达。
- 返回按钮是否出现、图标内容和点击行为都由调用方控制。
- `LocalContentColor` 会传递给三个 slot，`titleContent` 同时继承 `HyperTopBarDefaults.TitleTextStyle`。
- 默认容器为 `Color.Transparent`；只有确实需要独立色块时才通过 `HyperTopBarDefaults.colors(containerColor = ...)` 指定背景。

<WasmPreview demo="topbar" title="HyperTopBar 交互预览" />
