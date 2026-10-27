/** Android Activity 的统一全面屏入口；界面图标亮暗仍由应用主题负责。 */
package hyper_ui

import android.app.Activity
import android.graphics.Color
import android.view.WindowManager
import android.view.Window

fun Activity.enableHyperEdgeToEdge() {
    window.enableHyperEdgeToEdge()
}

/** Activity 与 Dialog 共用窗口配置，避免弹窗回退到平台系统栏底色或对比度蒙层。 */
@Suppress("DEPRECATION")
internal fun Window.enableHyperEdgeToEdge() {
    // 库最低 API 30：直接关闭 decor 适配，允许内容覆盖系统栏及显示缺口。
    setDecorFitsSystemWindows(false)
    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    statusBarColor = Color.TRANSPARENT
    navigationBarColor = Color.TRANSPARENT
    isStatusBarContrastEnforced = false
    isNavigationBarContrastEnforced = false
    attributes = attributes.apply {
        layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
    }
}
