/** 文件职责：移植参考 HyperDropdown 的字段触发器、锚点菜单与受控选项。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.icon.HyperCheckIcon
import hyper_ui.core.interaction.hyperNoRippleClickable

@Immutable
data class HyperOption<T>(val value: T, val label: String, val enabled: Boolean = true)

@Composable
fun <T> HyperDropdown(
    options: List<HyperOption<T>>,
    value: T? = null,
    onChanged: ((T) -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
    placeholder: String = "请选择",
    menuMaxHeight: Dp = 320.dp,
    width: Dp? = null
) {
    require(menuMaxHeight > 0.dp) { "menuMaxHeight 必须大于 0.dp" }
    require(width == null || width > 0.dp) { "width 必须大于 0.dp" }
    val active = enabled && onChanged != null
    var expanded by remember { mutableStateOf(false) }
    var anchor by remember { mutableStateOf<Rect?>(null) }
    val rotation by androidx.compose.animation.core.animateFloatAsState(if (expanded) 180f else 0f,
        androidx.compose.animation.core.tween(HyperMotionSpec.MenuDuration, easing = HyperMotionSpec.OverlayCurve), label = "HyperDropdownChevron")
    val selected = options.firstOrNull { it.value == value }
    LaunchedEffect(active) { if (!active) expanded = false }
    Column(modifier.then(if (width != null) Modifier.width(width) else Modifier)) {
        if (label != null) HyperText(label, Modifier.padding(bottom = 8.dp), fontSize = 13.sp, lineHeight = 16.9.sp, fontWeight = FontWeight.SemiBold)
        BoxWithConstraints {
            val triggerWidth = width ?: if (constraints.hasBoundedWidth) maxWidth else 240.dp
            Row(Modifier.width(triggerWidth).onGloballyPositioned { anchor = it.boundsInWindow() }
                .defaultMinSize(minHeight = 48.dp)
                .hyperNoRippleClickable(enabled = active, onClick = { expanded = !expanded })
                .hyperFrostedSurface(hyperGlass.surfaceSubtle, RoundedCornerShape(16.dp), elevation = 0.dp,
                    border = BorderStroke(1.dp, HyperColors.fieldBorder))
                .padding(horizontal = 14.dp, vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
                HyperText(selected?.label ?: placeholder, Modifier.weight(1f),
                    color = if (selected == null) HyperColors.secondaryText else HyperColors.primaryText,
                    fontSize = 14.sp, lineHeight = 18.2.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                Spacer(Modifier.width(10.dp))
                HyperIcon(HyperReferenceIcons.ChevronDown,
                    null, Modifier.size(18.dp).graphicsLayer { rotationZ = rotation }, HyperColors.secondaryText)
            }
            HyperAnchoredSurface({ expanded = false }, triggerWidth, menuMaxHeight, PaddingValues(5.dp), anchorBounds = anchor, visible = expanded) {
                if (options.isEmpty()) HyperText("暂无选项", Modifier.fillMaxWidth().padding(16.dp),
                    color = HyperColors.secondaryText, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                options.forEach { option ->
                    HyperListTile(type = "plain", selected = option.value == value, enabled = active && option.enabled,
                        showChevron = false, title = { HyperText(option.label, maxLines = 2) },
                        trailing = if (option.value == value) {
                            { CompositionLocalProvider(LocalHyperContentColor provides HyperColors.accent) {
                                HyperCheckIcon(Modifier.size(18.dp))
                            } }
                        } else null,
                        onPressed = { onChanged?.invoke(option.value); expanded = false })
                }
            }
        }
    }
}
