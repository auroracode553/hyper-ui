/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/components/selection/HyperSelectionControls 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
    val trackShape = RoundedCornerShape(percent = 50)
    val resolvedCheckedTrackColor = if (checkedTrackColor == Color.Unspecified) {
        HyperColors.accent
    } else {
        checkedTrackColor
    }
    val resolvedUncheckedTrackColor = if (uncheckedTrackColor == Color.Unspecified) {
        HyperColors.elevatedContainer
    } else {
        uncheckedTrackColor
    }
    val trackColor = if (!enabled) {
        HyperColors.disabledContainer
    } else if (checked) {
        resolvedCheckedTrackColor
    } else {
        resolvedUncheckedTrackColor
    }
    val thumbColor = if (!enabled) {
        HyperColors.disabledText
    } else if (checked) {
        checkedThumbColor
    } else {
        uncheckedThumbColor
    }
    val trackBorderColor = if (enabled) HyperColors.fieldBorder else HyperColors.divider
    val thumbBorderColor = if (enabled) HyperColors.fieldBorder else HyperColors.divider
    val thumbProgress = if (checked) 1f else 0f

    Box(
        modifier = modifier
            .width(HyperSwitchDefaults.TrackWidth)
            .height(HyperSwitchDefaults.TrackHeight)
            .shadow(
                elevation = HyperSwitchDefaults.TrackElevation,
                shape = trackShape,
                clip = false
            )
            .background(trackColor, trackShape)
            .border(
                border = BorderStroke(
                    width = HyperSwitchDefaults.TrackBorderWidth,
                    color = trackBorderColor
                ),
                shape = trackShape
            )
            .hyperNoRippleClickable(
                enabled = enabled,
                role = Role.Switch,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        val thumbTravel = HyperSwitchDefaults.TrackWidth -
            HyperSwitchDefaults.ThumbSize -
            HyperSwitchDefaults.TrackPadding * 2f

        Box(
            modifier = Modifier
                .offset(x = HyperSwitchDefaults.TrackPadding + thumbTravel * thumbProgress)
                .size(HyperSwitchDefaults.ThumbSize)
                .shadow(
                    elevation = HyperSwitchDefaults.ThumbElevation,
                    shape = CircleShape,
                    clip = false
                )
                .clip(CircleShape)
                .background(thumbColor)
                .border(
                    border = BorderStroke(
                        width = HyperSwitchDefaults.ThumbBorderWidth,
                        color = thumbBorderColor
                    ),
                    shape = CircleShape
                )
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
    val resolvedCheckedColor = if (checkedColor == Color.Unspecified) {
        HyperColors.accent
    } else {
        checkedColor
    }
    val resolvedUncheckedColor = if (uncheckedColor == Color.Unspecified) {
        HyperColors.elevatedContainer
    } else {
        uncheckedColor
    }
    val resolvedUncheckedBorderColor = if (uncheckedBorderColor == Color.Unspecified) {
        HyperColors.accent
    } else {
        uncheckedBorderColor
    }
    val backgroundColor = if (!enabled) {
        HyperColors.disabledContainer
    } else if (checked) {
        resolvedCheckedColor
    } else {
        resolvedUncheckedColor
    }
    val borderColor = if (enabled) resolvedUncheckedBorderColor else HyperColors.divider
    val checkmarkSize = if (checked) HyperCheckboxDefaults.CheckmarkSize else 0.dp
    val resolvedCheckmarkColor = if (enabled) checkmarkColor else HyperColors.disabledText
    val shape = RoundedCornerShape(HyperCheckboxDefaults.CornerRadius)

    Box(
        modifier = modifier
            .size(HyperCheckboxDefaults.BoxSize)
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (checked) {
                    Modifier
                } else {
                    Modifier.border(
                        width = HyperCheckboxDefaults.BorderWidth,
                        color = borderColor,
                        shape = shape
                    )
                }
            )
            .hyperNoRippleClickable(
                enabled = enabled,
                role = Role.Checkbox,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(checkmarkSize)) {
            val path = Path().apply {
                moveTo(size.width * 0.22f, size.height * 0.52f)
                lineTo(size.width * 0.42f, size.height * 0.72f)
                lineTo(size.width * 0.80f, size.height * 0.28f)
            }

            drawPath(
                path = path,
                color = resolvedCheckmarkColor,
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
    val resolvedSelectedColor = if (selectedColor == Color.Unspecified) {
        HyperColors.accent
    } else {
        selectedColor
    }
    val resolvedUnselectedColor = if (unselectedColor == Color.Unspecified) {
        HyperColors.elevatedContainer
    } else {
        unselectedColor
    }
    val resolvedUnselectedBorderColor = if (unselectedBorderColor == Color.Unspecified) {
        HyperColors.accent
    } else {
        unselectedBorderColor
    }
    val backgroundColor = if (!enabled) {
        HyperColors.disabledContainer
    } else if (selected) {
        resolvedSelectedColor
    } else {
        resolvedUnselectedColor
    }
    val borderColor = if (enabled) resolvedUnselectedBorderColor else HyperColors.divider
    val innerDotSize = if (selected) HyperRadioDefaults.InnerDotSize else 0.dp
    val resolvedInnerDotColor = if (enabled) innerDotColor else HyperColors.disabledText

    Box(
        modifier = modifier
            .size(HyperRadioDefaults.OuterSize)
            .clip(CircleShape)
            .background(backgroundColor)
            .then(
                if (selected) {
                    Modifier
                } else {
                    Modifier.border(
                        width = HyperRadioDefaults.BorderWidth,
                        color = borderColor,
                        shape = CircleShape
                    )
                }
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
                .background(resolvedInnerDotColor)
        )
    }
}

object HyperSwitchDefaults {
    val TrackWidth = 54.dp
    val TrackHeight = 32.dp
    val TrackPadding = 2.dp
    val TrackElevation = 1.dp
    val TrackBorderWidth = 1.dp
    val ThumbSize = 28.dp
    val ThumbElevation = 2.dp
    val ThumbBorderWidth = 1.dp
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
