---
description: 在少量互斥选项之间切换，支持等宽、图标和禁用项。
---

# HyperSegmentedControl

在少量互斥选项之间切换，支持等宽、图标和禁用项。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="segmented" title="HyperSegmentedControl 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperSegmentedControl(options, selected, { selected = it }) { HyperText(it) }
```

</details>

## 使用约定

**样式约定**

共享 subtle 表面 radius 18dp、内部 padding 4dp，总高 40dp。条目高 32dp、radius 16dp、padding 12dp；文字 13sp，选中 600、普通 500；选中底面使用 selection。

等宽模式的选中底板按 260ms selectionCurve 连续平移；内容宽度模式按 160ms 过渡底色。

**状态归属**

调用方持有 selectedValue；equalWidth=false 按内容收缩。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperSegmentedControl

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun <T> HyperSegmentedControl(
    options: List<T>,
    selectedValue: T,
    onChanged: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemEnabled: (T) -> Boolean = { true },
    equalWidth: Boolean = true,
    shape: Shape = HyperSegmentedControlDefaults.Shape,
    itemShape: Shape = HyperSegmentedControlDefaults.ItemShape,
    colors: HyperSegmentedControlColors = HyperSegmentedControlDefaults.colors(),
    containerPadding: PaddingValues = HyperSegmentedControlDefaults.ContainerPadding,
    itemContentPadding: PaddingValues = HyperSegmentedControlDefaults.ItemContentPadding,
    itemContent: @Composable HyperSegmentOptionScope.(item: T) -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| options | List&lt;T&gt; | 是 | — | 可选颜色配置。 |
| selectedValue | T | 是 | — | 当前选中项的值。 |
| onChanged | (T) -&gt; Unit | 是 | — | 用户修改值或选择项时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| enabled | Boolean | 否 | true | 是否允许用户交互。 |
| itemEnabled | (T) -&gt; Boolean | 否 | { true } | 判断单个选项是否可操作。 |
| equalWidth | Boolean | 否 | true | 各分段是否等宽。 |
| shape | Shape | 否 | HyperSegmentedControlDefaults.Shape | 组件容器的形状。 |
| itemShape | Shape | 否 | HyperSegmentedControlDefaults.ItemShape | 单个选项的形状。 |
| colors | HyperSegmentedControlColors | 否 | HyperSegmentedControlDefaults.colors() | 组件各状态的颜色配置。 |
| containerPadding | PaddingValues | 否 | HyperSegmentedControlDefaults.ContainerPadding | 容器内部留白。 |
| itemContentPadding | PaddingValues | 否 | HyperSegmentedControlDefaults.ItemContentPadding | 单个选项的内部留白。 |
| itemContent | @Composable HyperSegmentOptionScope.(item: T) -&gt; Unit | 是 | — | 每个数据项的自定义内容。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperSegmentedControlDefaults.colors**

```kotlin
@Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        selectedItemColor: Color = Color.Unspecified,
        unselectedItemColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperSegmentedControlColors
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 否 | Color.Unspecified | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| selectedItemColor | Color | 否 | Color.Unspecified | 选中底板颜色。 |
| unselectedItemColor | Color | 否 | Color.Unspecified | 未选中条目的底色。 |
| selectedContentColor | Color | 否 | Color.Unspecified | 选中条目的文字与图标颜色。 |
| unselectedContentColor | Color | 否 | Color.Unspecified | 未选中条目的文字与图标颜色。 |
| disabledContentColor | Color | 否 | Color.Unspecified | 禁用时的文字与图标颜色。 |

**HyperSegmentedControlColors 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| containerColor | Color | 是 | — | 表面背景色；Color.Unspecified 使用组件的主题默认值。 |
| selectedItemColor | Color | 是 | — | 选中底板颜色。 |
| unselectedItemColor | Color | 是 | — | 未选中条目的底色。 |
| selectedContentColor | Color | 是 | — | 选中条目的文字与图标颜色。 |
| unselectedContentColor | Color | 是 | — | 未选中条目的文字与图标颜色。 |
| disabledContentColor | Color | 是 | — | 禁用时的文字与图标颜色。 |

</details>
