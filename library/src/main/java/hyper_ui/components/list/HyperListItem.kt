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
import androidx.compose.ui.unit.Dp
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
    val minHeight = HyperListItemDefaults.minHeight(
        hasSupportingContent = supportingContent != null
    )
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

    Column(
        // modifier 描述完整列表项外壳，必须同时约束内容行和分割线。
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = minHeight)
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
                verticalArrangement = Arrangement.spacedBy(HyperListItemDefaults.TextGap)
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
            HyperListDivider(
                modifier = dividerModifier,
                color = dividerColor
            )
        }
    }
}

/** 列表组件共用同一分割线实现，保证普通列表与分段列表的尺寸一致。 */
@Composable
internal fun HyperListDivider(
    modifier: Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(HyperListItemDefaults.DividerHeight)
            .background(color)
    )
}

object HyperListItemDefaults {
    /** 单行保持紧凑；带说明的双行项增加纵向呼吸空间，避免标题与描述显得拥挤。 */
    val SingleLineMinHeight = 44.dp
    val SupportingMinHeight = 54.dp
    val ContentGap = 12.dp
    val TextGap = 3.dp
    val ContentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 4.dp)
    val DividerInset = 16.dp
    val DividerHeight = 1.dp

    /** 根据是否存在 supporting slot 返回稳定的默认行高，调用方无需判断内容密度。 */
    fun minHeight(hasSupportingContent: Boolean): Dp = if (hasSupportingContent) {
        SupportingMinHeight
    } else {
        SingleLineMinHeight
    }

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
            lineHeight = 17.sp
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
