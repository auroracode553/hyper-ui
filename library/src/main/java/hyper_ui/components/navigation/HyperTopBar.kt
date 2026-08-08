package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Immutable
data class HyperTopBarColors(
    val containerColor: Color,
    val contentColor: Color
)

@Composable
fun HyperTopBar(
    modifier: Modifier = Modifier,
    minHeight: androidx.compose.ui.unit.Dp = HyperTopBarDefaults.MinHeight,
    contentPadding: PaddingValues = HyperTopBarDefaults.ContentPadding,
    colors: HyperTopBarColors = HyperTopBarDefaults.colors(),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperTopBarDefaults.ContentGap),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    navigationContent: (@Composable RowScope.() -> Unit)? = null,
    titleContent: @Composable RowScope.() -> Unit,
    actionContent: (@Composable RowScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .hyperGlassSurface(
                containerColor = colors.containerColor,
                shape = HyperTopBarDefaults.Shape
            )
            .padding(contentPadding),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
            navigationContent?.invoke(this)
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = verticalAlignment,
                content = titleContent
            )
            actionContent?.invoke(this)
        }
    }
}

object HyperTopBarDefaults {
    val MinHeight = 56.dp
    val ContentGap = 8.dp
    val ContentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
    val Shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)

    @Composable
    fun colors(
        containerColor: Color = Color.Transparent,
        contentColor: Color = Color.Unspecified
    ): HyperTopBarColors = HyperTopBarColors(
        containerColor = containerColor,
        contentColor = resolveHyperContainerColor(contentColor, HyperColors.primaryText)
    )
}
