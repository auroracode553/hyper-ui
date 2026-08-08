package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable

@Immutable
data class HyperListItemColors(
    val contentColor: Color,
    val supportingColor: Color,
    val disabledContentColor: Color,
    val dividerColor: Color
)

@Composable
fun HyperListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minHeight: Dp = HyperListItemDefaults.MinHeight,
    contentPadding: PaddingValues = HyperListItemDefaults.ContentPadding,
    dividerVisible: Boolean = false,
    dividerInset: Dp = HyperListItemDefaults.DividerInset,
    colors: HyperListItemColors = HyperListItemDefaults.colors(),
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    headlineContent: @Composable ColumnScope.() -> Unit,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
) {
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val supportingColor = if (enabled) colors.supportingColor else colors.disabledContentColor
    val clickModifier = if (onClick != null) {
        Modifier.hyperNoRippleClickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .then(clickModifier)
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingContent != null) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
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
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    headlineContent()
                }
                if (supportingContent != null) {
                    CompositionLocalProvider(LocalContentColor provides supportingColor) {
                        supportingContent()
                    }
                }
            }

            if (trailingContent != null) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        content = trailingContent
                    )
                }
            }
        }

        if (dividerVisible) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dividerInset)
                    .height(HyperListItemDefaults.DividerHeight)
                    .background(colors.dividerColor)
            )
        }
    }
}

object HyperListItemDefaults {
    val MinHeight = 68.dp
    val ContentGap = 14.dp
    val ContentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    val DividerInset = 20.dp
    val DividerHeight = 1.dp

    @Composable
    fun colors(
        contentColor: Color = Color.Unspecified,
        supportingColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperListItemColors {
        val resolvedContentColor = resolveHyperContainerColor(contentColor, HyperColors.primaryText)

        return HyperListItemColors(
            contentColor = resolvedContentColor,
            supportingColor = resolveHyperContainerColor(supportingColor, HyperColors.secondaryText),
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                resolvedContentColor.copy(alpha = HyperStyleDefaults.DisabledAlpha)
            ),
            dividerColor = resolveHyperContainerColor(dividerColor, HyperColors.divider)
        )
    }
}
