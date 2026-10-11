/** 文件职责：语义图标入口，共用 HyperReferenceIcons 的 Lucide 路径。 */
package hyper_ui.core.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hyper_ui.HyperIcon
import hyper_ui.HyperReferenceIcons

@Composable
internal fun HyperGaugeIcon(modifier: Modifier = Modifier) {
    HyperIcon(HyperReferenceIcons.Gauge, null, modifier)
}

@Composable
internal fun HyperCloseIcon(modifier: Modifier = Modifier) {
    HyperIcon(HyperReferenceIcons.Close, null, modifier)
}

@Composable
internal fun HyperResetIcon(modifier: Modifier = Modifier) {
    HyperIcon(HyperReferenceIcons.Reset, null, modifier)
}

@Composable
internal fun HyperHorizontalDragIcon(modifier: Modifier = Modifier) {
    HyperIcon(HyperReferenceIcons.HorizontalDrag, null, modifier)
}

@Composable
internal fun HyperEditIcon(modifier: Modifier = Modifier) {
    HyperIcon(HyperReferenceIcons.Edit, null, modifier)
}

@Composable
internal fun HyperFastForwardIcon(modifier: Modifier = Modifier) {
    HyperIcon(HyperReferenceIcons.FastForward, null, modifier)
}

@Composable
internal fun HyperChargingIcon(modifier: Modifier = Modifier) {
    HyperIcon(HyperReferenceIcons.Charging, null, modifier)
}

@Composable
internal fun HyperCheckIcon(modifier: Modifier = Modifier) {
    HyperIcon(HyperReferenceIcons.Check, null, modifier)
}
