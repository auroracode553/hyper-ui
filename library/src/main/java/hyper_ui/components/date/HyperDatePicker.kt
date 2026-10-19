/** 文件职责：Android 单日期选择入口；使用原生 LocalDate，界面与 Preview 共用月历面板。 */
package hyper_ui

import androidx.compose.runtime.Composable
import java.time.LocalDate

object HyperDatePicker {
    /** 临时选择由面板维护；确定后先 onClose，退出动画结束再通过 onChanged 提交。 */
    @Composable
    fun date(
        visible: Boolean,
        onClose: () -> Unit,
        onChanged: (LocalDate) -> Unit,
        initialDate: LocalDate = LocalDate.now(),
        firstDate: LocalDate = LocalDate.of(1900, 1, 1),
        lastDate: LocalDate = LocalDate.of(2100, 12, 31),
    ) {
        require(!lastDate.isBefore(firstDate)) { "lastDate 必须不早于 firstDate" }
        HyperDatePickerSheet(
            visible = visible,
            onClose = onClose,
            onChanged = { onChanged(LocalDate.of(it.year, it.month, it.day)) },
            initialDate = initialDate.coerceIn(firstDate, lastDate).toCalendarDate(),
            firstDate = firstDate.toCalendarDate(),
            lastDate = lastDate.toCalendarDate(),
            today = LocalDate.now().toCalendarDate(),
        )
    }
}

private fun LocalDate.toCalendarDate() = HyperCalendarDate(year, monthValue, dayOfMonth)
