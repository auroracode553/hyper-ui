---
description: 悬浮底部入口，提供单动作与多项导航两种形态。
---

# HyperTabBar

悬浮底部入口，提供单动作与多项导航两种形态。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="tab-bar-multiple" title="HyperTabBar 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperTabBar(items, onItemClick = { selected = it }, itemSelected = { it == selected }, itemLabel = { it.label }) { item ->
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(1.dp)) {
        HyperIcon(item.icon, null)
        HyperText(item.label)
    }
}
```

<WasmPreview demo="tab-bar-single" title="单动作与角标" />

</details>

## 使用约定

**样式约定**

type 为 multiple 或 single。multiple 高 56dp，单元格为 max(76dp, 最宽 12sp 粗体标签 +32dp)，容器另加 8dp；外间距 start/end 20dp、top 8dp。指示器 160ms easeOutCubic。图标 23dp、文字 12sp、间距 1dp。single 为 56dp 方形 regular 玻璃、22dp 图标、16dp 角标。

**状态归属**

multiple 至少 2 项，single 恰好 1 项。itemLabel 必须与实际显示文字一致。320..430dp 内父约束优先；选中值由调用方管理。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperTabBar

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun <T> HyperTabBar(
    items: List<T>,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    type: String = "multiple",
    enabled: Boolean = true,
    itemSelected: (T) -> Boolean = { false },
    itemEnabled: (T) -> Boolean = { true },
    itemLabel: (T) -> String = { "" },
    badgeCount: Int = 0,
    safeArea: Boolean = true,
    margin: PaddingValues = if (type == "single") PaddingValues(0.dp) else PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp),
    color: Color = HyperColors.accent,
    itemContent: @Composable HyperTabBarItemScope.(item: T) -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| items | List&lt;T&gt; | 是 | — | 待呈现条目。 |
| onItemClick | (T) -&gt; Unit | 是 | — | 标签栏单项点击时的回调。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| type | String | 否 | "multiple" | 渲染形态；合法值见本页样式约定。 |
| enabled | Boolean | 否 | true | 是否允许用户操作。 |
| itemSelected | (T) -&gt; Boolean | 否 | { false } | 按项提供选中状态。 |
| itemEnabled | (T) -&gt; Boolean | 否 | { true } | 按项提供可用状态。 |
| itemLabel | (T) -&gt; String | 否 | { "" } | 用于测量导航单元格的标签；应与实际显示文案一致。 |
| badgeCount | Int | 否 | 0 | 单动作角标数量，0 隐藏，超过 99 显示 99+。 |
| safeArea | Boolean | 否 | true | 避让底部系统安全区。 |
| margin | PaddingValues | 否 | if (type == "single") PaddingValues(0.dp) else PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp) | 导航容器外部间距。 |
| color | Color | 否 | HyperColors.accent | 颜色覆盖。 |
| itemContent | @Composable HyperTabBarItemScope.(item: T) -&gt; Unit | 是 | — | 条目内容插槽。 |
