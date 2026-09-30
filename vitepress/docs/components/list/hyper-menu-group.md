# HyperMenuGroup

包名：`hyper_ui`。

<WasmPreview demo="hyper_menu_list" title="HyperMenuGroup 交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun <T> HyperMenuGroup(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperMenuGroupDefaults.ContentPadding),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    colors: HyperMenuGroupColors = HyperMenuGroupDefaults.colors(),
    itemContent: @Composable (item: T) -> Unit
)

@Composable
fun HyperMenuGroup(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperMenuGroupDefaults.ContentPadding),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    colors: HyperMenuGroupColors = HyperMenuGroupDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit
)
```

## 最小用法

```kotlin
@Composable
fun MenuOptions(options: List<String>) {
    HyperMenuGroup(items = options) { option ->
        HyperListTile(
            headlineContent = { Text(option) },
            dividerVisible = true,
            onClick = { /* 由调用方处理 */ }
        )
    }
}

@Composable
fun SettingsGroup(enabled: Boolean, onEnabledChange: (Boolean) -> Unit) {
    HyperMenuGroup {
        HyperListTile(
            headlineContent = { Text("推送通知") },
            supportingContent = { Text("接收重要消息提醒") },
            trailingContent = {
                HyperSwitch(
                    checked = enabled,
                    onCheckedChange = onEnabledChange
                )
            }
        )
    }
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
