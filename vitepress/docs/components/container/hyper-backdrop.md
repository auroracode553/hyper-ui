# HyperBackdrop 与玻璃令牌

包名：`hyper_ui`。背景与前景由调用方提供；背景采样状态由 `rememberHyperBackdropState()` 创建，可传给 `hyperBackdropSource`。材质颜色通过 `HyperThemeConfig(glass = ...)` 注入。

<WasmPreview demo="glass_material" title="玻璃材质交互预览" />

## 公开签名与默认值

```kotlin
@Composable
fun rememberHyperBackdropState(): HyperBackdropState

@Composable
fun HyperBackdrop(
    modifier: Modifier = Modifier,
    state: HyperBackdropState = rememberHyperBackdropState(),
    background: @Composable BoxScope.() -> Unit = { HyperSoftBackground(Modifier.matchParentSize()) },
    content: @Composable BoxScope.() -> Unit
)

fun Modifier.hyperBackdropSource(state: HyperBackdropState): Modifier

@Composable
fun HyperSoftBackground(
    modifier: Modifier = Modifier,
    intensity: Float = 1f,
    content: @Composable BoxScope.() -> Unit = {}
)
```

`HyperGlassTokens` 包含 `surface`、`surfaceStrong`、`surfaceSubtle`、`edgeHighlight`、`edgeShade`、`shadow`、`controlTrack`、`selection`、`pressed`、`scrim`；`light()` 和 `dark()` 与 Flutter 参考令牌一致。默认背景模糊半径为 20dp，模态面板为 28dp。背景必须位于采样源中，避免面板对自身反复采样。

## 最小用法

```kotlin
HyperThemeConfig {
    HyperBackdrop() {
        HyperPanel { HyperText("玻璃卡片") }
    }
}
```

调用方持有动态背景和主题配置；组件只处理绘制与布局。高对比度或减弱动态效果需要调用方选择相应玻璃令牌和背景。
