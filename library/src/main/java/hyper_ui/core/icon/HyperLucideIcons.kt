/** 文件职责：集中映射 HyperUI 内置语义图标到 Lucide Android VectorDrawable。 */
package hyper_ui.core.icon

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.composables.icons.lucide.R as LucideR
import hyper_ui.HyperIcon
import hyper_ui.LocalHyperContentColor

@Composable
internal fun HyperGaugeIcon(modifier: Modifier = Modifier) {
    HyperLucideIcon(LucideR.drawable.lucide_ic_gauge, modifier)
}

@Composable
internal fun HyperCloseIcon(modifier: Modifier = Modifier) {
    HyperLucideIcon(LucideR.drawable.lucide_ic_x, modifier)
}

@Composable
internal fun HyperResetIcon(modifier: Modifier = Modifier) {
    HyperLucideIcon(LucideR.drawable.lucide_ic_rotate_ccw, modifier)
}

@Composable
internal fun HyperHorizontalDragIcon(modifier: Modifier = Modifier) {
    HyperLucideIcon(LucideR.drawable.lucide_ic_move_horizontal, modifier)
}

@Composable
internal fun HyperEditIcon(modifier: Modifier = Modifier) {
    HyperLucideIcon(LucideR.drawable.lucide_ic_pencil, modifier)
}

@Composable
internal fun HyperFastForwardIcon(modifier: Modifier = Modifier) {
    HyperLucideIcon(LucideR.drawable.lucide_ic_fast_forward, modifier)
}

@Composable
internal fun HyperChargingIcon(modifier: Modifier = Modifier) {
    HyperLucideIcon(LucideR.drawable.lucide_ic_zap, modifier)
}

@Composable
internal fun HyperCheckIcon(modifier: Modifier = Modifier) {
    HyperLucideIcon(LucideR.drawable.lucide_ic_check, modifier)
}

@Composable
private fun HyperLucideIcon(
    drawableResource: Int,
    modifier: Modifier
) {
    HyperIcon(
        painter = painterResource(drawableResource),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        tint = LocalHyperContentColor.current
    )
}
