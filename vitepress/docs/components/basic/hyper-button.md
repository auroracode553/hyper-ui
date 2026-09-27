# HyperButton

`HyperButton` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；尺寸、颜色和插槽按下列源码签名配置。 `variant` 只表示视觉层级；成功色等语义色通过 `colors` 注入。`loading` 阻止重复点击。

<WasmPreview demo="button" title="HyperButton 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    variant: HyperButtonVariant = HyperButtonVariant.Filled,
    height: Dp = HyperButtonDefaults.MinHeight,
    colors: HyperButtonColors = HyperButtonDefaults.colors(variant),
    border: BorderStroke? = HyperButtonDefaults.border(variant, enabled),
    shape: Shape = HyperButtonDefaults.Shape,
    contentPadding: PaddingValues = HyperButtonDefaults.contentPadding(height),
    role: Role = Role.Button,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
)
```

## 最小用法

```kotlin
HyperButton(onClick = onSave, variant = HyperButtonVariant.Filled) { HyperText("保存") }
```

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- Android 原生窗口和系统工具仅在 Android 目标可用；Preview 使用跨平台示例。
