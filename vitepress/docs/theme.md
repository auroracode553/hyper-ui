# 主题与颜色

## `rgba`

使用整数 RGB 与 Float alpha 分量创建 Compose `Color`：

```kotlin
fun rgba(
    red: Int,
    green: Int,
    blue: Int,
    alpha: Float = 1f
): Color
```

- `red`、`green`、`blue` 会限制在 `0..255`。
- `alpha` 会限制在 `0f..1f`。

```kotlin
val brandColor = rgba(255, 103, 0)
```

组件根据角色选择实色或玻璃表面。紧凑控件和媒体反馈使用半透明基底；大面积导航 `HyperDrawer` 与浮层菜单 `HyperDropdown` 使用 `HyperColors.cardContainer` 的不透明基底，并继续保留宽柔光、底部弱阴影和至多一层投影。二者收到带 alpha 的自定义容器色时，会先与页面背景合成为不透明颜色。`HyperTabBar` 采用页面背景并以 0.5dp 顶部发丝线分层。

组件源码禁止使用 `Color(0xFFRRGGBB)` 十六进制硬编码。需要直接构造颜色时，使用四个 Float RGBA 分量；调用方也可优先使用 `rgba(...)`。

## `HyperThemeConfig`

```kotlin
@Composable
fun HyperThemeConfig(
    themeColor: Color = HyperStyleDefaults.DefaultThemeColor,
    successColor: Color = HyperStyleDefaults.SuccessColor,
    content: @Composable () -> Unit
)
```

`HyperThemeConfig` 只提供 HyperUI 的品牌色，不替代应用的 `MaterialTheme`。

## `HyperThemeColors`

```kotlin
data class HyperThemeColors(
    val themeColor: Color = HyperStyleDefaults.DefaultThemeColor,
    val successColor: Color = HyperStyleDefaults.SuccessColor
)

object HyperTheme {
    val colors: HyperThemeColors
        @Composable @ReadOnlyComposable get()
}
```

在 Composable 中可通过 `HyperTheme.colors` 读取当前配置。通常调用方只需传入 `HyperThemeConfig`，不需要直接读取或修改内部对象。

## 公开默认值

```kotlin
object HyperStyleDefaults {
    val DefaultThemeColor = rgba(255, 103, 0, 1f)
    val SuccessColor = rgba(52, 199, 89, 1f)
    val InfoColor = rgba(144, 147, 153, 1f)
    val WarningColor = rgba(230, 162, 60, 1f)
    val DangerColor = rgba(255, 59, 48, 1f)
    const val DisabledAlpha = 0.38f
    val SmallCornerRadius = 12.dp
    val MediumCornerRadius = 16.dp
    val LargeCornerRadius = 24.dp
    val ExtraLargeCornerRadius = 28.dp

    val CardElevation = 4.dp
}
```

`HyperColors` 根据当前 `MaterialTheme` 明暗模式提供以下只读值：

| 属性 | 类型 | 用途 |
| --- | --- | --- |
| `accent` | `Color` | 当前主题强调色 |
| `success` | `Color` | 当前成功色 |
| `info` | `Color` | 信息语义色 |
| `warning` | `Color` | 警告语义色 |
| `danger` | `Color` | 危险语义色 |
| `isLight` | `Boolean` | 当前背景是否为浅色 |
| `pageBackground` | `Color` | 页面背景 |
| `cardContainer` | `Color` | 卡片/面板背景 |
| `softContainer` | `Color` | 柔和轨道或容器背景 |
| `fieldContainer` | `Color` | 输入字段背景 |
| `elevatedContainer` | `Color` | 浮起控件背景 |
| `disabledContainer` | `Color` | 禁用容器背景 |
| `accentContainer` | `Color` | 强调色与柔和容器混合后的不透明背景 |
| `primaryText` | `Color` | 主文字 |
| `secondaryText` | `Color` | 次文字 |
| `disabledText` | `Color` | 禁用文字 |
| `divider` | `Color` | 分割线 |
| `fieldBorder` | `Color` | 输入框描边 |
| `panelBorder` | `BorderStroke` | 面板描边 |

不要把 `HyperColors` 的结果缓存到全局变量；这些值依赖当前 Composition，应在 Composable 上下文中读取。
