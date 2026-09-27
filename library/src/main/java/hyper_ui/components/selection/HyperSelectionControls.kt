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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import hyper_ui.core.icon.HyperCheckIcon

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
    val progress by animateFloatAsState(if (checked) 1f else 0f, tween(160), label = "HyperSwitch")
    val targetTrack = when {
        !enabled && checked -> HyperColors.accent.copy(alpha = 0.38f).compositeOver(HyperColors.softContainer)
        !enabled -> HyperColors.softContainer.copy(alpha = 0.7f)
        checked -> resolveHyperContainerColor(checkedTrackColor, HyperColors.accent)
        else -> resolveHyperContainerColor(uncheckedTrackColor, HyperColors.softContainer)
    }
    val track by animateColorAsState(targetTrack, tween(160), label = "HyperSwitchTrack")
    val targetThumb = when {
        !enabled -> HyperColors.cardContainer.copy(alpha = 0.75f)
        checked -> checkedThumbColor
        else -> resolveHyperContainerColor(uncheckedThumbColor, HyperColors.cardContainer)
    }
    val thumb by animateColorAsState(targetThumb, tween(160), label = "HyperSwitchThumb")
    val outline = when {
        checked -> Color.Transparent
        !enabled -> HyperColors.secondaryText.copy(alpha = 0.12f)
        else -> HyperColors.secondaryText.copy(alpha = 0.26f)
    }
    val source = remember { MutableInteractionSource() }
    Box(
        modifier.size(52.dp, 44.dp).toggleable(
            value = checked, enabled = enabled, role = Role.Switch,
            interactionSource = source, indication = null, onValueChange = onCheckedChange
        ),
        contentAlignment = Alignment.Center
    ) {
        Box(Modifier.width(HyperSwitchDefaults.TrackWidth).height(HyperSwitchDefaults.TrackHeight)
            .background(track, CircleShape).border(HyperSwitchDefaults.TrackBorderWidth, outline, CircleShape)) {
            // 固定滑块尺寸与 4dp 内边距，让关闭态留出清晰的轨道边缘；逻辑方向自动适配 RTL。
            Box(Modifier.align(Alignment.CenterStart)
                .offset(x = HyperSwitchDefaults.TrackPadding + 20.dp * progress)
                .size(HyperSwitchDefaults.ThumbSize)
                .shadow(if (enabled) 2.dp else 0.dp, CircleShape)
                .clip(CircleShape).background(thumb))
        }
    }
}

@Composable
fun HyperCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedColor: Color = Color.Unspecified,
    uncheckedColor: Color = Color.Unspecified,
    uncheckedBorderColor: Color = Color.Unspecified,
    checkmarkColor: Color = Color(1f, 1f, 1f, 1f)
) {
    val selectedColor = resolveHyperContainerColor(checkedColor, HyperColors.accent)
    val fill = if (checked) selectedColor else resolveHyperContainerColor(uncheckedColor, hyperGlass.surfaceSubtle)
    val border = if (checked) selectedColor else resolveHyperContainerColor(uncheckedBorderColor, HyperColors.fieldBorder)
    val shape = RoundedCornerShape(7.dp)
    Box(
        modifier.size(44.dp).toggleable(
            value = checked, enabled = enabled, role = Role.Checkbox,
            interactionSource = remember { MutableInteractionSource() }, indication = null,
            onValueChange = onCheckedChange
        ), contentAlignment = Alignment.Center
    ) {
        Box(Modifier.size(21.dp).clip(shape).background(fill)
            .border(1.5.dp, border, shape), contentAlignment = Alignment.Center) {
            if (checked) CompositionLocalProvider(LocalHyperContentColor provides checkmarkColor) {
                HyperCheckIcon(Modifier.size(15.dp))
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
    val ring = when {
        !enabled -> HyperColors.secondaryText.copy(alpha = 80 / 255f)
        selected -> active
        else -> resolveHyperContainerColor(unselectedBorderColor, HyperColors.secondaryText)
    }
    Box(
        modifier.size(40.dp).then(if (onClick != null) Modifier.selectable(
            selected = selected, enabled = enabled, role = Role.RadioButton,
            interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick
        ) else Modifier), contentAlignment = Alignment.Center
    ) {
        Box(Modifier.size(20.dp).clip(CircleShape)
            .background(resolveHyperContainerColor(unselectedColor, Color.Transparent))
            .border(2.dp, ring, CircleShape), contentAlignment = Alignment.Center) {
            if (selected) Box(Modifier.size(10.dp).background(
                if (enabled) resolveHyperContainerColor(innerDotColor, active) else ring, CircleShape))
        }
    }
}

object HyperSwitchDefaults {
    val TrackWidth = 52.dp
    val TrackHeight = 32.dp
    val TrackPadding = 4.dp
    val TrackElevation = 0.dp
    val TrackBorderWidth = 1.dp
    val ThumbSize = 24.dp
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
    val OuterSize = 20.dp
    val InnerDotSize = 10.dp
    val BorderWidth = 2.dp
}
