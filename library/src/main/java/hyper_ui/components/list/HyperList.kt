/** 文件职责：提供无卡片表面的页面懒列表，行内容与交互由调用方提供。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HyperList(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = HyperListDefaults.ContentPadding,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(0.dp),
    content: LazyListScope.() -> Unit
) {
    // 透明容器上下文让 auto 行采用 plain 布局；页面列表不绘制卡片、阴影或裁切。
    CompositionLocalProvider(
        LocalHyperListTileDividerSuppressed provides false,
        LocalHyperListTileContainerColor provides Color.Transparent
    ) {
        LazyColumn(
            modifier = modifier.fillMaxWidth().then(contentModifier),
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            content = content
        )
    }
}

object HyperListDefaults {
    // 外层横向留白由页面决定，行保留自己的内容内边距。
    val ContentPadding: PaddingValues = PaddingValues(0.dp)
}
