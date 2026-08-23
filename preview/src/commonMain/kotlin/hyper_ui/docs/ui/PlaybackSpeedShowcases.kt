/** 文件职责：集中提供播放速度刻度与设置面板的交互 Preview。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperButton
import hyper_ui.HyperButtonTone
import hyper_ui.HyperPlaybackSpeedPanelDefaults
import hyper_ui.HyperPlaybackSpeedPanelOverlay
import hyper_ui.HyperPlaybackSpeedScale
import hyper_ui.HyperPlaybackSpeedScaleDefaults

@Composable
fun PlaybackSpeedScaleDemo() {
    var selectedSpeed by remember { mutableStateOf(2f) }
    var useCustomColors by remember { mutableStateOf(false) }
    val speedOptions = HyperPlaybackSpeedScaleDefaults.SpeedOptions
    val selectedIndex = speedOptions.indexOf(selectedSpeed).coerceAtLeast(0)
    val scaleColors = if (useCustomColors) {
        HyperPlaybackSpeedScaleDefaults.colors(
            containerColor = Color(0.12f, 0.10f, 0.18f, 0.96f),
            activeTrackColor = Color(0.62f, 0.48f, 1f, 0.88f),
            selectedTickColor = Color(0.78f, 0.70f, 1f, 1f),
            selectedLabelColor = Color(0.78f, 0.70f, 1f, 1f)
        )
    } else {
        HyperPlaybackSpeedScaleDefaults.colors()
    }

    Column(
        modifier = Modifier.widthIn(max = 560.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        HyperPlaybackSpeedScale(
            selectedSpeed = selectedSpeed,
            colors = scaleColors
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                tone = HyperButtonTone.Outline,
                enabled = selectedIndex > 0,
                onClick = { selectedSpeed = speedOptions[selectedIndex - 1] }
            ) {
                Text("向左调速")
            }
            HyperButton(
                enabled = selectedIndex < speedOptions.lastIndex,
                onClick = { selectedSpeed = speedOptions[selectedIndex + 1] }
            ) {
                Text("向右调速")
            }
        }
        HyperButton(
            tone = HyperButtonTone.Outline,
            onClick = { useCustomColors = !useCustomColors }
        ) {
            Text(if (useCustomColors) "恢复默认配色" else "查看自定义配色")
        }
        Text(
            text = "紧凑双层布局；固定深色不跟随页面主题，手势仍由调用方持有。",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PlaybackSpeedPanelDemo() {
    var currentSpeed by remember { mutableStateOf(1f) }
    var panelVisible by remember { mutableStateOf(true) }
    var useCustomColors by remember { mutableStateOf(false) }
    var feedback by remember { mutableStateOf("拖动滑块可实时更新速度") }
    val panelColors = if (useCustomColors) {
        HyperPlaybackSpeedPanelDefaults.colors(
            containerTopColor = Color(0.12f, 0.10f, 0.18f, 0.96f),
            containerBottomColor = Color(0.07f, 0.06f, 0.11f, 0.97f),
            accentColor = Color(0.62f, 0.48f, 1f, 1f)
        )
    } else {
        HyperPlaybackSpeedPanelDefaults.colors()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                HyperButton(onClick = { panelVisible = true }) {
                    Text("打开速度面板")
                }
                HyperButton(
                    tone = HyperButtonTone.Outline,
                    onClick = { useCustomColors = !useCustomColors }
                ) {
                    Text(if (useCustomColors) "恢复默认配色" else "查看自定义配色")
                }
            }
            Text(
                text = feedback,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        }
        HyperPlaybackSpeedPanelOverlay(
            visible = panelVisible,
            currentSpeed = currentSpeed,
            onSpeedChange = { speed ->
                currentSpeed = speed
                feedback = "当前 ${speed}x"
            },
            onDismissRequest = { panelVisible = false },
            onCustomSpeedRequest = { feedback = "已请求自定义速度" },
            colors = panelColors
        )
    }
}
