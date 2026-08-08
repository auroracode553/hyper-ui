# HyperButton

- 分类：基础组件
- 包名：`hyper_ui`
- 状态模型：无业务状态；点击事件由调用方处理
- 源码：`library/src/main/java/hyper_ui/components/button/HyperButton.kt`
- Preview 注册：`preview/src/commonMain/kotlin/hyper_ui/docs/data/BasicComponentDemos.kt`
- Preview 交互：`preview/src/commonMain/kotlin/hyper_ui/docs/ui/BasicComponentShowcases.kt`

## 公开 API

```kotlin
@Composable
fun HyperButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: HyperButtonVariant = HyperButtonVariant.Primary,
    minHeight: Dp = 40.dp,
    horizontalPadding: Dp = 14.dp,
    fontSize: TextUnit = 15.sp,
    verticalPadding: Dp = 8.dp,
    defaultBorderColor: Color = Color.Unspecified,
    leadingIcon: (@Composable RowScope.() -> Unit)? = null,
    trailingIcon: (@Composable RowScope.() -> Unit)? = null
)
```

## 关键公开类型

```kotlin
enum class HyperButtonVariant {
    Default,
    Primary,
    Success,
    Info,
    Warning,
    Danger
}
```

## 参数

| 参数 | 类型 | 必填 | 默认值 | 状态归属 / 作用 |
| --- | --- | --- | --- | --- |
| `text` | `String` | 是 | 无 | 调用方提供的按钮文案。 |
| `onClick` | `() -> Unit` | 是 | 无 | 调用方处理点击后的业务逻辑。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 调整按钮外层布局。 |
| `enabled` | `Boolean` | 否 | `true` | 调用方控制可用态；为 `false` 时不触发 `onClick`。 |
| `variant` | `HyperButtonVariant` | 否 | `HyperButtonVariant.Primary` | 选择六种视觉变体之一。 |
| `minHeight` | `Dp` | 否 | `40.dp` | 按钮最小高度。 |
| `horizontalPadding` | `Dp` | 否 | `14.dp` | 水平内边距。 |
| `fontSize` | `TextUnit` | 否 | `15.sp` | 文案字号。 |
| `verticalPadding` | `Dp` | 否 | `8.dp` | 垂直内边距。 |
| `defaultBorderColor` | `Color` | 否 | `Color.Unspecified` | 仅 `Default` 变体使用；未指定时取主题强调色的 50% 透明度。 |
| `leadingIcon` | `@Composable RowScope.() -> Unit` | 否 | `null` | 文案前方的可组合插槽。 |
| `trailingIcon` | `@Composable RowScope.() -> Unit` | 否 | `null` | 文案后方的可组合插槽。 |

## 状态归属

- 组件只处理按压、禁用透明度和颜色等 UI 表现。
- 点击次数、加载状态、提交结果、页面跳转等均由调用方持有。
- 需要防止重复提交时，由调用方通过 `enabled` 或自己的提交状态控制。

## 最小用法

```kotlin
HyperButton(
    text = "保存",
    onClick = { /* 调用应用自己的保存逻辑 */ }
)
```

带调用方状态：

```kotlin
var submitting by remember { mutableStateOf(false) }

HyperButton(
    text = if (submitting) "保存中" else "保存",
    enabled = !submitting,
    onClick = { submitting = true },
    variant = HyperButtonVariant.Primary
)
```

## 约束与行为

- `Default` 为透明背景加边框；其余变体使用主题/语义色容器，并统一叠加 `glassHighlightBrush` 顶部玻璃高光（对齐 `HyperIconButton` 托盘风格）。
- `Primary` 读取 HyperUI 主题强调色，`Success` 读取成功色，`Danger` 读取 `MaterialTheme.colorScheme.error`。
- 非 `Default` 变体使用默认颜色时，`enabled = false` 会回退到 `HyperColors.disabledContainer`；自定义颜色按 `HyperStyleDefaults.DisabledAlpha` 缩放透明度。
- 组件只接受字符串文案；图标应放入 `leadingIcon` 或 `trailingIcon`，不要假设存在 `content` 参数。
- 不要把网络请求、数据库访问或导航规则写入组件实现；这些逻辑放在 `onClick` 中调用业务层。
- 项目颜色规范禁止十六进制硬编码；自定义颜色使用项目允许的 RGBA 写法。

## 常见误用

```kotlin
// 错误：HyperButton 没有自动提交状态，也没有 content 参数。
HyperButton(onClick = { }) { Text("保存") }
```

应显式传入 `text`，并由调用方管理提交状态。

## 相关 API

- `HyperButtonVariant`
- `HyperThemeConfig`
- `HyperColors`
- `HyperColors.elevatedContainer`
- `HyperColors.disabledContainer`
- `HyperColors.glassHighlightBrush`

## 交互预览

<WasmPreview demo="button" title="HyperButton 交互预览" />
