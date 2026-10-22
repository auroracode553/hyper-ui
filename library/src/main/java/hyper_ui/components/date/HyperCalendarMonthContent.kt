/** 文件职责：内嵌月历与日期选择器共用六行日期网格；只读模式合并绘制。 */
package hyper_ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

@Composable
internal fun HyperCalendarMonthContent(
    month: HyperCalendarDate,
    modifier: Modifier = Modifier,
    selectedDate: HyperCalendarDate? = null,
    endDate: HyperCalendarDate? = null,
    firstDate: HyperCalendarDate? = null,
    lastDate: HyperCalendarDate? = null,
    today: HyperCalendarDate,
    weekStart: Int = 1,
    size: String = "default",
    showWeekdays: Boolean = true,
    readOnly: Boolean = false,
    expanded: Boolean = false,
    emphasizedDays: Set<Int> = emptySet(),
    onChanged: ((HyperCalendarDate) -> Unit)? = null,
) {
    require(weekStart in 1..7)
    val start = month.monthStart
    val offset = (start.mondayColumn - weekStart + 1 + 7) % 7
    val rowHeight = HyperCalendarDayDefaults.minimumHeight(size)
    Column(modifier) {
        if (showWeekdays) {
            Row {
                repeat(7) { column ->
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        HyperText("一二三四五六日"[(weekStart - 1 + column) % 7].toString(),
                            fontSize = 12.sp, lineHeight = 16.8.sp, color = HyperColors.secondaryText)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
        }
        val gridModifier = if (expanded) Modifier.weight(1f) else Modifier.height(rowHeight * 6)
        if (readOnly) {
            val measurer = rememberTextMeasurer()
            val direction = LocalLayoutDirection.current
            val primary = HyperColors.accent
            val rangeBackground = HyperColors.selectionBackground
            val foreground = HyperColors.primaryText
            val muted = HyperColors.secondaryText.copy(alpha = 105 / 255f)
            val fontFamily = HyperTheme.typography.bodyLarge.fontFamily
            Canvas(gridModifier.fillMaxWidth()) {
                val width = this.size.width / 7
                val height = this.size.height / 6
                val radius = maxOf(0f, min(width, min(rowHeight.toPx() - 6.dp.toPx(), height - 6.dp.toPx()))) / 2
                repeat(start.daysInMonth) { index ->
                    val date = start.copy(day = index + 1)
                    val cell = offset + index
                    val column = if (direction == LayoutDirection.Rtl) 6 - cell % 7 else cell % 7
                    val center = Offset((column + .5f) * width, (cell / 7 + .5f) * height)
                    val enabled = (firstDate == null || date >= firstDate) && (lastDate == null || date <= lastDate)
                    val active = date == selectedDate || date == endDate
                    if (selectedDate != null && endDate != null && date > selectedDate && date < endDate) {
                        drawRect(rangeBackground, center - Offset(width / 2, height / 2), Size(width, height))
                    }
                    if (active) drawCircle(primary, radius, center)
                    else if (date == today && enabled) drawCircle(primary, radius, center, style = Stroke(1.dp.toPx()))
                    val text = measurer.measure("${index + 1}", style = TextStyle(
                        fontFamily = fontFamily,
                        fontSize = (when (size) { "small" -> 10; "large" -> 17; else -> 14 }).sp,
                        color = when {
                            active -> Color(1f, 1f, 1f, 1f)
                            !enabled -> muted
                            index + 1 in emphasizedDays -> primary
                            else -> foreground
                        },
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                    ))
                    drawText(text, topLeft = center - Offset(text.size.width / 2f, text.size.height / 2f))
                }
            }
        } else Column(gridModifier) {
            repeat(6) { row ->
                Row(Modifier.weight(1f)) {
                    repeat(7) { column ->
                        val day = row * 7 + column - offset + 1
                        if (day !in 1..start.daysInMonth) Spacer(Modifier.weight(1f))
                        else {
                            val date = start.copy(day = day)
                            HyperCalendarDay(day, modifier = Modifier.weight(1f).fillMaxHeight(),
                                size = size, expanded = true,
                                selected = date == selectedDate || date == endDate,
                                today = date == today,
                                emphasized = day in emphasizedDays,
                                enabled = (firstDate == null || date >= firstDate) && (lastDate == null || date <= lastDate),
                                inRange = selectedDate != null && endDate != null && date > selectedDate && date < endDate,
                                onPressed = onChanged?.let { { it(date) } },
                            )
                        }
                    }
                }
            }
        }
    }
}
