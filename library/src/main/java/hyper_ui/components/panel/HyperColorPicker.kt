package hyper_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.core.interaction.hyperNoRippleClickable

data class HyperColorOption(
    val id: String,
    val label: String,
    val color: Color
)

object HyperColorPickerDefaults {
    val colorSize = 36.dp
    val horizontalSpacing = 10.dp
    val verticalSpacing = 14.dp
    val labelTopSpacing = 5.dp
    val colorBorderWidth = 1.dp

    val presetOptions: List<HyperColorOption> = listOf(
        HyperColorOption("classic_red", "经典红", rgba(231, 76, 60, 1f)),
        HyperColorOption("brick_red", "砖红", rgba(184, 92, 56, 1f)),
        HyperColorOption("peach_orange", "蜜橘", rgba(255, 140, 105, 1f)),
        HyperColorOption("vibrant_orange", "活力橙", rgba(255, 103, 0, 1f)),
        HyperColorOption("sunny_yellow", "暖阳黄", rgba(255, 159, 67, 1f)),
        HyperColorOption("amber_yellow", "琥珀黄", rgba(255, 183, 0, 1f)),
        HyperColorOption("golden", "金盏", rgba(255, 195, 0, 1f)),
        HyperColorOption("lemon_green", "柠檬绿", rgba(164, 209, 82, 1f)),
        HyperColorOption("bud_green", "嫩芽绿", rgba(123, 200, 108, 1f)),
        HyperColorOption("emerald", "翡翠绿", rgba(46, 204, 113, 1f)),
        HyperColorOption("forest_green", "森林绿", rgba(39, 174, 96, 1f)),
        HyperColorOption("mint_green", "薄荷绿", rgba(26, 188, 156, 1f)),
        HyperColorOption("pine_green", "青松绿", rgba(0, 200, 150, 1f)),
        HyperColorOption("lake_blue", "湖蓝", rgba(72, 201, 176, 1f)),
        HyperColorOption("sky_blue", "天蓝", rgba(93, 173, 226, 1f)),
        HyperColorOption("ocean_blue", "海蓝", rgba(64, 120, 255, 1f)),
        HyperColorOption("sapphire", "宝石蓝", rgba(41, 128, 185, 1f)),
        HyperColorOption("navy_blue", "藏蓝", rgba(30, 55, 153, 1f)),
        HyperColorOption("indigo", "靛青", rgba(56, 103, 214, 1f)),
        HyperColorOption("purple_blue", "紫蓝", rgba(108, 92, 231, 1f)),
        HyperColorOption("violet", "紫罗兰", rgba(156, 89, 209, 1f)),
        HyperColorOption("lavender", "薰衣草", rgba(162, 155, 254, 1f)),
        HyperColorOption("sunset_orange", "日暮橙", rgba(235, 109, 52, 1f)),
        HyperColorOption("deep_sea", "深海蓝", rgba(15, 118, 178, 1f)),
        HyperColorOption("moss_green", "苔藓绿", rgba(122, 168, 82, 1f)),
        HyperColorOption("wine_red", "酒红", rgba(192, 57, 43, 1f)),
        HyperColorOption("warm_brown", "暖棕", rgba(211, 84, 0, 1f)),
        HyperColorOption("olive_green", "橄榄绿", rgba(106, 176, 76, 1f)),
        HyperColorOption("cyan_blue", "青蓝", rgba(34, 166, 179, 1f)),
        HyperColorOption("hibiscus", "木槿紫", rgba(179, 51, 113, 1f)),
        HyperColorOption("warm_gray", "暖灰", rgba(149, 165, 166, 1f)),
        HyperColorOption("graphite", "石墨黑", rgba(45, 52, 54, 1f))
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HyperColorPicker(
    options: List<HyperColorOption> = HyperColorPickerDefaults.presetOptions,
    selectedId: String,
    onSelected: (HyperColorOption) -> Unit,
    modifier: Modifier = Modifier,
    colorSize: Dp = HyperColorPickerDefaults.colorSize,
    horizontalSpacing: Dp = HyperColorPickerDefaults.horizontalSpacing,
    verticalSpacing: Dp = HyperColorPickerDefaults.verticalSpacing,
    labelTopSpacing: Dp = HyperColorPickerDefaults.labelTopSpacing
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        options.forEach { option ->
            ColorPickItem(
                option = option,
                selected = option.id == selectedId,
                onClick = { onSelected(option) },
                colorSize = colorSize,
                labelTopSpacing = labelTopSpacing,
                modifier = Modifier.widthIn(min = colorSize + 12.dp)
            )
        }
    }
}

@Composable
private fun ColorPickItem(
    option: HyperColorOption,
    selected: Boolean,
    onClick: () -> Unit,
    colorSize: Dp,
    labelTopSpacing: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 选中时用 accent 色环形描边：外层固定尺寸 + accent 背景，内层色块小一圈形成"环"
        val ringWidth = 2.dp
        val ringColor = if (selected) HyperColors.accent else Color.Transparent

        Box(
            modifier = Modifier
                .size(colorSize + ringWidth * 2)
                .clip(CircleShape)
                .then(
                    if (selected) {
                        Modifier.background(ringColor)
                    } else {
                        Modifier
                    }
                )
                .hyperNoRippleClickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(colorSize)
                    .clip(CircleShape)
                    .background(option.color)
                    .border(
                        border = BorderStroke(
                            width = HyperColorPickerDefaults.colorBorderWidth,
                            color = HyperColors.fieldBorder
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (selected) {
                    Box(
                        modifier = Modifier
                            .size(colorSize * 0.42f)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(labelTopSpacing))
        Text(
            text = option.label,
            color = if (selected) HyperColors.primaryText else HyperColors.secondaryText,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
        )
    }
}
