/** 文件职责：在 hyper_ui 中负责承载 HyperProgress 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui

import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
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
data class HyperProgressColors(
    val trackColor: Color,
    val indicatorColor: Color
)

/** 线性或圆形进度指示器；通过 type 选择渲染形态。 */
@Composable
fun HyperProgress(
    progress: Float? = null,
    modifier: Modifier = Modifier,
    type: String = "linear",
    size: String = "default",
    shape: Shape = HyperProgressDefaults.LinearShape,
    strokeWidth: Dp = HyperProgressDefaults.CircularStrokeWidth,
    colors: HyperProgressColors = HyperProgressDefaults.colors(),
    trackBorder: BorderStroke? = null
) {
    require(type == "linear" || type == "circular") {
        "不支持的 HyperProgress type: $type"
    }
    if (type == "circular") {
        HyperCircularProgress(
            progress = progress,
            modifier = modifier,
            size = size,
            strokeWidth = strokeWidth,
            colors = colors
        )
    } else {
        HyperLinearProgress(
            progress = progress,
            modifier = modifier,
            size = size,
            shape = shape,
            colors = colors,
            trackBorder = trackBorder
        )
    }
}

@Composable
private fun HyperLinearProgress(
    progress: Float? = null,
    modifier: Modifier = Modifier,
    size: String = "default",
    shape: Shape = HyperProgressDefaults.LinearShape,
    colors: HyperProgressColors = HyperProgressDefaults.colors(),
    trackBorder: BorderStroke? = null
) {
    val resolvedHeight = hyperComponentSize(size, 3.dp, HyperProgressDefaults.LinearHeight, 8.dp)
    val coercedProgress = progress?.coerceIn(0f, 1f)
    val resolvedTrackColor = resolveHyperContainerColor(colors.trackColor, hyperGlass.controlTrack)
    val resolvedIndicatorColor = resolveHyperContainerColor(colors.indicatorColor, HyperColors.accent)
    val semanticsInfo = if (coercedProgress == null) {
        ProgressBarRangeInfo.Indeterminate
    } else {
        ProgressBarRangeInfo(coercedProgress, 0f..1f)
    }

    BoxWithConstraints(
        modifier = modifier
            .height(resolvedHeight)
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
                segmentWidth = maxWidth * HyperProgressDefaults.IndeterminateSegmentFraction,
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
private fun HyperCircularProgress(
    progress: Float? = null,
    modifier: Modifier = Modifier,
    size: String = "default",
    strokeWidth: Dp = HyperProgressDefaults.CircularStrokeWidth,
    colors: HyperProgressColors = HyperProgressDefaults.colors()
) {
    val resolvedSize = hyperComponentSize(size, 24.dp, HyperProgressDefaults.CircularSize, 48.dp)
    val coercedProgress = progress?.coerceIn(0f, 1f)
    val resolvedTrackColor = resolveHyperContainerColor(colors.trackColor, hyperGlass.controlTrack)
    val resolvedIndicatorColor = resolveHyperContainerColor(colors.indicatorColor, HyperColors.accent)
    val rotation = if (coercedProgress == null) hyperIndeterminatePhase() * 360f else 0f
    val displayedProgress = coercedProgress
        ?: HyperProgressDefaults.CircularIndeterminateSweepFraction
    val semanticsInfo = if (coercedProgress == null) {
        ProgressBarRangeInfo.Indeterminate
    } else {
        ProgressBarRangeInfo(coercedProgress, 0f..1f)
    }

    Canvas(
        modifier = modifier
            .size(resolvedSize)
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
            startAngle = -90f + rotation,
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
    val phase = hyperIndeterminatePhase()
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(segmentWidth)
                .offset(x = (maxWidth + segmentWidth) * phase - segmentWidth)
                .hyperSolidSurface(
                    containerColor = indicatorColor,
                    shape = segmentShape
                )
        )
    }
}

object HyperProgressDefaults {
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
    ): HyperProgressColors {
        val resolvedTrackColor = resolveHyperContainerColor(trackColor, hyperGlass.controlTrack)
        return HyperProgressColors(
            trackColor = resolvedTrackColor,
            indicatorColor = resolveHyperContainerColor(indicatorColor, HyperColors.accent)
        )
    }

    @Composable
    fun linearTrackBorder(color: Color = Color.Unspecified): BorderStroke = hyperSolidPanelBorder(
        color = color,
        backgroundColor = hyperGlass.controlTrack
    )
}

/** 语义加载动画只在未指定进度时运行，确定进度不创建无限动画。 */
@Composable
private fun hyperIndeterminatePhase(): Float {
    val transition = rememberInfiniteTransition(label = "HyperLoading")
    val phase by transition.animateFloat(0f, 1f,
        infiniteRepeatable(tween(1333, easing = LinearEasing)), label = "HyperLoadingPhase")
    return phase
}
