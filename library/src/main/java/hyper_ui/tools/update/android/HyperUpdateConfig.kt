package hyper_ui

import org.json.JSONObject

/** 只接收发布配置中的公开更新字段，禁止把发布凭据传入客户端。 */
data class HyperUpdateConfig(
    val repository: String,
    val branch: String,
    val indexFile: String,
    val autoCheckWindowMs: Long = HyperUpdateCheckHistory.DEFAULT_WINDOW_MS,
    val autoCheckLimit: Int = HyperUpdateCheckHistory.DEFAULT_MAX_CHECKS,
    val currentNotes: String = "",
) {
    init {
        require(Regex("[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+").matches(repository)) { "更新仓库必须为 owner/repository" }
        require(branch.isNotBlank() && branch.split('/').all { it.matches(Regex("[A-Za-z0-9_-][A-Za-z0-9._-]*")) }) { "索引分支无效" }
        require(indexFile.endsWith(".json") && indexFile.split('/').all {
            it.matches(Regex("[A-Za-z0-9_-][A-Za-z0-9._-]*"))
        }) { "更新索引路径无效" }
        require(autoCheckWindowMs >= 60_000L && autoCheckLimit > 0) { "自动检查窗口至少为 1 分钟，次数必须为正数" }
    }

    val indexUrl: String get() = "https://gitee.com/$repository/raw/$branch/$indexFile"

    companion object {
        fun fromJson(json: String): HyperUpdateConfig {
            val root = JSONObject(json)
            val update = root.getJSONObject("update")
            val windowMs = if (update.has("autoCheckWindowMinutes")) {
                val minutes = update.getLong("autoCheckWindowMinutes")
                require(minutes in 1..525600) { "检查窗口超出有效范围" }
                minutes * 60_000L
            } else HyperUpdateCheckHistory.DEFAULT_WINDOW_MS
            val limit = if (update.has("autoCheckLimit")) update.getInt("autoCheckLimit")
                else HyperUpdateCheckHistory.DEFAULT_MAX_CHECKS
            return HyperUpdateConfig(root.getString("repository"),
                update.getString("branch"), update.getString("indexFile"), windowMs, limit, root.optString("currentNotes"))
        }
    }
}
