/** 文件职责：展示 HyperNavBar 的常见返回布局、操作反馈和沉浸滚动。 */
package hyper_ui.docs.ui

import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.House

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import hyper_ui.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperButton
import hyper_ui.HyperCard
import hyper_ui.HyperCardDefaults
import hyper_ui.HyperIcon
import hyper_ui.HyperNavBar
import hyper_ui.HyperNavBarBackButton
import hyper_ui.HyperNavBarDefaults
import hyper_ui.HyperNavBarPage
import hyper_ui.HyperText
import hyper_ui.docs.theme.LocalDocsColorScheme

private enum class NavBarExample(
    val label: String,
    val explanation: String,
    val type: String,
    val title: String?
) {
    Basic("基础返回", "只显示尖括号返回按钮", HyperNavBarDefaults.TypeBackOnly, null),
    TitleOnly("仅标题", "只显示页面标题，不显示返回按钮", HyperNavBarDefaults.TypeTitleOnly, "今日灵感"),
    BackWithTitle("返回与标题", "返回图标旁显示页面标题", HyperNavBarDefaults.TypeBackWithTitle, "今日灵感"),
    More("更多操作", "返回、页面标题与更多操作", HyperNavBarDefaults.TypeMore, "消息"),
    Edit("编辑返回", "返回、页面标题与保存操作", HyperNavBarDefaults.TypeEdit, "编辑资料"),
    Custom("自定义", "自定义插槽与分享操作", HyperNavBarDefaults.TypeCustom, "创作空间")
}

@Composable
fun NavBarDemo() {
    // 模拟安全区位于导航背景内部，与真机及 Flutter 的层级一致。
    CompositionLocalProvider(LocalHyperSafeAreaWindowInsets provides WindowInsets(top = PreviewTopSafeArea)) {
        NavBarPageDemo()
    }
}

@Composable
private fun NavBarPageDemo() {
    // 正文经过导航后方，渐隐在导航自身底部结束，不覆盖下一行内容。
    val sections = remember {
        listOf("留一点空白", "光影与秩序", "日常里的灵感", "透明导航栏下的连续滚动")
    }
    var example by remember { mutableStateOf(NavBarExample.Basic) }
    var actionMessage by remember { mutableStateOf("切换布局或点击顶部操作，查看导航反馈。") }
    // 同一状态交给主列表和库；派生值只由导航读取，正文不维护监听。
    val scrollState = rememberLazyListState()
    HyperNavBarPage(scrollState = scrollState, active = sections.isNotEmpty(),
        modifier = Modifier.fillMaxSize(),
        navBar = {
            val pageTitle = example.title
            HyperNavBar(
                type = example.type,
                title = if (pageTitle == null) null else {
                    { HyperText(pageTitle, maxLines = 1) }
                },
                onBackPressed = { actionMessage = "已请求返回上一页" },
                onMorePressed = { actionMessage = "已点击更多" },
                onSavePressed = { actionMessage = "已点击保存" },
                leading = if (example == NavBarExample.Custom) {
                    {
                        HyperNavBarBackButton(
                            onPressed = { actionMessage = "已请求返回上一页" }
                        )
                    }
                } else null,
                trailing = if (example == NavBarExample.Custom) {
                    {
                        HyperButton(onPressed = { actionMessage = "已点击分享" }, size = "small") {
                            HyperText("分享")
                        }
                    }
                } else null
            )
        },
        navBarSize = "default",
        contentPadding = PaddingValues(bottom = 24.dp),
        bottomSafeArea = false
    ) { immersivePadding ->
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize().background(LocalDocsColorScheme.current.background),
            contentPadding = immersivePadding,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "controls") {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)
                        .background(LocalDocsColorScheme.current.primaryContainer, RoundedCornerShape(20.dp))
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HyperIcon(Lucide.House, "导航示例", Modifier.size(28.dp))
                    HyperText("常见顶部导航", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    HyperText(example.explanation, fontSize = 14.sp)
                    NavBarExampleChoices(example) { selected ->
                        example = selected
                        actionMessage = "已切换为「${selected.label}」"
                    }
                    HyperText(actionMessage, fontSize = 13.sp)
                    HyperText("向上滚动显示向下渐隐的毛玻璃，回到顶部恢复透明。", fontSize = 12.sp)
                }
            }
            items(items = sections, key = { it }) { section ->
                HyperCard(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    colors = HyperCardDefaults.colors(containerColor = LocalDocsColorScheme.current.surface)
                ) {
                    Column(Modifier.padding(4.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        HyperText("灵感 ${sections.indexOf(section) + 1}", fontSize = 12.sp, color = LocalDocsColorScheme.current.onSurfaceVariant)
                        HyperText(section, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        HyperText("模糊和底色向下淡出到透明，标题与按钮保持清晰。", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun NavBarExampleChoices(selected: NavBarExample, onSelect: (NavBarExample) -> Unit) {
    NavBarExample.entries.toList().chunked(2).forEach { rowExamples ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            rowExamples.forEach { example ->
                HyperButton(
                    onPressed = { onSelect(example) },
                    modifier = Modifier.weight(1f),
                    type = if (example == selected) null else null,
                    size = "small"
                ) { HyperText(example.label, maxLines = 1) }
            }
        }
    }
}
