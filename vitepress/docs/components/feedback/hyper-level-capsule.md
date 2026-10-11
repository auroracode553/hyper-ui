---
description: 以竖向胶囊形态展示比例，支持状态颜色与数值变化。
---

# HyperLevelCapsule

以竖向胶囊形态展示比例，支持状态颜色与数值变化。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="level_capsule" title="HyperLevelCapsule 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperLevelCapsule(
    progress = brightness,
    label = "${(brightness * 100).toInt()}%"
)
```

</details>

## 使用约定

**约束**

业务状态由调用方管理，组件只负责 UI 与回调。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperLevelCapsule

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperLevelCapsule(
    progress: Float,
    label: String,
    modifier: Modifier = Modifier,
    shape: Shape = HyperLevelCapsuleDefaults.Shape,
    colors: HyperLevelCapsuleColors = HyperLevelCapsuleDefaults.colors(),
    iconContent: (@Composable () -> Unit)? = null
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| progress | Float | 是 | — | 由调用方提供的进度值。 |
| label | String | 是 | — | 数值旁显示的标签文字。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| shape | Shape | 否 | HyperLevelCapsuleDefaults.Shape | 组件容器的形状。 |
| colors | HyperLevelCapsuleColors | 否 | HyperLevelCapsuleDefaults.colors() | 组件各状态的颜色配置。 |
| iconContent | (@Composable () -&gt; Unit)? | 否 | null | 图标区域的自定义内容。 |
