---
description: 统一正文、辅助文字和强调文字的字号、行高与颜色。
---

# HyperText

统一正文、辅助文字和强调文字的字号、行高与颜色。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="text" title="HyperText 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperText("正文")
```

</details>

## 使用约定

**样式约定**

独立使用为 14sp、行高 1.4；在按钮、列表与导航插槽中继承局部排版。颜色、字重与显式 TextStyle 仍可按用途配置，不提供尺寸档位。

设置有限 maxLines 时，未指定 overflow 默认显示省略号；未限制行数时默认裁切。显式 overflow 优先。

不需要文本布局结果时，`onTextLayout` 保持默认 null；需要布局信息时显式传入回调，回调时机由 BasicText 管理。

**状态归属**

显式 style、fontSize、fontWeight、lineHeight 会覆盖参考默认排版。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperText

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow? = null,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| text | String | 是 | — | 提示文字。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| color | Color | 否 | Color.Unspecified | 自定义颜色，覆盖默认配色。 |
| fontSize | TextUnit | 否 | TextUnit.Unspecified | 显式字号；未指定时继承组件或插槽排版。 |
| fontStyle | FontStyle? | 否 | null | 文字样式，例如斜体。 |
| fontWeight | FontWeight? | 否 | null | 文字字重。 |
| fontFamily | FontFamily? | 否 | null | 字体族；null 使用当前排版字体。 |
| letterSpacing | TextUnit | 否 | TextUnit.Unspecified | 字符间距；未指定时继承排版。 |
| textDecoration | TextDecoration? | 否 | null | 下划线、删除线等文字装饰。 |
| textAlign | TextAlign? | 否 | null | 段落的水平对齐方式。 |
| lineHeight | TextUnit | 否 | TextUnit.Unspecified | 显式行高；未指定时继承对应字号的行高。 |
| overflow | TextOverflow? | 否 | null | 显式覆盖截断策略；省略时有限 maxLines 使用 Ellipsis，否则 Clip。 |
| softWrap | Boolean | 否 | true | 是否允许自动换行。 |
| maxLines | Int | 否 | Int.MAX_VALUE | 允许显示的最大行数。 |
| minLines | Int | 否 | 1 | 最少占用的文本行数。 |
| onTextLayout | ((TextLayoutResult) -&gt; Unit)? | 否 | null | 按需获取文本布局结果；不观察布局时保留 null，使用普通字符串布局路径。 |
| style | TextStyle? | 否 | null | 显式文本样式；null 继承当前插槽或字号排版。 |
