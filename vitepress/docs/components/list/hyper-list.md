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
