# HyperListItem

`HyperListItem` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

<WasmPreview demo="hyper_list" title="HyperListItem 交互预览" />

## 公开签名与默认值

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

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- 公开 API 仅支持 Android 手机端；Preview 只是文档交互工具。
