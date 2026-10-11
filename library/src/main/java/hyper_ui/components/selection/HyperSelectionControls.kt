/** 文件职责：受控选择控件；可视尺寸来自 Flutter，触摸尺寸与语义独立。 */
package hyper_ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpOffset
import hyper_ui.core.icon.HyperCheckIcon
import hyper_ui.core.interaction.hyperNoRippleClickable
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.state.ToggleableState

@Composable
fun HyperSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedTrackColor: Color = Color.Unspecified,
    uncheckedTrackColor: Color = Color.Unspecified,
    checkedThumbColor: Color = Color(1f, 1f, 1f, 1f),
    uncheckedThumbColor: Color = Color.Unspecified
) {
    val curve = HyperMotionSpec.SelectionCurve
    val progress by animateFloatAsState(if (checked) 1f else 0f, tween(HyperMotionSpec.StateDuration, easing = curve), label = "HyperSwitch")
    val targetTrack = when {
        checked -> resolveHyperContainerColor(checkedTrackColor, HyperColors.accent)
            .let { if (enabled) it else it.copy(alpha = 90 / 255f) }
        else -> resolveHyperContainerColor(uncheckedTrackColor,
            hyperGlass.controlTrack.compositeOver(if (enabled) hyperGlass.surfaceStrong else hyperGlass.surfaceSubtle))
    }
    val track by animateColorAsState(targetTrack, tween(HyperMotionSpec.StateDuration, easing = curve), label = "HyperSwitchTrack")
    val targetThumb = when {
        checked -> checkedThumbColor
        else -> resolveHyperContainerColor(uncheckedThumbColor, HyperColors.cardContainer)
    }
    val thumb by animateColorAsState(targetThumb, tween(HyperMotionSpec.StateDuration, easing = curve), label = "HyperSwitchThumb")
    val outline = when {
        checked -> Color.Transparent
        else -> hyperGlass.border
    }
    val source = remember { MutableInteractionSource() }
    Box(
        modifier.size(52.dp, 32.dp)
            .hyperNoRippleClickable(enabled, Role.Switch, source, onClick = { onCheckedChange(!checked) })
            .semantics { toggleableState = if (checked) ToggleableState.On else ToggleableState.Off; if (!enabled) disabled() },
        contentAlignment = Alignment.Center
    ) {
        Box(Modifier.width(HyperSwitchDefaults.TrackWidth).height(HyperSwitchDefaults.TrackHeight)
            .background(track, CircleShape).border(HyperSwitchDefaults.TrackBorderWidth, outline, CircleShape)) {
            // 固定滑块尺寸与 4dp 内边距，让关闭态留出清晰的轨道边缘；逻辑方向自动适配 RTL。
            Box(Modifier.align(Alignment.CenterStart)
                .offset(x = HyperSwitchDefaults.TrackPadding + 20.dp * progress)
                .size(HyperSwitchDefaults.ThumbSize)
                .dropShadow(CircleShape, Shadow(radius = 3.dp, offset = DpOffset(0.dp, 1.dp),
                    color = hyperGlass.shadow.copy(alpha = hyperGlass.shadow.alpha * if (enabled) 1f else 0.5f)))
                .clip(CircleShape).background(thumb))
        }
    }
}

