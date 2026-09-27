/** 文件职责：在 hyper_ui 中提供核心文本组件 HyperText，基于 foundation BasicText，替代 基础文本组件。 */
package hyper_ui

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * 超UI 文本组件，签名与 基础文本组件 保持一致。
 * 默认颜色取 [LocalHyperContentColor]，默认样式取 [LocalHyperTextStyle]。
 */
@Composable
fun HyperText(
    text: String,
    modifier: Modifier = Modifier,
    type: String = "h4",
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle? = null
) {
    require(type in setOf("h1", "h2", "h3", "h4", "h5", "h6")) {
        "不支持的 HyperText type: $type"
    }
    val resolvedStyle = style ?: when (type) {
        "h1" -> HyperTheme.typography.headlineSmall.copy(fontSize = 32.sp, lineHeight = 40.sp, fontWeight = FontWeight.Bold)
        "h2" -> HyperTheme.typography.titleLarge
        "h3" -> HyperTheme.typography.titleMedium
        "h4" -> HyperTheme.typography.bodyLarge
        "h5" -> HyperTheme.typography.bodySmall.copy(fontSize = 13.sp, lineHeight = 18.sp)
        else -> HyperTheme.typography.bodySmall
    }
    val resolvedColor = when {
        color != Color.Unspecified -> color
        resolvedStyle.color != Color.Unspecified -> resolvedStyle.color
        else -> LocalHyperContentColor.current
    }
    // 显式组件字号样式仍继承主题字体，Wasm 等无系统中文字库的平台也能正确显示。
    val baseStyle = if (resolvedStyle.fontFamily == null) {
        resolvedStyle.copy(fontFamily = HyperTheme.typography.bodyLarge.fontFamily)
    } else resolvedStyle
    val mergedStyle = baseStyle.merge(
        TextStyle(
            color = resolvedColor,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign ?: TextAlign.Unspecified,
            lineHeight = lineHeight
        )
    )
    BasicText(
        text = text,
        modifier = modifier,
        style = mergedStyle,
        softWrap = softWrap,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout
    )
}
