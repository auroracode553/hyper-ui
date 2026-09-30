# HyperList

`HyperList` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

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
            HyperListItem(
                headlineContent = { Text(account.name) },
                dividerVisible = account != accounts.lastOrNull()
            )
        }
    }
}
```

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- 公开 API 仅支持 Android 手机端；Preview 只是文档交互工具。
