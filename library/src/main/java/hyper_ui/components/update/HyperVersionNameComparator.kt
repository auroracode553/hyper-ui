/** 文件职责：以跨平台字符串算法比较语义化版本名，避免依赖平台数值类型。 */
package hyper_ui

object HyperVersionNameComparator {
    fun isNewer(candidate: String, current: String): Boolean {
        return compare(candidate, current) > 0
    }

    fun compare(left: String, right: String): Int {
        val leftVersion = ParsedVersion.parse(left)
        val rightVersion = ParsedVersion.parse(right)
        val componentCount = maxOf(leftVersion.core.size, rightVersion.core.size)

        repeat(componentCount) { index ->
            val leftComponent = leftVersion.core.getOrElse(index) { "0" }
            val rightComponent = rightVersion.core.getOrElse(index) { "0" }
            val componentComparison = compareNumericIdentifiers(leftComponent, rightComponent)
            if (componentComparison != 0) return componentComparison
        }

        return comparePrerelease(leftVersion.prerelease, rightVersion.prerelease)
    }

    private fun comparePrerelease(left: List<String>, right: List<String>): Int {
        if (left.isEmpty() && right.isEmpty()) return 0
        if (left.isEmpty()) return 1
        if (right.isEmpty()) return -1

        val identifierCount = maxOf(left.size, right.size)
        repeat(identifierCount) { index ->
            val leftIdentifier = left.getOrNull(index) ?: return -1
            val rightIdentifier = right.getOrNull(index) ?: return 1
            val comparison = comparePrereleaseIdentifier(leftIdentifier, rightIdentifier)
            if (comparison != 0) return comparison
        }
        return 0
    }

    private fun comparePrereleaseIdentifier(left: String, right: String): Int {
        val leftIsNumber = left.all(Char::isDigit)
        val rightIsNumber = right.all(Char::isDigit)
        return when {
            leftIsNumber && rightIsNumber -> compareNumericIdentifiers(left, right)
            leftIsNumber -> -1
            rightIsNumber -> 1
            else -> left.lowercase().compareTo(right.lowercase())
        }
    }

    private fun compareNumericIdentifiers(left: String, right: String): Int {
        val normalizedLeft = left.trimStart('0').ifEmpty { "0" }
        val normalizedRight = right.trimStart('0').ifEmpty { "0" }
        return normalizedLeft.length.compareTo(normalizedRight.length)
            .takeIf { it != 0 }
            ?: normalizedLeft.compareTo(normalizedRight)
    }

    private data class ParsedVersion(
        val core: List<String>,
        val prerelease: List<String>
    ) {
        companion object {
            private val semanticVersionPattern = Regex(
                pattern = "^(?:v)?(\\d+(?:\\.\\d+)*)(?:-([0-9A-Za-z.-]+))?(?:\\+[0-9A-Za-z.-]+)?$",
                option = RegexOption.IGNORE_CASE
            )

            fun parse(rawVersion: String): ParsedVersion {
                val match = semanticVersionPattern.matchEntire(rawVersion.trim())
                    ?: throw IllegalArgumentException("无法比较版本号：$rawVersion")
                return ParsedVersion(
                    core = match.groupValues[1].split('.'),
                    prerelease = match.groupValues[2]
                        .takeIf(String::isNotEmpty)
                        ?.split('.')
                        .orEmpty()
                )
            }
        }
    }
}
