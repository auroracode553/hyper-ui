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

## Props（参数）

### HyperMenuGroup（数据项形式）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| items | List&lt;T&gt; | 是 | — | 由调用方提供的选项或列表数据。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperMenuGroupDefaults.ContentPadding) | 内部内容区域的布局修饰符。 |
| verticalArrangement | Arrangement.Vertical | 否 | Arrangement.spacedBy(0.dp) | 子项的垂直排列方式。 |
| colors | HyperMenuGroupColors | 否 | HyperMenuGroupDefaults.colors() | 组件各状态的颜色配置。 |
| itemContent | @Composable (item: T) -&gt; Unit | 是 | — | 每个数据项的自定义内容。 |

### HyperMenuGroup（内容 Slot 形式）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier.padding(HyperMenuGroupDefaults.ContentPadding) | 内部内容区域的布局修饰符。 |
| verticalArrangement | Arrangement.Vertical | 否 | Arrangement.spacedBy(0.dp) | 子项的垂直排列方式。 |
| colors | HyperMenuGroupColors | 否 | HyperMenuGroupDefaults.colors() | 组件各状态的颜色配置。 |
| content | @Composable ColumnScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

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
