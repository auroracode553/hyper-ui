package hyper_ui

import org.json.JSONObject

internal data class HyperUpdateInfo(
    val version: String,
    val versionCode: Int,
    val notes: String,
    val url: String,
    val size: Long? = null,
) {
    fun toJson(): JSONObject = JSONObject().put("versionName", version).put("versionCode", versionCode)
        .put("notes", notes).put("downloadUrl", url).put("size", size ?: JSONObject.NULL)
}

/** 只读取公开静态索引；404、错误 JSON 或无效下载地址均属于检查失败。 */
internal class HyperUpdateRepository(private val configuration: HyperUpdateConfig, private val http: HyperUpdateHttpClient) {
    private val repository = configuration.repository
    private val downloadPrefix = "/$repository/releases/download/"

    fun latest(): HyperUpdateInfo = parse(JSONObject(http.text(configuration.indexUrl)))

    fun parse(index: JSONObject): HyperUpdateInfo {
        val version = index.getString("versionName")
        HyperUpdateVersion(version)
        val code = index.get("versionCode").toString().toLongOrNull() ?: error("客户端 versionCode 必须为整数")
        require(code in 1..Int.MAX_VALUE.toLong()) { "客户端 versionCode 必须为正整数" }
        val uri = java.net.URI(index.getString("downloadUrl"))
        require(uri.scheme == "https" && uri.host == "gitee.com" && uri.userInfo == null &&
            uri.port == -1 && uri.query == null && uri.fragment == null &&
            uri.path.startsWith(downloadPrefix) && uri.path.endsWith(".apk") &&
            uri.path.split('/').none { it == "." || it == ".." }) { "APK 地址不在发布仓库的下载目录" }
        val size = if (index.has("size") && !index.isNull("size")) index.getLong("size").also {
            require(it in 1..100L * 1024 * 1024) { "APK 大小无效" }
        } else null
        return HyperUpdateInfo(version, code.toInt(), index.optString("notes").takeUnless { it == "null" }.orEmpty(),
            uri.toASCIIString(), size)
    }
}
