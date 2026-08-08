package hyper_ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable

@Immutable
data class HyperChipColors(
    val selectedContainerColor: Color,
    val unselectedContainerColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

class HyperChipScope internal constructor(
    val selected: Boolean,
    val enabled: Boolean
)

@Composable
fun HyperChip(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = HyperChipDefaults.Shape,
    minHeight: androidx.compose.ui.unit.Dp = HyperChipDefaults.MinHeight,
    contentPadding: PaddingValues = HyperChipDefaults.ContentPadding,
    colors: HyperChipColors = HyperChipDefaults.colors(),
    role: Role = Role.Tab,
    content: @Composable HyperChipScope.() -> Unit
) {
    val targetContainerColor = when {
        !enabled -> colors.disabledContainerColor
        selected -> colors.selectedContainerColor
        else -> colors.unselectedContainerColor
    }
    val targetContentColor = when {
        !enabled -> colors.disabledContentColor
        selected -> colors.selectedContentColor
        else -> colors.unselectedContentColor
    }
    val containerColor by animateColorAsState(
        targetValue = targetContainerColor,
        animationSpec = spring(
            stiffness = Spring.StiffnessMedium,
            dampingRatio = Spring.DampingRatioNoBouncy
        ),
        label = "hyperChipContainer"
    )
    val contentColor by animateColorAsState(
        targetValue = targetContentColor,
        animationSpec = spring(
            stiffness = Spring.StiffnessMedium,
            dampingRatio = Spring.DampingRatioNoBouncy
        ),
        label = "hyperChipContent"
    )
    val scope = HyperChipScope(selected = selected, enabled = enabled)

    Row(
        modifier = modifier
            .heightIn(min = minHeight)
            .hyperGlassSurface(
                containerColor = containerColor,
                shape = shape
            )
            .hyperNoRippleClickable(
                enabled = enabled,
                role = role,
                onClick = onClick
            )
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(HyperChipDefaults.ContentGap)
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            scope.content()
        }
    }
}

@Composable
fun <T> HyperChipRow(
    items: List<T>,
    selectedItem: T?,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = HyperChipDefaults.RowPadding,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperChipDefaults.RowGap),
    chipContent: @Composable HyperChipScope.(item: T) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement
    ) {
        items(items) { item ->
            HyperChip(
                selected = item == selectedItem,
                onClick = { onSelected(item) }
            ) {
                chipContent(item)
            }
        }
    }
}

object HyperChipDefaults {
    val MinHeight = 32.dp
    val ContentGap = 6.dp
    val RowGap = 8.dp
    val ContentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
    val RowPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    val Shape: Shape = RoundedCornerShape(percent = 50)

    @Composable
    fun colors(
        selectedContainerColor: Color = Color.Unspecified,
        unselectedContainerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperChipColors {
        val resolvedUnselectedContentColor = resolveHyperContainerColor(
            unselectedContentColor,
            HyperColors.primaryText
        )

        return HyperChipColors(
            selectedContainerColor = resolveHyperContainerColor(selectedContainerColor, HyperColors.accent),
            unselectedContainerColor = resolveHyperContainerColor(
                unselectedContainerColor,
                HyperColors.elevatedContainer
            ),
            selectedContentColor = resolveHyperContainerColor(
                selectedContentColor,
                rgba(255, 255, 255, 1f)
            ),
            unselectedContentColor = resolvedUnselectedContentColor,
            disabledContainerColor = resolveHyperContainerColor(disabledContainerColor, HyperColors.disabledContainer),
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                resolvedUnselectedContentColor.copy(alpha = HyperStyleDefaults.DisabledAlpha)
            )
        )
    }
}
