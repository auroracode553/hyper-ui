package hyper_ui

import org.json.JSONObject

/** 只接收发布配置中的公开更新字段，禁止把发布凭据传入客户端。 */
data class HyperUpdateConfig(
    val repository: String,
    val branch: String,
    val indexFile: String,
    val checkIntervalMs: Long = 6 * 60 * 60_000L,
    val currentNotes: String = "",
) {
    init {
        require(Regex("[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+").matches(repository)) { "更新仓库必须为 owner/repository" }
        require(branch.isNotBlank() && branch.split('/').all { it.matches(Regex("[A-Za-z0-9_-][A-Za-z0-9._-]*")) }) { "索引分支无效" }
        require(indexFile.endsWith(".json") && indexFile.split('/').all {
            it.matches(Regex("[A-Za-z0-9_-][A-Za-z0-9._-]*"))
        }) { "更新索引路径无效" }
        require(checkIntervalMs >= 60_000L) { "自动检查间隔至少为 1 分钟" }
    }

    val indexUrl: String get() = "https://gitee.com/$repository/raw/$branch/$indexFile"

    companion object {
        fun fromJson(json: String): HyperUpdateConfig {
            val root = JSONObject(json)
            val update = root.getJSONObject("update")
            val minutes = update.getLong("checkIntervalMinutes")
            require(minutes in 1..525600) { "检查间隔超出有效范围" }
            return HyperUpdateConfig(root.getString("repository"),
                update.getString("branch"), update.getString("indexFile"), minutes * 60_000L, root.optString("currentNotes"))
        }
    }
}
