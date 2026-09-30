# HyperIconButton

包名：`hyper_ui`。

`size` 支持 `small`、`default`、`large`，图标内容仍通过 Slot 注入。

<WasmPreview demo="icon_button" title="HyperIconButton 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: String = "default",
    shape: Shape = HyperIconButtonDefaults.Shape,
    colors: HyperIconButtonColors = HyperIconButtonDefaults.colors(),
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
)
```

## 最小用法

```kotlin
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Modifier
import hyper_ui.*

HyperIconButton(onClick = onSearch) {
    HyperIcon(
        imageVector = Icons.Default.Search,
        contentDescription = "搜索",
        modifier = Modifier.size(HyperIconButtonDefaults.IconSize)
    )
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
