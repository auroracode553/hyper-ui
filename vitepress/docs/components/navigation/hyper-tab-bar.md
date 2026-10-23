---
description: 提供悬浮、非悬浮贴底导航与可重复触发的动作入口。
---

# HyperTabBar

底部入口，提供单动作与多项导航；多项支持悬浮和非悬浮贴底布局。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="tab-bar-multiple" title="HyperTabBar 交互预览" />

<WasmPreview demo="tab-bar-docked" title="非悬浮动作栏：重复点击与禁用项" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperTabBar(items, selectedIndex = selectedIndex, onChanged = { selectedIndex = it }, itemLabel = { it.label }) { item ->
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

type 为 multiple 或 single。floating 默认 true，保留内容收缩的悬浮胶囊：multiple 高 56dp，单元格为 max(76dp, 最宽 12sp 粗体标签 +32dp)，容器另加 8dp；外间距 start/end 20dp、top 8dp。floating=false 仅用于 multiple，默认零外间距，全宽平直页面表面，无胶囊、圆角或浮层阴影，底部安全区位于表面内部。图标 23dp、文字 12sp、间距 1dp。single 保留 56dp 圆形玻璃、22dp 图标、16dp 角标。

非悬浮形态直接放入 HyperScaffold.bottomBar，默认总高度 60dp，底部安全区计入其中；操作区至少 40dp，大字体或较高的三键导航区才按需增加总高度。不要再额外叠加系统底部 inset、固定留白或业务 height。悬浮内容区仍至少 56dp，宿主仍让正文经过底栏后方。

**状态归属**

multiple 至少 2 项，single 恰好 1 项。itemLabel 必须与实际显示文字一致。320..430dp 内父约束优先；选中值由调用方管理。

itemEnabled 返回 false 的项目降低透明度并停止点击及拖动提交。single 点击时调用 onChanged(0)；multiple 在 selectedIndex 非空时仍采用选择模式，重复点击当前项不回调，拖动松手才提交。

selectedIndex=null 表示动作模式：没有常驻选中块，每次有效点击都会调用 onChanged，且横向拖动不触发动作。浏览器的前进、后退、标签管理和菜单应采用此模式，不能伪造固定选中项。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperTabBar

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun <T> HyperTabBar(
    items: List<T>,
    onChanged: (Int) -> Unit,
    selectedIndex: Int?,
    modifier: Modifier = Modifier,
    type: String = "multiple",
    itemLabel: (T) -> String = { "" },
    badgeCount: Int = 0,
    safeArea: Boolean = true,
    floating: Boolean = true,
    margin: PaddingValues = if (!floating || type == "single") PaddingValues(0.dp) else PaddingValues(start = 20.dp, top = 8.dp, end = 20.dp),
    color: Color = HyperColors.accent,
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperTabBarItemScope.(item: T) -> Unit
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| items | List&lt;T&gt; | 是 | — | 由调用方提供的选项或列表数据。 |
| onChanged | (Int) -&gt; Unit | 是 | — | 用户修改值或选择项时通知调用方。 |
| selectedIndex | Int? | 是 | — | 当前选中索引；null 为可重复点击的动作模式。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |
| type | String | 否 | &quot;multiple&quot; | 组件的视觉或布局形态。 |
| itemLabel | (T) -&gt; String | 否 | { &quot;&quot; } | 每个导航项的文字。 |
| badgeCount | Int | 否 | 0 | 单动作角标数量，0 隐藏，超过 99 显示 99+。 |
| safeArea | Boolean | 否 | true | 是否避开底部系统安全区。 |
| floating | Boolean | 否 | true | true 为悬浮胶囊；false 为全宽非悬浮 multiple 底栏。 |
| margin | PaddingValues | 否 | 非悬浮与 single 为零；悬浮 multiple 为 start/end 20dp、top 8dp | 导航容器外部间距。 |
| color | Color | 否 | HyperColors.accent | 自定义颜色，覆盖默认配色。 |
| itemEnabled | (T) -&gt; Boolean | 否 | { true } | 每项是否可交互，禁用项透明度为 0.4。 |
| itemContent | @Composable HyperTabBarItemScope.(item: T) -&gt; Unit | 是 | — | 每个数据项的自定义内容。 |

### HyperTabBarItemScope

itemContent 的接收者，仅由组件创建。

```kotlin
class HyperTabBarItemScope internal constructor(val selected: Boolean)
```

| 属性 | 类型 | 作用 |
| --- | --- | --- |
| selected | Boolean | 当前项是否选中；single 始终为 true。 |
