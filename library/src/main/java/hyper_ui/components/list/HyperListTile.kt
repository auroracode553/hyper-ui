/** 文件职责：参考 HyperListTile 的自动表面、选中态、图标块与受控动作。 */
package hyper_ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ChevronRight

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable

@Immutable
data class HyperListTileColors(
    val contentColor: Color,
    val supportingColor: Color,
    val disabledContentColor: Color,
    val dividerColor: Color
)

internal val LocalHyperListTileDividerSuppressed = staticCompositionLocalOf { false }
internal val LocalHyperListTileContainerColor = staticCompositionLocalOf { Color.Unspecified }

@Composable
fun HyperListTile(
    title: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperListTileDefaults.ContentPadding),
    dividerModifier: Modifier = Modifier.padding(horizontal = 14.dp),
    type: String = "auto",
    selected: Boolean = false,
    enabled: Boolean = true,
    showChevron: Boolean = true,
    dividerVisible: Boolean = false,
    colors: HyperListTileColors = HyperListTileDefaults.colors(),
    onPressed: (() -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    leadingColor: Color = HyperColors.accent,
    leading: (@Composable RowScope.() -> Unit)? = null,
    subtitle: (@Composable ColumnScope.() -> Unit)? = null,
    meta: (@Composable ColumnScope.() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null
) {
    require(type in setOf("auto", "plain", "glass")) { "type 必须为 auto、plain 或 glass" }
    val plain = type == "plain" || (type == "auto" &&
        (LocalHyperSurfacePresent.current || LocalHyperListTileContainerColor.current != Color.Unspecified))
    val rowShape = RoundedCornerShape(if (plain) 12.dp else 15.dp)
    val selectedBackground by androidx.compose.animation.animateColorAsState(
        if (selected) hyperGlass.selection else Color.Transparent,
        androidx.compose.animation.core.tween(HyperMotionSpec.MenuDuration, easing = HyperMotionSpec.EaseOutCubic),
        label = "HyperListTileSelection")
    val hasLeading = leading != null || leadingIcon != null
    val hasTrailing = trailing != null || (showChevron && onPressed != null)
    val surface = if (plain) Modifier else Modifier
        .then(if (onPressed != null) Modifier.hyperNoRippleClickable(enabled = enabled, role = Role.Button,
            onClick = onPressed) else Modifier)
        .hyperFrostedSurface(
        if (selected) hyperGlass.surfaceStrong else hyperGlass.surface,
        RoundedCornerShape(18.dp),
        border = if (selected) BorderStroke(1.dp, HyperColors.accent.copy(alpha = 90 / 255f)) else null
    ).padding(3.dp)
    Column(modifier.fillMaxWidth().then(surface)) {
        Row(Modifier.fillMaxWidth().defaultMinSize(minHeight = 46.dp)
            .then(if (plain && onPressed != null) Modifier.hyperNoRippleClickable(enabled = enabled, role = Role.Button,
                pressedScale = 0.992f, onClick = onPressed) else Modifier)
            .background(selectedBackground, rowShape)
            .then(contentModifier), verticalAlignment = Alignment.CenterVertically) {
            if (hasLeading) {
                CompositionLocalProvider(LocalHyperContentColor provides if (enabled) colors.contentColor else colors.disabledContentColor,
                    LocalHyperIconSize provides 18.dp) {
                    if (leading != null) leading() else Box(
                        Modifier.size(34.dp).hyperSolidSurface(leadingColor.copy(alpha = 28 / 255f),
                            RoundedCornerShape(11.dp), border = BorderStroke(1.dp, hyperGlass.border)),
                        contentAlignment = Alignment.Center
                    ) { HyperIcon(leadingIcon!!, null, color = leadingColor) }
                }
            }
            Column(Modifier.weight(1f).padding(start = if (hasLeading) 12.dp else 0.dp,
                end = if (hasTrailing) 8.dp else 0.dp)) {
                CompositionLocalProvider(LocalHyperContentColor provides if (enabled) colors.contentColor else colors.disabledContentColor,
                    LocalHyperTextStyle provides HyperListTileDefaults.HeadlineTextStyle) { title() }
                if (subtitle != null) {
                    Spacer(Modifier.height(3.dp))
                    CompositionLocalProvider(LocalHyperContentColor provides colors.supportingColor,
                        LocalHyperTextStyle provides HyperListTileDefaults.SupportingTextStyle) { subtitle() }
                }
                if (meta != null) {
                    Spacer(Modifier.height(4.dp))
                    CompositionLocalProvider(LocalHyperContentColor provides colors.supportingColor,
                        LocalHyperTextStyle provides HyperTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 15.4.sp)) { meta() }
                }
            }
            if (hasTrailing) {
                CompositionLocalProvider(LocalHyperContentColor provides colors.supportingColor,
                    LocalHyperTextStyle provides HyperListTileDefaults.TrailingTextStyle) {
                    if (trailing != null) trailing() else HyperIcon(Lucide.ChevronRight,
                        null, Modifier.size(18.dp))
                }
            }
        }
        if (dividerVisible && !LocalHyperListTileDividerSuppressed.current) {
            HyperListDivider(dividerModifier, colors.dividerColor)
        }
    }
}

@Composable
internal fun HyperListDivider(modifier: Modifier, color: Color) {
    HyperDivider(modifier, color)
}

object HyperListTileDefaults {
    val SingleLineMinHeight = 46.dp
    val SupportingMinHeight = 46.dp
    val ContentGap = 12.dp
    val TextGap = 3.dp
    val ContentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
    val DividerInset = 14.dp
    val DividerHeight = 1.dp
    fun minHeight(hasSupportingContent: Boolean): Dp = 46.dp
    val LeadingTextStyle: TextStyle
        @Composable get() = HyperTheme.typography.bodyMedium
    val HeadlineTextStyle: TextStyle
        @Composable get() = HyperTheme.typography.bodyMedium.copy(fontSize = 14.sp, lineHeight = 17.5.sp, fontWeight = FontWeight.SemiBold)
    val SupportingTextStyle: TextStyle
        @Composable get() = HyperTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 16.2.sp)
    val TrailingTextStyle: TextStyle
        @Composable get() = HyperTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 18.2.sp)
    @Composable
    fun colors(
        contentColor: Color = Color.Unspecified,
        supportingColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ) = HyperListTileColors(
        resolveHyperContainerColor(contentColor, HyperColors.primaryText),
        resolveHyperContainerColor(supportingColor, HyperColors.secondaryText),
        resolveHyperContainerColor(disabledContentColor, HyperColors.secondaryText),
        resolveHyperContainerColor(dividerColor, HyperColors.divider)
    )
}
