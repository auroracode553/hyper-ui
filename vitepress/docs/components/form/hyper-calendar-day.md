---
description: 共用日期选择器外观的通用日期格
---

# HyperCalendarDay

包名 hyper_ui。Android 手机竖屏 320–430dp；Desktop/Wasm 仅为文档预览。

<WasmPreview demo="calendar_day" title="日期格与附加信息" />

默认日期行高 42dp、选中圆形直径 36dp，沿用 HyperDatePicker 的实色选中与今日描边。仅提供一套日期格规格，辅助文字和附属条目按内容计算所需高度。组件不计算历法、节日或业务日程。

inRange 使用独立的范围底色：亮色 RGBA(239,246,255,1)，深色 RGBA(20,37,68,1)，不随强调色叠加透明度。月历普通和只读绘制共用同一底色。

附属条目保持 10sp 文字、1.2 行高，垂直内边距合计 2dp、行间距 2dp；默认文字缩放下三行条目所需高度由 minimumHeight 统一计算。宿主使用 minimumHeight 计算，不自行复制尺寸。

## 公开 API

```kotlin
@Composable
fun HyperCalendarDay(
    day: Int,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    today: Boolean = false,
    enabled: Boolean = true,
    adjacent: Boolean = false,
    emphasized: Boolean = false,
    inRange: Boolean = false,
    expanded: Boolean = false,
    subtitle: String? = null,
    subtitleColor: Color = Color.Unspecified,
    badge: String? = null,
    badgeColor: Color = Color.Unspecified,
    items: List<HyperCalendarItem> = emptyList(),
    itemCount: Int? = null,
    onPressed: (() -> Unit)? = null,
    onLongPressed: (() -> Unit)? = null,
    onItemsPressed: (() -> Unit)? = null,
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| day | Int | 是 | 无 | 1–31 的日期数字 |
| modifier | Modifier | 否 | Modifier | 宿主布局 |
| selected | Boolean | 否 | false | 受控实色选中态 |
| today | Boolean | 否 | false | 今日描边 |
| enabled | Boolean | 否 | true | 是否允许交互 |
| adjacent | Boolean | 否 | false | 跨月弱化 |
| emphasized | Boolean | 否 | false | 主题色数字 |
| inRange | Boolean | 否 | false | 区间中间背景 |
| expanded | Boolean | 否 | false | 填充宿主有限高度 |
| subtitle | String? | 否 | null | 日期辅助文字 |
| subtitleColor | Color | 否 | Color.Unspecified | 默认主题次要文字色 |
| badge | String? | 否 | null | 短角标文字 |
| badgeColor | Color | 否 | Color.Unspecified | 默认主题色 |
| items | List&lt;HyperCalendarItem&gt; | 否 | emptyList() | 按剩余高度最多显示三项 |
| itemCount | Int? | 否 | null | 默认 items.size，不能少于已提供条目数 |
| onPressed | (() -&gt; Unit)? | 否 | null | 日期点击 |
| onLongPressed | (() -&gt; Unit)? | 否 | null | 日期长按 |
| onItemsPressed | (() -&gt; Unit)? | 否 | null | 条目点击，回退到 onPressed |

条目最多占三行：三项以内逐条显示，超过容量时最后一行单独显示剩余计数，不挤占标题宽度。完全放不下时显示圆点。只展示时不传回调，enabled 控制弱化外观。expanded 要求有限高度。

### HyperCalendarItem

```kotlin
data class HyperCalendarItem(val label: String, val color: Color = Color.Unspecified)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| label | String | 是 | 无 | 条目文字 |
| color | Color | 否 | Color.Unspecified | 默认主题色 |

### 最小行高

```kotlin
@Composable
fun HyperCalendarDayDefaults.minimumHeight(hasSubtitle: Boolean = false, itemRows: Int = 0): Dp
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| hasSubtitle | Boolean | 否 | false | 是否为辅助文字预留高度 |
| itemRows | Int | 否 | 0 | 为 0–3 行条目预留高度，包含溢出数量行 |

```kotlin
HyperCalendarDay(21, modifier = Modifier.size(80.dp, 120.dp), expanded = true, subtitle = "辅助说明",
    items = listOf(HyperCalendarItem("预约确认")), itemCount = 3,
    onPressed = selectDate, onItemsPressed = openItems)
```
