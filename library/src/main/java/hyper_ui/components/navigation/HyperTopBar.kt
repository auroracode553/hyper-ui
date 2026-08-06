package hyper_ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HyperTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    rightSlot: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            HyperTopBarBackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(4.dp))
        }

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = HyperColors.primaryText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 34.sp
        )

        rightSlot?.invoke()
    }
}

@Composable
private fun HyperTopBarBackButton(
    onClick: () -> Unit
) {
    HyperIconButton(
        imageVector = Icons.Default.ArrowBack,
        contentDescription = "返回",
        onClick = onClick
    )
}
