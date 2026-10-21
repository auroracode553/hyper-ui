/** 文件职责：Android 内嵌月历的原生日期入口，复用平台无关的网格。 */
package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.LocalDate

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
) = HyperCalendarMonthContent(
    month = month.asCalendarDate(), modifier = modifier,
    selectedDate = selectedDate?.asCalendarDate(), endDate = endDate?.asCalendarDate(),
    firstDate = firstDate?.asCalendarDate(), lastDate = lastDate?.asCalendarDate(),
    today = LocalDate.now().asCalendarDate(), weekStart = weekStart, size = size,
    showWeekdays = showWeekdays, readOnly = readOnly, expanded = expanded,
    emphasizedDays = emphasizedDays,
    onChanged = onChanged?.let { callback -> { callback(LocalDate.of(it.year, it.month, it.day)) } },
)

private fun LocalDate.asCalendarDate() = HyperCalendarDate(year, monthValue, dayOfMonth)
