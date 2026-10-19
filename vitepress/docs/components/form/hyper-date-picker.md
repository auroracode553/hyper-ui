---
description: 与 Flutter HyperDatePicker 单日期模式一致的底部玻璃月历。
---

# HyperDatePicker

底部月历选择单个日期，支持翻月、月份选择、前后一年与十年跳转。日期使用 Android 原生 java.time.LocalDate。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="date_picker" title="HyperDatePicker 交互预览" />

Preview 的本机日期模拟为 2026-10-19，与 Android 共用月历及面板源码。Desktop/Wasm 仅用于文档预览，不开放为消费平台；公开入口的 LocalDate 转换只参与 Android 编译。

## 最小调用

```kotlin
import androidx.compose.runtime.*
import hyper_ui.*
import java.time.LocalDate

var visible by remember { mutableStateOf(false) }
var date by remember { mutableStateOf(LocalDate.now()) }

HyperButton(onPressed = { visible = true }) { HyperText("选择日期") }
HyperDatePicker.date(
    visible = visible,
    onClose = { visible = false },
    onChanged = { date = it },
    initialDate = date,
)
```

限制可选日期时，直接传 firstDate 与 lastDate，例如 lastDate = LocalDate.now().minusDays(1)。范围外日期、月份和翻页动作统一禁用，不需要应用自行写输入框或解析日期。

## 状态与使用约束

- visible 和最终选中日期由调用方管理。每次打开按 initialDate 初始化临时选择；越界初值限制到最近的范围边界。
- 点击日期只修改临时选择。确定时先通知 onClose，调用方必须令 visible = false；退出动画结束后通过 onChanged 提交一次日期。点击遮罩或系统返回只调用 onClose，不提交日期。
- 范围两端均包含，lastDate 必须不早于 firstDate；支持公历 1–9999 年。自动处理闰年和跨年翻月。
- 本入口提供单日期模式，不包含时间和日期区间模式。不新增日期库依赖，不读取业务数据或申请权限。

## 视觉约定

参考 Flutter 的 hyper_date_picker.dart 与 hyper_action_sheet.dart：底部 prominent 玻璃面板，30dp 圆角、16dp 内边距、32×4dp 把手、18sp 标题，最多占视口高度的 85%，内容可滚动并避让底部安全区。

周一开始的六行月历，星期标题 12sp，行高 42dp，选中圆形 36dp，日期文字 14sp。选中态品牌色填充与白字；可选的今日未选中时显示 1dp 品牌色圆环；禁用文字为 secondaryText 的 105/255 透明度。年月标题展开十二个月，月份按钮大号 44dp；确定按钮大号、整行、filled/primary。颜色、玻璃和按压反馈复用现有主题。

## 公开 API

```kotlin
object HyperDatePicker {
    @Composable
    fun date(
        visible: Boolean,
        onClose: () -> Unit,
        onChanged: (LocalDate) -> Unit,
        initialDate: LocalDate = LocalDate.now(),
        firstDate: LocalDate = LocalDate.of(1900, 1, 1),
        lastDate: LocalDate = LocalDate.of(2100, 12, 31),
    )
}
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| visible | Boolean | 是 | — | 调用方控制面板显示和退出。 |
| onClose | () -&gt; Unit | 是 | — | 点击遮罩、系统返回或确定时请求关闭；调用方设置 visible = false。 |
| onChanged | (LocalDate) -&gt; Unit | 是 | — | 确定并完成退出动画后提交选中日期；取消不触发。 |
| initialDate | LocalDate | 否 | LocalDate.now() | 每次打开时的初始选择，越界时限制到可选范围。 |
| firstDate | LocalDate | 否 | LocalDate.of(1900, 1, 1) | 包含的最早可选日期。 |
| lastDate | LocalDate | 否 | LocalDate.of(2100, 12, 31) | 包含的最晚可选日期，不能早于 firstDate。 |
