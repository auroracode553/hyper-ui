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

选择字段 subtle 材质 radius 16dp，最小高 48dp，padding 14×11dp。菜单 strong radius 18dp、padding 5dp，选项为 plain 列表行；选中项带 18dp 勾。菜单宽度匹配字段、默认最大高 320dp。

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
    value: T?,
    onValueChange: (T) -> Unit,
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
| options | List&lt;HyperOption&lt;T&gt;&gt; | 是 | — | 值、标签、可用状态组成的选项列表。 |
| value | T? | 是 | — | 调用方持有的当前值。 |
| onValueChange | (T) -&gt; Unit | 是 | — | 用户修改值时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| label | String? | 否 | null | 显示的标签文字。 |
| placeholder | String | 否 | "请选择" | 未选择选项时显示的占位文字。 |
| menuMaxHeight | Dp | 否 | 320.dp | 选项菜单最大高度；同时受可用安全空间限制。 |
| width | Dp? | 否 | null | 字段宽度；null 时使用父约束。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperOption 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | T | 是 | — | 调用方持有的当前值。 |
| label | String | 是 | — | 显示的标签文字。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |

</details>
