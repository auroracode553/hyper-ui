# HyperButton

包名：`hyper_ui`。

<WasmPreview demo="button" title="HyperButton 交互预览" />

`type` 支持 `filled`、`tonal`、`outline`、`ghost`、`danger`、`icon`；`size` 控制尺寸。`type = "icon"` 用于方形图标按钮。

## 公开签名与默认值

```kotlin
@Composable
fun HyperButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    type: String = "filled",
    size: String = "default",
    colors: HyperButtonColors = HyperButtonDefaults.colors(type),
    border: BorderStroke? = HyperButtonDefaults.border(type, enabled),
    shape: Shape = HyperButtonDefaults.Shape,
    contentPadding: PaddingValues = HyperButtonDefaults.contentPadding(size),
    role: Role = Role.Button,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
)
```

## 最小用法

```kotlin
HyperButton(onClick = onSave, type = "filled") { HyperText("保存") }

HyperButton(onClick = onSearch, type = "icon") {
    HyperIcon(Icons.Default.Search, contentDescription = "搜索")
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
