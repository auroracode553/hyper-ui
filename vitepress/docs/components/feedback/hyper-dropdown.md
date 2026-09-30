# HyperDropdown

`HyperDropdown` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。

<WasmPreview demo="dropdown" title="HyperDropdown 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperDropdownDefaults.MenuPadding),
    alignment: Alignment = Alignment.TopEnd,
    offset: DpOffset = DpOffset(0.dp, HyperDropdownDefaults.AnchorOffsetY),
    shape: Shape = HyperDropdownDefaults.Shape,
    colors: HyperDropdownColors = HyperDropdownDefaults.colors(),
    content: @Composable HyperDropdownScope.() -> Unit
)
```

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- 公开 API 仅支持 Android 手机端；Preview 只是文档交互工具。
