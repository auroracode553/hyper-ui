---
description: 为悬浮表面提供共享的背景采样，配合 Solid、Soft、Clear 三种材质。
---

# HyperBackdrop 与玻璃令牌

为悬浮表面提供共享的背景采样，配合 Solid、Soft、Clear 三种材质。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="glass_material" title="玻璃材质交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
HyperThemeConfig {
    HyperBackdrop() {
        HyperCard { HyperText("玻璃卡片") }
    }
}
```

</details>

## 使用约定

**约束**

业务状态由调用方管理，组件只负责 UI 与回调。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperBackdrop

<details class="component-doc__details">
<summary>查看公开签名</summary>

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

</details>

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
