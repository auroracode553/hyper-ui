package hyper_ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import hyper_ui.core.interaction.hyperNoRippleClickable

enum class HyperDrawerPosition {
    Left,
    Right,
    Top,
    Bottom
}

@Composable
fun HyperDrawer(
    open: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    position: HyperDrawerPosition = HyperDrawerPosition.Left,
    drawerWidth: Dp = HyperDrawerDefaults.Width,
    drawerHeight: Dp = HyperDrawerDefaults.Height,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp),
    containerColor: Color = Color.Unspecified,
    scrimColor: Color = Color.Transparent,
    dismissOnClickOutside: Boolean = false,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val usesDefaultContainerColor = containerColor == Color.Unspecified
    val resolvedContainerColor = if (usesDefaultContainerColor) {
        HyperColors.elevatedContainer
    } else {
        containerColor
    }
    val hasVisibleBackground = resolvedContainerColor.alpha > 0f
    val highlightModifier = if (hasVisibleBackground) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
    }
    val drawerAlignment = when (position) {
        HyperDrawerPosition.Left -> Alignment.CenterStart
        HyperDrawerPosition.Right -> Alignment.CenterEnd
        HyperDrawerPosition.Top -> Alignment.TopCenter
        HyperDrawerPosition.Bottom -> Alignment.BottomCenter
    }

    Box(modifier = modifier.fillMaxSize()) {
        content()

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(HyperDrawerDefaults.DrawerZIndex)
        ) {
            if (open && dismissOnClickOutside) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .hyperNoRippleClickable(onClick = onDismissRequest)
                )
            }

            val maxDrawerWidth = maxWidth * HyperDrawerDefaults.MaxWidthFraction
            val maxDrawerHeight = maxHeight * HyperDrawerDefaults.MaxHeightFraction
            val resolvedDrawerWidth = drawerWidth.coerceAtMost(maxDrawerWidth)
            val resolvedDrawerHeight = drawerHeight.coerceAtMost(maxDrawerHeight)
            val drawerSizeModifier = when (position) {
                HyperDrawerPosition.Left,
                HyperDrawerPosition.Right -> Modifier
                    .fillMaxHeight()
                    .width(resolvedDrawerWidth)

                HyperDrawerPosition.Top,
                HyperDrawerPosition.Bottom -> Modifier
                    .fillMaxWidth()
                    .height(resolvedDrawerHeight)
            }

            AnimatedVisibility(
                visible = open,
                modifier = Modifier.align(drawerAlignment),
                enter = drawerEnterTransition(position),
                exit = drawerExitTransition(position)
            ) {
                Column(
                    modifier = drawerSizeModifier
                        .clip(drawerShape(position))
                        .background(resolvedContainerColor)
                        .then(highlightModifier)
                        .padding(contentPadding),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    content = drawerContent
                )
            }
        }
    }
}

@Composable
fun HyperDrawerHeader(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    leadingIcon: ImageVector? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            DrawerIconBadge(imageVector = leadingIcon)
            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = HyperColors.primaryText,
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Bold
            )
            if (!description.isNullOrBlank()) {
                Text(
                    text = description,
                    color = HyperColors.secondaryText,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun HyperDrawerItem(
    title: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    description: String? = null,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    showDivider: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailing: @Composable RowScope.() -> Unit = {}
) {
    val enabledAlpha = if (enabled) 1f else HyperStyleDefaults.DisabledAlpha
    val itemBackground = if (selected) {
        HyperColors.accent.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }
    val itemHighlightModifier = if (selected) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
    }
    val titleColor = if (selected) HyperColors.accent else HyperColors.primaryText
    val rowClickModifier = if (onClick != null) {
        Modifier.hyperNoRippleClickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(HyperStyleDefaults.SmallCornerRadius))
                .background(itemBackground)
                .then(itemHighlightModifier)
                .then(rowClickModifier)
                .heightIn(min = HyperDrawerDefaults.ItemMinHeight)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = titleColor.copy(alpha = enabledAlpha),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = title,
                    color = titleColor.copy(alpha = enabledAlpha),
                    fontSize = 16.sp,
                    lineHeight = 21.sp,
                    fontWeight = FontWeight.Medium
                )
                if (!description.isNullOrBlank()) {
                    Text(
                        text = description,
                        color = HyperColors.secondaryText.copy(alpha = enabledAlpha),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = trailing
            )
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                color = HyperColors.divider
            )
        }
    }
}

