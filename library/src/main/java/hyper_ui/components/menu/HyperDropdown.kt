/** 文件职责：提供 HyperDropdown 浮层菜单、菜单项语义及受控关闭行为。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Immutable
data class HyperDropdownColors(
    val containerColor: Color,
    val contentColor: Color,
    val dangerContentColor: Color,
    val disabledContentColor: Color,
    val pressedContainerColor: Color,
    val dividerColor: Color
)

enum class HyperDropdownItemTone {
    Normal,
    Danger
}

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
    val resolvedColors = resolveHyperDropdownColors(colors)

    Popup(
        alignment = alignment,
        offset = intOffset,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true)
    ) {
        Column(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .widthIn(max = HyperDropdownDefaults.MaxWidth)
                .heightIn(max = HyperDropdownDefaults.MaxHeight)
                .hyperDropdownSurface(
                    shape = shape,
                    containerColor = resolvedColors.containerColor,
                    visuals = hyperDropdownSurfaceVisuals()
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
        tone: HyperDropdownItemTone = HyperDropdownItemTone.Normal,
        content: @Composable RowScope.() -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val pressed by interactionSource.collectIsPressedAsState()
        val contentColor = when {
            !enabled -> colors.disabledContentColor
            tone == HyperDropdownItemTone.Danger -> colors.dangerContentColor
            else -> colors.contentColor
        }
        val pressedContainerColor = if (enabled && pressed) {
            colors.pressedContainerColor
        } else {
            Color.Transparent
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(HyperDropdownDefaults.ItemHeight)
                .clip(HyperDropdownDefaults.ItemShape)
                .background(pressedContainerColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
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
                ProvideTextStyle(HyperDropdownDefaults.ItemTextStyle) {
                    content()
                }
            }
        }
    }

    @Composable
    fun Divider(modifier: Modifier = Modifier) {
        HorizontalDivider(
            modifier = modifier.padding(HyperDropdownDefaults.DividerPadding),
            color = colors.dividerColor
        )
    }
}

object HyperDropdownDefaults {
    val MaxWidth = 220.dp
    val MaxHeight = 432.dp
    val ItemHeight = 48.dp
    val AnchorOffsetY = 52.dp
    val Elevation = 10.dp
    val Shape: Shape = RoundedCornerShape(26.dp)
    val ItemShape: Shape = RoundedCornerShape(16.dp)
    val MenuPadding = PaddingValues(vertical = 8.dp)
    val ItemPadding = PaddingValues(horizontal = 22.dp)
    val DividerPadding = PaddingValues(horizontal = 22.dp, vertical = 6.dp)
    val ItemTextStyle = TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    )

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        dangerContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        pressedContainerColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperDropdownColors {
        val isLight = HyperColors.isLight
        return HyperDropdownColors(
            containerColor = resolveHyperContainerColor(
                containerColor,
                if (isLight) {
                    Color(1f, 1f, 1f, 0.96f)
                } else {
                    Color(0.14f, 0.14f, 0.15f, 0.96f)
                }
            ),
            contentColor = resolveHyperContainerColor(contentColor, HyperColors.primaryText),
            dangerContentColor = resolveHyperContainerColor(dangerContentColor, HyperColors.danger),
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                HyperColors.disabledText
            ),
            pressedContainerColor = resolveHyperContainerColor(
                pressedContainerColor,
                if (isLight) {
                    Color(0f, 0f, 0f, 0.055f)
                } else {
                    Color(1f, 1f, 1f, 0.075f)
                }
            ),
            dividerColor = resolveHyperContainerColor(
                dividerColor,
                if (isLight) {
                    Color(0f, 0f, 0f, 0.07f)
                } else {
                    Color(1f, 1f, 1f, 0.09f)
                }
            )
        )
    }
}
