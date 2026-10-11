package hyper_ui

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToLong

internal fun normalizeHyperProgress(value: Float?): Float? = value?.takeIf { it.isFinite() }?.coerceIn(0f, 1f)

internal fun hyperProgressPercentage(value: Float?): Int? {
    val amount = normalizeHyperProgress(value) ?: return null
    return if (amount >= 1f) 100 else floor((amount * 100f).toDouble() + 1e-9).toInt().coerceIn(0, 99)
}

/** 两端统一采用十进制字节单位；未知总量不伪造百分比。 */
internal fun formatHyperTransferBytes(bytes: Double): String {
    var amount = if (bytes.isFinite() && bytes > 0) bytes else 0.0
    val units = listOf("B", "KB", "MB", "GB", "TB")
    var unit = 0
    while (amount >= 1000 && unit < units.lastIndex) { amount /= 1000; unit++ }
    val number = if (unit == 0) amount.roundToLong().toString() else {
        val tenths = (amount * 10).roundToLong()
        "${tenths / 10}.${tenths % 10}"
    }
    return "$number ${units[unit]}"
}

internal fun formatHyperTransferSeconds(seconds: Double): String = when {
    seconds < 60 -> "${ceil(seconds).toLong()} 秒"
    seconds < 3600 -> "${ceil(seconds / 60).toLong()} 分钟"
    seconds < 86400 -> "${ceil(seconds / 3600).toLong()} 小时"
    else -> "${ceil(seconds / 86400).toLong()} 天"
}
