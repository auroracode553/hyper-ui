/** 文件职责：底部操作列表的数据契约，不持有弹层或业务状态。 */
package hyper_ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class HyperAction<T>(
    val value: T,
    val label: String,
    val icon: ImageVector? = null,
    val type: String = "default",
    val enabled: Boolean = true,
    val selected: Boolean = false
) {
    init { require(type == "default" || type == "danger") }

    val isDanger: Boolean get() = type == "danger"
}
