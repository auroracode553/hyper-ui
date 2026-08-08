# HyperCheckbox

- 分类：表单组件
- 包名：`hyper_ui`
- 状态模型：受控组件；`checked` 由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/selection/HyperSelectionControls.kt`
- Preview 注册：`preview/src/commonMain/kotlin/hyper_ui/docs/data/FormComponentDemos.kt`
- Preview 交互：`preview/src/commonMain/kotlin/hyper_ui/docs/ui/FormComponentShowcases.kt`

## 公开 API

```kotlin
@Composable
fun HyperCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedColor: Color = Color.Unspecified,
    uncheckedColor: Color = Color.Unspecified,
    uncheckedBorderColor: Color = Color.Unspecified,
    checkmarkColor: Color = rgba(255, 255, 255, 1f)
)
```

## 关键公开类型

```kotlin
object HyperCheckboxDefaults {
    val BoxSize = 24.dp
    val CornerRadius = 8.dp
    val BorderWidth = 2.dp
    val CheckmarkSize = 16.dp
}
```

`HyperCheckboxDefaults` 是公开尺寸常量集合；当前 `HyperCheckbox` 签名不接收该对象作为配置参数。

## 参数

| 参数 | 类型 | 必填 | 默认值 | 状态归属 / 作用 |
| --- | --- | --- | --- | --- |
| `checked` | `Boolean` | 是 | 无 | 调用方持有的当前选中状态。 |
| `onCheckedChange` | `(Boolean) -> Unit` | 是 | 无 | 点击时回传 `!checked`；调用方应据此更新状态。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 调整外层布局。 |
| `enabled` | `Boolean` | 否 | `true` | 调用方控制可用态；为 `false` 时不回调。 |
| `checkedColor` | `Color` | 否 | `Color.Unspecified` | 选中背景色；未指定时使用 `HyperColors.accent`。 |
| `uncheckedColor` | `Color` | 否 | `Color.Unspecified` | 未选中背景色；未指定时使用 `HyperColors.elevatedContainer`（半透明玻璃托盘）。 |
| `uncheckedBorderColor` | `Color` | 否 | `Color.Unspecified` | 未选中描边色；未指定时使用 `HyperColors.accent`。 |
| `checkmarkColor` | `Color` | 否 | `rgba(255, 255, 255, 1f)` | 选中勾号颜色。 |

## 状态归属

- 组件不会在内部保存业务选中值。
- 调用方必须把 `onCheckedChange` 返回的新值写回自己的状态。
- 组件内部只处理背景、描边、勾号动画和禁用透明度。

## 最小用法

```kotlin
var accepted by remember { mutableStateOf(false) }

HyperCheckbox(
    checked = accepted,
    onCheckedChange = { accepted = it }
)
```

## 约束与行为

- 这是无涟漪点击的受控组件，语义角色为 `Role.Checkbox`。
- `enabled = false` 时既不切换，也不调用 `onCheckedChange`。
- 未选中态默认使用卡片背景和主题色 `2.dp` 描边。
- 尺寸来自 `HyperCheckboxDefaults`，当前公开签名没有单独的尺寸参数。
- 项目颜色规范禁止十六进制硬编码；自定义颜色使用项目允许的 RGBA 写法。

## 常见误用

```kotlin
// 错误：把常量传入后期待组件自行切换。
HyperCheckbox(
    checked = false,
    onCheckedChange = { }
)
```

## 相关 API

- `HyperCheckboxDefaults`
- `HyperThemeConfig`
- `HyperColors.accent`

## 交互预览

<WasmPreview demo="checkbox" title="HyperCheckbox 交互预览" />
