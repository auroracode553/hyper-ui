# HyperCheckbox

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="checkbox" title="HyperCheckbox 交互预览" />

## 样式约定

44dp 触摸区、21dp 可见方框、7dp 圆角、1.5dp 轮廓、15dp 勾或横线。

## 状态归属与使用约束

checked 为 Boolean?。两态必须非 null；三态按 false → true → null → false 循环。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperCheckbox(checked, { checked = it }, tristate = true)
```

## HyperCheckbox API / Props

```kotlin
@Composable
fun HyperCheckbox(
    checked: Boolean?,
    onCheckedChange: (Boolean?) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tristate: Boolean = false,
    checkedColor: Color = Color.Unspecified,
    uncheckedColor: Color = Color.Unspecified,
    uncheckedBorderColor: Color = Color.Unspecified,
    checkmarkColor: Color = Color(1f, 1f, 1f, 1f)
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| checked | Boolean? | 是 | — | 调用方持有的选择值。 |
| onCheckedChange | (Boolean?) -&gt; Unit | 是 | — | 用户改变选中值时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| tristate | Boolean | 否 | false | 允许 null 不确定状态，false → true → null → false。 |
| checkedColor | Color | 否 | Color.Unspecified | 勾选时的控件颜色。 |
| uncheckedColor | Color | 否 | Color.Unspecified | 未勾选时的控件颜色。 |
| uncheckedBorderColor | Color | 否 | Color.Unspecified | 未勾选时边框颜色。 |
| checkmarkColor | Color | 否 | Color(1f, 1f, 1f, 1f) | 勾选标记颜色。 |
