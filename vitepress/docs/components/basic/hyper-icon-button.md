# HyperIconButton

`HyperIconButton` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

按下时整个玻璃表面与图标一起缩至 0.975、透明度降至 0.92，释放或拖出取消后恢复。使用 `size` 调整外径，默认 36dp。

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

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- Android 原生窗口和系统工具仅在 Android 目标可用；Preview 使用跨平台示例。
