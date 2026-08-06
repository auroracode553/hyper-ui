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
    val resolvedCheckedTrackColor = if (checkedTrackColor == Color.Unspecified) {
        HyperColors.accent
    } else {
        checkedTrackColor
    }
    val resolvedUncheckedTrackColor = if (uncheckedTrackColor == Color.Unspecified) {
        HyperColors.softContainer
    } else {
        uncheckedTrackColor
    }
    val thumbProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessMedium,
            dampingRatio = Spring.DampingRatioNoBouncy
        ),
        label = "hyperSwitchThumbProgress"
    )
    val trackColor by animateColorAsState(
        targetValue = (if (checked) resolvedCheckedTrackColor else resolvedUncheckedTrackColor).copy(alpha = enabledAlpha),
        label = "hyperSwitchTrackColor"
    )
    val thumbColor by animateColorAsState(
        targetValue = (if (checked) checkedThumbColor else uncheckedThumbColor).copy(alpha = enabledAlpha),
        label = "hyperSwitchThumbColor"
    )

    Box(
        modifier = modifier
            .width(HyperSwitchDefaults.TrackWidth)
            .height(HyperSwitchDefaults.TrackHeight)
            .clip(RoundedCornerShape(percent = 50))
            .background(trackColor)
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
    val resolvedCheckedColor = if (checkedColor == Color.Unspecified) {
        HyperColors.accent
    } else {
        checkedColor
    }
    val resolvedUncheckedColor = if (uncheckedColor == Color.Unspecified) {
        HyperColors.cardContainer
    } else {
        uncheckedColor
    }
    val resolvedUncheckedBorderColor = if (uncheckedBorderColor == Color.Unspecified) {
        HyperColors.accent
    } else {
        uncheckedBorderColor
    }
    val backgroundColor by animateColorAsState(
        targetValue = (if (checked) resolvedCheckedColor else resolvedUncheckedColor).copy(alpha = enabledAlpha),
        label = "hyperCheckboxBackgroundColor"
    )
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
    val resolvedSelectedColor = if (selectedColor == Color.Unspecified) {
        HyperColors.accent
    } else {
        selectedColor
    }
    val resolvedUnselectedColor = if (unselectedColor == Color.Unspecified) {
        HyperColors.cardContainer
    } else {
        unselectedColor
    }
    val resolvedUnselectedBorderColor = if (unselectedBorderColor == Color.Unspecified) {
        HyperColors.accent
    } else {
        unselectedBorderColor
    }
    val backgroundColor by animateColorAsState(
        targetValue = (if (selected) resolvedSelectedColor else resolvedUnselectedColor).copy(alpha = enabledAlpha),
        label = "hyperRadioBackgroundColor"
    )
    val borderColor by animateColorAsState(
        targetValue = (if (selected) {
            Color.Transparent
        } else {
            resolvedUnselectedBorderColor
        }).copy(alpha = if (selected) 0f else enabledAlpha),
        label = "hyperRadioBorderColor"
    )
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
