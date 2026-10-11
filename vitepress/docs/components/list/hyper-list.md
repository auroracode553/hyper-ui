---
description: 页面级懒列表，适合较多数据，项目结构通过 LazyListScope 组合。
---

# HyperList

页面级懒列表，适合较多数据，项目结构通过 LazyListScope 组合。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="hyper_list" title="HyperList 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import hyper_ui.*

HyperList(modifier = Modifier.fillMaxSize()) {
    items(accounts, key = { it.id }) { account ->
        HyperListTile(
            headlineContent = { HyperText(account.name) },
            onClick = { onOpen(account) }
        )
    }
}
```

</details>

## 使用约定

**滚动布局**

列表需要有限高度。页面正文使用 Modifier.fillMaxSize()；控件与列表纵向组合时，在有限高度的 Column 内给列表 Modifier.weight(1f)。外层不要再嵌套 verticalScroll。默认 18dp 圆角、内容 4dp 留白，子项自行管理 14dp 横向内边距。

**约束**

业务状态由调用方管理，组件只负责 UI 与回调。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperList

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperList(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = HyperListDefaults.ContentPadding,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    shape: Shape = HyperListDefaults.Shape,
    border: BorderStroke? = null,
    colors: HyperListColors = HyperListDefaults.colors(),
    content: LazyListScope.() -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| contentModifier | Modifier | 否 | Modifier | 内部内容区域的布局修饰符。 |
| state | LazyListState | 否 | rememberLazyListState() | 组件使用的状态对象，由调用方提供或记忆。 |
| contentPadding | PaddingValues | 否 | HyperListDefaults.ContentPadding | 主体内容的内部留白。 |
| verticalArrangement | Arrangement.Vertical | 否 | Arrangement.spacedBy(0.dp) | 子项的垂直排列方式。 |
| shape | Shape | 否 | HyperListDefaults.Shape | 组件容器的形状。 |
| border | BorderStroke? | 否 | null | 容器边框配置。 |
| colors | HyperListColors | 否 | HyperListDefaults.colors() | 组件各状态的颜色配置。 |
| content | LazyListScope.() -&gt; Unit | 是 | — | 组件主体内容，由调用方提供。 |
