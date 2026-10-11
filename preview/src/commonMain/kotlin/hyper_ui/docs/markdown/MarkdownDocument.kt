/** 文件职责：解析唯一的 VitePress Markdown 正文，保留表格、代码与可折叠内容。 */
package hyper_ui.docs.markdown

sealed interface MarkdownBlock {
    data class Heading(val level: Int, val text: String) : MarkdownBlock
    data class Paragraph(val text: String) : MarkdownBlock
    data class Bullet(val text: String) : MarkdownBlock
    data class Quote(val text: String) : MarkdownBlock
    data class Code(val language: String, val content: String) : MarkdownBlock
    data class Table(val headers: List<String>, val rows: List<List<String>>) : MarkdownBlock
    data class Disclosure(val title: String, val blocks: List<MarkdownBlock>) : MarkdownBlock
}

fun parseMarkdown(markdown: String): List<MarkdownBlock> {
    val lines = markdown.replace("\r\n", "\n").split('\n')
    val blocks = mutableListOf<MarkdownBlock>()
    var index = 0

    // VitePress 页面元数据不属于正文；桌面 Preview 也读取同一份 Markdown。
    if (lines.firstOrNull()?.trim() == "---") {
        index = 1
        while (index < lines.size && lines[index].trim() != "---") index += 1
        if (index < lines.size) index += 1
    }

    while (index < lines.size) {
        val trimmed = lines[index].trim()
        when {
            trimmed.isEmpty() || trimmed.startsWith("<WasmPreview") -> index += 1
            trimmed.startsWith("<details") -> {
                val content = mutableListOf<String>()
                var depth = 1
                var inCode = false
                index += 1
                while (index < lines.size && depth > 0) {
                    val line = lines[index]
                    val value = line.trim()
                    if (value.startsWith("```")) inCode = !inCode
                    if (!inCode) {
                        if (value.startsWith("<details")) depth += 1
                        if (value == "</details>") depth -= 1
                    }
                    if (depth > 0) content += line
                    index += 1
                }
                val summaryIndex = content.indexOfFirst { it.trim().startsWith("<summary>") }
                val title = if (summaryIndex >= 0) cleanInlineMarkdown(content.removeAt(summaryIndex)) else "详细说明"
                blocks += MarkdownBlock.Disclosure(title, parseMarkdown(content.joinToString("\n")))
            }
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
        trimmed.startsWith("<details") ||
        isMarkdownTableHeader(lines, index)
}

private fun cleanInlineMarkdown(value: String): String = value
    .replace(Regex("</?(?:p|summary|span|strong|em)(?:\\s[^>]*)?>"), "")
    .replace(Regex("`([^`]*)`"), "\$1")
    .replace(Regex("\\[([^]]+)]\\([^)]+\\)"), "\$1")
    .replace("**", "")
    .replace("__", "")
    .replace("&lt;", "<")
    .replace("&gt;", ">")
    .replace("&amp;", "&")
