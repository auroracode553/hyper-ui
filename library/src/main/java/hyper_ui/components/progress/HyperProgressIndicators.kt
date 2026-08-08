package hyper_ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
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
    height: Dp = HyperProgressIndicatorDefaults.LinearHeight,
    shape: Shape = HyperProgressIndicatorDefaults.LinearShape,
    colors: HyperProgressIndicatorColors = HyperProgressIndicatorDefaults.colors()
) {
    val coercedProgress = progress?.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = coercedProgress ?: 0f,
        animationSpec = tween(
            durationMillis = HyperProgressIndicatorDefaults.ProgressAnimationMillis,
            easing = LinearEasing
        ),
        label = "hyperLinearProgressValue"
    )
    val semanticsInfo = if (coercedProgress == null) {
        ProgressBarRangeInfo.Indeterminate
    } else {
        ProgressBarRangeInfo(coercedProgress, 0f..1f)
    }

    BoxWithConstraints(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .hyperGlassSurface(
                containerColor = colors.trackColor,
                shape = shape
            )
            .semantics {
                progressBarRangeInfo = semanticsInfo
            }
    ) {
        if (coercedProgress == null) {
            IndeterminateLinearSegment(
                indicatorColor = colors.indicatorColor,
                segmentWidth = maxWidth * HyperProgressIndicatorDefaults.IndeterminateSegmentFraction,
                segmentShape = shape
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .hyperGlassSurface(
                        containerColor = colors.indicatorColor,
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
    size: Dp = HyperProgressIndicatorDefaults.CircularSize,
    strokeWidth: Dp = HyperProgressIndicatorDefaults.CircularStrokeWidth,
    colors: HyperProgressIndicatorColors = HyperProgressIndicatorDefaults.colors()
) {
    val coercedProgress = progress?.coerceIn(0f, 1f)
    val transition = rememberInfiniteTransition(label = "hyperCircularProgressTransition")
    val indeterminateRotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = HyperProgressIndicatorDefaults.CircularAnimationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "hyperCircularProgressRotation"
    )
    val animatedProgress by animateFloatAsState(
        targetValue = coercedProgress ?: HyperProgressIndicatorDefaults.CircularIndeterminateSweepFraction,
        animationSpec = tween(
            durationMillis = HyperProgressIndicatorDefaults.ProgressAnimationMillis,
            easing = LinearEasing
        ),
        label = "hyperCircularProgressValue"
    )
    val semanticsInfo = if (coercedProgress == null) {
        ProgressBarRangeInfo.Indeterminate
    } else {
        ProgressBarRangeInfo(coercedProgress, 0f..1f)
    }

    Canvas(
        modifier = modifier
            .size(size)
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
        val sweepAngle = (animatedProgress * 360f).coerceIn(0f, 360f)
        val startAngle = if (coercedProgress == null) {
            indeterminateRotation - 90f
        } else {
            -90f
        }

        drawCircle(
            color = colors.trackColor,
            radius = (this.size.minDimension - strokePx) / 2f,
            style = Stroke(width = strokePx, cap = StrokeCap.Round)
        )
        drawArc(
            color = colors.indicatorColor,
            startAngle = startAngle,
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
    val transition = rememberInfiniteTransition(label = "hyperLinearProgressTransition")
    val offsetProgress by transition.animateFloat(
        initialValue = -HyperProgressIndicatorDefaults.IndeterminateSegmentFraction,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = HyperProgressIndicatorDefaults.LinearAnimationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "hyperLinearProgressOffset"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(segmentWidth)
                .offset(x = maxWidth * offsetProgress)
                .hyperGlassSurface(
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
    const val ProgressAnimationMillis = 180
    const val LinearAnimationMillis = 1100
    const val CircularAnimationMillis = 900
    const val IndeterminateSegmentFraction = 0.36f
    const val CircularIndeterminateSweepFraction = 0.26f

    @Composable
    fun colors(
        trackColor: Color = Color.Unspecified,
        indicatorColor: Color = Color.Unspecified
    ): HyperProgressIndicatorColors = HyperProgressIndicatorColors(
        trackColor = resolveHyperContainerColor(trackColor, HyperColors.elevatedContainer),
        indicatorColor = resolveHyperContainerColor(indicatorColor, HyperColors.accent)
    )
}
