/** 文件职责：解析 Preview 使用的 VitePress Markdown，并提取公开 API 参数。 */
package hyper_ui.docs.markdown

sealed interface MarkdownBlock {
    data class Heading(val level: Int, val text: String) : MarkdownBlock
    data class Paragraph(val text: String) : MarkdownBlock
    data class Bullet(val text: String) : MarkdownBlock
    data class Quote(val text: String) : MarkdownBlock
    data class Code(val language: String, val content: String) : MarkdownBlock
    data class Table(val headers: List<String>, val rows: List<List<String>>) : MarkdownBlock
}

data class ApiParameter(
    val name: String,
    val type: String,
    val defaultValue: String
)

data class ApiSignature(
    val name: String,
    val kind: String,
    val parameters: List<ApiParameter>
)

fun parseMarkdown(markdown: String): List<MarkdownBlock> {
    val lines = markdown.replace("\r\n", "\n").split('\n')
    val blocks = mutableListOf<MarkdownBlock>()
    var index = 0

    while (index < lines.size) {
        val trimmed = lines[index].trim()
        when {
            trimmed.isEmpty() || trimmed.startsWith("<WasmPreview") -> index += 1
            trimmed.startsWith("```") -> {
                val language = trimmed.removePrefix("```").trim()
                val code = mutableListOf<String>()
                index += 1
                while (index < lines.size && !lines[index].trim().startsWith("```")) {
                    code += lines[index]
                    index += 1
                }
                if (index < lines.size) index += 1
                blocks += MarkdownBlock.Code(language, code.joinToString("\n").trimEnd())
            }
            trimmed.startsWith("#") -> {
                val level = trimmed.takeWhile { it == '#' }.length.coerceIn(1, 4)
                blocks += MarkdownBlock.Heading(level, cleanInlineMarkdown(trimmed.drop(level).trim()))
                index += 1
            }
            isMarkdownTableHeader(lines, index) -> {
                val headers = parseTableRow(lines[index])
                val rows = mutableListOf<List<String>>()
                index += 2
                while (index < lines.size && lines[index].trim().startsWith("|")) {
                    rows += parseTableRow(lines[index])
                    index += 1
                }
                blocks += MarkdownBlock.Table(headers, rows)
            }
            trimmed.startsWith("- ") -> {
                blocks += MarkdownBlock.Bullet(cleanInlineMarkdown(trimmed.removePrefix("- ")))
                index += 1
            }
            trimmed.startsWith("> ") -> {
                blocks += MarkdownBlock.Quote(cleanInlineMarkdown(trimmed.removePrefix("> ")))
                index += 1
            }
            else -> {
                val paragraph = mutableListOf(trimmed)
                index += 1
                while (index < lines.size && !startsNewBlock(lines, index)) {
                    paragraph += lines[index].trim()
                    index += 1
                }
                blocks += MarkdownBlock.Paragraph(
                    cleanInlineMarkdown(paragraph.joinToString(" "))
                )
            }
        }
    }
    return blocks
}

fun extractApiSignatures(blocks: List<MarkdownBlock>): List<ApiSignature> = blocks
    .filterIsInstance<MarkdownBlock.Code>()
    .filter { it.language.isBlank() || it.language.equals("kotlin", ignoreCase = true) }
    .flatMap { extractDeclarations(it.content) }

private fun extractDeclarations(code: String): List<ApiSignature> {
    val declarations = mutableListOf<ApiSignature>()
    val pattern = Regex("(?:data\\s+class|fun)\\s+(?:<[^>]+>\\s*)?([A-Za-z_][A-Za-z0-9_]*)\\s*\\(")
    pattern.findAll(code).forEach { match ->
        val openingParenthesis = code.indexOf('(', startIndex = match.range.first)
        val closingParenthesis = findMatchingParenthesis(code, openingParenthesis)
        if (openingParenthesis < 0 || closingParenthesis < 0) return@forEach

        val declarationPrefix = code.substring(match.range.first, openingParenthesis)
        val kind = if (declarationPrefix.startsWith("data class")) "数据属性" else "函数参数"
        val parameters = splitTopLevel(code.substring(openingParenthesis + 1, closingParenthesis))
            .mapNotNull(::parseParameter)
        if (parameters.isNotEmpty()) {
            declarations += ApiSignature(
                name = match.groupValues[1],
                kind = kind,
                parameters = parameters
            )
        }
    }
    return declarations
}

