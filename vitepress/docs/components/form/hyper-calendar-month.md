---
description: 无卡片的受控内嵌月历
---

# HyperCalendarMonth

包名 hyper_ui。Android 手机竖屏 320–430dp；Desktop/Wasm 仅为文档预览，复用内部网格。

<WasmPreview demo="calendar_month" title="内嵌月历" />

月份与选中日期由宿主持有，组件不带卡片和导航。默认六行，沿用日期选择器 42dp 行高、36dp 选中圆形和今日描边。只读模式合并绘制整月，不创建逐日期点击和动画节点。

## 公开 API

```kotlin
@Composable
fun HyperCalendarMonth(
    month: LocalDate,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    endDate: LocalDate? = null,
    firstDate: LocalDate? = null,
    lastDate: LocalDate? = null,
    weekStart: Int = 1,
    size: String = "default",
    showWeekdays: Boolean = true,
    readOnly: Boolean = false,
    expanded: Boolean = false,
    emphasizedDays: Set<Int> = emptySet(),
    onChanged: ((LocalDate) -> Unit)? = null,
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| month | LocalDate | 是 | 无 | 仅使用年月 |
| modifier | Modifier | 否 | Modifier | 宿主布局 |
| selectedDate | LocalDate? | 否 | null | 受控选中日期 |
| endDate | LocalDate? | 否 | null | 区间结束，不早于 selectedDate |
| firstDate | LocalDate? | 否 | null | 最早可选日期 |
| lastDate | LocalDate? | 否 | null | 最晚可选日期 |
| weekStart | Int | 否 | 1 | 周一为 1、周日为 7 |
| size | String | 否 | default | small、default、large |
| showWeekdays | Boolean | 否 | true | 星期表头 |
| readOnly | Boolean | 否 | false | 只读合并绘制，不触发点击 |
| expanded | Boolean | 否 | false | 填充宿主有限高度并平分六行 |
| emphasizedDays | Set&lt;Int&gt; | 否 | emptySet() | 当前月份中使用主题色的日期数字，含义由宿主决定 |
| onChanged | ((LocalDate) -&gt; Unit)? | 否 | null | 日期点击 |

原生日期使用 java.time.LocalDate，年份范围 1–9999。只读整月点击、月份分页、月盘收起和业务数据由宿主负责。

日期区间内部的普通日期格与 readOnly 合并绘制使用同一范围底色：亮色 RGBA(239,246,255,1)、深色 RGBA(20,37,68,1)。

```kotlin
HyperCalendarMonth(month = month, selectedDate = selected,
    weekStart = 7, onChanged = { selected = it })
HyperCalendarMonth(month = month, modifier = Modifier.height(120.dp),
    size = "small", readOnly = true, expanded = true, showWeekdays = false)
```
