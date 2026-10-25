package hyper_ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/** 系统仅在本应用被成功替换后发送；不把安装器打开或返回当作成功。 */
class HyperUpdateInstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_MY_PACKAGE_REPLACED) return
        val pending = goAsync()
        val worker = hyperUpdateExecutor("apk-cleanup")
        try {
            worker.execute {
                try {
                    HyperUpdateDownloadRecords(context).cleanupInstalled()
                } catch (error: Exception) {
                    Log.w("HyperUpdate", "安装包清理未完成，下次启动继续尝试", error)
                } finally {
                    pending.finish()
                }
            }
        } catch (error: Exception) {
            Log.w("HyperUpdate", "无法启动安装包清理，下次启动继续尝试", error)
            pending.finish()
        } finally {
            worker.shutdown()
        }
    }
}