private fun parseParameter(source: String): ApiParameter? {
    val normalized = source.trim().removePrefix("val ").removePrefix("var ")
    if (normalized.isBlank()) return null
    val colonIndex = findTopLevelCharacter(normalized, ':')
    if (colonIndex <= 0) return null
    val equalsIndex = findTopLevelCharacter(normalized, '=', startIndex = colonIndex + 1)
    val name = normalized.substring(0, colonIndex).trim()
    val type = if (equalsIndex >= 0) {
        normalized.substring(colonIndex + 1, equalsIndex).trim()
    } else {
        normalized.substring(colonIndex + 1).trim()
    }
    val defaultValue = if (equalsIndex >= 0) {
        normalized.substring(equalsIndex + 1).trim().replace(Regex("\\s+"), " ")
    } else {
        "必填"
    }
    return ApiParameter(name = name, type = type, defaultValue = defaultValue)
}

private fun splitTopLevel(source: String): List<String> {
    val result = mutableListOf<String>()
    var start = 0
    var roundDepth = 0
    var angleDepth = 0
    var squareDepth = 0
    var curlyDepth = 0

    source.forEachIndexed { index, character ->
        when (character) {
            '(' -> roundDepth += 1
            ')' -> roundDepth -= 1
            '<' -> angleDepth += 1
            '>' -> angleDepth = (angleDepth - 1).coerceAtLeast(0)
            '[' -> squareDepth += 1
            ']' -> squareDepth -= 1
            '{' -> curlyDepth += 1
            '}' -> curlyDepth -= 1
            ',' -> if (roundDepth == 0 && angleDepth == 0 && squareDepth == 0 && curlyDepth == 0) {
                result += source.substring(start, index)
                start = index + 1
            }
        }
    }
    result += source.substring(start)
    return result
}

private fun findMatchingParenthesis(source: String, openingIndex: Int): Int {
    if (openingIndex < 0) return -1
    var depth = 0
    for (index in openingIndex until source.length) {
        when (source[index]) {
            '(' -> depth += 1
            ')' -> {
                depth -= 1
                if (depth == 0) return index
            }
        }
    }
    return -1
}

private fun findTopLevelCharacter(source: String, target: Char, startIndex: Int = 0): Int {
    var roundDepth = 0
    var angleDepth = 0
    var squareDepth = 0
    var curlyDepth = 0
    for (index in startIndex until source.length) {
        when (source[index]) {
            '(' -> roundDepth += 1
            ')' -> roundDepth -= 1
            '<' -> angleDepth += 1
            '>' -> angleDepth = (angleDepth - 1).coerceAtLeast(0)
            '[' -> squareDepth += 1
            ']' -> squareDepth -= 1
            '{' -> curlyDepth += 1
            '}' -> curlyDepth -= 1
            target -> if (roundDepth == 0 && angleDepth == 0 && squareDepth == 0 && curlyDepth == 0) {
                return index
            }
        }
    }
    return -1
}

private fun isMarkdownTableHeader(lines: List<String>, index: Int): Boolean {
    if (index + 1 >= lines.size || !lines[index].trim().startsWith("|")) return false
    val separator = lines[index + 1].trim()
    return separator.startsWith("|") && separator
        .removePrefix("|")
        .removeSuffix("|")
        .split('|')
        .all { cell -> cell.trim().matches(Regex(":?-{3,}:?")) }
}

private fun parseTableRow(line: String): List<String> = line.trim()
    .removePrefix("|")
    .removeSuffix("|")
    .split('|')
    .map { cleanInlineMarkdown(it.trim()) }

private fun startsNewBlock(lines: List<String>, index: Int): Boolean {
    val trimmed = lines[index].trim()
    return trimmed.isEmpty() ||
        trimmed.startsWith("#") ||
        trimmed.startsWith("```") ||
        trimmed.startsWith("- ") ||
        trimmed.startsWith("> ") ||
        trimmed.startsWith("<WasmPreview") ||
        isMarkdownTableHeader(lines, index)
}

private fun cleanInlineMarkdown(value: String): String = value
    .replace(Regex("`([^`]*)`"), "\$1")
    .replace(Regex("\\[([^]]+)]\\([^)]+\\)"), "\$1")
    .replace("**", "")
    .replace("__", "")
