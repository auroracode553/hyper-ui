package hyper_ui

import android.app.DownloadManager
import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

/** 只记录本工具的系统下载 ID；升级确认后定向清理，不扫描公共下载目录。 */
internal class HyperUpdateDownloadRecords(context: Context) {
    private val context = context.applicationContext
    private val preferences = this.context.getSharedPreferences("app_update_download", Context.MODE_PRIVATE)
    private val downloads = this.context.getSystemService(DownloadManager::class.java)

    fun current(): JSONObject? = synchronized(preferences) {
        preferences.getString("task", null)?.let(::JSONObject)
    }

    fun save(id: Long, release: JSONObject) = synchronized(preferences) {
        // 清理失败留下的旧记录不能因新下载覆盖而丢失。
        retireCurrent()
        val task = JSONObject().put("id", id).put("release", release)
        check(preferences.edit().putString("task", task.toString()).commit()) { "无法保存更新下载记录" }
    }

    fun forget() = synchronized(preferences) {
        check(preferences.edit().remove("task").commit()) { "无法清除更新下载记录" }
    }

    /** 已完成旧包被更高版本替代时，保留最小清理记录。 */
    fun retireCurrent() = synchronized(preferences) {
        val task = current() ?: return@synchronized
        val id = task.getLong("id")
        val code = task.getJSONObject("release").getLong("versionCode")
        require(id > 0 && code > 0) { "更新下载清理记录无效" }
        val records = retired()
        if ((0 until records.length()).none { records.getJSONObject(it).getLong("id") == id }) {
            records.put(JSONObject().put("id", id).put("versionCode", code))
        }
        check(preferences.edit().putString("retired", records.toString()).remove("task").commit()) {
            "无法保留旧安装包清理记录"
        }
    }

    fun cleanupInstalled() = synchronized(preferences) {
        @Suppress("DEPRECATION")
        val installedCode = context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode
        current()?.let { task ->
            if (task.getJSONObject("release").getLong("versionCode") <= installedCode) retireCurrent()
        }
        val records = retired()
        if (records.length() == 0) return@synchronized
        val remaining = JSONArray()
        for (index in 0 until records.length()) {
            val record = records.getJSONObject(index)
            val id = record.getLong("id")
            val code = record.getLong("versionCode")
            require(id > 0 && code > 0) { "更新下载清理记录无效" }
            if (code > installedCode) {
                remaining.put(record)
                continue
            }
            // remove 同时删除该系统任务及文件；失败保留记录，后续启动继续尝试。
            runCatching { downloads.remove(id) }.onFailure {
                remaining.put(record)
                Log.w("HyperUpdate", "安装成功后的 APK 清理失败，保留记录待重试", it)
            }
        }
        if (remaining.length() != records.length()) {
            val editor = preferences.edit()
            if (remaining.length() == 0) editor.remove("retired")
            else editor.putString("retired", remaining.toString())
            check(editor.commit()) { "无法保存安装包清理结果" }
        }
    }

    private fun retired() = JSONArray(preferences.getString("retired", null) ?: "[]")
}
