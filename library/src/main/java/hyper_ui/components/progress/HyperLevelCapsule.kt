/** 文件职责：提供用于亮度、音量等连续比例反馈的竖向胶囊组件。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Immutable
data class HyperLevelCapsuleColors(
    val containerColor: Color,
    val progressColor: Color,
    val labelColor: Color,
    val iconColor: Color
)

/**
 * 竖向比例胶囊。调用方负责手势、显示时机与百分比文案，组件只渲染当前比例。
 * 默认尺寸可通过 [modifier] 中的 width/height 覆盖。
 */
@Composable
fun HyperLevelCapsule(
    progress: Float,
    label: String,
    modifier: Modifier = Modifier,
    shape: Shape = HyperLevelCapsuleDefaults.Shape,
    colors: HyperLevelCapsuleColors = HyperLevelCapsuleDefaults.colors(),
    iconContent: (@Composable () -> Unit)? = null
) {
    val coercedProgress = progress.coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .width(HyperLevelCapsuleDefaults.Width)
            .height(HyperLevelCapsuleDefaults.Height)
            .hyperGlassSurface(
                shape = shape,
                visuals = hyperGlassSurfaceVisuals(
                    containerColor = colors.containerColor,
                    elevation = HyperLevelCapsuleDefaults.Elevation,
                    topLightAlpha = if (HyperColors.isLight) 0.36f else 0.14f,
                    bottomShadeAlpha = if (HyperColors.isLight) 0.05f else 0.16f,
                    shadowAlpha = if (HyperColors.isLight) 0.18f else 0.34f
                )
            )
            .padding(HyperLevelCapsuleDefaults.ContentInset)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(coercedProgress, 0f..1f)
            }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(coercedProgress)
                .clip(shape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colors.progressColor.copy(alpha = colors.progressColor.alpha * 0.72f),
                            colors.progressColor
                        )
                    )
                )
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HyperLevelCapsuleDefaults.ContentSpacing)
        ) {
            if (iconContent != null) {
                Box(
                    modifier = Modifier.size(HyperLevelCapsuleDefaults.IconSize),
                    contentAlignment = Alignment.Center
                ) {
                    CompositionLocalProvider(LocalContentColor provides colors.iconColor) {
                        iconContent()
                    }
                }
            }
            Text(
                text = label,
                color = colors.labelColor,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }
    }
}

object HyperLevelCapsuleDefaults {
    val Width = 52.dp
    val Height = 156.dp
    val Shape: Shape = RoundedCornerShape(percent = 50)
    val ContentInset = 3.dp
    val ContentSpacing = 6.dp
    val IconSize = 20.dp
    val Elevation = 8.dp

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        progressColor: Color = Color.Unspecified,
        labelColor: Color = Color.Unspecified,
        iconColor: Color = Color.Unspecified
    ): HyperLevelCapsuleColors {
        val defaultContentColor = if (HyperColors.isLight) {
            HyperColors.primaryText.copy(alpha = 0.94f)
        } else {
            Color(1f, 1f, 1f, 0.94f)
        }
        return HyperLevelCapsuleColors(
            containerColor = if (containerColor == Color.Unspecified) {
                Color(1f, 1f, 1f, if (HyperColors.isLight) 0.56f else 0.20f)
            } else {
                containerColor
            },
            progressColor = if (progressColor == Color.Unspecified) {
                HyperColors.accent.copy(alpha = 0.48f)
            } else {
                progressColor
            },
            labelColor = if (labelColor == Color.Unspecified) {
                defaultContentColor
            } else {
                labelColor
            },
            iconColor = if (iconColor == Color.Unspecified) {
                defaultContentColor
            } else {
                iconColor
            }
        )
    }
}