@Composable
private fun DrawerIconBadge(
    imageVector: ImageVector
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(HyperStyleDefaults.MediumCornerRadius))
            .background(HyperColors.accent.copy(alpha = 0.12f))
            .background(HyperColors.glassHighlightBrush),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = HyperColors.accent,
            modifier = Modifier.size(22.dp)
        )
    }
}

private fun drawerShape(position: HyperDrawerPosition): RoundedCornerShape {
    val radius = HyperStyleDefaults.ExtraLargeCornerRadius
    return when (position) {
        HyperDrawerPosition.Left -> RoundedCornerShape(
            topEnd = radius,
            bottomEnd = radius
        )
        HyperDrawerPosition.Right -> RoundedCornerShape(
            topStart = radius,
            bottomStart = radius
        )
        HyperDrawerPosition.Top -> RoundedCornerShape(
            bottomStart = radius,
            bottomEnd = radius
        )
        HyperDrawerPosition.Bottom -> RoundedCornerShape(
            topStart = radius,
            topEnd = radius
        )
    }
}

private fun drawerEnterTransition(position: HyperDrawerPosition) = when (position) {
    HyperDrawerPosition.Left -> slideInHorizontally(
        animationSpec = tween(HyperDrawerDefaults.AnimationMillis),
        initialOffsetX = { fullWidth -> -fullWidth }
    )
    HyperDrawerPosition.Right -> slideInHorizontally(
        animationSpec = tween(HyperDrawerDefaults.AnimationMillis),
        initialOffsetX = { fullWidth -> fullWidth }
    )
    HyperDrawerPosition.Top -> slideInVertically(
        animationSpec = tween(HyperDrawerDefaults.AnimationMillis),
        initialOffsetY = { fullHeight -> -fullHeight }
    )
    HyperDrawerPosition.Bottom -> slideInVertically(
        animationSpec = tween(HyperDrawerDefaults.AnimationMillis),
        initialOffsetY = { fullHeight -> fullHeight }
    )
} + fadeIn(animationSpec = tween(HyperDrawerDefaults.AnimationMillis))

private fun drawerExitTransition(position: HyperDrawerPosition) = when (position) {
    HyperDrawerPosition.Left -> slideOutHorizontally(
        animationSpec = tween(HyperDrawerDefaults.AnimationMillis),
        targetOffsetX = { fullWidth -> -fullWidth }
    )
    HyperDrawerPosition.Right -> slideOutHorizontally(
        animationSpec = tween(HyperDrawerDefaults.AnimationMillis),
        targetOffsetX = { fullWidth -> fullWidth }
    )
    HyperDrawerPosition.Top -> slideOutVertically(
        animationSpec = tween(HyperDrawerDefaults.AnimationMillis),
        targetOffsetY = { fullHeight -> -fullHeight }
    )
    HyperDrawerPosition.Bottom -> slideOutVertically(
        animationSpec = tween(HyperDrawerDefaults.AnimationMillis),
        targetOffsetY = { fullHeight -> fullHeight }
    )
} + fadeOut(animationSpec = tween(HyperDrawerDefaults.AnimationMillis))

object HyperDrawerDefaults {
    val Width = 320.dp
    val Height = 320.dp
    val ItemMinHeight = 54.dp
    @Deprecated("HyperDrawer no longer renders a scrim; kept only for source compatibility.")
    val ScrimColor = Color.Transparent
    const val MaxWidthFraction = 0.88f
    const val MaxHeightFraction = 0.88f
    const val AnimationMillis = 240
    @Deprecated("HyperDrawer no longer renders a scrim; kept only for source compatibility.")
    const val ScrimZIndex = 8f
    const val DrawerZIndex = 9f
}
