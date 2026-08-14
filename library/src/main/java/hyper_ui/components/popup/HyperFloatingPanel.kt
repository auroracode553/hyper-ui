/** 文件职责：复用 HyperPopup 与 HyperDialog 的实色面板、滚动正文和操作区布局。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
internal fun HyperFloatingPanel(
    title: String?,
    modifier: Modifier,
    shape: Shape,
    containerColor: Color,
    border: BorderStroke?,
    horizontalAlignment: Alignment.Horizontal,
    verticalArrangement: Arrangement.Vertical,
    actionArrangement: Arrangement.Horizontal,
    showScrollIndicator: Boolean,
    actionContent: (@Composable RowScope.() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit
) {
    val resolvedTitle = title?.trim()?.takeIf(String::isNotEmpty)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .hyperSolidSurface(
                containerColor = containerColor,
                shape = shape,
                border = border
            )
            .padding(HyperFloatingPanelDefaults.ContentPadding),
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(HyperFloatingPanelDefaults.ContentSpacing)
    ) {
        CompositionLocalProvider(LocalContentColor provides HyperColors.primaryText) {
            if (resolvedTitle != null) {
                HyperFloatingPanelTitle(title = resolvedTitle)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            end = if (showScrollIndicator) {
                                HyperFloatingPanelDefaults.ScrollIndicatorContentPadding
                            } else {
                                0.dp
                            }
                        )
                        .verticalScroll(scrollState),
                    horizontalAlignment = horizontalAlignment,
                    verticalArrangement = verticalArrangement,
                    content = content
                )

                if (showScrollIndicator) {
                    HyperFloatingPanelScrollIndicator(
                        scrollState = scrollState,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }

            if (actionContent != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = actionArrangement,
                    verticalAlignment = Alignment.CenterVertically,
                    content = actionContent
                )
            }
        }
    }
}

@Composable
private fun HyperFloatingPanelTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.fillMaxWidth(),
        color = HyperColors.primaryText,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun HyperFloatingPanelScrollIndicator(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    if (scrollState.maxValue <= 0) return

    val density = LocalDensity.current
    val indicatorColor = resolveHyperOpaqueColor(
        color = HyperColors.divider,
        fallbackColor = HyperColors.divider,
        backgroundColor = HyperColors.cardContainer
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxHeight()
            .width(HyperFloatingPanelDefaults.ScrollIndicatorWidth)
    ) {
        if (!constraints.hasBoundedHeight) return@BoxWithConstraints

        val viewportPx = constraints.maxHeight.toFloat()
        if (viewportPx <= 0f) return@BoxWithConstraints

        val contentPx = viewportPx + scrollState.maxValue.toFloat()
        val minThumbPx = with(density) {
            HyperFloatingPanelDefaults.ScrollIndicatorMinHeight.toPx()
        }
        val thumbHeightPx = (viewportPx * viewportPx / contentPx).coerceAtLeast(minThumbPx)
        val travelPx = (viewportPx - thumbHeightPx).coerceAtLeast(0f)
        val scrollProgress = scrollState.value / scrollState.maxValue.toFloat()

        Box(
            modifier = Modifier
                .offset(y = with(density) { (travelPx * scrollProgress).toDp() })
                .width(HyperFloatingPanelDefaults.ScrollIndicatorWidth)
                .height(with(density) { thumbHeightPx.toDp() })
                .background(indicatorColor, RoundedCornerShape(percent = 50))
        )
    }
}

internal object HyperFloatingPanelDefaults {
    val Shape: Shape = RoundedCornerShape(20.dp)
    val ContentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp)
    val ContentSpacing = 16.dp
    val ActionSpacing = 12.dp
    const val ShowScrollIndicator = true
    val ScrollIndicatorWidth = 3.dp
    val ScrollIndicatorContentPadding = 10.dp
    val ScrollIndicatorMinHeight = 32.dp
}
