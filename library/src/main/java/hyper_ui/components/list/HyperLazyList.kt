package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

internal val LazyListCornerRadius = HyperStyleDefaults.LargeCornerRadius

@Composable
fun <T> HyperLazyList(
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    itemContent: @Composable (item: T) -> Unit
) {
    val hasVisibleBackground = HyperColors.elevatedContainer.alpha > 0f
    LazyColumn(
        modifier = modifier.clip(RoundedCornerShape(LazyListCornerRadius)),
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement
    ) {
        itemsIndexed(
            items = items,
            key = key?.let { itemKey ->
                { _: Int, item: T -> itemKey(item) }
            }
        ) { index, item ->
            val isFirst = index == 0
            val isLast = index == items.lastIndex
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(listItemShape(isFirst, isLast))
                    .background(HyperColors.elevatedContainer)
                    .then(if (hasVisibleBackground) Modifier.background(HyperColors.glassHighlightBrush) else Modifier)
            ) {
                itemContent(item)
            }
        }
    }
}

internal fun listItemShape(
    isFirst: Boolean,
    isLast: Boolean
) = RoundedCornerShape(
    topStart = if (isFirst) LazyListCornerRadius else 0.dp,
    topEnd = if (isFirst) LazyListCornerRadius else 0.dp,
    bottomEnd = if (isLast) LazyListCornerRadius else 0.dp,
    bottomStart = if (isLast) LazyListCornerRadius else 0.dp
)
