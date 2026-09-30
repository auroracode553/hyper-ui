/** 文件职责：提供 Preview 全局主题、方向、背景和材质切换。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import hyper_ui.HyperColorPickerDefaults
import hyper_ui.HyperPanel
import hyper_ui.HyperPanelDefaults
import hyper_ui.HyperText
import hyper_ui.HyperTheme
import hyper_ui.HyperThemeConfig
import hyper_ui.docs.ThemeColorController
import hyper_ui.docs.theme.DocsMaterial
import hyper_ui.docs.theme.LocalDocsColorScheme
import hyper_ui.docs.theme.docsGlassTokens

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
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DocsMaterial.values().forEach { material ->
                MaterialPreview(
                    material = material,
                    selected = material == controller.material,
                    controller = controller
                )
            }
        }
    }
}

@Composable
private fun PreviewToggle(
    label: String,
    onClick: () -> Unit
) {
    HyperButton(
        onClick = onClick,
        type = "tonal",
        size = "small",
        contentPadding = HyperButtonDefaults.contentPadding("small")
    ) { HyperText(label, maxLines = 1) }
}

@Composable
private fun MaterialPreview(
    material: DocsMaterial,
    selected: Boolean,
    controller: ThemeColorController
) {
    val label = when (material) {
        DocsMaterial.Solid -> "实色"
        DocsMaterial.Soft -> "柔和"
        DocsMaterial.Clear -> "清透"
    }
    HyperThemeConfig(
        themeColor = controller.color,
        darkTheme = controller.darkTheme,
        glass = docsGlassTokens(controller.darkTheme, material)
    ) {
        HyperPanel(
            modifier = Modifier
                .width(78.dp)
                .height(46.dp)
                .clickable { controller.updateMaterial(material) },
            contentModifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            border = if (selected) HyperPanelDefaults.border(HyperTheme.colors.themeColor) else HyperPanelDefaults.border()
        ) {
            HyperText(label, fontSize = 12.sp, maxLines = 1)
        }
    }
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
            selectedId = HyperColorPickerDefaults.presetOptions
                .minByOrNull {
                    val rDiff = it.color.red - currentColor.red
                    val gDiff = it.color.green - currentColor.green
                    val bDiff = it.color.blue - currentColor.blue
                    rDiff * rDiff + gDiff * gDiff + bDiff * bDiff
                }?.id ?: "",
            onSelected = { option -> onColorChange(option.color) },
            colorSize = 28.dp,
            horizontalSpacing = 6.dp,
            verticalSpacing = 8.dp,
            labelTopSpacing = 3.dp
        )
    }
}
