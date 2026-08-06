package hyper_ui.docs.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import hyper_ui.docs.generated.resources.Res
import hyper_ui.docs.generated.resources.noto_sans_sc_wght
import org.jetbrains.compose.resources.Font

@Composable
internal fun docsTypography(): Typography {
    val base = Typography()
    val fontFamily = docsFontFamily()
    return base.copy(
        displayLarge = base.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = base.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = base.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = base.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = base.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = base.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = base.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = base.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = base.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = base.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = base.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = base.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = base.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = base.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = base.labelSmall.copy(fontFamily = fontFamily)
    )
}

@Composable
private fun docsFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.noto_sans_sc_wght, weight = FontWeight.Normal),
        Font(Res.font.noto_sans_sc_wght, weight = FontWeight.Medium),
        Font(Res.font.noto_sans_sc_wght, weight = FontWeight.Bold)
    )
}
