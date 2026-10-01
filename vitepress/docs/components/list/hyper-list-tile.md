# HyperListTile

包名：`hyper_ui`。

<WasmPreview demo="hyper_list" title="HyperListTile 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperListTile(
    headlineContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperListTileDefaults.ContentPadding),
    dividerModifier: Modifier = Modifier.padding(start = HyperListTileDefaults.DividerInset),
    enabled: Boolean = true,
    dividerVisible: Boolean = false,
    colors: HyperListTileColors = HyperListTileDefaults.colors(),
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
)
```

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| headlineContent | @Composable ColumnScope.() -&gt; Unit | 是 | — | 列表行主标题内容。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperListTileDefaults.ContentPadding) | 内部内容区域的布局修饰符。 |
| dividerModifier | Modifier | 否 | Modifier.padding(start = HyperListTileDefaults.DividerInset) | 分隔线的布局修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| dividerVisible | Boolean | 否 | false | 是否显示行分隔线。 |
| colors | HyperListTileColors | 否 | HyperListTileDefaults.colors() | 组件各状态的颜色配置。 |
| onClick | (() -&gt; Unit)? | 否 | null | 用户点击时执行的回调。 |
| leadingContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 内容前方的自定义区域。 |
| supportingContent | (@Composable ColumnScope.() -&gt; Unit)? | 否 | null | 标题或输入框下方的辅助内容。 |
| trailingContent | (@Composable RowScope.() -&gt; Unit)? | 否 | null | 内容末端的自定义区域。 |

## 最小用法

```kotlin
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR

HyperListTile(
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
