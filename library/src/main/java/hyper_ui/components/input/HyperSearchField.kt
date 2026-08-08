package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HyperSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索",
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val shape = RoundedCornerShape(HyperStyleDefaults.MediumCornerRadius)
    val visuals = hyperInputFieldVisuals(focused = focused, enabled = enabled)
    val hintColor = HyperColors.secondaryText
    val containerHasVisibleBackground = visuals.containerColor.alpha > 0f
    val containerHighlightModifier = if (containerHasVisibleBackground) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = HyperColors.primaryText.copy(alpha = visuals.contentAlpha),
            fontSize = 16.sp,
            lineHeight = 22.sp
        ),
        cursorBrush = SolidColor(HyperColors.accent),
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp)
                    .clip(shape)
                    .background(visuals.containerColor)
                    .background(visuals.focusOverlayColor)
                    .then(containerHighlightModifier)
                    .padding(start = 16.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = hintColor.copy(alpha = visuals.contentAlpha),
                    modifier = Modifier.size(20.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = hintColor.copy(alpha = visuals.contentAlpha),
                            fontSize = 16.sp,
                            lineHeight = 22.sp
                        )
                    }
                    innerTextField()
                }
                if (value.isNotEmpty()) {
                    HyperIconButton(
                        imageVector = Icons.Default.Close,
                        contentDescription = "清空搜索",
                        onClick = { onValueChange("") },
                        tint = hintColor,
                        backgroundColor = HyperColors.elevatedContainer,
                        size = 32.dp,
                        iconSize = 18.dp
                    )
                }
            }
        }
    )
}
