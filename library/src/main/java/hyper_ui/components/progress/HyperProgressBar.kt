package hyper_ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HyperProgressBar(
    progress: Float?,
    modifier: Modifier = Modifier,
    height: Dp = HyperProgressBarDefaults.Height,
    shape: Shape = HyperProgressBarDefaults.Shape,
    trackColor: Color = Color.Unspecified,
    progressColor: Color = Color.Unspecified
) {
    val coercedProgress = progress?.coerceIn(0f, 1f)
    val usesDefaultTrackColor = trackColor == Color.Unspecified
    val resolvedTrackColor = if (usesDefaultTrackColor) {
        HyperColors.elevatedContainer
    } else {
        trackColor
    }
    val resolvedProgressColor = if (progressColor == Color.Unspecified) {
        HyperColors.accent
    } else {
        progressColor
    }
    val trackHasVisibleBackground = resolvedTrackColor.alpha > 0f
    val trackHighlightModifier = if (trackHasVisibleBackground) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
    }
    val progressHasVisibleBackground = resolvedProgressColor.alpha > 0f
    val progressHighlightModifier = if (progressHasVisibleBackground) {
        Modifier.background(HyperColors.glassHighlightBrush)
    } else {
        Modifier
    }
    val animatedProgress by animateFloatAsState(
        targetValue = coercedProgress ?: 0f,
        animationSpec = tween(
            durationMillis = HyperProgressBarDefaults.ProgressAnimationMillis,
            easing = LinearEasing
        ),
        label = "hyperProgressValue"
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
            .clip(shape)
            .background(resolvedTrackColor)
            .then(trackHighlightModifier)
            .semantics {
                progressBarRangeInfo = semanticsInfo
            }
    ) {
        if (coercedProgress == null) {
            IndeterminateProgressSegment(
                progressColor = resolvedProgressColor,
                segmentWidth = maxWidth * HyperProgressBarDefaults.IndeterminateSegmentFraction,
                segmentShape = shape,
                highlightModifier = progressHighlightModifier
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(shape)
                    .background(resolvedProgressColor)
                    .then(progressHighlightModifier)
            )
        }
    }
}

@Composable
fun HyperLoadingProgress(
    modifier: Modifier = Modifier,
    height: Dp = HyperProgressBarDefaults.Height,
    shape: Shape = HyperProgressBarDefaults.Shape,
    trackColor: Color = Color.Unspecified,
    progressColor: Color = Color.Unspecified
) {
    HyperProgressBar(
        progress = null,
        modifier = modifier,
        height = height,
        shape = shape,
        trackColor = trackColor,
        progressColor = progressColor
    )
}

@Composable
private fun IndeterminateProgressSegment(
    progressColor: Color,
    segmentWidth: Dp,
    segmentShape: Shape,
    highlightModifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "hyperLoadingProgressTransition")
    val offsetProgress by transition.animateFloat(
        initialValue = -HyperProgressBarDefaults.IndeterminateSegmentFraction,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = HyperProgressBarDefaults.IndeterminateAnimationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "hyperLoadingProgressOffset"
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
                .clip(segmentShape)
                .background(progressColor)
                .then(highlightModifier)
        )
    }
}

object HyperProgressBarDefaults {
    val Height = 4.dp
    val Shape: Shape = RoundedCornerShape(percent = 50)
    const val ProgressAnimationMillis = 180
    const val IndeterminateAnimationMillis = 1100
    const val IndeterminateSegmentFraction = 0.36f
}
