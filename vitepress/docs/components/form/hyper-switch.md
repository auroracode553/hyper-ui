# HyperSwitch

- 分类：表单组件
- 包名：`hyper_ui`
- 状态模型：受控组件；`checked` 由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/selection/HyperSelectionControls.kt`
- Preview 注册：`preview/src/commonMain/kotlin/hyper_ui/docs/data/FormComponentDemos.kt`
- Preview 交互：`preview/src/commonMain/kotlin/hyper_ui/docs/ui/FormComponentShowcases.kt`

## 公开 API

```kotlin
@Composable
fun HyperSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedTrackColor: Color = Color.Unspecified,
    uncheckedTrackColor: Color = Color.Unspecified,
    checkedThumbColor: Color = rgba(255, 255, 255, 1f),
    uncheckedThumbColor: Color = rgba(255, 255, 255, 1f)
)
```

## 关键公开类型

```kotlin
object HyperSwitchDefaults {
    val TrackWidth = 54.dp
    val TrackHeight = 32.dp
    val ThumbSize = 28.dp
    val ThumbElevation = 2.dp
}
```

`HyperSwitchDefaults` 是公开视觉常量集合；当前 `HyperSwitch` 签名不接收该对象作为配置参数。源码会读取前三个尺寸值，但当前实现未读取 `ThumbElevation`。

## 参数

| 参数 | 类型 | 必填 | 默认值 | 状态归属 / 作用 |
| --- | --- | --- | --- | --- |
| `checked` | `Boolean` | 是 | 无 | 调用方持有的当前开关状态。 |
| `onCheckedChange` | `(Boolean) -> Unit` | 是 | 无 | 点击时回传 `!checked`；调用方应据此更新状态。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 调整外层布局。 |
| `enabled` | `Boolean` | 否 | `true` | 调用方控制可用态；为 `false` 时不回调。 |
| `checkedTrackColor` | `Color` | 否 | `Color.Unspecified` | 选中轨道色；未指定时使用 `HyperColors.accent`。 |
| `uncheckedTrackColor` | `Color` | 否 | `Color.Unspecified` | 未选中轨道色；未指定时使用 `HyperColors.elevatedContainer`（半透明玻璃托盘）。 |
| `checkedThumbColor` | `Color` | 否 | `rgba(255, 255, 255, 1f)` | 选中滑块色。 |
| `uncheckedThumbColor` | `Color` | 否 | `rgba(255, 255, 255, 1f)` | 未选中滑块色。 |

## 状态归属

- 组件不会在内部保存业务开关值。
- 调用方必须把 `onCheckedChange` 返回的新值写回自己的状态。
- 组件内部只处理滑块位置、颜色动画和禁用透明度。

## 最小用法

```kotlin
var enabled by remember { mutableStateOf(false) }

HyperSwitch(
    checked = enabled,
    onCheckedChange = { enabled = it }
)
```

## 约束与行为

- 这是无涟漪点击的受控组件，语义角色为 `Role.Switch`。
- `enabled = false` 时既不切换，也不调用 `onCheckedChange`。
- 轨道与滑块尺寸来自 `HyperSwitchDefaults`，当前公开签名没有单独的尺寸参数。
- 当前版本不要依赖 `HyperSwitchDefaults.ThumbElevation` 产生阴影；该常量尚未参与渲染。
- 项目颜色规范禁止十六进制硬编码；自定义颜色使用项目允许的 RGBA 写法。

## 常见误用

```kotlin
// 错误：忽略回调值，checked 将永远不变。
HyperSwitch(
    checked = enabled,
    onCheckedChange = { }
)
```

## 相关 API

- `HyperSwitchDefaults`
- `HyperThemeConfig`
- `HyperColors.accent`

## 交互预览

<WasmPreview demo="switch" title="HyperSwitch 交互预览" />
