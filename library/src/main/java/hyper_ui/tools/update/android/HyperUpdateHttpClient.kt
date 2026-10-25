package hyper_ui

import java.io.File
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URI
import android.os.SystemClock

internal class HyperUpdateHttpException(val status: Int) : java.io.IOException(
    if (status == 404) "更新索引或文件不存在（HTTP 404），请检查发布仓库" else "服务端返回 HTTP $status")

/** 有界下载；每个请求独立连接，避免更新链路与实时连接互相阻塞。 */
class HyperUpdateHttpClient(private val allowLocalHttp: Boolean = false) : AutoCloseable {
    private var closed = false
    private var active: HttpURLConnection? = null
    fun text(url: String): String = bytes(url).toString(Charsets.UTF_8)

    fun bytes(url: String, maxBytes: Int = 2 * 1024 * 1024): ByteArray {
        require(maxBytes in 1..8 * 1024 * 1024) { "更新索引读取上限无效" }
        val connection = open(url)
        connection.useCaches = false
        connection.setRequestProperty("Cache-Control", "no-cache")
        return try {
            if (connection.responseCode !in 200..299) throw HyperUpdateHttpException(connection.responseCode)
            connection.inputStream.use { input ->
                val output = ByteArrayOutputStream()
                val buffer = ByteArray(8192)
                while (output.size() <= maxBytes) {
                    check(!Thread.currentThread().isInterrupted) { "更新请求已取消" }
                    val count = input.read(buffer, 0, minOf(buffer.size, maxBytes + 1 - output.size()))
                    if (count < 0) break
                    output.write(buffer, 0, count)
                }
                check(output.size() <= maxBytes) { "服务端响应过大" }
                output.toByteArray()
            }
        } finally { release(connection) }
    }

    fun download(url: String, target: File, limit: Long,
        onChunk: ((ByteArray, Int) -> Unit)? = null, onProgress: (Long, Double?) -> Unit) {
        require(limit > 0) { "下载大小上限必须为正数" }
        val connection = open(url)
        try {
            if (connection.responseCode !in 200..299) throw HyperUpdateHttpException(connection.responseCode)
            connection.inputStream.use { input -> target.outputStream().use { output ->
                val buffer = ByteArray(64 * 1024)
                var total = 0L
                var previousBytes = 0L
                var previousTime = SystemClock.elapsedRealtime()
                onProgress(0, null)
                while (true) {
                    check(!Thread.currentThread().isInterrupted) { "下载已取消" }
                    val count = input.read(buffer)
                    if (count < 0) break
                    total += count
                    check(total <= limit) { "资源包超过声明大小" }
                    output.write(buffer, 0, count)
                    onChunk?.invoke(buffer, count)
                    // 控制主线程快照频率，同时提供真实字节速度；完成时补发最后一次。
                    val now = SystemClock.elapsedRealtime()
                    if (now - previousTime >= 500 || total == limit) {
                        val speed = if (now > previousTime) (total - previousBytes) * 1000.0 / (now - previousTime) else null
                        onProgress(total, speed)
                        previousBytes = total
                        previousTime = now
                    }
                }
                if (total != previousBytes) {
                    val now = SystemClock.elapsedRealtime()
                    onProgress(total, if (now > previousTime) (total - previousBytes) * 1000.0 / (now - previousTime) else null)
                }
                output.fd.sync()
            } }
        } finally { release(connection) }
    }

    @Synchronized private fun open(url: String): HttpURLConnection {
        check(!closed) { "更新请求已关闭" }
        val uri = URI(url)
        require(uri.host != null && uri.userInfo == null && (uri.scheme == "https" ||
            (allowLocalHttp && uri.scheme == "http" && uri.host in setOf("127.0.0.1", "localhost", "10.0.2.2")))) {
            "下载地址必须为 HTTPS；仅显式本地调试允许回环 HTTP"
        }
        return (uri.toURL().openConnection() as HttpURLConnection).apply {
            connectTimeout = 20_000
            readTimeout = 45_000
            setRequestProperty("User-Agent", "HyperUI-Update")
        }.also { active = it }
    }

    private fun release(connection: HttpURLConnection) {
        connection.disconnect()
        synchronized(this) { if (active === connection) active = null }
    }

    override fun close() {
        val connection = synchronized(this) {
            closed = true
            active.also { active = null }
        }
        connection?.disconnect()
    }
}
