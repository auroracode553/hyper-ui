/** 文件职责：提供使用 HyperPanel 承载的页面级空数据状态。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    val containerColor: Color,
    val iconContentColor: Color,
    val titleColor: Color,
    val descriptionColor: Color
)

/**
 * 页面级空数据状态。
 *
 * 组件只负责居中布局和空状态视觉；图标、操作及其业务行为由调用方通过 Slot 注入。
 * [modifier] 控制页面占位区域，[panelModifier] 控制内部卡片外壳。
 */
@Composable
fun HyperEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    panelModifier: Modifier = Modifier,
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
        HyperPanel(
            modifier = panelModifier.widthIn(max = HyperEmptyStateDefaults.PanelMaxWidth),
            contentModifier = Modifier.padding(HyperEmptyStateDefaults.ContentPadding),
            colors = HyperPanelDefaults.colors(
                containerColor = resolvedColors.containerColor
            ),
            verticalArrangement = Arrangement.spacedBy(HyperEmptyStateDefaults.ContentSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            iconContent?.let { icon ->
                CompositionLocalProvider(
                    LocalContentColor provides resolvedColors.iconContentColor
                ) {
                    icon()
                }
            }

            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                color = resolvedColors.titleColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            if (supportingText != null) {
                Text(
                    text = supportingText,
                    modifier = Modifier.fillMaxWidth(),
                    color = resolvedColors.descriptionColor,
                    style = MaterialTheme.typography.bodyMedium,
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
    val ContentPadding = PaddingValues(horizontal = 24.dp, vertical = 28.dp)
    val ContentSpacing = 12.dp
    val ActionSpacing = 8.dp

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        iconContentColor: Color = Color.Unspecified,
        titleColor: Color = Color.Unspecified,
        descriptionColor: Color = Color.Unspecified
    ): HyperEmptyStateColors = resolveHyperEmptyStateColors(
        HyperEmptyStateColors(
            containerColor = containerColor,
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
    val containerColor = resolveHyperOpaqueColor(
        color = colors.containerColor,
        fallbackColor = HyperColors.cardContainer,
        backgroundColor = HyperColors.pageBackground
    )
    return HyperEmptyStateColors(
        containerColor = containerColor,
        iconContentColor = resolveHyperOpaqueColor(
            color = colors.iconContentColor,
            fallbackColor = HyperColors.accent,
            backgroundColor = containerColor
        ),
        titleColor = resolveHyperOpaqueColor(
            color = colors.titleColor,
            fallbackColor = HyperColors.primaryText,
            backgroundColor = containerColor
        ),
        descriptionColor = resolveHyperOpaqueColor(
            color = colors.descriptionColor,
            fallbackColor = HyperColors.secondaryText,
            backgroundColor = containerColor
        )
    )
}
