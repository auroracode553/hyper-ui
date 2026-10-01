# HyperList

包名：`hyper_ui`。

<WasmPreview demo="hyper_list" title="HyperList 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperList(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = HyperListDefaults.ContentPadding,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    shape: Shape = HyperListDefaults.Shape,
    border: BorderStroke? = null,
    colors: HyperListColors = HyperListDefaults.colors(),
    content: LazyListScope.() -> Unit
)
```

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier | 内部内容区域的布局修饰符。 |
| state | LazyListState | 否 | rememberLazyListState() | 组件使用的状态对象，由调用方提供或记忆。 |
| contentPadding | PaddingValues | 否 | HyperListDefaults.ContentPadding | 主体内容的内部留白。 |
| verticalArrangement | Arrangement.Vertical | 否 | Arrangement.spacedBy(0.dp) | 子项的垂直排列方式。 |
| shape | Shape | 否 | HyperListDefaults.Shape | 组件容器的形状。 |
| border | BorderStroke? | 否 | null | 容器边框配置。 |
| colors | HyperListColors | 否 | HyperListDefaults.colors() | 组件各状态的颜色配置。 |
| content | LazyListScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

## 最小用法

```kotlin
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import hyper_ui.*

@Composable
fun AccountList(accounts: List<Account>) {
    HyperList {
        items(
            items = accounts,
            key = { account -> account.id },
            contentType = { "account" }
        ) { account ->
            HyperListTile(
                headlineContent = { Text(account.name) },
                dividerVisible = account != accounts.lastOrNull()
            )
        }
    }
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
