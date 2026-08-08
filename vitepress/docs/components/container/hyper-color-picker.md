# HyperColorPicker

- 分类：容器组件
- 包名：`hyper_ui`
- 状态模型：受控组件；`selectedId` 由调用方持有
- 源码：`library/src/main/java/hyper_ui/components/panel/HyperColorPicker.kt`
- Preview 注册：`preview/src/commonMain/kotlin/hyper_ui/docs/data/ContainerComponentDemos.kt`
- Preview 交互：`preview/src/commonMain/kotlin/hyper_ui/docs/ui/ContainerComponentShowcases.kt`

## 公开 API

```kotlin
@Composable
fun HyperColorPicker(
    options: List<HyperColorOption> = HyperColorPickerDefaults.presetOptions,
    selectedId: String,
    onSelected: (HyperColorOption) -> Unit,
    modifier: Modifier = Modifier,
    colorSize: Dp = HyperColorPickerDefaults.colorSize,
    horizontalSpacing: Dp = HyperColorPickerDefaults.horizontalSpacing,
    verticalSpacing: Dp = HyperColorPickerDefaults.verticalSpacing,
    labelTopSpacing: Dp = HyperColorPickerDefaults.labelTopSpacing
)
```

## 关键公开类型

```kotlin
data class HyperColorOption(
    val id: String,
    val label: String,
    val color: Color
)

object HyperColorPickerDefaults {
    val colorSize = 36.dp
    val horizontalSpacing = 10.dp
    val verticalSpacing = 14.dp
    val labelTopSpacing = 5.dp
    val colorBorderWidth = 1.dp
    val presetOptions: List<HyperColorOption>
}
```

`presetOptions` 在源码中初始化为下表的 32 项。

| `id` | 标签 | 颜色 |
| --- | --- | --- |
| `classic_red` | 经典红 | `rgba(231, 76, 60, 1f)` |
| `brick_red` | 砖红 | `rgba(184, 92, 56, 1f)` |
| `peach_orange` | 蜜橘 | `rgba(255, 140, 105, 1f)` |
| `vibrant_orange` | 活力橙 | `rgba(255, 103, 0, 1f)` |
| `sunny_yellow` | 暖阳黄 | `rgba(255, 159, 67, 1f)` |
| `amber_yellow` | 琥珀黄 | `rgba(255, 183, 0, 1f)` |
| `golden` | 金盏 | `rgba(255, 195, 0, 1f)` |
| `lemon_green` | 柠檬绿 | `rgba(164, 209, 82, 1f)` |
| `bud_green` | 嫩芽绿 | `rgba(123, 200, 108, 1f)` |
| `emerald` | 翡翠绿 | `rgba(46, 204, 113, 1f)` |
| `forest_green` | 森林绿 | `rgba(39, 174, 96, 1f)` |
| `mint_green` | 薄荷绿 | `rgba(26, 188, 156, 1f)` |
| `pine_green` | 青松绿 | `rgba(0, 200, 150, 1f)` |
| `lake_blue` | 湖蓝 | `rgba(72, 201, 176, 1f)` |
| `sky_blue` | 天蓝 | `rgba(93, 173, 226, 1f)` |
| `ocean_blue` | 海蓝 | `rgba(64, 120, 255, 1f)` |
| `sapphire` | 宝石蓝 | `rgba(41, 128, 185, 1f)` |
| `navy_blue` | 藏蓝 | `rgba(30, 55, 153, 1f)` |
| `indigo` | 靛青 | `rgba(56, 103, 214, 1f)` |
| `purple_blue` | 紫蓝 | `rgba(108, 92, 231, 1f)` |
| `violet` | 紫罗兰 | `rgba(156, 89, 209, 1f)` |
| `lavender` | 薰衣草 | `rgba(162, 155, 254, 1f)` |
| `sunset_orange` | 日暮橙 | `rgba(235, 109, 52, 1f)` |
| `deep_sea` | 深海蓝 | `rgba(15, 118, 178, 1f)` |
| `moss_green` | 苔藓绿 | `rgba(122, 168, 82, 1f)` |
| `wine_red` | 酒红 | `rgba(192, 57, 43, 1f)` |
| `warm_brown` | 暖棕 | `rgba(211, 84, 0, 1f)` |
| `olive_green` | 橄榄绿 | `rgba(106, 176, 76, 1f)` |
| `cyan_blue` | 青蓝 | `rgba(34, 166, 179, 1f)` |
| `hibiscus` | 木槿紫 | `rgba(179, 51, 113, 1f)` |
| `warm_gray` | 暖灰 | `rgba(149, 165, 166, 1f)` |
| `graphite` | 石墨黑 | `rgba(45, 52, 54, 1f)` |

