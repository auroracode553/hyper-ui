/** 文件职责：提供等宽分段控制器及其选中、禁用视觉状态。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.width
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable
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
data class HyperSegmentedControlColors(
    val containerColor: Color,
    val selectedItemColor: Color,
    val unselectedItemColor: Color,
    val selectedContentColor: Color,
    val unselectedContentColor: Color,
    val disabledContentColor: Color
)

class HyperSegmentOptionScope internal constructor(
    val selected: Boolean,
    val enabled: Boolean
)

/**
 * 等宽分段控制器。
 *
 * 组件仅渲染分段、状态与点击边界；选中项及业务内容由调用方持有。
 */
@Composable
fun <T> HyperSegmentedControl(
    options: List<T>,
    selectedValue: T,
    onChanged: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    itemEnabled: (T) -> Boolean = { true },
    equalWidth: Boolean = true,
    shape: Shape = HyperSegmentedControlDefaults.Shape,
    itemShape: Shape = HyperSegmentedControlDefaults.ItemShape,
    colors: HyperSegmentedControlColors = HyperSegmentedControlDefaults.colors(),
    containerPadding: PaddingValues = HyperSegmentedControlDefaults.ContainerPadding,
    itemContentPadding: PaddingValues = HyperSegmentedControlDefaults.ItemContentPadding,
    itemContent: @Composable HyperSegmentOptionScope.(item: T) -> Unit
) {
    val curve = HyperMotionSpec.SelectionCurve
    val selectedIndex = options.indexOf(selectedValue)
    val visualIndex = if (LocalLayoutDirection.current == LayoutDirection.Rtl)
        options.lastIndex - selectedIndex else selectedIndex
    val selectionPosition by animateFloatAsState(visualIndex.toFloat(), tween(HyperMotionSpec.SelectionDuration, easing = curve),
        label = "HyperSegmentedPosition")
    BoxWithConstraints(
        modifier = modifier
            .then(if (equalWidth) Modifier.fillMaxWidth() else Modifier)
            .height(HyperSegmentedControlDefaults.Height)
            .hyperGlassSurface(shape, hyperGlassSurfaceVisuals(colors.containerColor,
                HyperSegmentedControlDefaults.ContainerElevation, blur = hyperGlass.blur))
            .padding(containerPadding)
    ) {
        if (equalWidth && options.isNotEmpty() && selectedIndex >= 0) {
            val cellWidth = maxWidth / options.size
            Box(Modifier.absoluteOffset(x = cellWidth * selectionPosition)
                .width(cellWidth).fillMaxHeight().background(colors.selectedItemColor, itemShape))
        }
        Row(Modifier.fillMaxHeight().then(if (equalWidth) Modifier.fillMaxWidth() else Modifier)) {
            options.forEach { item ->
                val selected = item == selectedValue
                val actualEnabled = enabled && itemEnabled(item)
                val itemBackground by animateColorAsState(
                    if (!equalWidth && selected) colors.selectedItemColor else colors.unselectedItemColor,
                    tween(HyperMotionSpec.StateDuration, easing = curve), label = "HyperSegmentedItemBackground")
                Box(Modifier.then(if (equalWidth) Modifier.weight(1f) else Modifier).fillMaxHeight()
                    .hyperNoRippleClickable(enabled = actualEnabled, role = Role.Tab, onClick = { onChanged(item) })
                    .semantics { this.selected = selected }
                    .background(itemBackground, itemShape).padding(itemContentPadding),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.runtime.CompositionLocalProvider(
                        LocalHyperContentColor provides when {
                            selected -> colors.selectedContentColor
                            actualEnabled -> colors.unselectedContentColor
                            else -> colors.disabledContentColor
                        },
                        LocalHyperTextStyle provides HyperTheme.typography.bodyMedium.copy(fontSize = 13.sp,
                            lineHeight = 18.2.sp, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium),
                        LocalHyperIconSize provides 16.dp
                    ) { HyperSegmentOptionScope(selected, actualEnabled).itemContent(item) }
                }
            }
        }
    }
}

object HyperSegmentedControlDefaults {
    val Height = 40.dp
    val ContainerElevation = 0.dp
    val ContainerPadding = PaddingValues(4.dp)
    val ItemContentPadding = PaddingValues(horizontal = 12.dp)
    val ItemContentSpacing = 8.dp
    val Shape: Shape = RoundedCornerShape(18.dp)
    val ItemShape: Shape = RoundedCornerShape(16.dp)

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        selectedItemColor: Color = Color.Unspecified,
        unselectedItemColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        unselectedContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperSegmentedControlColors {
        val resolvedContainerColor = resolveHyperContainerColor(
            containerColor,
            hyperGlass.surfaceSubtle
        )
        val resolvedSelectedItemColor = resolveHyperContainerColor(
            selectedItemColor,
            hyperGlass.selection
        )

        return HyperSegmentedControlColors(
            containerColor = resolvedContainerColor,
            selectedItemColor = resolvedSelectedItemColor,
            unselectedItemColor = resolveHyperContainerColor(unselectedItemColor, Color.Transparent),
            selectedContentColor = resolveHyperContainerColor(
                selectedContentColor,
                HyperColors.primaryText
            ),
            unselectedContentColor = resolveHyperContainerColor(unselectedContentColor, HyperColors.primaryText),
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                HyperColors.secondaryText
            )
        )
    }
}
