/** 文件职责：在 hyper_ui 中负责提供 library/src/main/java/hyper_ui/components/list/HyperListItem 可复用界面组件及交互封装。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable

@Immutable
data class HyperListItemColors(
    val contentColor: Color,
    val supportingColor: Color,
    val disabledContentColor: Color,
    val dividerColor: Color
)

internal val LocalHyperListItemDividerSuppressed = staticCompositionLocalOf { false }
internal val LocalHyperListItemContainerColor = staticCompositionLocalOf { Color.Unspecified }

/**
 * 列表项组件。
 *
 * modifier 控制列表项外壳，contentModifier 控制列表项内部内容布局。
 */
@Composable
fun HyperListItem(
    headlineContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperListItemDefaults.ContentPadding),
    dividerModifier: Modifier = Modifier.padding(start = HyperListItemDefaults.DividerInset),
    enabled: Boolean = true,
    dividerVisible: Boolean = false,
    colors: HyperListItemColors = HyperListItemDefaults.colors(),
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
) {
    val containerColor = currentHyperListItemContainerColor()
    val requestedContentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val requestedSupportingColor = if (enabled) colors.supportingColor else colors.disabledContentColor
    val contentColor = resolveHyperOpaqueColor(
        color = requestedContentColor,
        fallbackColor = if (enabled) HyperColors.primaryText else HyperColors.secondaryText,
        backgroundColor = containerColor
    )
    val supportingColor = resolveHyperOpaqueColor(
        color = requestedSupportingColor,
        fallbackColor = HyperColors.secondaryText,
        backgroundColor = containerColor
    )
    val dividerColor = resolveHyperOpaqueColor(
        color = colors.dividerColor,
        fallbackColor = HyperColors.divider,
        backgroundColor = containerColor
    )
    val shouldShowDivider = dividerVisible && !LocalHyperListItemDividerSuppressed.current
    val clickModifier = if (onClick != null) {
        Modifier.hyperNoRippleClickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = HyperListItemDefaults.MinHeight)
                .then(clickModifier)
                .then(contentModifier),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingContent != null) {
                CompositionLocalProvider(
                    LocalContentColor provides contentColor,
                    LocalTextStyle provides HyperListItemDefaults.LeadingTextStyle
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        content = leadingContent
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = if (leadingContent == null) 0.dp else HyperListItemDefaults.ContentGap,
                        end = if (trailingContent == null) 0.dp else HyperListItemDefaults.ContentGap
                    ),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                // 为裸 Text(...) 提供稳定的列表层级，调用方显式 style 仍可覆盖。
                CompositionLocalProvider(
                    LocalContentColor provides contentColor,
                    LocalTextStyle provides HyperListItemDefaults.HeadlineTextStyle
                ) {
                    headlineContent()
                }
                if (supportingContent != null) {
                    CompositionLocalProvider(
                        LocalContentColor provides supportingColor,
                        LocalTextStyle provides HyperListItemDefaults.SupportingTextStyle
                    ) {
                        supportingContent()
                    }
                }
            }

            if (trailingContent != null) {
                CompositionLocalProvider(
                    LocalContentColor provides contentColor,
                    LocalTextStyle provides HyperListItemDefaults.TrailingTextStyle
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        content = trailingContent
                    )
                }
            }
        }

        if (shouldShowDivider) {
            Box(
                modifier = dividerModifier
                    .fillMaxWidth()
                    .height(HyperListItemDefaults.DividerHeight)
                    .background(dividerColor)
            )
        }
    }
}

object HyperListItemDefaults {
    val MinHeight = 68.dp
    val ContentGap = 14.dp
    val ContentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    val DividerInset = 20.dp
    val DividerHeight = 1.dp

    val LeadingTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 15.sp,
            lineHeight = 20.sp
        )

    val HeadlineTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            lineHeight = 22.sp
        )

    val SupportingTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

    val TrailingTextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.labelMedium.copy(
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

    @Composable
    fun colors(
        contentColor: Color = Color.Unspecified,
        supportingColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperListItemColors {
        val containerColor = currentHyperListItemContainerColor()
        val resolvedContentColor = resolveHyperOpaqueColor(
            color = contentColor,
            fallbackColor = HyperColors.primaryText,
            backgroundColor = containerColor
        )

        return HyperListItemColors(
            contentColor = resolvedContentColor,
            supportingColor = resolveHyperOpaqueColor(
                color = supportingColor,
                fallbackColor = HyperColors.secondaryText,
                backgroundColor = containerColor
            ),
            disabledContentColor = resolveHyperOpaqueColor(
                color = disabledContentColor,
                fallbackColor = HyperColors.secondaryText,
                backgroundColor = containerColor
            ),
            dividerColor = resolveHyperOpaqueColor(
                color = dividerColor,
                fallbackColor = HyperColors.divider,
                backgroundColor = containerColor
            )
        )
    }
}

/** 返回父列表提供的实色背景；独立列表项默认按页面背景解析颜色。 */
@Composable
private fun currentHyperListItemContainerColor(): Color {
    val providedColor = LocalHyperListItemContainerColor.current
    return if (providedColor == Color.Unspecified) HyperColors.pageBackground else providedColor
}
