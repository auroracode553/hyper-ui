/** 文件职责：月历内部的公历计算，供 Android 与文档 Preview 复用，不依赖平台日期 API。 */
package hyper_ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class HyperCalendarDate(val year: Int, val month: Int, val day: Int = 1) : Comparable<HyperCalendarDate> {
    init {
        require(year in 1..9999 && month in 1..12)
        require(day in 1..daysInMonth)
    }

    val daysInMonth: Int
        get() = when (month) {
            2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }

    val monthStart: HyperCalendarDate get() = copy(day = 1)

    // Sakamoto 公历星期算法；月历以周一为第 0 列。
    val mondayColumn: Int
        get() {
            val adjustedYear = if (month < 3) year - 1 else year
            val monthOffsets = intArrayOf(0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4)
            val sundayColumn = (adjustedYear + adjustedYear / 4 - adjustedYear / 100 +
                adjustedYear / 400 + monthOffsets[month - 1] + day) % 7
            return (sundayColumn + 6) % 7
        }

    fun shiftMonth(offset: Int): HyperCalendarDate? {
        val index = (year - 1) * 12 + month - 1 + offset
        if (index !in 0 until 9999 * 12) return null
        return HyperCalendarDate(index / 12 + 1, index % 12 + 1)
    }

    fun overlapsMonth(first: HyperCalendarDate, last: HyperCalendarDate): Boolean =
        copy(day = daysInMonth) >= first && monthStart <= last

    override fun compareTo(other: HyperCalendarDate): Int =
        compareValuesBy(this, other, HyperCalendarDate::year, HyperCalendarDate::month, HyperCalendarDate::day)
}
