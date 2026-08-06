package hyper_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HyperConfirmDialog(
    show: Boolean,
    title: String,
    message: String,
    confirmText: String = "确定",
    cancelText: String = "取消",
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    if (!show) {
        return
    }

    HyperDialog(
        show = show,
        onDismissRequest = onCancel,
        horizontalAlignment = Alignment.CenterHorizontally,
        actions = {
            HyperButton(
                text = cancelText,
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                variant = HyperButtonVariant.Default
            )
            HyperButton(
                text = confirmText,
                onClick = onConfirm,
                modifier = Modifier.weight(1f),
                variant = HyperButtonVariant.Primary
            )
        }
    ) {
        Text(
            text = title,
            color = HyperColors.primaryText,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        if (message.isNotBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 360.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = message,
                    color = HyperColors.secondaryText,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
