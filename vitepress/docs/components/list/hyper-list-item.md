# HyperListItem

包名：`hyper_ui`。

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

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
