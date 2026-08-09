/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/components/list/HyperList 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class HyperListColors(
    val containerColor: Color
)

@Composable
fun <T> HyperList(
    items: List<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = HyperListDefaults.border(),
    colors: HyperListColors = HyperListDefaults.colors(),
    itemContent: @Composable (item: T) -> Unit
) {
    val containerColor = colors.containerColor
    val hasVisibleBackground = containerColor.alpha > 0f
    val shape = HyperListDefaults.Shape
    Column(
        modifier = modifier
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .clip(shape)
            .verticalScroll(rememberScrollState())
            .padding(contentPadding),
        verticalArrangement = verticalArrangement
    ) {
        items.forEachIndexed { index, item ->
            val isFirst = index == 0
            val isLast = index == items.lastIndex
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(listItemShape(isFirst, isLast))
                    .background(containerColor)
                    .then(if (hasVisibleBackground) Modifier.background(HyperColors.glassHighlightBrush) else Modifier)
            ) {
                CompositionLocalProvider(
                    LocalHyperListItemDividerSuppressed provides isLast
                ) {
                    itemContent(item)
                }
            }
        }
    }
}

@Composable
fun HyperList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    border: BorderStroke? = HyperListDefaults.border(),
    colors: HyperListColors = HyperListDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = HyperListDefaults.Shape
    CompositionLocalProvider(LocalHyperListItemDividerSuppressed provides false) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .hyperGlassSurface(
                    containerColor = colors.containerColor,
                    shape = shape,
                    border = border
                )
                .padding(contentPadding),
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}

object HyperListDefaults {
    val Shape: Shape = RoundedCornerShape(LazyListCornerRadius)

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperListColors = HyperListColors(
        containerColor = resolveHyperContainerColor(
            containerColor = containerColor,
            fallbackColor = HyperColors.elevatedContainer
        )
    )

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke = hyperPanelBorder(color)
}
