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

## Props（参数）

### HyperBackdrop

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| state | HyperBackdropState | 否 | rememberHyperBackdropState() | 组件使用的状态对象，由调用方提供或记忆。 |
| background | @Composable BoxScope.() -&gt; Unit | 否 | { HyperSoftBackground(Modifier.matchParentSize()) } | 供玻璃表面采样的背景内容。 |
| content | @Composable BoxScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |

### hyperBackdropSource

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| state | HyperBackdropState | 是 | — | 组件使用的状态对象，由调用方提供或记忆。 |

### HyperSoftBackground

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| intensity | Float | 否 | 1f | 柔和背景效果的强度。 |
| content | @Composable BoxScope.() -&gt; Unit | 否 | {} | 组件主体内容，由调用方提供。 |

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
