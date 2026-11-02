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

HyperButton(onPressed = onClose) { HyperText("关闭") }
HyperButton(onPressed = onSave, type = "filled", colorStyle = "primary") { HyperText("保存") }
HyperButton(onPressed = onConfirm, colorStyle = "success") { HyperText("确认") }
```

</details>

## 使用约定

**样式约定**

按钮统一高度 38dp，圆角为高度一半。省略 type 时使用默认玻璃表面；显式类型仅支持 filled/icon。主操作使用 filled 与 colorStyle = primary，独立图标入口使用 icon。默认文字 13sp、图标 17dp（纯图标按钮 20dp）。按下缩放 0.975、透明度 0.92，进入 85ms、恢复 180ms。

默认文字行高为 15.6sp，使用 LineHeightStyle.Alignment.Proportional 与 Trim.None，单行也保留完整行框，再由按钮的 verticalAlignment 居中。文字与图标、加载指示器共用内容中心线；不通过固定像素偏移调整字形，显式文字样式与对齐覆盖仍由调用方决定。

colorStyle 支持 primary/success/info/warning/danger；color 为自定义基色，优先于 colorStyle。两者都省略时为白色中性玻璃，深色模式随主题。配色不会改变 type；例如同一个 success 配色可用于默认玻璃、filled 或 icon。

默认、filled 与 icon 统一复用 core/surface/HyperGlassStyle.kt 的按钮参数和 HyperControlSurface.kt 的玻璃绘制，不保留旧渐变与品牌色投影皮肤。默认/图标按钮采用中性半透明底面；filled 将原语义基色以 alpha ×0.9 叠在取样上，前景继续按原亮度规则计算。显式 colors 背景与 border 覆盖继续生效，不擅自改变调用方颜色。

背景模糊为 8→6dp、折射边缘 12→14dp、位移 16→18dp、阴影半径 6→4.5dp、高光强度 0.5→1，随按压进度同步变化；文字与图标位于玻璃采样层上方。常规轮廓为共用的 0.5dp 柔和高光，语义配色的默认线框为 0.5dp；自定义 border 替代默认高光描边，不叠加实体边框。禁用态保留 controlTrack 与弱化前景，不采样、不投影、不响应按压；loading 保持正常玻璃配色并阻止点击。

需要可见背景折射时，由 HyperBackdrop 分开承载背景和组件；未提供背景源时保留半透明底面与交互，不影响按钮调用。38dp 高度、68dp 最小文字宽度、原内容留白和回调时机均由原布局管理。

**状态归属**

loading 与 enabled 由调用方控制，加载时停止点击。

两端交互预览以本 Kotlin 文档的 ButtonDemo 为基准，分组、文案、图标和 8/16dp 间距保持一致；仅保留一个禁用文字按钮，不重复展示外观相同的禁用配色。点击默认按钮可切换“处理中”的加载状态；处理中按钮在加载时拦截点击，空闲时点击再次进入加载。

手机预览使用同一可采样渐变与圆形背景、52dp 顶部留白与 350×680 外框；中英文使用合并的 Roboto / Noto Sans SC 预览字体，玻璃与阴影采用共用表面参数。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperButton

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperButton(
    onPressed: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    type: String? = null,
    color: Color = Color.Unspecified,
    colorStyle: String? = null,
    colors: HyperButtonColors? = null,
    border: BorderStroke? = HyperButtonDefaults.border(type, enabled && onPressed != null, color = color, colorStyle = colorStyle),
    shape: Shape = HyperButtonDefaults.shape(),
    contentPadding: PaddingValues = HyperButtonDefaults.contentPadding(),
    role: Role = Role.Button,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| onPressed | (() -&gt; Unit)? | 否 | null | 点击操作回调；为 null 时不能触发操作。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| loading | Boolean | 否 | false | 是否处于加载态；加载时阻止重复点击。 |
| type | String? | 否 | null | 省略为中性玻璃；filled 为语义色玻璃，icon 为圆形玻璃图标。 |
| color | Color | 否 | Color.Unspecified | 自定义颜色，覆盖默认配色。 |
| colorStyle | String? | 否 | null | 预设语义配色；自定义 color 优先。 |
| colors | HyperButtonColors? | 否 | null | 省略时按 type/color/colorStyle 解析玻璃配色；显式传入时完整保留背景与各状态颜色。 |
| border | BorderStroke? | 否 | HyperButtonDefaults.border(type, enabled &amp;&amp; onPressed != null, color = color, colorStyle = colorStyle) | 显式实体边框；默认中性玻璃无额外边框，语义描边为 0.5dp，禁用边框为 1dp。 |
| shape | Shape | 否 | HyperButtonDefaults.shape() | 组件容器的形状。 |
| contentPadding | PaddingValues | 否 | HyperButtonDefaults.contentPadding() | 主体内容的内部留白。 |
| role | Role | 否 | Role.Button | 组件的无障碍语义角色。 |
| horizontalArrangement | Arrangement.Horizontal | 否 | Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally) | 子项的水平排列方式。 |
| verticalAlignment | Alignment.Vertical | 否 | Alignment.CenterVertically | 子项的垂直对齐方式。 |
| content | @Composable RowScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperButtonDefaults.shape**

```kotlin
fun shape(): Shape
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| — | — | — | — | 无参数，采用组件统一规格。 |

**HyperButtonDefaults.iconSize**

```kotlin
fun iconSize(iconOnly: Boolean = false): Dp
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| iconOnly | Boolean | 否 | false | 是否只显示图标。 |

**HyperButtonDefaults.height**

```kotlin
fun height(): Dp
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| — | — | — | — | 无参数，采用组件统一规格。 |

**HyperButtonDefaults.contentPadding**

```kotlin
fun contentPadding(): PaddingValues
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| — | — | — | — | 无参数，采用组件统一规格。 |

**HyperButtonDefaults.textStyle**

```kotlin
@Composable
    fun textStyle(): TextStyle
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| — | — | — | — | 无参数，采用组件统一规格。 |

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
| type | String? | 否 | null | 组件的视觉或布局形态。 |
| containerColor | Color | 否 | Color.Unspecified | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| contentColor | Color | 否 | Color.Unspecified | 主要文字与图标颜色。 |
| disabledContainerColor | Color | 否 | Color.Unspecified | 禁用时的表面背景色。 |
| disabledContentColor | Color | 否 | Color.Unspecified | 禁用时的文字与图标颜色。 |
| color | Color | 否 | Color.Unspecified | 自定义颜色，覆盖默认配色。 |
| colorStyle | String? | 否 | null | 预设语义配色；自定义 color 优先。 |

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
| type | String? | 否 | null | 组件的视觉或布局形态。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| color | Color | 否 | Color.Unspecified | 自定义颜色，覆盖默认配色。 |
| colorStyle | String? | 否 | null | 预设语义配色；自定义 color 优先。 |

**HyperButtonColors 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 是 | — | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| contentColor | Color | 是 | — | 主要文字与图标颜色。 |
| disabledContainerColor | Color | 是 | — | 禁用时的表面背景色。 |
| disabledContentColor | Color | 是 | — | 禁用时的文字与图标颜色。 |

</details>
