/** 文件职责：提供等宽分段控制器及其选中、禁用视觉状态。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Immutable
data class HyperSegmentedColors(
    val containerColor: Color,
    val selectedItemColor: Color,
    val unselectedItemColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledItemColor: Color,
    val disabledContentColor: Color
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
    equalWidth: Boolean = true,
    shape: Shape = HyperSegmentedDefaults.Shape,
    itemShape: Shape = HyperSegmentedDefaults.ItemShape,
    colors: HyperSegmentedColors = HyperSegmentedDefaults.colors(),
    containerPadding: PaddingValues = HyperSegmentedDefaults.ContainerPadding,
    itemContentPadding: PaddingValues = HyperSegmentedDefaults.ItemContentPadding,
    itemContent: @Composable HyperSegmentedItemScope.(item: T) -> Unit
) {
    Row(
        modifier = modifier
            .then(if (equalWidth) Modifier.fillMaxWidth() else Modifier)
            .height(HyperSegmentedDefaults.Height)
            .hyperGlassSurface(
                shape = shape,
                visuals = hyperGlassSurfaceVisuals(
                    containerColor = colors.containerColor,
                    elevation = HyperSegmentedDefaults.ContainerElevation,
                    blur = 14.dp,

                )
            )
            .padding(containerPadding)
    ) {
        items.forEach { item ->
            val selected = item == selectedItem
            val actualEnabled = enabled && itemEnabled(item)
            val scope = HyperSegmentedItemScope(
                selected = selected,
                enabled = actualEnabled
            )

            HyperButton(
                onClick = { onSelected(item) },
                modifier = Modifier
                    .then(if (equalWidth) Modifier.weight(1f) else Modifier)
                    .fillMaxHeight()
                    .semantics { this.selected = selected },
                enabled = actualEnabled,
                type = "ghost",
                size = "small",
                colors = HyperButtonColors(
                    containerColor = if (selected) {
                        colors.selectedItemColor
                    } else {
                        colors.unselectedItemColor
                    },
                    contentColor = if (selected) {
                        colors.selectedContentColor
                    } else {
                        colors.unselectedContentColor
                    },
                    disabledContainerColor = colors.disabledItemColor,
                    disabledContentColor = colors.disabledContentColor
                ),
                border = null,
                shape = itemShape,
                contentPadding = itemContentPadding,
                role = Role.Tab,
                horizontalArrangement = Arrangement.spacedBy(
                    HyperSegmentedDefaults.ItemContentSpacing,
                    Alignment.CenterHorizontally
                )
            ) {
                scope.itemContent(item)
            }
        }
    }
}

object HyperSegmentedDefaults {
    val Height = 40.dp
    val ContainerElevation = 0.dp
    val ContainerPadding = PaddingValues(4.dp)
    val ItemContentPadding = PaddingValues(horizontal = 12.dp)
    val ItemContentSpacing = 6.dp
    val Shape: Shape = RoundedCornerShape(18.dp)
    val ItemShape: Shape = RoundedCornerShape(16.dp)

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        selectedItemColor: Color = Color.Unspecified,
        unselectedItemColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledItemColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperSegmentedColors {
        val resolvedContainerColor = resolveHyperContainerColor(
            containerColor,
            hyperGlass.surfaceSubtle
        )
        val resolvedSelectedItemColor = resolveHyperContainerColor(
            selectedItemColor,
            HyperColors.softContainer
        )

        return HyperSegmentedColors(
            containerColor = resolvedContainerColor,
            selectedItemColor = resolvedSelectedItemColor,
            unselectedItemColor = resolveHyperContainerColor(unselectedItemColor, Color.Transparent),
            selectedContentColor = resolveHyperContainerColor(
                selectedContentColor,
                HyperColors.primaryText
            ),
            unselectedContentColor = resolveHyperContainerColor(unselectedContentColor, HyperColors.primaryText),
            disabledItemColor = resolveHyperContainerColor(
                disabledItemColor,
                HyperColors.softContainer
            ),
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                HyperColors.secondaryText
            )
        )
    }
}
