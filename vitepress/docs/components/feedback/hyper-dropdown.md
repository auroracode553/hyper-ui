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
