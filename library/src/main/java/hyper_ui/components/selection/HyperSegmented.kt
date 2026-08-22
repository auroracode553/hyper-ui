/** 文件职责：提供等宽分段控制器及其选中、禁用视觉状态。 */
package hyper_ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
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
            .hyperGlassSurface(
                shape = shape,
                visuals = hyperGlassSurfaceVisuals(
                    containerColor = colors.containerColor,
                    elevation = HyperSegmentedDefaults.ContainerElevation,
                    topLightAlpha = if (HyperColors.isLight) 0.24f else 0.10f,
                    bottomShadeAlpha = if (HyperColors.isLight) 0.035f else 0.10f,
                    shadowAlpha = if (HyperColors.isLight) 0.08f else 0.18f
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
                    .weight(1f)
                    .semantics { this.selected = selected },
                enabled = actualEnabled,
                tone = if (selected) HyperButtonTone.Primary else HyperButtonTone.Plain,
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
                role = Role.Tab
            ) {
                scope.itemContent(item)
            }
        }
    }
}

object HyperSegmentedDefaults {
    val ContainerElevation = 1.dp
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
        disabledContentColor: Color = Color.Unspecified
    ): HyperSegmentedColors {
        val resolvedContainerColor = resolveHyperContainerColor(
            containerColor,
            Color(1f, 1f, 1f, if (HyperColors.isLight) 0.48f else 0.14f)
        )
        val resolvedSelectedItemColor = resolveHyperContainerColor(
            selectedItemColor,
            HyperColors.accent
        )

        return HyperSegmentedColors(
            containerColor = resolvedContainerColor,
            selectedItemColor = resolvedSelectedItemColor,
            unselectedItemColor = resolveHyperContainerColor(unselectedItemColor, HyperColors.cardContainer),
            selectedContentColor = resolveHyperContainerColor(
                selectedContentColor,
                Color(1f, 1f, 1f, 1f)
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
