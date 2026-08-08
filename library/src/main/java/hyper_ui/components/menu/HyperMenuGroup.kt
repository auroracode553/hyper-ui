package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class HyperMenuGroupColors(
    val containerColor: Color
)

@Composable
fun HyperMenuGroup(
    modifier: Modifier = Modifier,
    colors: HyperMenuGroupColors = HyperMenuGroupDefaults.colors(),
    shape: Shape = HyperMenuGroupDefaults.Shape,
    elevation: Dp = HyperMenuGroupDefaults.Elevation,
    border: BorderStroke? = HyperMenuGroupDefaults.border(),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .hyperGlassSurface(
                containerColor = colors.containerColor,
                shape = shape,
                elevation = elevation,
                border = border
            ),
        content = content
    )
}

@Composable
fun HyperMenuItem(
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
    HyperListItem(
        modifier = modifier,
        enabled = enabled,
        minHeight = minHeight,
        contentPadding = contentPadding,
        dividerVisible = dividerVisible,
        dividerInset = dividerInset,
        colors = colors,
        onClick = onClick,
        leadingContent = leadingContent,
        headlineContent = headlineContent,
        supportingContent = supportingContent,
        trailingContent = trailingContent
    )
}

object HyperMenuGroupDefaults {
    val Shape: Shape = RoundedCornerShape(HyperStyleDefaults.LargeCornerRadius)
    val Elevation = 0.dp

    @Composable
    fun colors(containerColor: Color = Color.Unspecified): HyperMenuGroupColors = HyperMenuGroupColors(
        containerColor = resolveHyperContainerColor(
            containerColor = containerColor,
            fallbackColor = HyperColors.elevatedContainer
        )
    )

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke = hyperPanelBorder(color)
}
