package hyper_ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** 独立分组标题与 HyperMenuGroup 共用排版，不自行增加页面边距或卡片表面。 */
@Composable
fun HyperSectionTitle(text: String, modifier: Modifier = Modifier) {
    HyperText(text, modifier = modifier, fontSize = 14.sp, fontWeight = FontWeight.Bold)
}
