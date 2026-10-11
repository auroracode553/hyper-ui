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
    size: String = "default",
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
    requireHyperComponentSize(size)
    val resolvedStyle = style ?: when (size) {
        "large" -> HyperTheme.typography.bodyLarge.copy(fontSize = 18.sp, lineHeight = 25.2.sp, fontWeight = FontWeight.Bold)
        "small" -> HyperTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 16.8.sp, color = HyperColors.secondaryText)
        else -> LocalHyperTextStyle.current
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
