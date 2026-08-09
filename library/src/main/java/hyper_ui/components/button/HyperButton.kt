/** 文件职责：在 hyper_ui 中负责提供 library/src/main/java/hyper_ui/components/button/HyperButton 可复用界面组件及交互封装。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable

enum class HyperButtonTone {
    Primary,
    Secondary,
    Tonal,
    Outline,
    Plain,
    Success,
    Info,
    Warning,
    Danger
}

@Immutable
data class HyperButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

@Composable
fun HyperButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tone: HyperButtonTone = HyperButtonTone.Primary,
    colors: HyperButtonColors = HyperButtonDefaults.colors(tone),
    border: BorderStroke? = HyperButtonDefaults.border(tone),
    shape: Shape = HyperButtonDefaults.Shape,
    minHeight: androidx.compose.ui.unit.Dp = HyperButtonDefaults.MinHeight,
    contentPadding: PaddingValues = HyperButtonDefaults.ContentPadding,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        HyperButtonDefaults.ContentSpacing,
        Alignment.CenterHorizontally
    ),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
) {
    val surfaceColor = HyperColors.cardContainer
    val requestedContainerColor = if (enabled) colors.containerColor else colors.disabledContainerColor
    val requestedContentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    val containerColor = resolveHyperOpaqueColor(
        color = requestedContainerColor,
        fallbackColor = if (enabled) HyperColors.accent else HyperColors.softContainer,
        backgroundColor = surfaceColor
    )
    val contentColor = resolveHyperOpaqueColor(
        color = requestedContentColor,
        fallbackColor = if (enabled) HyperColors.primaryText else HyperColors.secondaryText,
        backgroundColor = containerColor
    )

    Row(
        modifier = modifier
            .heightIn(min = minHeight)
            .hyperSolidSurface(
                containerColor = containerColor,
                shape = shape,
                border = border
            )
            .hyperNoRippleClickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            )
            .padding(contentPadding),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            content()
        }
    }
}

object HyperButtonDefaults {
    val MinHeight = 40.dp
    val ContentSpacing = 8.dp
    val ContentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
    val Shape: Shape = RoundedCornerShape(HyperStyleDefaults.LargeCornerRadius)

    @Composable
    fun colors(
        tone: HyperButtonTone = HyperButtonTone.Primary,
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperButtonColors {
        val surfaceColor = HyperColors.cardContainer
        val accentColor = resolveHyperOpaqueColor(
            color = HyperColors.accent,
            fallbackColor = HyperColors.accent,
            backgroundColor = surfaceColor
        )
        val defaultContainerColor = when (tone) {
            HyperButtonTone.Primary -> accentColor
            HyperButtonTone.Secondary -> HyperColors.softContainer
            HyperButtonTone.Tonal -> blendHyperOpaqueColors(
                backgroundColor = surfaceColor,
                foregroundColor = accentColor,
                foregroundFraction = 0.14f
            )
            HyperButtonTone.Outline,
            HyperButtonTone.Plain -> surfaceColor
            HyperButtonTone.Success -> HyperColors.success
            HyperButtonTone.Info -> HyperColors.info
            HyperButtonTone.Warning -> HyperColors.warning
            HyperButtonTone.Danger -> HyperColors.danger
        }
        val defaultContentColor = when (tone) {
            HyperButtonTone.Primary,
            HyperButtonTone.Success,
            HyperButtonTone.Info,
            HyperButtonTone.Warning,
            HyperButtonTone.Danger -> rgba(255, 255, 255, 1f)
            HyperButtonTone.Tonal -> HyperColors.accent
            HyperButtonTone.Secondary,
            HyperButtonTone.Outline,
            HyperButtonTone.Plain -> HyperColors.primaryText
        }
        val resolvedContainerColor = resolveHyperOpaqueColor(
            color = containerColor,
            fallbackColor = defaultContainerColor,
            backgroundColor = surfaceColor
        )
        val resolvedContentColor = resolveHyperOpaqueColor(
            color = contentColor,
            fallbackColor = defaultContentColor,
            backgroundColor = resolvedContainerColor
        )
        val resolvedDisabledContainerColor = resolveHyperOpaqueColor(
            color = disabledContainerColor,
            fallbackColor = HyperColors.softContainer,
            backgroundColor = surfaceColor
        )
        val resolvedDisabledContentColor = resolveHyperOpaqueColor(
            color = disabledContentColor,
            fallbackColor = HyperColors.secondaryText,
            backgroundColor = resolvedDisabledContainerColor
        )

        return HyperButtonColors(
            containerColor = resolvedContainerColor,
            contentColor = resolvedContentColor,
            disabledContainerColor = resolvedDisabledContainerColor,
            disabledContentColor = resolvedDisabledContentColor
        )
    }

    @Composable
    fun border(
        tone: HyperButtonTone = HyperButtonTone.Primary,
        color: Color = Color.Unspecified
    ): BorderStroke? = when (tone) {
        HyperButtonTone.Outline -> BorderStroke(
            width = 1.dp,
            color = resolveHyperOpaqueColor(
                color = color,
                fallbackColor = HyperColors.accent,
                backgroundColor = HyperColors.cardContainer
            )
        )
        else -> null
    }
}
