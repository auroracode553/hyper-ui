# HyperText

包名：hyper_ui。调用方使用 `import hyper_ui.*`。

<WasmPreview demo="text" title="HyperText 交互预览" />

## 样式约定

small/default/large 字号 12/14/18sp，行高 1.4；large 使用粗体。default 继承所在按钮、列表或导航插槽的局部排版。

## 状态归属与使用约束

显式 style、fontSize、fontWeight、lineHeight 会覆盖参考默认排版。

支持范围为 Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于 Preview。显式尺寸、颜色或排版覆盖会改变参考默认效果。

## 最小调用

```kotlin
import hyper_ui.*

HyperText("正文", size = "default")
```

## HyperText API / Props

```kotlin
@Composable
fun HyperText(
    text: String,
    modifier: Modifier = Modifier,
    size: String = "default",
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle? = null
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| text | String | 是 | — | 可覆盖的 text 配置；默认表达式见此表。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| size | String | 否 | "default" | small/default/large 尺寸档位。 |
| color | Color | 否 | Color.Unspecified | 颜色覆盖。 |
| fontSize | TextUnit | 否 | TextUnit.Unspecified | 可覆盖的 fontSize 配置；默认表达式见此表。 |
| fontStyle | FontStyle? | 否 | null | 可覆盖的 fontStyle 配置；默认表达式见此表。 |
| fontWeight | FontWeight? | 否 | null | 可覆盖的 fontWeight 配置；默认表达式见此表。 |
| fontFamily | FontFamily? | 否 | null | 可覆盖的 fontFamily 配置；默认表达式见此表。 |
| letterSpacing | TextUnit | 否 | TextUnit.Unspecified | 可覆盖的 letterSpacing 配置；默认表达式见此表。 |
| textDecoration | TextDecoration? | 否 | null | 可覆盖的 textDecoration 配置；默认表达式见此表。 |
| textAlign | TextAlign? | 否 | null | 可覆盖的 textAlign 配置；默认表达式见此表。 |
| lineHeight | TextUnit | 否 | TextUnit.Unspecified | 可覆盖的 lineHeight 配置；默认表达式见此表。 |
| overflow | TextOverflow | 否 | TextOverflow.Clip | 可覆盖的 overflow 配置；默认表达式见此表。 |
| softWrap | Boolean | 否 | true | 可覆盖的 softWrap 配置；默认表达式见此表。 |
| maxLines | Int | 否 | Int.MAX_VALUE | 可覆盖的 maxLines 配置；默认表达式见此表。 |
| minLines | Int | 否 | 1 | 可覆盖的 minLines 配置；默认表达式见此表。 |
| onTextLayout | (TextLayoutResult) -&gt; Unit | 否 | {} | 可覆盖的 onTextLayout 配置；默认表达式见此表。 |
| style | TextStyle? | 否 | null | 可覆盖的 style 配置；默认表达式见此表。 |
