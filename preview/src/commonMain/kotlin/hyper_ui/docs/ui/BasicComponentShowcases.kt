/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/ui/BasicComponentShowcases 可复用界面组件及交互封装。 */
package hyper_ui.docs.ui
import hyper_ui.*
import hyper_ui.docs.theme.LocalDocsColorScheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ButtonDemo() {
    var clicks by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HyperButton(
                onClick = { clicks += 1 },
                size = "small"
            ) {
                HyperText(text = "小按钮")
            }
            HyperButton(onClick = { clicks += 1 }) {
                HyperText(text = "默认按钮")
            }
            HyperButton(
                onClick = {},
                loading = true,
                type = "outline"
            ) {
                HyperText(text = "处理中")
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HyperButton(
                onClick = { clicks += 1 },
                type = "outline"
            ) {
                HyperText(text = "导出")
            }
            HyperButton(
                onClick = { clicks = 0 },
                type = "danger"
            ) {
                HyperText(text = "删除")
            }
            HyperButton(onClick = { clicks += 1 }, type = "tonal") {
                HyperIcon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                HyperText(text = "带图标胶囊", softWrap = false, maxLines = 1)
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HyperButton(type = "icon", onClick = { clicks += 1 }) {
                HyperIcon(Icons.Default.Search, contentDescription = "搜索", modifier = Modifier.size(18.dp))
            }
            HyperButton(type = "icon", onClick = { clicks += 1 }) {
                HyperIcon(Icons.Default.Notifications, contentDescription = "通知", modifier = Modifier.size(18.dp))
            }
            HyperButton(type = "icon", onClick = { clicks = 0 }) {
                HyperIcon(Icons.Default.Delete, contentDescription = "删除", modifier = Modifier.size(18.dp))
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HyperButton(onClick = {}, enabled = false, type = "outline") {
                HyperText("禁用")
            }
            HyperButton(
                onClick = { clicks += 1 },
                size = "small",
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                HyperText(text = "小尺寸 slot", fontSize = 13.sp)
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HyperButton({ loading = !loading }, type = "ghost") { HyperText("切换加载") }
            HyperButton({ clicks += 1 }, loading = loading) { HyperText("提交") }
        }
        HyperText(
            text = "点击次数：$clicks · 按住观察缩放，拖出按钮再松开可取消点击",
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}
