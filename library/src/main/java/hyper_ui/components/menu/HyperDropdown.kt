/** 文件职责：提供 HyperDropdown 浮层菜单及其菜单项作用域。 */
package hyper_ui

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import hyper_ui.core.interaction.hyperNoRippleClickable

@Immutable
data class HyperDropdownColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContentColor: Color,
    val dividerColor: Color
)

@Composable
fun HyperDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperDropdownDefaults.MenuPadding),
    alignment: Alignment = Alignment.TopEnd,
    offset: DpOffset = DpOffset(0.dp, HyperDropdownDefaults.AnchorOffsetY),
    shape: Shape = HyperDropdownDefaults.Shape,
    colors: HyperDropdownColors = HyperDropdownDefaults.colors(),
    content: @Composable HyperDropdownScope.() -> Unit
) {
    if (!expanded) {
        return
    }

    val intOffset = LocalDensity.current.run {
        IntOffset(offset.x.roundToPx(), offset.y.roundToPx())
    }
    val resolvedContainerColor = resolveHyperOpaqueColor(
        color = colors.containerColor,
        fallbackColor = HyperColors.cardContainer,
        backgroundColor = HyperColors.pageBackground
    )
    val resolvedColors = HyperDropdownColors(
        containerColor = resolvedContainerColor,
        contentColor = resolveHyperContainerColor(colors.contentColor, HyperColors.primaryText),
        disabledContentColor = resolveHyperContainerColor(colors.disabledContentColor, HyperColors.disabledText),
        dividerColor = resolveHyperContainerColor(
            colors.dividerColor,
            if (HyperColors.isLight) Color(0f, 0f, 0f, 0.08f) else Color(1f, 1f, 1f, 0.10f)
        )
    )

    Popup(
        alignment = alignment,
        offset = intOffset,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true)
    ) {
        Column(
            modifier = modifier
                .width(HyperDropdownDefaults.MenuWidth)
                .heightIn(max = HyperDropdownDefaults.MaxHeight)
                .hyperGlassSurface(
                    shape = shape,
                    visuals = hyperGlassSurfaceVisuals(
                        containerColor = resolvedColors.containerColor,
                        elevation = HyperDropdownDefaults.Elevation,
                        topLightAlpha = if (HyperColors.isLight) 0.32f else 0.12f,
                        bottomShadeAlpha = if (HyperColors.isLight) 0.04f else 0.13f,
                        shadowAlpha = if (HyperColors.isLight) 0.18f else 0.34f
                    )
                )
                .verticalScroll(rememberScrollState())
                .then(contentModifier)
        ) {
            val scope = remember(onDismissRequest, resolvedColors) {
                HyperDropdownScope(
                    onDismiss = onDismissRequest,
                    colors = resolvedColors
                )
            }
            scope.content()
        }
    }
}

class HyperDropdownScope internal constructor(
    private val onDismiss: () -> Unit,
    private val colors: HyperDropdownColors
) {
    @Composable
    fun Item(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        contentModifier: Modifier = Modifier.padding(HyperDropdownDefaults.ItemPadding),
        enabled: Boolean = true,
        closeOnClick: Boolean = true,
        content: @Composable RowScope.() -> Unit
    ) {
        val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(HyperDropdownDefaults.ItemHeight)
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
                .then(contentModifier),
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

object HyperDropdownDefaults {
    val MenuWidth = 184.dp
    val MaxHeight = 420.dp
    val ItemHeight = 48.dp
    val AnchorOffsetY = 52.dp
    val Elevation = 8.dp
    val Shape: Shape = RoundedCornerShape(20.dp)
    val MenuPadding = PaddingValues(vertical = 8.dp)
    val ItemPadding = PaddingValues(horizontal = 20.dp)

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperDropdownColors {
        val resolvedContainerColor = resolveHyperOpaqueColor(
            color = containerColor,
            fallbackColor = HyperColors.cardContainer,
            backgroundColor = HyperColors.pageBackground
        )
        val resolvedContentColor = resolveHyperContainerColor(contentColor, HyperColors.primaryText)

        return HyperDropdownColors(
            containerColor = resolvedContainerColor,
            contentColor = resolvedContentColor,
            disabledContentColor = resolveHyperContainerColor(disabledContentColor, HyperColors.disabledText),
            dividerColor = resolveHyperContainerColor(
                dividerColor,
                if (HyperColors.isLight) Color(0f, 0f, 0f, 0.08f) else Color(1f, 1f, 1f, 0.10f)
            ),
        )
    }
}
