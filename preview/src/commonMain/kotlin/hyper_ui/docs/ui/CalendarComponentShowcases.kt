/** 文件职责：通用日期格与内嵌月历的交互示例，两端展示同一组状态。 */
package hyper_ui.docs.ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.ChevronRight

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hyper_ui.*

@Composable
fun CalendarDayDemo() {
    var selected by remember { mutableStateOf(9) }
    var feedback by remember { mutableStateOf("点击选择日期，长按查看反馈") }
    Column {
        Row {
            listOf(7, 8, 9, 10).forEach { day ->
                HyperCalendarDay(day, modifier = Modifier.weight(1f), today = day == 8,
                    selected = selected == day, enabled = day != 10, inRange = day == 7,
                    onPressed = { selected = day })
            }
        }
        Spacer(Modifier.height(16.dp))
        HyperCalendarDay(11, modifier = Modifier.width(100.dp).height(
            HyperCalendarDayDefaults.minimumHeight(hasSubtitle = true, itemRows = 3)), expanded = true,
            subtitle = "辅助说明", badge = "待", badgeColor = HyperColors.warning,
            items = listOf(HyperCalendarItem("预约确认"), HyperCalendarItem("资料提交")), itemCount = 4,
            onPressed = { feedback = "选择 11 日" }, onLongPressed = { feedback = "长按 11 日" },
            onItemsPressed = { feedback = "查看全部 4 个条目" })
        Spacer(Modifier.height(16.dp))
        HyperText(feedback)
    }
}

@Composable
fun CalendarMonthDemo() {
    var month by remember { mutableStateOf(HyperCalendarDate(2026, 10)) }
    var selected by remember { mutableStateOf(HyperCalendarDate(2026, 10, 21)) }
    var sunday by remember { mutableStateOf(false) }
    var readOnly by remember { mutableStateOf(false) }
    var emphasized by remember { mutableStateOf(true) }
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            HyperButton(type = "icon", onPressed = { month.shiftMonth(-1)?.let { month = it } }) {
                HyperIcon(Lucide.ChevronLeft, null)
            }
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) { HyperText("${month.year} 年 ${month.month} 月") }
            HyperButton(type = "icon", onPressed = { month.shiftMonth(1)?.let { month = it } }) {
                HyperIcon(Lucide.ChevronRight, null)
            }
        }
        Spacer(Modifier.height(12.dp))
        HyperCalendarMonthContent(month = month, selectedDate = selected,
            firstDate = HyperCalendarDate(1900, 1), lastDate = HyperCalendarDate(2100, 12, 31),
            today = HyperCalendarDate(2026, 10, 21), weekStart = if (sunday) 7 else 1,
            readOnly = readOnly,
            emphasizedDays = if (emphasized) setOf(1, 2, 3, 4, 5, 6, 7) else emptySet(),
            onChanged = { selected = it })
        Spacer(Modifier.height(16.dp))
        HyperText("已选：${selected.year}-${selected.month}-${selected.day}")
        HyperSwitch(value = sunday, onChanged = { sunday = it }, label = "周日开始")
        HyperSwitch(value = readOnly, onChanged = { readOnly = it }, label = "只读展示")
        HyperSwitch(value = emphasized, onChanged = { emphasized = it }, label = "强调日期")
    }
}
