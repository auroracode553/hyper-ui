# HyperDropdown

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="dropdown" title="HyperDropdown 交互预览" />

## 样式约定

选择字段 subtle 材质 radius 16dp，最小高 48dp，padding 14×11dp。菜单 strong radius 18dp、padding 5dp，选项为 plain 列表行；选中项带 18dp 勾。菜单宽度匹配字段、默认最大高 320dp。

## 状态归属与使用约束

值由调用方持有，展开状态由组件维护。空选项和禁用项不派发值变化。浮层按安全区和键盘空间翻转、限制尺寸。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperDropdown(listOf(HyperOption("all", "全部"), HyperOption("saved", "收藏")), value, { value = it }, label = "范围")
```

## HyperDropdown API / Props

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

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| options | List&lt;HyperOption&lt;T&gt;&gt; | 是 | — | 值、标签、可用状态组成的选项列表。 |
| value | T? | 是 | — | 调用方持有的当前值。 |
| onValueChange | (T) -&gt; Unit | 是 | — | 用户修改值时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| label | String? | 否 | null | 可覆盖的 label 配置；默认表达式见此表。 |
| placeholder | String | 否 | "请选择" | 可覆盖的 placeholder 配置；默认表达式见此表。 |
| menuMaxHeight | Dp | 否 | 320.dp | 选项菜单最大高度；同时受可用安全空间限制。 |
| width | Dp? | 否 | null | 字段宽度；null 时使用父约束。 |

## HyperOption 配置

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| value | T | 是 | — | 调用方持有的当前值。 |
| label | String | 是 | — | 可覆盖的 label 配置；默认表达式见此表。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
