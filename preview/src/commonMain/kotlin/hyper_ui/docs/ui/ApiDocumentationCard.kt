/** 文件职责：在 Preview 示例代码下方加载并渲染 VitePress API Markdown。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.docs.generated.resources.Res
import hyper_ui.docs.markdown.ApiSignature
import hyper_ui.docs.markdown.MarkdownBlock
import hyper_ui.docs.markdown.extractApiSignatures
import hyper_ui.docs.markdown.parseMarkdown
import hyper_ui.docs.theme.DocsBorder
import hyper_ui.docs.theme.DocsCodeBackground
import hyper_ui.docs.theme.DocsCodeText

@Composable
internal fun ApiDocumentationCard(documentPaths: List<String>) {
    var documents by remember(documentPaths) { mutableStateOf<Map<String, String>>(emptyMap()) }
    var loadError by remember(documentPaths) { mutableStateOf<String?>(null) }

    LaunchedEffect(documentPaths) {
        try {
            val loadedDocuments = linkedMapOf<String, String>()
            for (path in documentPaths) {
                loadedDocuments[path] = Res.readBytes("files/api/$path").decodeToString()
            }
            documents = loadedDocuments
            loadError = null
        } catch (error: Throwable) {
            loadError = error.message ?: "未知资源错误"
        }
    }

    DocsCard {
        SectionLabel(title = "API 文档与属性表")
        when {
            loadError != null -> Text(
                text = "API 文档加载失败：$loadError",
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp
            )
            documents.isEmpty() -> Text(
                text = "正在加载 API 文档…",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
            else -> documents.forEach { (path, markdown) ->
                ApiDocumentSection(path = path, markdown = markdown)
            }
        }
    }
}

@Composable
private fun ApiDocumentSection(path: String, markdown: String) {
    val blocks = remember(markdown) { parseMarkdown(markdown) }
    val signatures = remember(blocks) { extractApiSignatures(blocks) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = path.substringAfterLast('/').removeSuffix(".md"),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        signatures.forEach { signature ->
            ApiSignatureTable(signature)
        }
        MarkdownContent(blocks)
    }
}

@Composable
private fun ApiSignatureTable(signature: ApiSignature) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "${signature.name} · ${signature.kind}",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
        DocumentationTable(
            headers = listOf("属性", "类型", "默认值 / 必填"),
            rows = signature.parameters.map { parameter ->
                listOf(parameter.name, parameter.type, parameter.defaultValue)
            },
            columnWidths = listOf(180.dp, 360.dp, 360.dp)
        )
    }
}

@Composable
private fun MarkdownContent(blocks: List<MarkdownBlock>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        blocks.forEach { block ->
            when (block) {
                is MarkdownBlock.Heading -> MarkdownHeading(block)
                is MarkdownBlock.Paragraph -> MarkdownText(block.text)
                is MarkdownBlock.Bullet -> Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MarkdownText("•")
                    MarkdownText(block.text, modifier = Modifier.weight(1f))
                }
                is MarkdownBlock.Quote -> Text(
                    text = block.text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = DocsBorder)
                        .padding(12.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
                is MarkdownBlock.Code -> Text(
                    text = block.content,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .background(DocsCodeBackground)
                        .padding(14.dp),
                    color = DocsCodeText,
                    fontSize = 12.sp,
                    lineHeight = 19.sp
                )
                is MarkdownBlock.Table -> DocumentationTable(
                    headers = block.headers,
                    rows = block.rows,
                    columnWidths = List(block.headers.size) { 220.dp }
                )
            }
        }
    }
}

@Composable
private fun MarkdownHeading(block: MarkdownBlock.Heading) {
    val fontSize = when (block.level) {
        1 -> 24.sp
        2 -> 20.sp
        3 -> 17.sp
        else -> 15.sp
    }
    val lineHeight = when (block.level) {
        1 -> 31.sp
        2 -> 27.sp
        3 -> 23.sp
        else -> 21.sp
    }
    Text(
        text = block.text,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun MarkdownText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 14.sp,
        lineHeight = 21.sp
    )
}

@Composable
internal fun DocumentationTable(
    headers: List<String>,
    rows: List<List<String>>,
    columnWidths: List<Dp>
) {
    val normalizedWidths = headers.indices.map { index -> columnWidths.getOrElse(index) { 220.dp } }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .border(width = 1.dp, color = DocsBorder)
    ) {
        DocumentationTableRow(
            cells = headers,
            columnWidths = normalizedWidths,
            header = true
        )
        rows.forEach { row ->
            DocumentationTableRow(
                cells = headers.indices.map { index -> row.getOrElse(index) { "" } },
                columnWidths = normalizedWidths,
                header = false
            )
        }
    }
}

@Composable
private fun DocumentationTableRow(
    cells: List<String>,
    columnWidths: List<Dp>,
    header: Boolean
) {
    Row {
        cells.forEachIndexed { index, cell ->
            Text(
                text = cell,
                modifier = Modifier
                    .width(columnWidths[index])
                    .background(
                        if (header) MaterialTheme.colorScheme.surfaceVariant
                        else MaterialTheme.colorScheme.surface
                    )
                    .border(width = 1.dp, color = DocsBorder)
                    .padding(horizontal = 10.dp, vertical = 9.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontWeight = if (header) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}
