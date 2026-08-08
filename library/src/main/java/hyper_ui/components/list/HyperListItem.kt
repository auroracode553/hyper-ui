package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable

@Composable
fun HyperListItem(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    leadingIcon: ImageVector? = null,
    minHeight: Dp = 68.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
    showDivider: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailing: @Composable RowScope.() -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        val rowClickModifier = if (onClick != null) {
            Modifier.hyperNoRippleClickable(onClick = onClick)
        } else {
            Modifier
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .then(rowClickModifier)
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                HyperListItemLeadingIcon(imageVector = leadingIcon)
                Spacer(modifier = Modifier.width(14.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = title,
                    color = HyperColors.primaryText,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium
                )
                if (!description.isNullOrBlank()) {
                    Text(
                        text = description,
                        color = HyperColors.secondaryText,
                        fontSize = 14.sp,
                        lineHeight = 19.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = trailing
            )
        }

        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = if (leadingIcon == null) 20.dp else 70.dp)
                    .height(1.dp)
                    .background(HyperColors.divider)
            )
        }
    }
}

@Composable
private fun HyperListItemLeadingIcon(
    imageVector: ImageVector
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(HyperStyleDefaults.SmallCornerRadius))
            .background(HyperColors.accent.copy(alpha = 0.10f))
            .background(HyperColors.glassHighlightBrush),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = HyperColors.accent,
            modifier = Modifier.size(20.dp)
        )
    }
}
