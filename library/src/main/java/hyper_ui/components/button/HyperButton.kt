/** 文件职责：按钮布局、状态和语义；材质由 HyperButtonSurface 绘制。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable

@Immutable
data class HyperButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

/** 受控动作按钮。loading 由调用方持有，加载期间阻止重复触发。 */
@Composable
fun HyperButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    type: String = "filled",
    size: String = "default",
    colors: HyperButtonColors = HyperButtonDefaults.colors(type),
    border: BorderStroke? = HyperButtonDefaults.border(type, enabled),
    shape: Shape = HyperButtonDefaults.Shape,
    contentPadding: PaddingValues = HyperButtonDefaults.contentPadding(size),
    role: Role = Role.Button,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
) {
    requireHyperComponentSize(size)
    val resolvedHeight = HyperButtonDefaults.height(size)
    require(type in setOf("filled", "tonal", "outline", "ghost", "danger", "icon")) {
        "不支持的 HyperButton type: $type"
    }
    val isIconType = type == "icon"
    val container = if (enabled) colors.containerColor else colors.disabledContainerColor
    val foreground = if (enabled) colors.contentColor else colors.disabledContentColor
    Row(
        modifier = modifier
            .then(
                if (isIconType) {
                    Modifier.size(resolvedHeight)
                } else {
                    Modifier
                        .defaultMinSize(minWidth = (68 + (resolvedHeight.value - 38) * 2.5f).coerceAtLeast(0f).dp)
                        .height(resolvedHeight)
                }
            )
            .hyperNoRippleClickable(enabled = enabled && !loading, role = role, onClick = onClick)
            .hyperButtonSurface(container, shape, type, enabled, border)
            .semantics { if (loading) stateDescription = "正在处理" }
            .then(if (isIconType) Modifier else Modifier.padding(contentPadding)),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        CompositionLocalProvider(
            LocalHyperContentColor provides foreground,
            LocalHyperTextStyle provides HyperButtonDefaults.textStyle(size)
        ) {
            if (loading) HyperProgress(
                type = "circular",
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                colors = HyperProgressDefaults.colors(Color.Transparent, foreground)
            )
            content()
        }
    }
}

object HyperButtonDefaults {
    val MinHeight = 38.dp
    val IconSize = 18.dp
    val ContentSpacing = 8.dp
    val ContentPadding = PaddingValues(horizontal = 14.dp)
    val Shape: Shape = RoundedCornerShape(16.dp)

    fun height(size: String = "default"): Dp =
        hyperComponentSize(size, small = 32.dp, normal = MinHeight, large = 48.dp)

    fun contentPadding(size: String = "default"): PaddingValues {
        val height = height(size)
        return PaddingValues(horizontal = (14 + (height.value - 38) * 2 / 3).coerceAtLeast(0f).dp)
    }

    @Composable
    fun textStyle(size: String = "default"): TextStyle {
        val height = height(size)
        val fontSize = (13 + (height.value - 38) / 6).coerceAtLeast(1f)
        return HyperTheme.typography.labelLarge.copy(fontSize = fontSize.sp, lineHeight = (fontSize * 1.2f).sp,
            fontWeight = FontWeight.SemiBold, letterSpacing = 0.05.sp)
    }

    @Composable
    fun colors(
        type: String = "filled",
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified
    ): HyperButtonColors {
        val fill = when (type) {
            "filled" -> HyperColors.accent
            "danger" -> HyperColors.danger
            "tonal" -> hyperGlass.selection
            "outline" -> hyperGlass.surfaceSubtle
            "ghost" -> Color.Transparent
            "icon" -> hyperGlass.surface
            else -> error("不支持的 HyperButton type: $type")
        }
        val foreground = if (type == "filled" || type == "danger")
            Color(1f, 1f, 1f, 1f) else HyperColors.primaryText
        return HyperButtonColors(
            resolveHyperContainerColor(containerColor, fill),
            resolveHyperContainerColor(contentColor, foreground),
            resolveHyperContainerColor(disabledContainerColor,
                if (type == "ghost" || type == "icon") Color.Transparent else hyperGlass.controlTrack),
            resolveHyperContainerColor(disabledContentColor, HyperColors.secondaryText.copy(alpha = 150 / 255f))
        )
    }

    @Composable
    fun border(
        type: String = "filled",
        enabled: Boolean = true,
        color: Color = Color.Unspecified
    ): BorderStroke? {
        val fallback = when {
            !enabled -> HyperColors.divider.copy(alpha = 110 / 255f)
            type == "ghost" -> Color.Transparent
            type == "icon" -> hyperGlass.edgeHighlight
            type == "outline" -> HyperColors.divider
            type == "tonal" -> hyperGlass.edgeHighlight
            else -> Color(1f, 1f, 1f, 45 / 255f)
        }
        val resolved = resolveHyperContainerColor(color, fallback)
        return if (resolved.alpha == 0f) null else BorderStroke(1.dp, resolved)
    }
}
