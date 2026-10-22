---
description: 在同层内容之间切换，支持等宽排列与横向滚动。
---

# HyperTabs

在同层内容之间切换，支持等宽排列与横向滚动。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="tabs" title="HyperTabs 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperTabs(tabs, selected, { selected = it }) { HyperText(it) }
```

</details>

## 使用约定

**样式约定**

共享 subtle 胶囊 radius 27dp，高 54dp。项目 48dp，指示底面 44dp。普通项继承 14sp/500，选中 600；scrollable 模式每项最小 76dp。

滚动模式底色切换使用 160ms easeOutCubic。文字继承当前插槽排版，选中 600、普通 500。

**状态归属**

同一共享表面承载全部条目。选中值由调用方持有；重复点击当前选中项不触发 onChanged。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperTabs

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun <T> HyperTabs(
    tabs: List<T>,
    selectedIndex: Int,
    onChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperTabsItemScope.(item: T) -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| tabs | List&lt;T&gt; | 是 | — | 页签数据。 |
| selectedIndex | Int | 是 | — | 当前选中项的索引，从 0 开始。 |
| onChanged | (Int) -&gt; Unit | 是 | — | 用户修改值或选择项时通知调用方。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| scrollable | Boolean | 否 | false | 是否允许内容滚动。 |
| itemEnabled | (T) -&gt; Boolean | 否 | { true } | 判断单个选项是否可操作。 |
| itemContent | @Composable HyperTabsItemScope.(item: T) -&gt; Unit | 是 | — | 每个数据项的自定义内容。 |
