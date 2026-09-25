/** 文件职责：为 VitePress iframe 单独渲染指定组件的交互示例。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hyper_ui.docs.data.ComponentDemo
import hyper_ui.docs.theme.DocsPreviewBackground

@Composable
internal fun EmbeddedComponentPreview(demo: ComponentDemo) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DocsPreviewBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        demo.content()
    }
}
