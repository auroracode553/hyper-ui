package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import hyper_ui.core.interaction.hyperNoRippleClickable

@Composable
fun HyperDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopEnd,
    offset: DpOffset = DpOffset(0.dp, HyperDropdownMenuDefaults.AnchorOffsetY),
    width: Dp = HyperDropdownMenuDefaults.MenuWidth,
    maxHeight: Dp = HyperDropdownMenuDefaults.MaxHeight,
    content: @Composable HyperDropdownMenuScope.() -> Unit
) {
    if (!expanded) {
        return
    }

    val shape = RoundedCornerShape(HyperStyleDefaults.LargeCornerRadius)
    val intOffset = LocalDensity.current.run {
        IntOffset(offset.x.roundToPx(), offset.y.roundToPx())
    }

    Popup(
        alignment = alignment,
        offset = intOffset,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true)
    ) {
        Box(modifier = Modifier.padding(end = 8.dp)) {
            Column(
                modifier = modifier
                    .width(width)
                    .heightIn(max = maxHeight)
                    .clip(shape)
                    .background(HyperColors.cardContainer)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp)
            ) {
                val scope = remember(onDismissRequest) {
                    HyperDropdownMenuScope(onDismissRequest)
                }
                scope.content()
            }
        }
    }
}

class HyperDropdownMenuScope internal constructor(
    private val onDismiss: () -> Unit
) {
    @Composable
    fun Item(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        leadingIcon: ImageVector? = null,
        textColor: Color = Color.Unspecified
    ) {
        val resolvedTextColor = if (textColor == Color.Unspecified) {
            HyperColors.primaryText
        } else {
            textColor
        }
        val enabledAlpha = if (enabled) 1f else HyperStyleDefaults.DisabledAlpha

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(HyperDropdownMenuDefaults.ItemHeight)
                .hyperNoRippleClickable(
                    enabled = enabled,
                    onClick = {
                        onClick()
                        onDismiss()
                    }
                )
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = resolvedTextColor.copy(alpha = enabledAlpha),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = text,
                color = resolvedTextColor.copy(alpha = enabledAlpha),
                fontSize = 15.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

    @Composable
    fun Divider(modifier: Modifier = Modifier) {
        HorizontalDivider(
            modifier = modifier.padding(horizontal = 20.dp, vertical = 6.dp),
            color = HyperColors.divider
        )
    }
}

object HyperDropdownMenuDefaults {
    val MenuWidth = 184.dp
    val MaxHeight = 420.dp
    val ItemHeight = 48.dp
    val AnchorOffsetY = 52.dp
}
