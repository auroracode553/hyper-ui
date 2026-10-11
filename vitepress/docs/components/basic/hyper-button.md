---
description: 胶囊按钮，用于主要操作、次要操作、危险操作和独立图标入口。
---

# HyperButton

胶囊按钮，用于主要操作、次要操作、危险操作和独立图标入口。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="button" title="HyperButton 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperButton(onClick = onClose) { HyperText("关闭") }
HyperButton(onClick = onSave, type = "filled", colorStyle = "primary") { HyperText("保存") }
HyperButton(onClick = onConfirm, type = "tonal", colorStyle = "success") { HyperText("确认") }
```

</details>

## 使用约定

**样式约定**

按钮 small/default/large 高度 32/38/44dp，圆角为高度一半。省略 type 时使用默认描边；显式类型仅支持 filled/tonal/icon。主操作使用 filled 与 colorStyle = primary，独立图标入口使用 icon。默认文字 13sp、图标 17dp（纯图标按钮 20dp）。按下缩放 0.975、透明度 0.92，进入 85ms、恢复 180ms。

colorStyle 支持 primary/success/info/warning/danger；color 为自定义基色，优先于 colorStyle。两者都省略时为白色中性样式，深色模式随主题。配色不会改变 type；例如同一个 success 配色可用于默认描边、filled、tonal 或 icon。

**状态归属**

loading 与 enabled 由调用方控制，加载时停止点击。

交互预览仅保留一个禁用文字按钮，不重复展示禁用后外观相同的配色。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperButton

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    type: String? = null,
    color: Color = Color.Unspecified,
    colorStyle: String? = null,
    size: String = "default",
    colors: HyperButtonColors = HyperButtonDefaults.colors(type, color = color, colorStyle = colorStyle),
    border: BorderStroke? = HyperButtonDefaults.border(type, enabled, color = color, colorStyle = colorStyle),
    shape: Shape = HyperButtonDefaults.shape(size),
    contentPadding: PaddingValues = HyperButtonDefaults.contentPadding(size),
    role: Role = Role.Button,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| onClick | () -&gt; Unit | 是 | — | 用户点击回调。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| loading | Boolean | 否 | false | 是否处于加载态；加载时阻止重复点击。 |
| type | String? | 否 | null | 省略时默认描边；显式类型为 filled、tonal、icon，其他值抛出参数错误。 |
| color | Color | 否 | Color.Unspecified | 自定义基色，优先于 colorStyle，随 type 应用于填充、文字与轮廓。 |
| colorStyle | String? | 否 | null | primary/success/info/warning/danger 语义配色；省略为白色中性样式。 |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |
| colors | HyperButtonColors | 否 | HyperButtonDefaults.colors(type, color = color, colorStyle = colorStyle) | 各状态配色配置。 |
| border | BorderStroke? | 否 | HyperButtonDefaults.border(type, enabled, color = color, colorStyle = colorStyle) | 轮廓覆盖。 |
| shape | Shape | 否 | HyperButtonDefaults.shape(size) | 表面形状。 |
| contentPadding | PaddingValues | 否 | HyperButtonDefaults.contentPadding(size) | 主体内容的内部留白。 |
| role | Role | 否 | Role.Button | 组件的无障碍语义角色。 |
| horizontalArrangement | Arrangement.Horizontal | 否 | Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally) | 子项的水平排列方式。 |
| verticalAlignment | Alignment.Vertical | 否 | Alignment.CenterVertically | 子项的垂直对齐方式。 |
| content | @Composable RowScope.() -&gt; Unit | 是 | — | 主体内容插槽。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperButtonDefaults.shape**

```kotlin
fun shape(size: String = "default"): Shape
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |

**HyperButtonDefaults.iconSize**

```kotlin
fun iconSize(size: String = "default", iconOnly: Boolean = false): Dp
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |
| iconOnly | Boolean | 否 | false | 计算纯图标入口的尺寸与内边距。 |

**HyperButtonDefaults.height**

```kotlin
fun height(size: String = "default"): Dp =
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |

**HyperButtonDefaults.contentPadding**

```kotlin
fun contentPadding(size: String = "default"): PaddingValues
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |

**HyperButtonDefaults.textStyle**

```kotlin
@Composable
    fun textStyle(size: String = "default"): TextStyle
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |

**HyperButtonDefaults.colors**

```kotlin
@Composable
    fun colors(
        type: String? = null,
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        color: Color = Color.Unspecified,
        colorStyle: String? = null
    ): HyperButtonColors
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| type | String? | 否 | null | 省略时默认描边；显式类型为 filled、tonal、icon。 |
| containerColor | Color | 否 | Color.Unspecified | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| contentColor | Color | 否 | Color.Unspecified | 主要文字与图标颜色。 |
| disabledContainerColor | Color | 否 | Color.Unspecified | 禁用时的表面背景色。 |
| disabledContentColor | Color | 否 | Color.Unspecified | 禁用时的文字与图标颜色。 |
| color | Color | 否 | Color.Unspecified | 自定义基色，优先于 colorStyle。 |
| colorStyle | String? | 否 | null | primary/success/info/warning/danger 预设配色。 |

**HyperButtonDefaults.border**

```kotlin
@Composable
    fun border(
        type: String? = null,
        enabled: Boolean = true,
        color: Color = Color.Unspecified,
        colorStyle: String? = null
    ): BorderStroke?
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| type | String? | 否 | null | 省略时默认描边；显式类型为 filled、tonal、icon。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| color | Color | 否 | Color.Unspecified | 自定义基色，参与轮廓配色。 |
| colorStyle | String? | 否 | null | primary/success/info/warning/danger 预设配色。 |

**HyperButtonColors 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 是 | — | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| contentColor | Color | 是 | — | 主要文字与图标颜色。 |
| disabledContainerColor | Color | 是 | — | 禁用时的表面背景色。 |
| disabledContentColor | Color | 是 | — | 禁用时的文字与图标颜色。 |

</details>
