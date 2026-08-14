/** 文件职责：提供用于亮度、音量等连续比例反馈的竖向胶囊组件。 */
package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    val borderColor: Color
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
    colors: HyperLevelCapsuleColors = HyperLevelCapsuleDefaults.colors()
) {
    val coercedProgress = progress.coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .width(HyperLevelCapsuleDefaults.Width)
            .height(HyperLevelCapsuleDefaults.Height)
            .border(
                width = HyperLevelCapsuleDefaults.BorderWidth,
                color = colors.borderColor,
                shape = shape
            )
            .clip(shape)
            .background(colors.containerColor)
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
                .background(colors.progressColor)
        )
        Text(
            text = label,
            modifier = Modifier.align(Alignment.Center),
            color = colors.labelColor,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Clip
        )
    }
}

object HyperLevelCapsuleDefaults {
    val Width = 40.dp
    val Height = 140.dp
    val Shape: Shape = RoundedCornerShape(percent = 50)
    val BorderWidth = 1.dp
    val ContentInset = 2.dp

    @Composable
    fun colors(
        containerColor: Color = Color.Unspecified,
        progressColor: Color = Color.Unspecified,
        labelColor: Color = Color.Unspecified,
        borderColor: Color = Color.Unspecified
    ): HyperLevelCapsuleColors = HyperLevelCapsuleColors(
        containerColor = if (containerColor == Color.Unspecified) {
            Color(0f, 0f, 0f, 0.4f)
        } else {
            containerColor
        },
        progressColor = if (progressColor == Color.Unspecified) {
            Color(1f, 1f, 1f, 1f)
        } else {
            progressColor
        },
        labelColor = if (labelColor == Color.Unspecified) HyperColors.accent else labelColor,
        borderColor = if (borderColor == Color.Unspecified) {
            Color(1f, 1f, 1f, 1f)
        } else {
            borderColor
        }
    )
}
