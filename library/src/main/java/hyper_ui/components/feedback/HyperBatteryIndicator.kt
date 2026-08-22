/** 文件职责：提供电量内显、充电图标外置的紧凑型系统电池指示器。 */
package hyper_ui

import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
data class HyperBatteryIndicatorColors(
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
fun HyperBatteryIndicator(
    percentage: Int,
    charging: Boolean,
    modifier: Modifier = Modifier,
    showPercentage: Boolean = true,
    contentDescription: String? = null,
    shape: Shape = HyperBatteryIndicatorDefaults.Shape,
    colors: HyperBatteryIndicatorColors = HyperBatteryIndicatorDefaults.colors()
) {
    val resolvedPercentage = percentage.coerceIn(0, 100)
    val resolvedContentDescription = contentDescription
    val fillColor = when {
        charging -> colors.chargingLevelColor
        resolvedPercentage <= HyperBatteryIndicatorDefaults.LowLevelThreshold -> colors.lowLevelColor
        else -> colors.levelColor
    }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = HyperBatteryIndicatorDefaults.Height)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = resolvedPercentage.toFloat(),
                    range = 0f..100f
                )
                if (resolvedContentDescription != null) {
                    this.contentDescription = resolvedContentDescription
                }
            },
        horizontalArrangement = Arrangement.spacedBy(HyperBatteryIndicatorDefaults.ElementSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(HyperBatteryIndicatorDefaults.TerminalSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(HyperBatteryIndicatorDefaults.BodyWidth)
                    .height(HyperBatteryIndicatorDefaults.Height)
                    .hyperGlassSurface(
                        shape = shape,
                        visuals = hyperGlassSurfaceVisuals(
                            containerColor = colors.containerColor,
                            elevation = HyperBatteryIndicatorDefaults.Elevation,
                            topLightAlpha = if (HyperColors.isLight) 0.34f else 0.13f,
                            bottomShadeAlpha = if (HyperColors.isLight) 0.06f else 0.16f,
                            shadowAlpha = if (HyperColors.isLight) 0.14f else 0.28f
                        )
                    )
                    .padding(HyperBatteryIndicatorDefaults.BodyInset)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxHeight()
                        .fillMaxWidth(resolvedPercentage / 100f)
                        .clip(HyperBatteryIndicatorDefaults.LevelShape)
                        .background(fillColor, HyperBatteryIndicatorDefaults.LevelShape)
                )
                if (showPercentage) {
                    Text(
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
                    .width(HyperBatteryIndicatorDefaults.TerminalWidth)
                    .height(HyperBatteryIndicatorDefaults.TerminalHeight)
                    .hyperGlassSurface(
                        shape = HyperBatteryIndicatorDefaults.TerminalShape,
                        visuals = hyperGlassSurfaceVisuals(
                            containerColor = colors.terminalColor,
                            elevation = HyperBatteryIndicatorDefaults.TerminalElevation,
                            topLightAlpha = if (HyperColors.isLight) 0.28f else 0.12f,
                            bottomShadeAlpha = if (HyperColors.isLight) 0.06f else 0.14f,
                            shadowAlpha = if (HyperColors.isLight) 0.10f else 0.22f
                        )
                    )
            )
        }

        if (charging) {
            ChargingBolt(
                color = colors.chargingIconColor,
                modifier = Modifier.size(
                    width = HyperBatteryIndicatorDefaults.ChargingIconWidth,
                    height = HyperBatteryIndicatorDefaults.ChargingIconHeight
                )
            )
        }
    }
}

@Composable
private fun ChargingBolt(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            moveTo(size.width * 0.58f, 0f)
            lineTo(size.width * 0.18f, size.height * 0.54f)
            lineTo(size.width * 0.49f, size.height * 0.54f)
            lineTo(size.width * 0.36f, size.height)
            lineTo(size.width * 0.84f, size.height * 0.40f)
            lineTo(size.width * 0.53f, size.height * 0.40f)
            close()
        }
        drawPath(path = path, color = color)
    }
}

object HyperBatteryIndicatorDefaults {
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
    val ChargingIconWidth = 9.dp
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
    ): HyperBatteryIndicatorColors = HyperBatteryIndicatorColors(
        containerColor = resolveHyperContainerColor(
            containerColor,
            Color(1f, 1f, 1f, if (HyperColors.isLight) 0.52f else 0.18f)
        ),
        levelColor = resolveHyperContainerColor(
            levelColor,
            Color(0.32f, 0.78f, 0.49f, 0.82f)
        ),
        lowLevelColor = resolveHyperContainerColor(
            lowLevelColor,
            Color(1f, 0.27f, 0.23f, 0.94f)
        ),
        chargingLevelColor = resolveHyperContainerColor(
            chargingLevelColor,
            Color(0.22f, 0.86f, 0.39f, 1f)
        ),
        percentageColor = resolveHyperContainerColor(
            percentageColor,
            Color(1f, 1f, 1f, 1f)
        ),
        terminalColor = resolveHyperContainerColor(
            terminalColor,
            Color(1f, 1f, 1f, if (HyperColors.isLight) 0.72f else 0.40f)
        ),
        chargingIconColor = resolveHyperContainerColor(
            chargingIconColor,
            Color(0.22f, 0.86f, 0.39f, 1f)
        )
    )
}
