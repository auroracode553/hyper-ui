/** 文件职责：提供等宽分段控制器及其选中、禁用视觉状态。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable

@Immutable
data class HyperSegmentedColors(
    val containerColor: Color,
    val selectedItemColor: Color,
    val unselectedItemColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledItemColor: Color,
    val disabledContentColor: Color,
    val selectedBorderColor: Color
)

class HyperSegmentedItemScope internal constructor(
    val selected: Boolean,
    val enabled: Boolean
)

/**
 * 等宽分段控制器。
 *
 * 组件仅渲染分段、状态与点击边界；选中项及业务内容由调用方持有。
 */
@Composable
fun <T> HyperSegmented(
    items: List<T>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemEnabled: (T) -> Boolean = { true },
    shape: Shape = HyperSegmentedDefaults.Shape,
    itemShape: Shape = HyperSegmentedDefaults.ItemShape,
    colors: HyperSegmentedColors = HyperSegmentedDefaults.colors(),
    containerPadding: PaddingValues = HyperSegmentedDefaults.ContainerPadding,
    itemContentPadding: PaddingValues = HyperSegmentedDefaults.ItemContentPadding,
    itemContent: @Composable HyperSegmentedItemScope.(item: T) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .hyperSolidSurface(
                containerColor = colors.containerColor,
                shape = shape
            )
            .padding(containerPadding)
    ) {
        items.forEach { item ->
            val selected = item == selectedItem
            val actualEnabled = enabled && itemEnabled(item)
            val itemColor = when {
                !actualEnabled -> colors.disabledItemColor
                selected -> colors.selectedItemColor
                else -> colors.unselectedItemColor
            }
            val contentColor = when {
                !actualEnabled -> colors.disabledContentColor
                selected -> colors.selectedContentColor
                else -> colors.unselectedContentColor
            }
            val elevationModifier = if (selected && actualEnabled) {
                Modifier.shadow(
                    elevation = HyperSegmentedDefaults.SelectedElevation,
                    shape = itemShape,
                    clip = false
                )
            } else {
                Modifier
            }
            val borderModifier = if (selected && actualEnabled) {
                Modifier.border(
                    width = HyperSegmentedDefaults.SelectedBorderWidth,
                    color = colors.selectedBorderColor,
                    shape = itemShape
                )
            } else {
                Modifier
            }
            val scope = HyperSegmentedItemScope(
                selected = selected,
                enabled = actualEnabled
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = HyperSegmentedDefaults.MinHeight)
                    .then(elevationModifier)
                    .background(itemColor, itemShape)
                    .then(borderModifier)
                    .semantics { this.selected = selected }
                    .hyperNoRippleClickable(
                        enabled = actualEnabled,
                        role = Role.Tab,
                        onClick = { onSelected(item) }
                    )
                    .padding(itemContentPadding),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    scope.itemContent(item)
                }
            }
        }
    }
}

object HyperSegmentedDefaults {
    val MinHeight = 40.dp
    val SelectedElevation = 2.dp
    val SelectedBorderWidth = 1.dp
    val ContainerPadding = PaddingValues(3.dp)
    val ItemContentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    val Shape: Shape = RoundedCornerShape(5.dp)
    val ItemShape: Shape = RoundedCornerShape(4.dp)

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        selectedItemColor: Color = Color.Unspecified,
        unselectedItemColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledItemColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        selectedBorderColor: Color = Color.Unspecified
    ): HyperSegmentedColors {
        val resolvedContainerColor = resolveHyperOpaqueColor(
            color = containerColor,
            fallbackColor = HyperColors.fieldContainer,
            backgroundColor = HyperColors.pageBackground
        )
        val resolvedSelectedItemColor = resolveHyperOpaqueColor(
            color = selectedItemColor,
            fallbackColor = HyperColors.cardContainer,
            backgroundColor = resolvedContainerColor
        )

        return HyperSegmentedColors(
            containerColor = resolvedContainerColor,
            selectedItemColor = resolvedSelectedItemColor,
            unselectedItemColor = resolveHyperContainerColor(unselectedItemColor, Color.Transparent),
            selectedContentColor = resolveHyperContainerColor(selectedContentColor, HyperColors.primaryText),
            unselectedContentColor = resolveHyperContainerColor(unselectedContentColor, HyperColors.secondaryText),
            disabledItemColor = resolveHyperContainerColor(disabledItemColor, HyperColors.disabledContainer),
            disabledContentColor = resolveHyperContainerColor(disabledContentColor, HyperColors.disabledText),
            selectedBorderColor = resolveHyperOpaqueColor(
                color = selectedBorderColor,
                fallbackColor = HyperColors.fieldBorder,
                backgroundColor = resolvedSelectedItemColor
            )
        )
    }
}
