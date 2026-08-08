# HyperRadioButton

- 分类：表单组件
- 包名：`hyper_ui`
- 状态模型：受控组件；`selected` 和同组互斥规则由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/selection/HyperSelectionControls.kt`
- Preview 注册：`preview/src/commonMain/kotlin/hyper_ui/docs/data/FormComponentDemos.kt`
- Preview 交互：`preview/src/commonMain/kotlin/hyper_ui/docs/ui/FormComponentShowcases.kt`

## 公开 API

```kotlin
@Composable
fun HyperRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedColor: Color = Color.Unspecified,
    unselectedColor: Color = Color.Unspecified,
    unselectedBorderColor: Color = Color.Unspecified,
    innerDotColor: Color = rgba(255, 255, 255, 1f)
)
```

## 关键公开类型

```kotlin
object HyperRadioDefaults {
    val OuterSize = 24.dp
    val InnerDotSize = 10.dp
    val BorderWidth = 2.dp
}
```

`HyperRadioDefaults` 是公开尺寸常量集合；当前 `HyperRadioButton` 签名不接收该对象作为配置参数。

## 参数

| 参数 | 类型 | 必填 | 默认值 | 状态归属 / 作用 |
| --- | --- | --- | --- | --- |
| `selected` | `Boolean` | 是 | 无 | 调用方持有的当前选中状态。 |
| `onClick` | `(() -> Unit)?` | 是，可传 `null` | 无 | 调用方在点击后更新同组选择；`null` 表示不可点击。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 调整外层布局。 |
| `enabled` | `Boolean` | 否 | `true` | 调用方控制可用态。 |
| `selectedColor` | `Color` | 否 | `Color.Unspecified` | 选中背景色；未指定时使用 `HyperColors.accent`。 |
| `unselectedColor` | `Color` | 否 | `Color.Unspecified` | 未选中背景色；未指定时使用 `HyperColors.elevatedContainer`（半透明玻璃托盘）。 |
| `unselectedBorderColor` | `Color` | 否 | `Color.Unspecified` | 未选中描边色；未指定时使用 `HyperColors.accent`。 |
| `innerDotColor` | `Color` | 否 | `rgba(255, 255, 255, 1f)` | 选中态内部圆点颜色。 |

## 状态归属

- 组件不会自行切换 `selected`，也不会管理一组单选项。
- 调用方持有组值，并在每个选项的 `onClick` 中写入对应值。
- 组件内部只处理背景、描边、圆点动画和禁用透明度。

## 最小用法

```kotlin
var mode by remember { mutableStateOf("balanced") }

HyperRadioButton(
    selected = mode == "balanced",
    onClick = { mode = "balanced" }
)
```

## 约束与行为

- 只有 `enabled && onClick != null` 时才可点击，语义角色为 `Role.RadioButton`。
- `onClick = null` 不等于 `enabled = false`：两者都会阻止点击，但禁用透明度只由 `enabled` 控制。
- 同组互斥、必选校验和选项标签布局均由调用方实现。
- 未选中态默认使用卡片背景和主题色 `2.dp` 描边。
- 项目颜色规范禁止十六进制硬编码；自定义颜色使用项目允许的 RGBA 写法。

## 常见误用

```kotlin
// 错误：两个固定 selected 值不会形成可交互的互斥组。
HyperRadioButton(selected = true, onClick = { })
HyperRadioButton(selected = false, onClick = { })
```

应让多个选项读取同一个调用方状态，并在各自回调中更新它。

## 相关 API

- `HyperRadioDefaults`
- `HyperThemeConfig`
- `HyperColors.accent`

## 交互预览

<WasmPreview demo="radio" title="HyperRadioButton 交互预览" />
