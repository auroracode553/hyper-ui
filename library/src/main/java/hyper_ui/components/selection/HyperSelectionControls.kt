package hyper_ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import hyper_ui.core.interaction.hyperNoRippleClickable

@Composable
fun HyperSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedTrackColor: Color = Color.Unspecified,
    uncheckedTrackColor: Color = Color.Unspecified,
    checkedThumbColor: Color = rgba(255, 255, 255, 1f),
    uncheckedThumbColor: Color = rgba(255, 255, 255, 1f)
) {
    val enabledAlpha = if (enabled) 1f else HyperStyleDefaults.DisabledAlpha
    // 对齐 HyperIconButton 玻璃托盘范式：
    // 标记使用默认背景 → 禁用态 disabledContainer 回退；track/thumb 可见背景叠 glass 高光
    val usesDefaultCheckedTrack = checkedTrackColor == Color.Unspecified
    val usesDefaultUncheckedTrack = uncheckedTrackColor == Color.Unspecified
    val resolvedCheckedTrackColor = if (usesDefaultCheckedTrack) {
        HyperColors.accent
    } else {
        checkedTrackColor
    }
    val resolvedUncheckedTrackColor = if (usesDefaultUncheckedTrack) {
        HyperColors.elevatedContainer
    } else {
        uncheckedTrackColor
    }
    val trackColor by animateColorAsState(
        targetValue = if (enabled) {
            if (checked) resolvedCheckedTrackColor else resolvedUncheckedTrackColor
        } else if (if (checked) usesDefaultCheckedTrack else usesDefaultUncheckedTrack) {
            HyperColors.disabledContainer
        } else {
            (if (checked) resolvedCheckedTrackColor else resolvedUncheckedTrackColor).copy(
                alpha = (if (checked) resolvedCheckedTrackColor else resolvedUncheckedTrackColor).alpha * enabledAlpha
            )
        },
        label = "hyperSwitchTrackColor"
    )
    val thumbColor by animateColorAsState(
        targetValue = (if (checked) checkedThumbColor else uncheckedThumbColor).copy(alpha = enabledAlpha),
        label = "hyperSwitchThumbColor"
    )
    val trackHasVisibleBackground =
        (if (checked) resolvedCheckedTrackColor else resolvedUncheckedTrackColor).alpha > 0f
    val trackHighlightModifier = if (trackHasVisibleBackground) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
    }
    val thumbHasVisibleBackground =
        (if (checked) checkedThumbColor else uncheckedThumbColor).alpha > 0f
    val thumbHighlightModifier = if (thumbHasVisibleBackground) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
    }
    val thumbProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessMedium,
            dampingRatio = Spring.DampingRatioNoBouncy
        ),
        label = "hyperSwitchThumbProgress"
    )

    Box(
        modifier = modifier
            .width(HyperSwitchDefaults.TrackWidth)
            .height(HyperSwitchDefaults.TrackHeight)
            .clip(RoundedCornerShape(percent = 50))
            .background(trackColor)
            .then(trackHighlightModifier)
            .hyperNoRippleClickable(
                enabled = enabled,
                role = Role.Switch,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = (HyperSwitchDefaults.TrackWidth - HyperSwitchDefaults.ThumbSize) * thumbProgress)
                .size(HyperSwitchDefaults.ThumbSize)
                .clip(CircleShape)
                .background(thumbColor)
                .then(thumbHighlightModifier)
        )
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
    checkmarkColor: Color = rgba(255, 255, 255, 1f)
) {
    val enabledAlpha = if (enabled) 1f else HyperStyleDefaults.DisabledAlpha
    // 对齐 HyperIconButton 玻璃托盘范式：
    // 标记使用默认背景 → 禁用态 disabledContainer 回退；Box 可见背景叠 glass 高光；保留未选中 border 语义
    val usesDefaultChecked = checkedColor == Color.Unspecified
    val usesDefaultUnchecked = uncheckedColor == Color.Unspecified
    val resolvedCheckedColor = if (usesDefaultChecked) {
        HyperColors.accent
    } else {
        checkedColor
    }
    val resolvedUncheckedColor = if (usesDefaultUnchecked) {
        HyperColors.elevatedContainer
    } else {
        uncheckedColor
    }
    val resolvedUncheckedBorderColor = if (uncheckedBorderColor == Color.Unspecified) {
        HyperColors.accent
    } else {
        uncheckedBorderColor
    }
    val backgroundColor by animateColorAsState(
        targetValue = if (enabled) {
            if (checked) resolvedCheckedColor else resolvedUncheckedColor
        } else if (if (checked) usesDefaultChecked else usesDefaultUnchecked) {
            HyperColors.disabledContainer
        } else {
            (if (checked) resolvedCheckedColor else resolvedUncheckedColor).copy(
                alpha = (if (checked) resolvedCheckedColor else resolvedUncheckedColor).alpha * enabledAlpha
            )
        },
        label = "hyperCheckboxBackgroundColor"
    )
    val hasVisibleBackground = (if (checked) resolvedCheckedColor else resolvedUncheckedColor).alpha > 0f
    val highlightModifier = if (hasVisibleBackground) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
    }
    val borderColor by animateColorAsState(
        targetValue = (if (checked) {
            Color.Transparent
        } else {
            resolvedUncheckedBorderColor
        }).copy(alpha = if (checked) 0f else enabledAlpha),
        label = "hyperCheckboxBorderColor"
    )
    val checkmarkAlpha by animateFloatAsState(
        targetValue = if (checked) enabledAlpha else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessMedium,
            dampingRatio = Spring.DampingRatioNoBouncy
        ),
        label = "hyperCheckboxCheckmarkAlpha"
    )
    val shape = RoundedCornerShape(HyperCheckboxDefaults.CornerRadius)

    Box(
        modifier = modifier
            .size(HyperCheckboxDefaults.BoxSize)
            .clip(shape)
            .background(backgroundColor)
            .then(highlightModifier)
            .border(
                border = BorderStroke(
                    width = HyperCheckboxDefaults.BorderWidth,
                    color = borderColor
                ),
                shape = shape
            )
            .hyperNoRippleClickable(
                enabled = enabled,
                role = Role.Checkbox,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(HyperCheckboxDefaults.CheckmarkSize)) {
            val path = Path().apply {
                moveTo(size.width * 0.22f, size.height * 0.52f)
                lineTo(size.width * 0.42f, size.height * 0.72f)
                lineTo(size.width * 0.80f, size.height * 0.28f)
            }

            drawPath(
                path = path,
                color = checkmarkColor.copy(alpha = checkmarkAlpha),
                style = Stroke(
                    width = size.width * 0.14f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}

@Composable
fun HyperRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedColor: Color = Color.Unspecified,
    unselectedColor: Color = Color.Unspecified,
    unselectedBorderColor: Color = Color.Unspecified,
    innerDotColor: Color = rgba(255, 255, 255, 1f)
) {
    val enabledAlpha = if (enabled) 1f else HyperStyleDefaults.DisabledAlpha
    // 对齐 HyperIconButton 玻璃托盘范式：
    // 标记使用默认背景 → 禁用态 disabledContainer 回退；outer 可见背景叠 glass 高光；保留未选中 border 语义
    val usesDefaultSelected = selectedColor == Color.Unspecified
    val usesDefaultUnselected = unselectedColor == Color.Unspecified
    val resolvedSelectedColor = if (usesDefaultSelected) {
        HyperColors.accent
    } else {
        selectedColor
    }
    val resolvedUnselectedColor = if (usesDefaultUnselected) {
        HyperColors.elevatedContainer
    } else {
        unselectedColor
    }
    val resolvedUnselectedBorderColor = if (unselectedBorderColor == Color.Unspecified) {
        HyperColors.accent
    } else {
        unselectedBorderColor
    }
    val backgroundColor by animateColorAsState(
        targetValue = if (enabled) {
            if (selected) resolvedSelectedColor else resolvedUnselectedColor
        } else if (if (selected) usesDefaultSelected else usesDefaultUnselected) {
            HyperColors.disabledContainer
        } else {
            (if (selected) resolvedSelectedColor else resolvedUnselectedColor).copy(
                alpha = (if (selected) resolvedSelectedColor else resolvedUnselectedColor).alpha * enabledAlpha
            )
        },
        label = "hyperRadioBackgroundColor"
    )
    val hasVisibleBackground = (if (selected) resolvedSelectedColor else resolvedUnselectedColor).alpha > 0f
    val highlightModifier = if (hasVisibleBackground) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
    }
    val borderColor by animateColorAsState(
        targetValue = (if (selected) {
            Color.Transparent
        } else {
            resolvedUnselectedBorderColor
        }).copy(alpha = if (selected) 0f else enabledAlpha),
        label = "hyperRadioBorderColor"
    )
    val innerDotHasVisibleBackground = innerDotColor.alpha > 0f
    val innerDotHighlightModifier = if (innerDotHasVisibleBackground) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
    }
    val innerDotSize by animateDpAsState(
        targetValue = if (selected) HyperRadioDefaults.InnerDotSize else 0.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessMedium,
            dampingRatio = Spring.DampingRatioNoBouncy
        ),
        label = "hyperRadioInnerDotSize"
    )

    Box(
        modifier = modifier
            .size(HyperRadioDefaults.OuterSize)
            .clip(CircleShape)
            .background(backgroundColor)
            .then(highlightModifier)
            .border(
                border = BorderStroke(
                    width = HyperRadioDefaults.BorderWidth,
                    color = borderColor
                ),
                shape = CircleShape
            )
            .hyperNoRippleClickable(
                enabled = enabled && onClick != null,
                role = Role.RadioButton,
                onClick = { onClick?.invoke() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(innerDotSize)
                .clip(CircleShape)
                .background(innerDotColor.copy(alpha = enabledAlpha))
                .then(innerDotHighlightModifier)
        )
    }
}

object HyperSwitchDefaults {
    val TrackWidth = 54.dp
    val TrackHeight = 32.dp
    val ThumbSize = 28.dp
    val ThumbElevation = 2.dp
}

object HyperCheckboxDefaults {
    val BoxSize = 24.dp
    val CornerRadius = 8.dp
    val BorderWidth = 2.dp
    val CheckmarkSize = 16.dp
}

object HyperRadioDefaults {
    val OuterSize = 24.dp
    val InnerDotSize = 10.dp
    val BorderWidth = 2.dp
}