@Composable
fun HyperCheckbox(
    checked: Boolean?,
    onCheckedChange: (Boolean?) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tristate: Boolean = false,
    checkedColor: Color = Color.Unspecified,
    uncheckedColor: Color = Color.Unspecified,
    uncheckedBorderColor: Color = Color.Unspecified,
    checkmarkColor: Color = Color(1f, 1f, 1f, 1f)
) {
    require(tristate || checked != null) { "checked = null 需要 tristate = true" }
    val selected = checked == true || (tristate && checked == null)
    val selectedColor = resolveHyperContainerColor(checkedColor, HyperColors.accent)
    val fill by animateColorAsState(if (selected) selectedColor else resolveHyperContainerColor(uncheckedColor, hyperGlass.surfaceSubtle),
        tween(HyperMotionSpec.StateDuration, easing = HyperMotionSpec.SelectionCurve), label = "HyperCheckboxFill")
    val border by animateColorAsState(if (selected) selectedColor else resolveHyperContainerColor(uncheckedBorderColor, HyperColors.fieldBorder),
        tween(HyperMotionSpec.StateDuration, easing = HyperMotionSpec.SelectionCurve), label = "HyperCheckboxBorder")
    val markProgress by animateFloatAsState(if (selected) 1f else 0f,
        tween(HyperMotionSpec.StateDuration, easing = HyperMotionSpec.OverlayCurve), label = "HyperCheckboxMark")
    val shape = RoundedCornerShape(7.dp)
    Box(
        modifier.size(44.dp)
            .hyperNoRippleClickable(enabled, Role.Checkbox, onClick = {
                onCheckedChange(if (tristate) when (checked) { true -> null; false -> true; null -> false } else checked != true)
            })
            .semantics { toggleableState = when (checked) { true -> ToggleableState.On; false -> ToggleableState.Off; null -> ToggleableState.Indeterminate }; if (!enabled) disabled() },
        contentAlignment = Alignment.Center
    ) {
        Box(Modifier.size(21.dp).clip(shape).background(fill)
            .border(1.5.dp, border, shape), contentAlignment = Alignment.Center) {
            CompositionLocalProvider(LocalHyperContentColor provides checkmarkColor) {
                val mark = Modifier.size(15.dp).graphicsLayer { alpha = markProgress; scaleX = 0.8f + 0.2f * markProgress; scaleY = scaleX }
                if (checked == null) HyperIcon(HyperReferenceIcons.Minus, null, mark)
                else HyperCheckIcon(mark)
            }
        }
    }
}

@Composable
fun HyperRadio(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedColor: Color = Color.Unspecified,
    unselectedColor: Color = Color.Unspecified,
    unselectedBorderColor: Color = Color.Unspecified,
    innerDotColor: Color = Color.Unspecified
) {
    val active = resolveHyperContainerColor(selectedColor, HyperColors.accent)
    val targetRing = when {
        selected -> active
        else -> resolveHyperContainerColor(unselectedBorderColor, HyperColors.fieldBorder)
    }
    val ring by animateColorAsState(targetRing, tween(HyperMotionSpec.StateDuration, easing = HyperMotionSpec.SelectionCurve), label = "HyperRadioRing")
    val ringWidth by animateFloatAsState(if (selected) 6f else 1.5f, tween(HyperMotionSpec.StateDuration, easing = HyperMotionSpec.SelectionCurve), label = "HyperRadioWidth")
    Box(
        modifier.size(44.dp).then(if (onClick != null) Modifier.hyperNoRippleClickable(
            enabled = enabled, role = Role.RadioButton, onClick = onClick
        ) else Modifier).semantics { this.selected = selected; if (!enabled || onClick == null) disabled() },
        contentAlignment = Alignment.Center
    ) {
        Box(Modifier.size(21.dp).clip(CircleShape)
            .background(resolveHyperContainerColor(if (selected) innerDotColor else unselectedColor, hyperGlass.surfaceSubtle))
            .border(ringWidth.dp, ring, CircleShape))
    }
}

object HyperSwitchDefaults {
    val TrackWidth = 52.dp
    val TrackHeight = 32.dp
    val TrackPadding = 5.dp
    val TrackElevation = 0.dp
    val TrackBorderWidth = 1.dp
    val ThumbSize = 22.dp
    val ThumbElevation = 0.dp
    val ThumbBorderWidth = 0.dp
}
object HyperCheckboxDefaults {
    val BoxSize = 21.dp
    val CornerRadius = 7.dp
    val BorderWidth = 1.5.dp
    val CheckmarkSize = 15.dp
}
object HyperRadioDefaults {
    val OuterSize = 21.dp
    val InnerDotSize = 9.dp
    val BorderWidth = 1.5.dp
}
