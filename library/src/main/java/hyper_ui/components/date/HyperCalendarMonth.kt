/** 文件职责：移植 Flutter HyperDatePicker 的月历、月份面板与年份跳转。 */
package hyper_ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.ChevronLeft
import com.composables.icons.lucide.ChevronRight

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable

@Composable
internal fun HyperCalendarPickerMonth(
    visible: Boolean,
    initialDate: HyperCalendarDate,
    firstDate: HyperCalendarDate,
    lastDate: HyperCalendarDate,
    selectedDate: HyperCalendarDate,
    today: HyperCalendarDate,
    onChanged: (HyperCalendarDate) -> Unit,
) {
    var month by remember(firstDate, lastDate) { mutableStateOf(initialDate.monthStart) }
    var choosingMonth by remember { mutableStateOf(false) }
    LaunchedEffect(visible, initialDate, firstDate, lastDate) {
        if (visible) {
            month = initialDate.monthStart
            choosingMonth = false
        }
    }
    fun available(target: HyperCalendarDate?) = target?.overlapsMonth(firstDate, lastDate) == true
    val previous = month.shiftMonth(-1)
    val next = month.shiftMonth(1)
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            HyperButton(onPressed = { previous?.let { month = it } }, enabled = available(previous), type = "icon") {
                HyperIcon(Lucide.ChevronLeft, "上个月")
            }
            HyperButton(onPressed = { choosingMonth = !choosingMonth }, modifier = Modifier.weight(1f)) {
                HyperText("${month.year} 年 ${month.month} 月")
                HyperIcon(Lucide.ChevronDown, null)
            }
            HyperButton(onPressed = { next?.let { month = it } }, enabled = available(next), type = "icon") {
                HyperIcon(Lucide.ChevronRight, "下个月")
            }
        }
        Spacer(Modifier.height(12.dp))
        if (choosingMonth) {
            HyperCalendarMonths(month, firstDate, lastDate, onYearChanged = { month = it }) {
                month = it
                choosingMonth = false
            }
        } else {
            HyperCalendarDays(month, firstDate, lastDate, selectedDate, today, onChanged)
        }
    }
}

@Composable
private fun HyperCalendarMonths(
    month: HyperCalendarDate,
    firstDate: HyperCalendarDate,
    lastDate: HyperCalendarDate,
    onYearChanged: (HyperCalendarDate) -> Unit,
    onChanged: (HyperCalendarDate) -> Unit,
) {
    // 年份跳转只切换显示月份，不能提交临时日期。
    fun changeYear(offset: Int): HyperCalendarDate {
        val year = month.year + offset
        val lower = if (year == firstDate.year) firstDate.month else 1
        val upper = if (year == lastDate.year) lastDate.month else 12
        return HyperCalendarDate(year, month.month.coerceIn(lower, upper))
    }
    val year = month.year
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            HyperButton(onPressed = { onYearChanged(changeYear(-10)) }, enabled = year - 10 >= firstDate.year) { HyperText("−10 年") }
            HyperButton(onPressed = { onYearChanged(changeYear(-1)) }, enabled = year > firstDate.year, type = "icon") {
                HyperIcon(Lucide.ChevronLeft, "上一年")
            }
            HyperText("$year 年", fontWeight = FontWeight.Bold)
            HyperButton(onPressed = { onYearChanged(changeYear(1)) }, enabled = year < lastDate.year, type = "icon") {
                HyperIcon(Lucide.ChevronRight, "下一年")
            }
            HyperButton(onPressed = { onYearChanged(changeYear(10)) }, enabled = year + 10 <= lastDate.year) { HyperText("+10 年") }
        }
        Spacer(Modifier.height(12.dp))
        repeat(4) { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) { column ->
                    val target = HyperCalendarDate(year, row * 3 + column + 1)
                    val selected = target == month
                    HyperButton(
                        onPressed = { onChanged(target) },
                        enabled = target.overlapsMonth(firstDate, lastDate),
                        modifier = Modifier.weight(1f),
                        type = if (selected) "filled" else null,
                        colorStyle = if (selected) "primary" else null,
                    ) { HyperText("${target.month} 月") }
                }
            }
            if (row < 3) Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun HyperCalendarDays(
    month: HyperCalendarDate,
    firstDate: HyperCalendarDate,
    lastDate: HyperCalendarDate,
    selectedDate: HyperCalendarDate,
    today: HyperCalendarDate,
    onChanged: (HyperCalendarDate) -> Unit,
) = HyperCalendarMonthContent(
    month = month, firstDate = firstDate, lastDate = lastDate,
    selectedDate = selectedDate, today = today, onChanged = onChanged,
)