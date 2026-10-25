package hyper_ui

/** 与资源协议共用语义化版本排序，预发行版本低于正式版本。 */
data class HyperUpdateVersion(val value: String) : Comparable<HyperUpdateVersion> {
    private val match = Regex("^v?(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-([0-9A-Za-z.-]+))?(?:\\+[0-9A-Za-z.-]+)?$")
        .matchEntire(value) ?: error("版本格式无效：$value")
    private val core = (1..3).map { match.groupValues[it].toLong() }
    private val pre = match.groupValues[4]
    override fun compareTo(other: HyperUpdateVersion): Int {
        core.zip(other.core).forEach { (a, b) -> if (a != b) return a.compareTo(b) }
        if (pre == other.pre) return 0
        if (pre.isEmpty()) return 1
        if (other.pre.isEmpty()) return -1
        val a = pre.split('.')
        val b = other.pre.split('.')
        a.zip(b).forEach { (x, y) ->
            val nx = x.toLongOrNull()
            val ny = y.toLongOrNull()
            val comparison = when {
                nx != null && ny != null -> nx.compareTo(ny)
                nx != null -> -1
                ny != null -> 1
                else -> x.compareTo(y)
            }
            if (comparison != 0) return comparison
        }
        return a.size.compareTo(b.size)
    }
}
