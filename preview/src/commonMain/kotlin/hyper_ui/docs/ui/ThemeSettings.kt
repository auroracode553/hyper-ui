/** 文件职责：提供 Preview 全局主题、方向与背景。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperButton
import hyper_ui.HyperButtonDefaults
import hyper_ui.HyperColorPicker
import hyper_ui.HyperText
import hyper_ui.docs.ThemeColorController
import hyper_ui.docs.theme.LocalDocsColorScheme

@Composable
internal fun DocsThemeSettings(controller: ThemeColorController) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        HyperText(
            text = "主题设置",
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 16.sp
        )
        DocsThemeControls(controller)
        ThemeColorPicker(
            currentColor = controller.color,
            onColorChange = controller::update
        )
    }
}

@Composable
internal fun DocsThemeControls(
    controller: ThemeColorController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            PreviewToggle(
                label = if (controller.darkTheme) "深色" else "浅色",
                onClick = controller::toggleDarkTheme
            )
            PreviewToggle(
                label = if (controller.rtl) "RTL" else "LTR",
                onClick = controller::toggleLayoutDirection
            )
            PreviewToggle(
                label = if (controller.patternedBackground) "纹理背景" else "柔色背景",
                onClick = controller::toggleBackground
            )
            PreviewToggle(
                label = if (controller.customAccent) "紫色" else "蓝色",
                onClick = controller::toggleAccent
            )
        }

    }
}

@Composable
private fun PreviewToggle(
    label: String,
    onClick: () -> Unit
) {
    HyperButton(type = null,
        onPressed = onClick,
        contentPadding = HyperButtonDefaults.contentPadding()
    ) { HyperText(label, maxLines = 1) }
}

@Composable
private fun ThemeColorPicker(
    currentColor: Color,
    onColorChange: (Color) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HyperText(
            text = "主题色",
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 16.sp
        )
        HyperColorPicker(
            value = currentColor,
            onChanged = onColorChange
        )
    }
}
