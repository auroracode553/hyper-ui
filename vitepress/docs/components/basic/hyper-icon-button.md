# HyperIconButton

- 分类：基础组件
- 包名：`hyper_ui`
- 状态模型：无业务状态；点击事件由调用方处理
- 源码：`library/src/main/java/hyper_ui/components/button/HyperIconButton.kt`
- Preview 注册：`preview/src/commonMain/kotlin/hyper_ui/docs/data/BasicComponentDemos.kt`
- Preview 交互：`preview/src/commonMain/kotlin/hyper_ui/docs/ui/BasicComponentShowcases.kt`

## 公开 API

```kotlin
@Composable
fun HyperIconButton(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    enabled: Boolean = true,
    size: Dp = 40.dp,
    iconSize: Dp = 22.dp,
    showBorder: Boolean = true
)
```

## 关键公开类型

无专属配置类型；图标类型使用 Compose 的 `ImageVector`。

## 参数

| 参数 | 类型 | 必填 | 默认值 | 状态归属 / 作用 |
| --- | --- | --- | --- | --- |
| `imageVector` | `ImageVector` | 是 | 无 | 调用方提供的图标。 |
| `contentDescription` | `String?` | 是 | 无 | 无障碍描述；纯装饰图标才传 `null`。 |
| `onClick` | `() -> Unit` | 是 | 无 | 调用方处理点击后的业务逻辑。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 调整按钮外层布局。 |
| `tint` | `Color` | 否 | `Color.Unspecified` | 图标颜色；未指定时使用 `HyperColors.primaryText`。 |
| `backgroundColor` | `Color` | 否 | `Color.Unspecified` | 圆形背景色；未指定时使用 `HyperColors.elevatedContainer`。 |
| `enabled` | `Boolean` | 否 | `true` | 调用方控制可用态；为 `false` 时不触发 `onClick`。 |
| `size` | `Dp` | 否 | `40.dp` | 圆形按钮的宽高。 |
| `iconSize` | `Dp` | 否 | `22.dp` | 内部图标尺寸。 |
| `showBorder` | `Boolean` | 否 | `true` | 当前实现保留此参数，但源码未使用它渲染边框。 |

## 状态归属

- 组件只处理禁用透明度和默认颜色等 UI 表现。
- 选中项、操作结果、页面跳转和弹窗显示状态均由调用方管理。

## 最小用法

```kotlin
HyperIconButton(
    imageVector = Icons.Default.Search,
    contentDescription = "搜索",
    onClick = { /* 打开应用自己的搜索页面 */ }
)
```

## 约束与行为

- 组件固定裁剪为圆形；`size` 控制外层，`iconSize` 控制内部图标。
- 有实际操作含义的图标必须提供可理解的 `contentDescription`。
- `enabled = false` 时点击回调不会执行，图标和背景按禁用态降低可见度。
- 当前版本不要依赖 `showBorder` 改变视觉效果；该参数虽然公开，但实现中未参与修饰符链。
- 项目颜色规范禁止十六进制硬编码；自定义颜色使用项目允许的 RGBA 写法。

## 常见误用

```kotlin
// 错误：不能依赖当前版本的 showBorder 来显示或隐藏边框。
HyperIconButton(
    imageVector = Icons.Default.Close,
    contentDescription = "关闭",
    onClick = onClose,
    showBorder = false
)
```

如需不同背景，使用 `backgroundColor`；如确实需要边框，先确认组件实现已支持该能力。

## 相关 API

- `ImageVector`
- `HyperColors.elevatedContainer`
- `HyperStyleDefaults.DisabledAlpha`

## 交互预览

<WasmPreview demo="icon_button" title="HyperIconButton 交互预览" />
