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

## Props（参数）

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| onClick | () -&gt; Unit | 是 | — | 用户点击时执行的回调。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| loading | Boolean | 否 | false | 是否处于加载态；加载时阻止重复点击。 |
| type | String | 否 | &quot;filled&quot; | 组件的视觉或布局形态。 |
| size | String | 否 | &quot;default&quot; | 组件尺寸档位。 |
| colors | HyperButtonColors | 否 | HyperButtonDefaults.colors(type) | 组件各状态的颜色配置。 |
| border | BorderStroke? | 否 | HyperButtonDefaults.border(type, enabled) | 容器边框配置。 |
| shape | Shape | 否 | HyperButtonDefaults.Shape | 组件容器的形状。 |
| contentPadding | PaddingValues | 否 | HyperButtonDefaults.contentPadding(size) | 主体内容的内部留白。 |
| role | Role | 否 | Role.Button | 组件的无障碍语义角色。 |
| horizontalArrangement | Arrangement.Horizontal | 否 | Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally) | 子项的水平排列方式。 |
| verticalAlignment | Alignment.Vertical | 否 | Alignment.CenterVertically | 子项的垂直对齐方式。 |
| content | @Composable RowScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

## 最小用法

```kotlin
HyperButton(onClick = onSave, type = "filled") { HyperText("保存") }

HyperButton(onClick = onSearch, type = "icon") {
    HyperIcon(Icons.Default.Search, contentDescription = "搜索")
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
