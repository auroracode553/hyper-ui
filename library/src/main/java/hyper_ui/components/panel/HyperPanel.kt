package hyper_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HyperPanel(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(12.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(HyperStyleDefaults.LargeCornerRadius)
    val resolvedContainerColor = if (containerColor == Color.Unspecified) {
        HyperColors.cardContainer
    } else {
        containerColor
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(resolvedContainerColor)
            .padding(contentPadding),
        verticalArrangement = verticalArrangement,
        content = content
    )
}
