package hyper_ui.docs.ui

import hyper_ui.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ColorPickerDemo() {
    var selectedColorId by remember { mutableStateOf("ocean_blue") }
    val currentOption = remember(selectedColorId) {
        HyperColorPickerDefaults.presetOptions.find { it.id == selectedColorId }
            ?: HyperColorPickerDefaults.presetOptions.first()
    }

    Column(
        modifier = Modifier.widthIn(max = 400.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HyperColorPicker(
            selectedId = selectedColorId,
            onSelected = { option -> selectedColorId = option.id }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(currentOption.color)
            )
            Text(
                text = "当前选择：${currentOption.label}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun PanelDemo() {
    var acknowledged by remember { mutableStateOf(false) }

    HyperPanel(
        modifier = Modifier.widthIn(max = 520.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContainerIconBadge(
                imageVector = if (acknowledged) Icons.Default.Star else Icons.Default.Check,
                tint = MaterialTheme.colorScheme.secondary,
                background = MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "系统状态",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (acknowledged) "已查看状态详情" else "运行正常，最近同步 2 分钟前",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            HyperButton(
                text = "查看详情",
                onClick = { acknowledged = true }
            )
            HyperButton(
                text = "重置",
                onClick = { acknowledged = false },
                variant = HyperButtonVariant.Default
            )
        }
    }
}

@Composable
private fun ContainerIconBadge(
    imageVector: ImageVector,
    tint: Color,
    background: Color
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
    }
}
