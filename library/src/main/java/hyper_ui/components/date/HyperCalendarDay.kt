/** 文件职责：日期选择器与内嵌月历共用的日期格、角标和轻量附属条目。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable
import kotlin.math.floor
import kotlin.math.min

@Immutable
data class HyperCalendarItem(val label: String, val color: Color = Color.Unspecified)

object HyperCalendarDayDefaults {
    @Composable
    fun minimumHeight(size: String = "default", hasSubtitle: Boolean = false, itemRows: Int = 0): Dp {
        requireHyperComponentSize(size)
        require(itemRows in 0..3)
        val font = when (size) { "small" -> 10f; "large" -> 17f; else -> 14f }
        val base = when (size) { "small" -> 20f; "large" -> 48f; else -> 42f }
        val scale = LocalDensity.current.fontScale
        return (maxOf(base, scale * font * 1.2f + (if (hasSubtitle) scale * 9 * 1.2f + 2 else 0f) + (if (size == "small") 6 else 12)) + itemRows * (scale * 10 * 1.2f + 8)).dp
    }
}

@Composable
fun HyperCalendarDay(
    day: Int,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    today: Boolean = false,
    enabled: Boolean = true,
    adjacent: Boolean = false,
    emphasized: Boolean = false,
    inRange: Boolean = false,
    size: String = "default",
    expanded: Boolean = false,
    subtitle: String? = null,
    subtitleColor: Color = Color.Unspecified,
    badge: String? = null,
    badgeColor: Color = Color.Unspecified,
    items: List<HyperCalendarItem> = emptyList(),
    itemCount: Int? = null,
    onPressed: (() -> Unit)? = null,
    onLongPressed: (() -> Unit)? = null,
    onItemsPressed: (() -> Unit)? = null,
) {
    require(day in 1..31)
    require(itemCount == null || itemCount >= items.size)
    val base = HyperCalendarDayDefaults.minimumHeight(size, subtitle != null)
    val density = LocalDensity.current
    val foreground = when {
        selected -> Color(1f, 1f, 1f, 1f)
        !enabled || adjacent -> HyperColors.secondaryText.copy(alpha = 105 / 255f)
        emphasized -> HyperColors.accent
        else -> HyperColors.primaryText
    }
    val auxiliary = when {
        selected -> Color(1f, 1f, 1f, 1f)
        !enabled || adjacent -> foreground
        subtitleColor != Color.Unspecified -> subtitleColor
        else -> HyperColors.secondaryText
    }
    val font = when (size) { "small" -> 10f; "large" -> 17f; else -> 14f }
    val labelHeight = (density.fontScale * 10 * 1.2f + 6).dp
    val total = itemCount ?: items.size
    val pressable = if (onPressed != null || onLongPressed != null) {
        Modifier.hyperNoRippleClickable(
            enabled = enabled,
            onLongClick = onLongPressed,
            onClick = { onPressed?.invoke() },
        )
    } else Modifier
    BoxWithConstraints(
        modifier.then(if (expanded) Modifier else Modifier.height(base))
            .background(if (inRange) HyperColors.accent.copy(alpha = .12f) else Color.Transparent)
            .then(pressable).clipToBounds(),
    ) {
        val height = if (constraints.hasBoundedHeight) maxHeight else base
        val visible = floor((height.value - base.value) / (labelHeight.value + 2)).toInt().coerceIn(0, 3)
        val overflow = visible > 0 && total > min(visible, items.size)
        val rows = min(items.size, if (overflow) visible - 1 else visible)
        val diameter = maxOf(0f, min(base.value - 6, height.value - 6)).dp
        Column {
            Box(Modifier.fillMaxWidth().height(minOf(base, height)), contentAlignment = Alignment.Center) {
                Box(Modifier.size(diameter)
                    .background(if (selected) HyperColors.accent else Color.Transparent, CircleShape)
                    .then(if (today && enabled && !selected) Modifier.border(1.dp, HyperColors.accent, CircleShape) else Modifier),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        HyperText("$day", fontSize = font.sp, lineHeight = (font * 1.2f).sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium, color = foreground)
                        if (subtitle != null) HyperText(subtitle, fontSize = 9.sp, lineHeight = 10.8.sp,
                            maxLines = 1, overflow = TextOverflow.Ellipsis, color = auxiliary)
                        if (visible == 0 && total > 0) {
                            Spacer(Modifier.height(2.dp))
                            Box(Modifier.size(3.dp).background(foreground, CircleShape))
                        }
                    }
                    if (badge != null) Box(
                        Modifier.align(Alignment.TopEnd).offset(x = 3.dp, y = (-2).dp)
                            .background(if (badgeColor == Color.Unspecified) HyperColors.accent else badgeColor, RoundedCornerShape(8.dp))
                            .padding(horizontal = 3.dp, vertical = 2.dp),
                    ) { HyperText(badge, fontSize = 8.sp, lineHeight = 8.sp, maxLines = 1, color = Color(1f, 1f, 1f, 1f)) }
                }
            }
            if (rows > 0 || overflow) Column(Modifier.hyperNoRippleClickable(
                enabled = enabled && (onItemsPressed != null || onPressed != null),
                onClick = { (onItemsPressed ?: onPressed)?.invoke() },
            )) {
                repeat(rows) { index ->
                    val item = items[index]
                    val tone = if (item.color == Color.Unspecified) HyperColors.accent else item.color
                    val background = tone.copy(alpha = 40 / 255f).compositeOver(HyperColors.pageBackground)
                    val color = lerp(tone, HyperColors.primaryText, if (background.luminance() > .5f) .45f else .65f)
                    Spacer(Modifier.height(2.dp))
                    Row(Modifier.fillMaxWidth().height(labelHeight).background(background, RoundedCornerShape(4.dp))
                        .padding(horizontal = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        HyperText(item.label, modifier = Modifier.weight(1f), fontSize = 10.sp,
                            lineHeight = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, color = color)
                    }
                }
                if (overflow) {
                    Spacer(Modifier.height(2.dp))
                    Box(Modifier.fillMaxWidth().height(labelHeight), contentAlignment = Alignment.Center) {
                        HyperText("+${total - rows}", fontSize = 10.sp, lineHeight = 12.sp, color = HyperColors.primaryText)
                    }
                }
            }
        }
    }
}
