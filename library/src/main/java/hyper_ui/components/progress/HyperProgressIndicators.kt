/** 文件职责：在 hyper_ui 中负责承载 library/src/main/java/hyper_ui/components/progress/HyperProgressIndicators 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class HyperProgressIndicatorColors(
    val trackColor: Color,
    val indicatorColor: Color
)

@Composable
fun HyperLinearProgressIndicator(
    progress: Float?,
    modifier: Modifier = Modifier,
    shape: Shape = HyperProgressIndicatorDefaults.LinearShape,
    colors: HyperProgressIndicatorColors = HyperProgressIndicatorDefaults.colors(),
    trackBorder: BorderStroke? = HyperProgressIndicatorDefaults.linearTrackBorder()
) {
    val coercedProgress = progress?.coerceIn(0f, 1f)
    val resolvedTrackColor = resolveHyperOpaqueColor(
        color = colors.trackColor,
        fallbackColor = HyperColors.softContainer,
        backgroundColor = HyperColors.pageBackground
    )
    val resolvedIndicatorColor = resolveHyperOpaqueColor(
        color = colors.indicatorColor,
        fallbackColor = HyperColors.accent,
        backgroundColor = resolvedTrackColor
    )
    val semanticsInfo = if (coercedProgress == null) {
        ProgressBarRangeInfo.Indeterminate
    } else {
        ProgressBarRangeInfo(coercedProgress, 0f..1f)
    }

    BoxWithConstraints(
        modifier = modifier
            .height(HyperProgressIndicatorDefaults.LinearHeight)
            .fillMaxWidth()
            .hyperSolidSurface(
                containerColor = resolvedTrackColor,
                shape = shape,
                border = trackBorder
            )
            .semantics {
                progressBarRangeInfo = semanticsInfo
            }
    ) {
        if (coercedProgress == null) {
            IndeterminateLinearSegment(
                indicatorColor = resolvedIndicatorColor,
                segmentWidth = maxWidth * HyperProgressIndicatorDefaults.IndeterminateSegmentFraction,
                segmentShape = shape
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(coercedProgress)
                    .hyperSolidSurface(
                        containerColor = resolvedIndicatorColor,
                        shape = shape
                    )
            )
        }
    }
}

@Composable
fun HyperCircularProgressIndicator(
    progress: Float?,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = HyperProgressIndicatorDefaults.CircularStrokeWidth,
    colors: HyperProgressIndicatorColors = HyperProgressIndicatorDefaults.colors()
) {
    val coercedProgress = progress?.coerceIn(0f, 1f)
    val resolvedTrackColor = resolveHyperOpaqueColor(
        color = colors.trackColor,
        fallbackColor = HyperColors.softContainer,
        backgroundColor = HyperColors.pageBackground
    )
    val resolvedIndicatorColor = resolveHyperOpaqueColor(
        color = colors.indicatorColor,
        fallbackColor = HyperColors.accent,
        backgroundColor = resolvedTrackColor
    )
    val displayedProgress = coercedProgress
        ?: HyperProgressIndicatorDefaults.CircularIndeterminateSweepFraction
    val semanticsInfo = if (coercedProgress == null) {
        ProgressBarRangeInfo.Indeterminate
    } else {
        ProgressBarRangeInfo(coercedProgress, 0f..1f)
    }

    Canvas(
        modifier = modifier
            .size(HyperProgressIndicatorDefaults.CircularSize)
            .semantics {
                progressBarRangeInfo = semanticsInfo
            }
    ) {
        val strokePx = strokeWidth.toPx()
        val inset = strokePx / 2f
        val arcSize = Size(
            width = this.size.width - strokePx,
            height = this.size.height - strokePx
        )
        val sweepAngle = (displayedProgress * 360f).coerceIn(0f, 360f)

        drawCircle(
            color = resolvedTrackColor,
            radius = (this.size.minDimension - strokePx) / 2f,
            style = Stroke(width = strokePx, cap = StrokeCap.Round)
        )
        drawArc(
            color = resolvedIndicatorColor,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = arcSize,
            style = Stroke(width = strokePx, cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun IndeterminateLinearSegment(
    indicatorColor: Color,
    segmentWidth: Dp,
    segmentShape: Shape
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(segmentWidth)
                .offset(x = (maxWidth - segmentWidth) / 2)
                .hyperSolidSurface(
                    containerColor = indicatorColor,
                    shape = segmentShape
                )
        )
    }
}

object HyperProgressIndicatorDefaults {
    val LinearHeight = 4.dp
    val LinearShape: Shape = RoundedCornerShape(percent = 50)
    val CircularSize = 32.dp
    val CircularStrokeWidth = 3.dp
    const val IndeterminateSegmentFraction = 0.36f
    const val CircularIndeterminateSweepFraction = 0.26f

    @Composable
    fun colors(
        trackColor: Color = Color.Unspecified,
        indicatorColor: Color = Color.Unspecified
    ): HyperProgressIndicatorColors {
        val resolvedTrackColor = resolveHyperOpaqueColor(
            color = trackColor,
            fallbackColor = HyperColors.softContainer,
            backgroundColor = HyperColors.pageBackground
        )
        return HyperProgressIndicatorColors(
            trackColor = resolvedTrackColor,
            indicatorColor = resolveHyperOpaqueColor(
                color = indicatorColor,
                fallbackColor = HyperColors.accent,
                backgroundColor = resolvedTrackColor
            )
        )
    }

    @Composable
    fun linearTrackBorder(color: Color = Color.Unspecified): BorderStroke = hyperSolidPanelBorder(
        color = color,
        backgroundColor = HyperColors.softContainer
    )
}
