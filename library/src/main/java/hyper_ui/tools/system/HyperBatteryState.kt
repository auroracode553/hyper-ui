/** 文件职责：提供 Android 电池状态的一次性读取与 Compose 生命周期安全订阅。 */
package hyper_ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlin.math.roundToInt

/**
 * Android 设备电池状态。
 *
 * `isAvailable` 为 false 表示系统没有返回有效的电量数据，调用方可选择隐藏电池 UI。
 */
@Immutable
data class HyperBatteryState(
    val percentage: Int,
    val isCharging: Boolean,
    val isAvailable: Boolean
) {
    companion object {
        val Unavailable = HyperBatteryState(
            percentage = 0,
            isCharging = false,
            isAvailable = false
        )
    }
}

/**
 * 一次性读取当前 Android 电池状态。
 *
 * 适合非 Compose 场景或创建初始状态；持续监听请使用 [rememberHyperBatteryState]。
 */
fun readHyperBatteryState(context: Context): HyperBatteryState {
    val applicationContext = context.applicationContext
    return registerBatteryReceiver(
        context = applicationContext,
        receiver = null
    ).toHyperBatteryState()
}

/**
 * 订阅 Android 电池状态，并在离开 Composition 时自动注销广播。
 *
 * 工具只读取系统公开广播，不申请权限，也不持有 Activity Context。
 */
@Composable
fun rememberHyperBatteryState(): State<HyperBatteryState> {
    val currentContext = LocalContext.current
    val applicationContext = remember(currentContext) { currentContext.applicationContext }
    val state = remember(applicationContext) {
        mutableStateOf(readHyperBatteryState(applicationContext))
    }

    DisposableEffect(applicationContext) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                state.value = intent.toHyperBatteryState()
            }
        }
        val stickyIntent = registerBatteryReceiver(
            context = applicationContext,
            receiver = receiver
        )
        if (stickyIntent != null) {
            state.value = stickyIntent.toHyperBatteryState()
        }

        onDispose {
            try {
                applicationContext.unregisterReceiver(receiver)
            } catch (_: IllegalArgumentException) {
                // 宿主提前注销时保持释放操作幂等。
            }
        }
    }

    return state
}

private fun registerBatteryReceiver(
    context: Context,
    receiver: BroadcastReceiver?
): Intent? {
    val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // ACTION_BATTERY_CHANGED 是受保护的系统广播，导出标记用于接收系统进程事件。
        context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
    } else {
        @Suppress("DEPRECATION")
        context.registerReceiver(receiver, filter)
    }
}

private fun Intent?.toHyperBatteryState(): HyperBatteryState {
    if (this == null) return HyperBatteryState.Unavailable

    val level = getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
    val scale = getIntExtra(BatteryManager.EXTRA_SCALE, -1)
    if (level < 0 || scale <= 0) return HyperBatteryState.Unavailable

    val status = getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN)
    val plugged = getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) != 0
    val charging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
        (status == BatteryManager.BATTERY_STATUS_FULL && plugged)

    return HyperBatteryState(
        percentage = (level.toFloat() / scale * 100f).roundToInt().coerceIn(0, 100),
        isCharging = charging,
        isAvailable = true
    )
}
