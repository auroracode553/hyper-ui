package hyper_ui

import android.content.Context
import android.os.Handler
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/** 按实际更新通道保存小字段；设备时间回拨不会导致永久跳过检查。 */
class HyperUpdateCheckHistory(
    context: Context,
    private val key: String,
    private val windowMs: Long = DEFAULT_WINDOW_MS,
    private val maxChecks: Int = DEFAULT_MAX_CHECKS,
) {
    companion object {
        const val DEFAULT_WINDOW_MS: Long = 60 * 60_000L
        const val DEFAULT_MAX_CHECKS: Int = 2
    }

    init { require(windowMs > 0 && maxChecks > 0) { "自动检查窗口和次数必须为正数" } }
    private val preferences = context.getSharedPreferences("hyper_update_checks", Context.MODE_PRIVATE)
    fun tryStartAutomaticCheck(): Boolean {
        val now = System.currentTimeMillis()
        val previous = preferences.getLong("${key}_window_started_at", 0)
        val inWindow = previous > 0 && now >= previous && now - previous < windowMs
        val count = if (inWindow) preferences.getInt("${key}_window_count", 0).coerceAtLeast(0) else 0
        if (count >= maxChecks) return false
        // 发起前占用一次额度，失败不归还；切换前后台和重启进程不重置窗口。
        preferences.edit().putLong("${key}_window_started_at", if (inWindow) previous else now)
            .putInt("${key}_window_count", count + 1).apply()
        return true
    }
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
