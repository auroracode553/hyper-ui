/** 文件职责：封装 Android 原生 Toast 的时长映射与主线程调度。 */
package hyper_ui

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

enum class HyperToastDuration {
    Short,
    Long
}

/** 显示文本 Toast；可从任意线程调用。 */
fun hyperToast(
    context: Context,
    message: CharSequence,
    duration: HyperToastDuration = HyperToastDuration.Short
) {
    val applicationContext = context.applicationContext ?: context
    val platformDuration = duration.toPlatformDuration()
    if (Looper.myLooper() == Looper.getMainLooper()) {
        Toast.makeText(applicationContext, message, platformDuration).show()
    } else {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(applicationContext, message, platformDuration).show()
        }
    }
}

/** 显示字符串资源 Toast；可从任意线程调用。 */
fun hyperToast(
    context: Context,
    messageResource: Int,
    duration: HyperToastDuration = HyperToastDuration.Short
) {
    hyperToast(
        context = context,
        message = context.getText(messageResource),
        duration = duration
    )
}

private fun HyperToastDuration.toPlatformDuration(): Int = when (this) {
    HyperToastDuration.Short -> Toast.LENGTH_SHORT
    HyperToastDuration.Long -> Toast.LENGTH_LONG
}
