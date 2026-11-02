---
description: 背景与内容的覆盖组合，固定玻璃组件共享独立背景源。
---

# HyperBackdrop

包名 hyper_ui。Android 手机竖屏 320–430dp；Desktop/Wasm 仅用于文档 Preview。

<WasmPreview demo="surface" title="背景与玻璃组合" />

## 使用方式

```kotlin
HyperBackdrop(background = { HyperBackground(Modifier.matchParentSize()) }) {
    HyperCard { HyperText("实色内容") }
}
```

背景与前景分别组合；只录制 background 子树，并将其提供给内部玻璃组件，content 不进入同一取样源，避免递归。组件不保存业务状态，不修改正文视口、Insets 或命中范围。HyperBackground 仅绘制静态环境色渐变，不提供全局材质档位。

## 公开 API

```kotlin
@Composable
fun HyperBackdrop(
    modifier: Modifier = Modifier,
    background: @Composable BoxScope.() -> Unit = { HyperBackground(Modifier.matchParentSize()) },
    content: @Composable BoxScope.() -> Unit
)

@Composable
fun HyperBackground(
    modifier: Modifier = Modifier,
    intensity: Float = 1f,
    content: @Composable BoxScope.() -> Unit = {}
)
```

### HyperBackdrop API / Props

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置和间距 |
| background | @Composable BoxScope.() -> Unit | 否 | { HyperBackground(Modifier.matchParentSize()) } | 底层背景内容 |
| content | @Composable BoxScope.() -> Unit | 是 | — | 覆盖在背景之上的内容 |

### HyperBackground API / Props

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置和间距 |
| intensity | Float | 否 | 1f | 静态渐变强度，范围 0..1；环境色最大 alpha 为 28/255，并合成到不透明画布色 |
| content | @Composable BoxScope.() -> Unit | 否 | {} | 主体内容 |
