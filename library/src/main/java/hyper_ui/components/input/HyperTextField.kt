package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HyperTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    minHeight: Dp = 52.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val shape = RoundedCornerShape(HyperStyleDefaults.MediumCornerRadius)
    val visuals = hyperInputFieldVisuals(focused = focused, enabled = enabled)
    val verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top

    Column(modifier = modifier) {
        if (!label.isNullOrBlank()) {
            Text(
                text = label,
                color = HyperColors.secondaryText.copy(alpha = visuals.contentAlpha),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(start = 18.dp, bottom = 6.dp)
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = singleLine,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = HyperColors.primaryText.copy(alpha = visuals.contentAlpha),
                fontSize = 16.sp,
                lineHeight = 22.sp
            ),
            cursorBrush = SolidColor(HyperColors.accent),
            interactionSource = interactionSource,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = minHeight)
                        .clip(shape)
                        .background(visuals.containerColor)
                        .background(visuals.focusOverlayColor)
                        .border(
                            width = visuals.outlineWidth,
                            color = visuals.outlineColor,
                            shape = shape
                        )
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalAlignment = verticalAlignment
                ) {
                    if (leadingContent != null) {
                        leadingContent()
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                start = if (leadingContent == null) 0.dp else 10.dp,
                                end = if (trailingContent == null) 0.dp else 10.dp
                            ),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty() && !placeholder.isNullOrBlank()) {
                            Text(
                                text = placeholder,
                                color = HyperColors.secondaryText.copy(alpha = visuals.contentAlpha),
                                fontSize = 16.sp,
                                lineHeight = 22.sp
                            )
                        }
                        innerTextField()
                    }

                    if (trailingContent != null) {
                        trailingContent()
                    }
                }
            }
        )
    }
}
