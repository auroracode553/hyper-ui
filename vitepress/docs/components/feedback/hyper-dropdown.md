---
description: 点击字段展开选项菜单，支持标签、占位、禁用项和受控单选。
---

# HyperDropdown

点击字段展开选项菜单，支持标签、占位、禁用项和受控单选。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="dropdown" title="HyperDropdown 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperDropdown(listOf(HyperOption("all", "全部"), HyperOption("saved", "收藏")), value, { value = it }, label = "范围")
```

</details>

## 使用约定

**样式约定**

选择字段 subtle 材质 radius 16dp，最小高 48dp，padding 14×11dp。width 为 null 时，按最长选项或占位文字的测量宽度加 68dp（包含内边距、箭头与勾选占位），默认限制在 160–240dp，再服从父级可用宽度。宽度基于全部选项，改变选中值时不会跳宽；字段标题不参与宽度计算，长标题换行。

需要更宽的表单字段时显式传 width，例如 width = 220.dp；默认不会占满父容器。菜单以字段最终测量宽度为准，受窗口 12dp 安全边距约束，默认最大高 320dp。菜单 strong radius 18dp、padding 5dp，选项为 plain 列表行；选中项带 18dp 勾。字段文字单行省略，长选项最多两行省略。

Compose 与 Flutter 使用相同的排序、负责人、220dp 每页数量、长选项、空选项与禁用预览，组间间距 24dp。

**状态归属**

值由调用方持有，展开状态由组件维护。空选项和禁用项不派发值变化。浮层按安全区和键盘空间翻转、限制尺寸。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperDropdown

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun <T> HyperDropdown(
    options: List<HyperOption<T>>,
    value: T? = null,
    onChanged: ((T) -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String = "请选择",
    menuMaxHeight: Dp = 320.dp,
    width: Dp? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| options | List&lt;HyperOption&lt;T&gt;&gt; | 是 | — | 可供选择的选项列表。 |
| value | T? | 否 | null | 由调用方持有的当前值。 |
| onChanged | ((T) -&gt; Unit)? | 否 | null | 用户修改值或选择项时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| label | String? | 否 | null | 数值旁显示的标签文字。 |
| placeholder | String | 否 | &quot;请选择&quot; | 未输入或未选择时的提示内容。 |
| menuMaxHeight | Dp | 否 | 320.dp | 下拉菜单最大高度。 |
| width | Dp? | 否 | null | 字段与菜单的期望宽度；省略时按文案自动取 160–240dp，显式值仍服从父级可用空间。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperOption 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | T | 是 | — | 调用方持有的当前值。 |
| label | String | 是 | — | 显示的标签文字。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |

</details>
