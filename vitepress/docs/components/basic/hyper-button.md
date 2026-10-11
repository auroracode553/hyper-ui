# HyperButton

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="button" title="HyperButton 交互预览" />

## 样式约定

按钮 small/default/large 高度 32/38/44dp，圆角为高度一半；支持 filled/tonal/outline/ghost/danger/icon。默认文字 13sp、图标 17dp（纯图标按钮 20dp）。按下缩放 0.975、透明度 0.92，进入 85ms、恢复 180ms。

## 状态归属与使用约束

loading 与 enabled 由调用方控制，加载时停止点击。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperButton(onClick = onSave) { HyperText("保存") }
```

## HyperButton API / Props

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
    shape: Shape = HyperButtonDefaults.shape(size),
    contentPadding: PaddingValues = HyperButtonDefaults.contentPadding(size),
    role: Role = Role.Button,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| onClick | () -&gt; Unit | 是 | — | 用户点击回调。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| loading | Boolean | 否 | false | 是否处于加载态；加载时阻止重复点击。 |
| type | String | 否 | "filled" | 渲染形态；合法值见本页样式约定。 |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |
| colors | HyperButtonColors | 否 | HyperButtonDefaults.colors(type) | 各状态配色配置。 |
| border | BorderStroke? | 否 | HyperButtonDefaults.border(type, enabled) | 轮廓覆盖。 |
| shape | Shape | 否 | HyperButtonDefaults.shape(size) | 表面形状。 |
| contentPadding | PaddingValues | 否 | HyperButtonDefaults.contentPadding(size) | 主体内容的内部留白。 |
| role | Role | 否 | Role.Button | 组件的无障碍语义角色。 |
| horizontalArrangement | Arrangement.Horizontal | 否 | Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally) | 子项的水平排列方式。 |
| verticalAlignment | Alignment.Vertical | 否 | Alignment.CenterVertically | 子项的垂直对齐方式。 |
| content | @Composable RowScope.() -&gt; Unit | 是 | — | 主体内容插槽。 |

## HyperButtonDefaults.shape API / Props

```kotlin
fun shape(size: String = "default"): Shape
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |

## HyperButtonDefaults.iconSize API / Props

```kotlin
fun iconSize(size: String = "default", iconOnly: Boolean = false): Dp
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |
| iconOnly | Boolean | 否 | false | 可覆盖的 iconOnly 配置；默认表达式见此表。 |

## HyperButtonDefaults.height API / Props

```kotlin
fun height(size: String = "default"): Dp =
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |

## HyperButtonDefaults.contentPadding API / Props

```kotlin
fun contentPadding(size: String = "default"): PaddingValues
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |

## HyperButtonDefaults.textStyle API / Props

```kotlin
@Composable
    fun textStyle(size: String = "default"): TextStyle
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |

## HyperButtonDefaults.colors API / Props

```kotlin
@Composable
    fun colors(
        type: String = "filled",
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperButtonColors
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| type | String | 否 | "filled" | 渲染形态；合法值见本页样式约定。 |
| containerColor | Color | 否 | Color.Unspecified | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| contentColor | Color | 否 | Color.Unspecified | 主要文字与图标颜色。 |
| disabledContainerColor | Color | 否 | Color.Unspecified | 禁用时的表面背景色。 |
| disabledContentColor | Color | 否 | Color.Unspecified | 禁用时的文字与图标颜色。 |

## HyperButtonDefaults.border API / Props

```kotlin
@Composable
    fun border(
        type: String = "filled",
        enabled: Boolean = true,
        color: Color = Color.Unspecified
    ): BorderStroke?
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| type | String | 否 | "filled" | 渲染形态；合法值见本页样式约定。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| color | Color | 否 | Color.Unspecified | 颜色覆盖。 |

## HyperButtonColors 配置

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 是 | — | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| contentColor | Color | 是 | — | 主要文字与图标颜色。 |
| disabledContainerColor | Color | 是 | — | 禁用时的表面背景色。 |
| disabledContentColor | Color | 是 | — | 禁用时的文字与图标颜色。 |
