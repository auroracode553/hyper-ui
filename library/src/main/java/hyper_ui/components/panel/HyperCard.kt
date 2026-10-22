/** 文件职责：在 hyper_ui 中负责提供 library/src/main/java/hyper_ui/components/panel/HyperCard 可复用界面组件及交互封装。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.CompositionLocalProvider
import hyper_ui.core.interaction.hyperNoRippleClickable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Immutable
data class HyperCardColors(
    val containerColor: Color
)

/**
 * 面板容器组件。
 *
 * modifier 控制面板外壳，contentModifier 控制内部内容区。
 */
@Composable
fun HyperCard(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier.padding(HyperCardDefaults.ContentPadding),
    selected: Boolean = false,
    onPressed: (() -> Unit)? = null,
    colors: HyperCardColors = HyperCardDefaults.colors(selected = selected),
    shape: Shape = HyperCardDefaults.Shape,
    elevation: Dp = HyperCardDefaults.Elevation,
    border: BorderStroke? = HyperCardDefaults.border(selected = selected),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(HyperCardDefaults.ContentSpacing),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    title: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
    child: (@Composable ColumnScope.() -> Unit)? = null
) {
    CompositionLocalProvider(LocalHyperSurfacePresent provides true) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onPressed != null) Modifier.hyperNoRippleClickable(onClick = onPressed) else Modifier)
            .hyperFrostedSurface(
                containerColor = colors.containerColor,
                shape = shape,
                elevation = elevation,
                border = border
            )
    ) {
        Column(
            modifier = Modifier.then(contentModifier).fillMaxWidth(),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement,
        ) {
            if (title != null || subtitle != null || leading != null || actions != null) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                    if (leading != null) { leading(); Spacer(Modifier.width(12.dp)) }
                    Column(Modifier.weight(1f)) {
                        if (title != null) CompositionLocalProvider(
                            LocalHyperTextStyle provides HyperTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp, lineHeight = 17.5.sp, fontWeight = FontWeight.SemiBold)
                        ) { title() }
                        if (subtitle != null) {
                            Spacer(Modifier.height(2.dp))
                            CompositionLocalProvider(LocalHyperContentColor provides HyperColors.secondaryText,
                                LocalHyperTextStyle provides HyperTheme.typography.bodyMedium.copy(fontSize = 13.sp, lineHeight = 16.9.sp)
                            ) { subtitle() }
                        }
                    }
                    if (actions != null) {
                        Spacer(Modifier.width(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), content = actions)
                    }
                }
            }
            child?.invoke(this)
            if (footer != null) {
                HyperDivider()
                footer()
            }
        }
    }
    }
}

object HyperCardDefaults {
    val Shape: Shape = RoundedCornerShape(HyperStyleDefaults.LargeCornerRadius)
    val Elevation = 12.dp
    // 参考 cardPadding = 12；边框仅绘制，不额外占位。
    val ContentPadding = PaddingValues(12.dp)
    val ContentSpacing = 12.dp

    @Composable
    fun colors(containerColor: Color = Color.Unspecified, selected: Boolean = false): HyperCardColors = HyperCardColors(
        containerColor = resolveHyperContainerColor(
            containerColor = containerColor,
            fallbackColor = if (selected) hyperGlass.surfaceStrong else hyperGlass.surface
        )
    )

    @Composable
    fun border(color: Color = Color.Unspecified, selected: Boolean = false): BorderStroke =
        BorderStroke(1.dp, resolveHyperContainerColor(color, if (selected) HyperColors.accent else hyperGlass.border))
}
