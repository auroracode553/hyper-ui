package hyper_ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import hyper_ui.core.interaction.hyperNoRippleClickable

enum class HyperDrawerPosition {
    Left,
    Right,
    Top,
    Bottom
}

@Immutable
data class HyperDrawerColors(
    val containerColor: Color,
    val contentColor: Color,
    val supportingColor: Color,
    val selectedContainerColor: Color,
    val selectedContentColor: Color,
    val disabledContentColor: Color,
    val dividerColor: Color
)

@Composable
fun HyperDrawer(
    open: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    position: HyperDrawerPosition = HyperDrawerPosition.Left,
    drawerWidth: Dp = HyperDrawerDefaults.Width,
    drawerHeight: Dp = HyperDrawerDefaults.Height,
    contentPadding: PaddingValues = HyperDrawerDefaults.ContentPadding,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    dismissOnClickOutside: Boolean = false,
    drawerContent: @Composable ColumnScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
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
                        .hyperGlassSurface(
                            containerColor = colors.containerColor,
                            shape = drawerShape(position)
                        )
                        .padding(contentPadding),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
                        drawerContent()
                    }
                }
            }
        }
    }
}

@Composable
fun HyperDrawerHeader(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = HyperDrawerDefaults.HeaderPadding,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    headlineContent: @Composable ColumnScope.() -> Unit,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent?.invoke(this)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = if (leadingContent == null) 0.dp else 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            headlineContent()
            supportingContent?.invoke(this)
        }
    }
}

@Composable
fun HyperDrawerItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    dividerVisible: Boolean = false,
    minHeight: Dp = HyperDrawerDefaults.ItemMinHeight,
    contentPadding: PaddingValues = HyperDrawerDefaults.ItemPadding,
    colors: HyperDrawerColors = HyperDrawerDefaults.colors(),
    onClick: (() -> Unit)? = null,
    leadingContent: (@Composable RowScope.() -> Unit)? = null,
    headlineContent: @Composable ColumnScope.() -> Unit,
    supportingContent: (@Composable ColumnScope.() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
) {
    val rowClickModifier = if (onClick != null) {
        Modifier.hyperNoRippleClickable(
            enabled = enabled,
            role = Role.Button,
            onClick = onClick
        )
    } else {
        Modifier
    }
    val containerColor = if (selected) colors.selectedContainerColor else Color.Transparent
    val contentColor = when {
        !enabled -> colors.disabledContentColor
        selected -> colors.selectedContentColor
        else -> colors.contentColor
    }
    val supportingColor = if (enabled) colors.supportingColor else colors.disabledContentColor

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .hyperGlassSurface(
                    containerColor = containerColor,
                    shape = RoundedCornerShape(HyperStyleDefaults.SmallCornerRadius)
                )
                .then(rowClickModifier)
                .heightIn(min = minHeight)
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingContent != null) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    leadingContent()
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = if (leadingContent == null) 0.dp else 14.dp,
                        end = if (trailingContent == null) 0.dp else 14.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    headlineContent()
                }
                if (supportingContent != null) {
                    CompositionLocalProvider(LocalContentColor provides supportingColor) {
                        supportingContent()
                    }
                }
            }

            if (trailingContent != null) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    trailingContent()
                }
            }
        }

        if (dividerVisible) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                color = colors.dividerColor
            )
        }
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
    val ContentPadding = PaddingValues(vertical = 16.dp)
    val HeaderPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
    val ItemPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
    val ItemMinHeight = 54.dp
    const val MaxWidthFraction = 0.88f
    const val MaxHeightFraction = 0.88f
    const val AnimationMillis = 240
    const val DrawerZIndex = 9f

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        supportingColor: Color = Color.Unspecified,
        selectedContainerColor: Color = Color.Unspecified,
        selectedContentColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
        dividerColor: Color = Color.Unspecified
    ): HyperDrawerColors {
        val resolvedContentColor = resolveHyperContainerColor(contentColor, HyperColors.primaryText)

        return HyperDrawerColors(
            containerColor = resolveHyperContainerColor(containerColor, HyperColors.elevatedContainer),
            contentColor = resolvedContentColor,
            supportingColor = resolveHyperContainerColor(supportingColor, HyperColors.secondaryText),
            selectedContainerColor = resolveHyperContainerColor(
                selectedContainerColor,
                HyperColors.accent.copy(alpha = 0.12f)
            ),
            selectedContentColor = resolveHyperContainerColor(selectedContentColor, HyperColors.accent),
            disabledContentColor = resolveHyperContainerColor(
                disabledContentColor,
                resolvedContentColor.copy(alpha = HyperStyleDefaults.DisabledAlpha)
            ),
            dividerColor = resolveHyperContainerColor(dividerColor, HyperColors.divider)
        )
    }
}
