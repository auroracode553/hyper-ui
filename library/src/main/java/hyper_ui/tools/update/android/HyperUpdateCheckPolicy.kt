package hyper_ui

import android.content.Context
import android.os.Handler
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/** 按实际更新通道保存小字段；设备时间回拨不会导致永久跳过检查。 */
class HyperUpdateCheckHistory(context: Context, private val key: String, private val intervalMs: Long) {
    private val preferences = context.getSharedPreferences("hyper_update_checks", Context.MODE_PRIVATE)
    fun isDue(): Boolean {
        val now = System.currentTimeMillis()
        fun elapsed(field: String, interval: Long): Boolean {
            val previous = preferences.getLong("${key}_$field", 0)
            return previous <= 0 || now < previous || now - previous >= interval
        }
        return elapsed("success", intervalMs) && elapsed("attempt", 60_000L)
    }
    fun started() { preferences.edit().putLong("${key}_attempt", System.currentTimeMillis()).apply() }
    fun succeeded() { preferences.edit().putLong("${key}_success", System.currentTimeMillis()).apply() }
    internal fun dismissApp(code: Int) {
        preferences.edit().putInt("${key}_dismissed_version", code)
            .putLong("${key}_dismissed_at", System.currentTimeMillis()).apply()
    }
    internal fun shouldRemindApp(code: Int): Boolean {
        val previous = preferences.getLong("${key}_dismissed_at", 0)
        val now = System.currentTimeMillis()
        return preferences.getInt("${key}_dismissed_version", 0) != code || now < previous ||
            now - previous >= 24 * 60 * 60_000L
    }
}

/** 仅首次页面就绪 5 秒、恢复前台 2 秒；不创建常驻轮询或后台任务。 */
class HyperUpdateCheckSchedule(private val main: Handler, private val check: () -> Unit) : AutoCloseable {
    private var ready = false
    private var foreground = false
    private var entered = false
    private val pending = Runnable { if (ready && foreground) check() }
    fun pageReady() {
        if (ready) return
        ready = true
        if (foreground) main.postDelayed(pending, 5000)
    }
    fun foreground(visible: Boolean) {
        if (foreground == visible) return
        foreground = visible
        main.removeCallbacks(pending)
        if (visible) {
            val delay = if (entered) 2000L else 5000L
            entered = true
            if (ready) main.postDelayed(pending, delay)
        }
    }
    override fun close() { main.removeCallbacks(pending); ready = false; foreground = false }
}

internal fun hyperUpdateExecutor(name: String) = ThreadPoolExecutor(
    1, 1, 10, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>(8),
    { task -> Thread(task, "HyperUI-$name") },
).apply { allowCoreThreadTimeOut(true) }
