# 主题配置

包名：hyper_ui。默认样式参考 D:/my_project/flutter_project/doc_ui_kit/ui，组件只读取主题，不保存应用主题状态。

<WasmPreview demo="theme-material" title="Solid / Soft / Clear 材质" />

## 最小配置

```kotlin
import hyper_ui.*

HyperThemeConfig(material = HyperMaterial.Solid) {
    HyperButton(onSave) { HyperText("保存") }
}
```

material 是正式 UI 库 API，支持全应用或局部子树。默认 Solid，与参考项目一致。Soft/Clear 需要用 HyperBackdrop 提供真实背景采样；显式 glass 会覆盖工厂产生的令牌。

## HyperThemeConfig API / Props

```kotlin
@Composable
fun HyperThemeConfig(
    themeColor: Color = HyperStyleDefaults.DefaultThemeColor,
    successColor: Color = HyperStyleDefaults.SuccessColor,
    darkTheme: Boolean = isSystemInDarkTheme(),
    typography: HyperTypography = HyperStyleDefaults.Typography,
    shapes: HyperShapes = HyperStyleDefaults.Shapes,
    material: HyperMaterial = HyperMaterial.Solid,
    glass: HyperGlassTokens = HyperGlassTokens.forMaterial(darkTheme, material),
    content: @Composable () -> Unit
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| themeColor | Color | 否 | HyperStyleDefaults.DefaultThemeColor | 品牌色 rgba(71, 111, 232) |
| successColor | Color | 否 | HyperStyleDefaults.SuccessColor | 成功色 rgba(34, 197, 94) |
| darkTheme | Boolean | 否 | isSystemInDarkTheme() | 浅/深色模式，默认跟随系统 |
| typography | HyperTypography | 否 | HyperStyleDefaults.Typography | 主题字体与排版 |
| shapes | HyperShapes | 否 | HyperStyleDefaults.Shapes | 通用形状；组件的专用圆角见各组件页 |
| material | HyperMaterial | 否 | HyperMaterial.Solid | Solid、Soft、Clear 三档材质 |
| glass | HyperGlassTokens | 否 | HyperGlassTokens.forMaterial(darkTheme, material) | 材质令牌，显式传入优先 |
| content | @Composable () -&gt; Unit | 是 | — | 使用该主题的子树 |

## 材质档位

| 档位 | 浅色 regular / strong / subtle | 深色基底 | blur / blurStrong |
| --- | --- | --- | --- |
| Solid | 白色，透明度 255 / 255 / 255 | rgba(27, 31, 39)，透明度 255 | 0 / 0dp |
| Soft | 白色，透明度 217 / 247 / 191 | regular rgba(30,32,38,217/255)，strong rgba(44,46,51,242/255)，subtle rgba(30,32,38,179/255) | 20 / 28dp |
| Clear | 白色，透明度 118 / 153 / 77 | rgba(27,31,39)，透明度 118 / 153 / 77 | 32 / 40dp |

边框为 edgeShade 合成到 edgeHighlight。玻璃使用两层主题阴影：radius 28dp、spread -6dp、offset (0,12dp)；第二层 radius 8dp、spread -3dp、offset (0,3dp)、透明度乘 0.42。subtle 表面不绘制阴影。边框只绘制，不增加组件尺寸或内容间距。

## HyperGlassTokens API / Props

```kotlin
// HyperGlassTokens 的 companion object 成员
companion object {
    fun forMaterial(darkTheme: Boolean, material: HyperMaterial): HyperGlassTokens
    fun light(): HyperGlassTokens
    fun dark(): HyperGlassTokens
}
```

light()/dark() 返回 Soft 基础令牌；forMaterial() 生成指定档位。

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| darkTheme | Boolean | 是 | — | forMaterial 的模式 |
| material | HyperMaterial | 是 | — | forMaterial 的材质 |

构造配置如下；前十项为必填，推荐从工厂 copy 覆盖。

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| surface | Color | 是 | — | regular 材质 |
| surfaceStrong | Color | 是 | — | prominent 材质 |
| surfaceSubtle | Color | 是 | — | subtle 材质 |
| edgeHighlight | Color | 是 | — | 迎光边缘 |
| edgeShade | Color | 是 | — | 轮廓合成色 |
| shadow | Color | 是 | — | 两层环境阴影 |
| controlTrack | Color | 是 | — | 控件轨道与禁用背景 |
| selection | Color | 是 | — | 选中条目底面 |
| pressed | Color | 是 | — | 按压状态令牌 |
| scrim | Color | 是 | — | 模态蒙层 |
| blur | Dp | 否 | 20.dp | regular/subtle 模糊半径 |
| blurStrong | Dp | 否 | 28.dp | prominent 模糊半径 |

border 为只读 Color 属性，计算为 edgeShade.compositeOver(edgeHighlight)。

## HyperTypography 配置 Props

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| headlineSmall | TextStyle | 否 | Default 字体，Medium，24sp / 32sp | 大标题 |
| bodyLarge | TextStyle | 否 | Default 字体，Normal，16sp / 24sp，字距 0sp | 较大正文 |
| bodyMedium | TextStyle | 否 | Default 字体，Normal，14sp / 20sp，字距 0sp | 正文基础样式 |
| bodySmall | TextStyle | 否 | Default 字体，Normal，12sp / 16sp，字距 0sp | 辅助文字 |
| titleLarge | TextStyle | 否 | Default 字体，Medium，22sp / 28sp，字距 0sp | 大标题基础 |
| titleMedium | TextStyle | 否 | Default 字体，Medium，16sp / 24sp，字距 0sp | 标题基础 |
| titleSmall | TextStyle | 否 | Default 字体，Medium，14sp / 20sp，字距 0sp | 小标题基础 |
| labelLarge | TextStyle | 否 | Default 字体，Medium，14sp / 20sp，字距 0sp | 操作标签基础 |
| labelMedium | TextStyle | 否 | Default 字体，Medium，12sp / 16sp，字距 0sp | 中标签基础 |
| labelSmall | TextStyle | 否 | Default 字体，Medium，11sp / 16sp，字距 0sp | 小标签基础 |

各组件会在基础字体族上应用参考字号和行高。主题默认 HyperText 正文为 14sp、19.6sp 行高。

## HyperShapes 与 HyperThemeColors 配置 Props

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| extraSmall | Shape | 否 | RoundedCornerShape(7.dp) | 最小圆角 |
| small | Shape | 否 | RoundedCornerShape(16.dp) | 小圆角 |
| medium | Shape | 否 | RoundedCornerShape(24.dp) | 中圆角 |
| large | Shape | 否 | RoundedCornerShape(28.dp) | 大圆角 |
| themeColor | Color | 否 | HyperStyleDefaults.DefaultThemeColor | HyperThemeColors 品牌色 |
| successColor | Color | 否 | HyperStyleDefaults.SuccessColor | HyperThemeColors 成功色 |

## rgba API / Props

```kotlin
fun rgba(red: Int, green: Int, blue: Int, alpha: Float = 1f): Color
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| red | Int | 是 | — | 红分量，限制到 0..255 |
| green | Int | 是 | — | 绿分量，限制到 0..255 |
| blue | Int | 是 | — | 蓝分量，限制到 0..255 |
| alpha | Float | 否 | 1f | 透明度，限制到 0..1 |

也可用 Color(red, green, blue, alpha) 四个 Float 分量构造；禁止十六进制颜色构造。

## 状态归属与平台

调用方持有模式、品牌和材质。HyperTheme 的 material、glass、colors、typography、shapes、isDark 为当前 Composition 的只读属性；HyperColors 的语义色也仅能在 Composable 中读取，不能缓存为全局变量。公开产品支持 Android 手机 320–430dp 竖屏，Desktop/Wasm 仅用于预览。

具体默认尺寸见 [样式对照](style-reference.md)；组件签名见 [组件索引](component-index.md)。
