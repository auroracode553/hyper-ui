package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import hyper_ui.core.interaction.hyperNoRippleClickable

@Immutable
data class HyperDropdownMenuColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContentColor: Color,
    val dividerColor: Color
)

@Composable
fun HyperDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopEnd,
    offset: DpOffset = DpOffset(0.dp, HyperDropdownMenuDefaults.AnchorOffsetY),
    width: Dp = HyperDropdownMenuDefaults.MenuWidth,
    maxHeight: Dp = HyperDropdownMenuDefaults.MaxHeight,
    shape: Shape = HyperDropdownMenuDefaults.Shape,
    colors: HyperDropdownMenuColors = HyperDropdownMenuDefaults.colors(),
    contentPadding: PaddingValues = HyperDropdownMenuDefaults.MenuPadding,
    border: BorderStroke? = HyperDropdownMenuDefaults.border(),
    content: @Composable HyperDropdownMenuScope.() -> Unit
) {
    if (!expanded) {
        return
    }

    val intOffset = LocalDensity.current.run {
        IntOffset(offset.x.roundToPx(), offset.y.roundToPx())
    }

    Popup(
        alignment = alignment,
        offset = intOffset,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true)
    ) {
        Column(
            modifier = modifier
                .width(width)
                .heightIn(max = maxHeight)
                .clip(shape)
                .background(color = colors.containerColor, shape = shape)
                .then(if (border != null) Modifier.border(border, shape) else Modifier)
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
        ) {
            val scope = remember(onDismissRequest, colors) {
                HyperDropdownMenuScope(
                    onDismiss = onDismissRequest,
                    colors = colors
                )
            }
            scope.content()
        }
    }
}

class HyperDropdownMenuScope internal constructor(
    private val onDismiss: () -> Unit,
    private val colors: HyperDropdownMenuColors
) {
    @Composable
    fun Item(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        closeOnClick: Boolean = true,
        contentPadding: PaddingValues = HyperDropdownMenuDefaults.ItemPadding,
        content: @Composable RowScope.() -> Unit
    ) {
        val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(HyperDropdownMenuDefaults.ItemHeight)
                .hyperNoRippleClickable(
                    enabled = enabled,
                    role = Role.Button,
                    onClick = {
                        onClick()
                        if (closeOnClick) {
                            onDismiss()
                        }
                    }
                )
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                content()
            }
        }
    }

    @Composable
    fun Divider(modifier: Modifier = Modifier) {
        HorizontalDivider(
            modifier = modifier.padding(horizontal = 20.dp, vertical = 6.dp),
            color = colors.dividerColor
        )
    }
}

object HyperDropdownMenuDefaults {
    val MenuWidth = 184.dp
    val MaxHeight = 420.dp
    val ItemHeight = 48.dp
    val AnchorOffsetY = 52.dp
    val Shape: Shape = RoundedCornerShape(20.dp)
    val MenuPadding = PaddingValues(vertical = 8.dp)
    val ItemPadding = PaddingValues(horizontal = 20.dp)

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperDropdownMenuColors {
        val resolvedContentColor = resolveHyperContainerColor(contentColor, HyperColors.primaryText)

        return HyperDropdownMenuColors(
            containerColor = resolveHyperContainerColor(containerColor, HyperColors.cardContainer),
            contentColor = resolvedContentColor,
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                resolvedContentColor.copy(alpha = HyperStyleDefaults.DisabledAlpha)
            ),
            dividerColor = resolveHyperContainerColor(dividerColor, HyperColors.divider)
        )
    }

    @Composable
    fun border(color: Color = Color.Unspecified): BorderStroke = hyperPanelBorder(color)
}