## 参数

| 参数 | 类型 | 必填 | 默认值 | 状态归属 / 作用 |
| --- | --- | --- | --- | --- |
| `options` | `List<HyperColorOption>` | 否 | `HyperColorPickerDefaults.presetOptions` | 调用方可替换的颜色选项列表。 |
| `selectedId` | `String` | 是 | 无 | 调用方持有的当前选项 ID。 |
| `onSelected` | `(HyperColorOption) -> Unit` | 是 | 无 | 点击后回传完整选项；调用方应更新 `selectedId`。 |
| `modifier` | `Modifier` | 否 | `Modifier` | 调整颜色板外层布局。 |
| `colorSize` | `Dp` | 否 | `HyperColorPickerDefaults.colorSize`（`36.dp`） | 单个颜色圆的尺寸。 |
| `horizontalSpacing` | `Dp` | 否 | `HyperColorPickerDefaults.horizontalSpacing`（`10.dp`） | 选项的水平间距。 |
| `verticalSpacing` | `Dp` | 否 | `HyperColorPickerDefaults.verticalSpacing`（`14.dp`） | 选项的垂直间距。 |
| `labelTopSpacing` | `Dp` | 否 | `HyperColorPickerDefaults.labelTopSpacing`（`5.dp`） | 颜色圆与标签之间的间距。 |

## 状态归属

- 组件不会在内部更新选中项。
- 调用方持有 `selectedId`，并在 `onSelected` 中读取 `option.id` 后写回。
- 将所选颜色应用到主题、保存到设置或同步到业务层均由调用方负责。

## 最小用法

```kotlin
var selectedId by remember { mutableStateOf("ocean_blue") }

HyperColorPicker(
    selectedId = selectedId,
    onSelected = { option -> selectedId = option.id }
)
```

自定义选项：

```kotlin
val colors = listOf(
    HyperColorOption("brand", "品牌色", rgba(64, 120, 255, 1f)),
    HyperColorOption("success", "成功色", rgba(52, 199, 89, 1f))
)

HyperColorPicker(
    options = colors,
    selectedId = selectedId,
    onSelected = { selectedId = it.id }
)
```

## 约束与行为

- 组件使用 `FlowRow` 响应式换行，并在水平方向居中排列。
- 每个色块默认带 `colorBorderWidth` 细描边，调用方传入白色或浅色自定义选项时仍能看清边界。
- 选中判断只比较 `option.id == selectedId`；自定义选项的 ID 应稳定且唯一。
- `selectedId` 不在 `options` 中时不会有任何选项呈现选中态。
- 选中态使用 `accent` 色环形描边：外层固定尺寸（`colorSize + 4dp`）+ `accent` 背景形成环，内层为色块本体 + 中心白色指示点；未选中时外层透明，避免选中时布局抖动。
- 组件没有 `enabled` 参数；如需业务禁用策略，由调用方决定是否接受 `onSelected` 的结果。
- `onSelected` 返回完整 `HyperColorOption`，不要把它当作只返回 ID 或 `Color`。
- 项目颜色规范禁止十六进制硬编码；自定义选项使用项目允许的 RGBA 写法。

## 常见误用

```kotlin
// 错误：回调参数不是 String。
HyperColorPicker(
    selectedId = selectedId,
    onSelected = { id -> selectedId = id }
)
```

正确回调参数是 `HyperColorOption`，应使用 `option.id`、`option.label` 或 `option.color`。

## 相关 API

- `HyperColorOption`
- `HyperColorPickerDefaults`
- `HyperThemeConfig`

## 交互预览

<WasmPreview demo="color-picker" title="HyperColorPicker 交互预览" />
