/** 文件职责：提供 Flutter HyEmptyState 风格的图标玻璃块与居中文案。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Immutable
data class HyperEmptyStateColors(
    val iconContentColor: Color,
    val titleColor: Color,
    val descriptionColor: Color
)

/**
 * 页面级空数据状态。
 *
 * 组件只负责居中布局和空状态内容；图标有独立玻璃底面。
 * 图标、操作及其业务行为由调用方通过 Slot 注入。
 * [modifier] 控制页面占位区域，[contentModifier] 控制内部内容列。
 */
@Composable
fun HyperEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    contentModifier: Modifier = Modifier,
    colors: HyperEmptyStateColors = HyperEmptyStateDefaults.colors(),
    iconContent: (@Composable () -> Unit)? = null,
    actionContent: (@Composable RowScope.() -> Unit)? = null
) {
    val resolvedColors = resolveHyperEmptyStateColors(colors)
    val supportingText = description?.trim()?.takeIf(String::isNotEmpty)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = HyperEmptyStateDefaults.HorizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = contentModifier
                .widthIn(max = HyperEmptyStateDefaults.PanelMaxWidth),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            iconContent?.let { icon ->
                CompositionLocalProvider(
                    LocalHyperContentColor provides resolvedColors.iconContentColor
                ) {
                    Box(Modifier.size(56.dp).hyperFrostedSurface(
                        hyperGlass.surfaceSubtle, RoundedCornerShape(24.dp), elevation = 0.dp,
                        border = androidx.compose.foundation.BorderStroke(1.dp, HyperColors.fieldBorder), blur = 14.dp
                    ), contentAlignment = Alignment.Center) { icon() }
                }
            }

            HyperText(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                color = resolvedColors.titleColor,
                style = HyperTheme.typography.titleMedium.copy(fontSize = 16.sp, lineHeight = 20.sp),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            if (supportingText != null) {
                HyperText(
                    text = supportingText,
                    modifier = Modifier.fillMaxWidth(),
                    color = resolvedColors.descriptionColor,
                    style = HyperTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 18.85.sp),
                    textAlign = TextAlign.Center
                )
            }

            actionContent?.let { actions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = HyperEmptyStateDefaults.ActionSpacing,
                        alignment = Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    content = actions
                )
            }
        }
    }
}

object HyperEmptyStateDefaults {
    val HorizontalPadding = 24.dp
    val PanelMaxWidth = 520.dp
    val ActionSpacing = 8.dp

    @Composable
    fun colors(
        iconContentColor: Color = Color.Unspecified,
        titleColor: Color = Color.Unspecified,
        descriptionColor: Color = Color.Unspecified
    ): HyperEmptyStateColors = resolveHyperEmptyStateColors(
        HyperEmptyStateColors(
            iconContentColor = iconContentColor,
            titleColor = titleColor,
            descriptionColor = descriptionColor
        )
    )
}

@Composable
private fun resolveHyperEmptyStateColors(
    colors: HyperEmptyStateColors
): HyperEmptyStateColors {
    return HyperEmptyStateColors(
        iconContentColor = resolveHyperContainerColor(colors.iconContentColor, HyperColors.accent),
        titleColor = resolveHyperContainerColor(colors.titleColor, HyperColors.primaryText),
        descriptionColor = resolveHyperContainerColor(colors.descriptionColor, HyperColors.secondaryText)
    )
}
