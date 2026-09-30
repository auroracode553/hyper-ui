# HyperButton

`HyperButton` 的公开 API 位于 `hyper_ui` 包。组件状态由调用方持有，通过参数和回调传入。外部间距使用 `modifier`；`type` 表示视觉层级，`size` 使用 `small`、`default`、`large` 控制尺寸。语义色通过 `colors` 注入，`loading` 阻止重复点击。

按下时立即开始 85ms 过渡，按钮整体缩至 0.975、透明度降至 0.92；释放或拖出取消后从当前进度用 180ms 恢复。拖出取消不会调用 `onClick`，禁用和加载态不触发按压反馈。

<WasmPreview demo="button" title="HyperButton 交互预览" />

`type` 支持 `filled`、`tonal`、`outline`、`ghost`、`danger`；`size` 支持 `small`、`default`、`large`。

## 公开签名与默认值

```kotlin
@Composable
fun HyperButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    type: String = "filled",
    size: String = "default",
    colors: HyperButtonColors = HyperButtonDefaults.colors(type),
    border: BorderStroke? = HyperButtonDefaults.border(type, enabled),
    shape: Shape = HyperButtonDefaults.Shape,
    contentPadding: PaddingValues = HyperButtonDefaults.contentPadding(size),
    role: Role = Role.Button,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
)
```

## 最小用法

```kotlin
HyperButton(onClick = onSave, type = "filled") { HyperText("保存") }
```

## 使用约束

- 使用 `HyperThemeConfig` 提供主题；需要采样背景时，将可视内容置于 `HyperBackdrop` 中。
- 组件不持有业务数据、导航或网络请求；`visible`、`value`、`selected` 等由调用方控制。
- Android 原生窗口和系统工具仅在 Android 目标可用；Preview 使用跨平台示例。
