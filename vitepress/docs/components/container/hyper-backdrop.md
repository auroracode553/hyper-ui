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

`HyperGlassTokens` 提供玻璃表面、边缘、阴影和状态色；背景必须位于采样源中，避免面板采样自身。

## 最小用法

```kotlin
HyperThemeConfig {
    HyperBackdrop() {
        HyperCard { HyperText("玻璃卡片") }
    }
}
```

## 约束

- 支持 Android 手机端；业务状态由调用方管理，组件只负责 UI 与回调。
