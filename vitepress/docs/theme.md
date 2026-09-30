# 主题配置

HyperUI 的颜色、排版、形状和玻璃材质由 `HyperThemeConfig` 统一提供。组件只读取主题令牌，不在组件内部保存全局主题状态。

<WasmPreview demo="theme-material" title="主题材质交互预览" />

## 配置主题

```kotlin
@Composable
fun AppRoot() {
    HyperThemeConfig(
        themeColor = rgba(71, 111, 232),
        successColor = rgba(34, 197, 94),
        darkTheme = isSystemInDarkTheme()
    ) {
        AppContent()
    }
}
```

`darkTheme` 默认跟随系统。需要完全自定义排版、形状或玻璃令牌时，传入 `typography`、`shapes` 或 `glass`；普通页面不需要覆盖这些内部令牌。

## Preview 材质

Preview 的主题设置提供三种材质对照：`实色`、`柔和`、`清透`。它们只改变 Preview 中的玻璃表面透明度，用来检查组件在不同材质下的层级；应用代码仍通过 `HyperThemeConfig(glass = ...)` 注入令牌。

## 颜色构造

```kotlin
fun rgba(
    red: Int,
    green: Int,
    blue: Int,
    alpha: Float = 1f
): Color
```

颜色分量会被限制在 `0..255`，透明度会被限制在 `0f..1f`。组件源码和调用示例禁止使用 `Color(0xFFRRGGBB)`。

## 主题语义色

```kotlin
HyperColors.accent
HyperColors.success
HyperColors.info
HyperColors.warning
HyperColors.danger
HyperColors.primaryText
HyperColors.secondaryText
HyperColors.disabledText
```

这些值依赖当前 Composition，只能在 Composable 上下文读取。不要把 `HyperColors` 的结果缓存到全局变量。

## 组件颜色覆盖

优先使用组件的 `colors` 工厂覆盖语义色：

```kotlin
HyperButton(
    type = "tonal",
    colors = HyperButtonDefaults.colors(
        contentColor = HyperColors.accent
    ),
    onClick = onOpen
) {
    HyperText("打开")
}
```

不要在调用方复制玻璃背景、描边和阴影。组件内部会根据 `type`、`enabled`、焦点和错误状态选择正确层级。

## 材质层级

| 层级 | 典型组件 | 规则 |
| --- | --- | --- |
| 内容层 | `HyperList`、`HyperListItem` | 低抬升，避免每一行重复铺设卡片 |
| 浮动层 | `HyperIconButton`、`HyperTabBar`、`HyperDropdown` | 只使用一层上下文阴影 |
| 模态层 | `HyperDialog`、`HyperDrawer` | 通过遮罩与页面内容分离 |

透明表面需要稳定背景。页面有动态背景时，再使用 `HyperBackdrop` 提供采样环境；普通页面不需要为每个组件单独创建背景层。
