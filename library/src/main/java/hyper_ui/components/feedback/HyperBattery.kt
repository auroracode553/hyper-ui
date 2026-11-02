/** 文件职责：提供电量内显、充电图标外置的紧凑型系统电池指示器。 */
package hyper_ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Zap

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class HyperBatteryColors(
    val containerColor: Color,
    val levelColor: Color,
    val lowLevelColor: Color,
    val chargingLevelColor: Color,
    val percentageColor: Color,
    val terminalColor: Color,
    val chargingIconColor: Color
)

/**
 * 紧凑电池状态组件。电量文字位于电池内部，充电闪电位于电池右侧。
 * 系统电量监听属于调用方职责，组件只渲染传入状态。
 */
@Composable
fun HyperBattery(
    percentage: Int,
    charging: Boolean,
    modifier: Modifier = Modifier,
    showPercentage: Boolean = true,
    contentDescription: String? = null,
    shape: Shape = HyperBatteryDefaults.Shape,
    colors: HyperBatteryColors = HyperBatteryDefaults.colors()
) {
    val resolvedPercentage = percentage.coerceIn(0, 100)
    val resolvedContentDescription = contentDescription
    val fillColor = when {
        charging -> colors.chargingLevelColor
        resolvedPercentage <= HyperBatteryDefaults.LowLevelThreshold -> colors.lowLevelColor
        else -> colors.levelColor
    }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = HyperBatteryDefaults.Height)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = resolvedPercentage.toFloat(),
                    range = 0f..100f
                )
                if (resolvedContentDescription != null) {
                    this.contentDescription = resolvedContentDescription
                }
            },
        horizontalArrangement = Arrangement.spacedBy(HyperBatteryDefaults.ElementSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(HyperBatteryDefaults.TerminalSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(HyperBatteryDefaults.BodyWidth)
                    .height(HyperBatteryDefaults.Height)
                    .hyperGlassSurface(
                        shape = shape,
                        visuals = hyperGlassSurfaceVisuals(
                            containerColor = colors.containerColor,
                            elevation = HyperBatteryDefaults.Elevation,

                        )
                    )
                    .padding(HyperBatteryDefaults.BodyInset)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .fillMaxWidth(resolvedPercentage / 100f)
                        .clip(HyperBatteryDefaults.LevelShape)
                        .background(fillColor, HyperBatteryDefaults.LevelShape)
                )
                if (showPercentage) {
                    HyperText(
                        text = "$resolvedPercentage%",
                        modifier = Modifier.align(Alignment.Center),
                        color = colors.percentageColor,
                        style = TextStyle(
                            fontSize = 9.sp,
                            lineHeight = 9.sp,
                            fontWeight = FontWeight.Bold,
                            shadow = Shadow(
                                color = Color(0f, 0f, 0f, 0.72f),
                                offset = Offset(0f, 1f),
                                blurRadius = 2f
                            )
                        ),
                        maxLines = 1
                    )
                }
            }
            Box(
                modifier = Modifier
                    .width(HyperBatteryDefaults.TerminalWidth)
                    .height(HyperBatteryDefaults.TerminalHeight)
                    .hyperGlassSurface(
                        shape = HyperBatteryDefaults.TerminalShape,
                        visuals = hyperGlassSurfaceVisuals(
                            containerColor = colors.terminalColor,
                            elevation = HyperBatteryDefaults.TerminalElevation,

                        )
                    )
            )
        }

        if (charging) {
            CompositionLocalProvider(
                LocalHyperContentColor provides colors.chargingIconColor
            ) {
                HyperIcon(Lucide.Zap, null,
                    modifier = Modifier.size(
                        width = HyperBatteryDefaults.ChargingIconWidth,
                        height = HyperBatteryDefaults.ChargingIconHeight
                    )
                )
            }
        }
    }
}

object HyperBatteryDefaults {
    val BodyWidth = 38.dp
    val Height = 18.dp
    val Shape: Shape = RoundedCornerShape(4.dp)
    val LevelShape: Shape = RoundedCornerShape(2.dp)
    val BodyInset = 2.dp
    val Elevation = 1.dp
    val TerminalWidth = 2.dp
    val TerminalHeight = 8.dp
    val TerminalShape: Shape = RoundedCornerShape(percent = 50)
    val TerminalElevation = 1.dp
    val TerminalSpacing = 1.dp
    val ElementSpacing = 4.dp
    val ChargingIconWidth = 14.dp
    val ChargingIconHeight = 14.dp
    const val LowLevelThreshold = 20

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        levelColor: Color = Color.Unspecified,
        lowLevelColor: Color = Color.Unspecified,
        chargingLevelColor: Color = Color.Unspecified,
        percentageColor: Color = Color.Unspecified,
        terminalColor: Color = Color.Unspecified,
        chargingIconColor: Color = Color.Unspecified
    ): HyperBatteryColors = HyperBatteryColors(
        containerColor = resolveHyperContainerColor(
            containerColor,
            hyperSurfaces.surfaceSubtle
        ),
        levelColor = resolveHyperContainerColor(
            levelColor,
            HyperColors.success
        ),
        lowLevelColor = resolveHyperContainerColor(
            lowLevelColor,
            HyperColors.danger
        ),
        chargingLevelColor = resolveHyperContainerColor(
            chargingLevelColor,
            HyperColors.success
        ),
        percentageColor = resolveHyperContainerColor(
            percentageColor,
            Color(1f, 1f, 1f, 1f)
        ),
        terminalColor = resolveHyperContainerColor(
            terminalColor,
            hyperSurfaces.surfaceSubtle
        ),
        chargingIconColor = resolveHyperContainerColor(
            chargingIconColor,
            HyperColors.success
        )
    )
}
