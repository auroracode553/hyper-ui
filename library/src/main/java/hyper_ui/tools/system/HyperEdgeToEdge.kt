/** Android Activity 的统一全面屏入口；界面图标亮暗仍由应用主题负责。 */
package hyper_ui

import android.app.Activity
import android.graphics.Color
import android.view.WindowManager

@Suppress("DEPRECATION")
fun Activity.enableHyperEdgeToEdge() {
    // 库最低 API 30：直接关闭 decor 适配，允许内容覆盖系统栏及显示缺口。
    window.setDecorFitsSystemWindows(false)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.TRANSPARENT
    window.isStatusBarContrastEnforced = false
    window.isNavigationBarContrastEnforced = false
    window.attributes = window.attributes.apply {
        layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
    }
}
