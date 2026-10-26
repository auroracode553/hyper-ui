/** IME 窗口本身常常宽于高，方向必须读取系统配置而非容器宽高。 */
package hyper_ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
internal fun isHyperSafeAreaPortrait(): Boolean =
    LocalConfiguration.current.orientation != Configuration.ORIENTATION_LANDSCAPE
