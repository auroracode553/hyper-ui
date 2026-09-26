/** 文件职责：在 hyper_ui 中负责承载 preview/src/commonMain/kotlin/hyper_ui/docs/theme/DocsTypography 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui.docs.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import hyper_ui.docs.generated.resources.Res
import hyper_ui.docs.generated.resources.noto_sans_sc_wght
import org.jetbrains.compose.resources.Font

/**
 * 文档默认文本样式：为未显式指定字体的文档文本统一装配 Noto Sans SC 字体族，
 * 替代原 material3 Typography 体系。
 */
@Composable
internal fun docsTextStyle(): TextStyle {
    return TextStyle(fontFamily = docsFontFamily())
}

@Composable
private fun docsFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.noto_sans_sc_wght, weight = FontWeight.Normal),
        Font(Res.font.noto_sans_sc_wght, weight = FontWeight.Medium),
        Font(Res.font.noto_sans_sc_wght, weight = FontWeight.Bold)
    )
}
