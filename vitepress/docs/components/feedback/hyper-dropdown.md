# HyperDropdown

包名：`hyper_ui`。

<WasmPreview demo="dropdown" title="HyperDropdown 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun HyperDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperDropdownDefaults.MenuPadding),
    alignment: Alignment = Alignment.TopEnd,
    offset: DpOffset = DpOffset(0.dp, HyperDropdownDefaults.AnchorOffsetY),
    shape: Shape = HyperDropdownDefaults.Shape,
    colors: HyperDropdownColors = HyperDropdownDefaults.colors(),
    content: @Composable HyperDropdownScope.() -> Unit
)
```

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| expanded | Boolean | 是 | — | 菜单是否展开，由调用方持有。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭时通知调用方更新可见状态。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperDropdownDefaults.MenuPadding) | 内部内容区域的布局修饰符。 |
| alignment | Alignment | 否 | Alignment.TopEnd | 弹出菜单相对于窗口的对齐方式。 |
| offset | DpOffset | 否 | DpOffset(0.dp, HyperDropdownDefaults.AnchorOffsetY) | 弹出菜单相对对齐位置的偏移。 |
| shape | Shape | 否 | HyperDropdownDefaults.Shape | 组件容器的形状。 |
| colors | HyperDropdownColors | 否 | HyperDropdownDefaults.colors() | 组件各状态的颜色配置。 |
| content | @Composable HyperDropdownScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

## 最小用法

```kotlin
HyperDropdown(
    expanded = expanded,
    onDismissRequest = { expanded = false }
) {
    Item(onClick = onSelect) { HyperText("菜单项") }
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
