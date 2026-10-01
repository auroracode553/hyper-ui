/** 文件职责：展示 HyperNavBar 的常见返回布局、操作反馈和沉浸滚动。 */
package hyper_ui.docs.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.HyperButton
import hyper_ui.HyperButtonDefaults
import hyper_ui.HyperCard
import hyper_ui.HyperCardDefaults
import hyper_ui.HyperIcon
import hyper_ui.HyperNavBar
import hyper_ui.HyperNavBarBackButton
import hyper_ui.HyperNavBarPage
import hyper_ui.HyperText
import hyper_ui.docs.theme.LocalDocsColorScheme

private enum class NavBarExample(
    val label: String,
    val explanation: String,
    val title: String?,
    val showsBack: Boolean = true
) {
    Basic("基础返回", "只显示尖括号返回按钮", null),
    TitleOnly("仅标题", "只显示页面标题，不显示返回按钮", "今日灵感", showsBack = false),
    BackWithTitle("返回与标题", "返回图标旁显示页面标题", "今日灵感"),
    More("更多操作", "返回、页面标题与更多操作", "消息"),
    Edit("编辑返回", "返回、页面标题与保存操作", "编辑资料")
}

@Composable
fun NavBarDemo() {
    val sections = remember {
        listOf("留一点空白", "光影与秩序", "日常里的灵感", "透明导航栏下的连续滚动")
    }
    var example by remember { mutableStateOf(NavBarExample.Basic) }
    var backEnabled by remember { mutableStateOf(true) }
    var actionMessage by remember { mutableStateOf("切换布局或点击顶部操作，查看导航反馈。") }

    HyperNavBarPage(
        modifier = Modifier.fillMaxSize(),
        navBar = {
            val pageTitle = example.title
            HyperNavBar(
                // 正文占满屏幕，导航操作行独立避让预览模拟的状态栏。
                modifier = Modifier.padding(top = PreviewTopSafeArea),
                titleContent = if (pageTitle == null) null else {
                    { HyperText(pageTitle, maxLines = 1) }
                },
                navigationContent = if (example.showsBack) {
                    {
                        HyperNavBarBackButton(
                            onClick = { actionMessage = "已请求返回上一页" },
                            enabled = backEnabled
                        )
                    }
                } else null,
                trailingContent = if (example == NavBarExample.More || example == NavBarExample.Edit) {
                    {
                        if (example == NavBarExample.More) {
                            NavBarActionIcon(Icons.Default.MoreVert, "更多") {
                                actionMessage = "已点击更多"
                            }
                        } else {
                            HyperButton(onClick = { actionMessage = "已点击保存" }, type = "ghost", size = "small") {
                                HyperText("保存")
                            }
                        }
                    }
                } else null
            )
        },
        navBarSize = "default",
        contentPadding = PaddingValues(top = PreviewTopSafeArea, bottom = 24.dp),
        bottomSafeArea = false
    ) { immersivePadding ->
        LazyColumn(
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
                    HyperIcon(Icons.Default.Home, "导航示例", Modifier.size(28.dp))
                    HyperText("常见顶部导航", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    HyperText(example.explanation, fontSize = 14.sp)
                    NavBarExampleChoices(example) { selected ->
                        example = selected
                        actionMessage = "已切换为「${selected.label}」"
                    }
                    if (example.showsBack) {
                        HyperButton(
                            onClick = {
                                backEnabled = !backEnabled
                                actionMessage = if (backEnabled) "返回按钮已启用" else "返回按钮已禁用"
                            },
                            type = "tonal",
                            size = "small"
                        ) { HyperText(if (backEnabled) "禁用返回" else "启用返回") }
                    }
                    HyperText(actionMessage, fontSize = 13.sp)
                    HyperText("向上滚动，观察正文经过透明导航栏与状态栏。", fontSize = 12.sp)
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
                        HyperText("导航栏保持透明，内容沿同一个滚动视口连续向上移动。", fontSize = 13.sp)
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
                    onClick = { onSelect(example) },
                    modifier = Modifier.weight(1f),
                    type = if (example == selected) "tonal" else "ghost",
                    size = "small"
                ) { HyperText(example.label, maxLines = 1) }
            }
        }
    }
}

@Composable
private fun NavBarActionIcon(imageVector: ImageVector, contentDescription: String, onClick: () -> Unit) {
    HyperButton(type = "icon", onClick = onClick) {
        HyperIcon(imageVector, contentDescription, Modifier.size(HyperButtonDefaults.IconSize))
    }
}
