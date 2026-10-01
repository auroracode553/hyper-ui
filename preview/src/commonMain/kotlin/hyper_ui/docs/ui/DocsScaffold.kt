/** 文件职责：在 hyper_ui 中负责承载 preview/src/commonMain/kotlin/hyper_ui/docs/ui/DocsScaffold 模块实现，并集中维护其依赖协作与核心逻辑。 */
package hyper_ui.docs.ui

import hyper_ui.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.docs.LocalThemeColor
import hyper_ui.docs.ThemeColorController
import hyper_ui.docs.data.ComponentDemo
import hyper_ui.docs.data.componentDemos
import hyper_ui.docs.theme.DocsCodeBackground
import hyper_ui.docs.theme.DocsCodeText
import hyper_ui.docs.theme.LocalDocsColorScheme

@Composable
fun HyperDocsApp(
    themeColorController: ThemeColorController,
    initialSelectedId: String? = null,
    embeddedPreview: Boolean = false
) {
    CompositionLocalProvider(
        LocalThemeColor provides themeColorController,
        LocalLayoutDirection provides if (themeColorController.rtl) {
            LayoutDirection.Rtl
        } else {
            LayoutDirection.Ltr
        }
    ) {
        val demos = remember { componentDemos() }
        var selectedId by remember(initialSelectedId) {
            mutableStateOf(
                initialSelectedId
                    ?.takeIf { candidateId -> demos.any { it.id == candidateId } }
                    ?: demos.first().id
            )
        }
        val selectedDemo = demos.firstOrNull { it.id == selectedId } ?: demos.first()

        if (embeddedPreview) {
            EmbeddedComponentPreview(demo = selectedDemo)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LocalDocsColorScheme.current.background)
            ) {
                SelectionContainer {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        if (maxWidth < 840.dp) {
                            MobileDocsLayout(
                                demos = demos,
                                selectedId = selectedId,
                                selectedDemo = selectedDemo,
                                onSelect = { selectedId = it }
                            )
                        } else {
                            DesktopDocsLayout(
                                demos = demos,
                                selectedId = selectedId,
                                selectedDemo = selectedDemo,
                                onSelect = { selectedId = it }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DesktopDocsLayout(
    demos: List<ComponentDemo>,
    selectedId: String,
    selectedDemo: ComponentDemo,
    onSelect: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        DocsSidebar(
            demos = demos,
            selectedId = selectedId,
            onSelect = onSelect,
            modifier = Modifier
                .width(320.dp)
                .fillMaxHeight()
        )
        ComponentContent(
            demo = selectedDemo,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MobileDocsLayout(
    demos: List<ComponentDemo>,
    selectedId: String,
    selectedDemo: ComponentDemo,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        MobilePreviewTopBar(
            demos = demos,
            selectedId = selectedId,
            selectedDemo = selectedDemo,
            onSelect = onSelect
        )
        MobilePreviewContent(demo = selectedDemo, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun DocsSidebar(
    demos: List<ComponentDemo>,
    selectedId: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val groupedDemos = demos.groupBy { it.group }
    val themeController = LocalThemeColor.current

    Column(
        modifier = modifier
            .background(LocalDocsColorScheme.current.surface)
            .border(width = 1.dp, color = LocalDocsColorScheme.current.outlineVariant)
            .verticalScroll(rememberScrollState())
            .padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            HyperText(
                text = "HyperUI",
                color = LocalDocsColorScheme.current.onSurface,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp
            )
            HyperText(
                text = "Android Compose 组件文档",
                color = LocalDocsColorScheme.current.onSurfaceVariant,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }

        DocsThemeSettings(controller = themeController)

        groupedDemos.forEach { (group, items) ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                HyperText(
                    text = group,
                    color = LocalDocsColorScheme.current.onSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 16.sp
                )
                items.forEach { demo ->
                    DocsNavItem(
                        title = demo.title,
                        selected = selectedId == demo.id,
                        onClick = { onSelect(demo.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MobilePreviewTopBar(
    demos: List<ComponentDemo>,
    selectedId: String,
    selectedDemo: ComponentDemo,
    onSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalDocsColorScheme.current.surface)
            .border(1.dp, LocalDocsColorScheme.current.outlineVariant)
    ) {
        HyperNavBar(
            modifier = Modifier.fillMaxWidth(),
            titleContent = { HyperText("HyperUI Preview", maxLines = 1) },
            subtitleContent = { HyperText(selectedDemo.description, maxLines = 1) }
        )
        DocsThemeControls(
            controller = LocalThemeColor.current,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            demos.forEach { demo ->
                DocsNavChip(demo.title, selectedId == demo.id) { onSelect(demo.id) }
            }
        }
    }
}

/** 手机模式只展示真实组件视口，代码和 API 详情留在桌面文档布局。 */
@Composable
private fun MobilePreviewContent(demo: ComponentDemo, modifier: Modifier = Modifier) {
    PreviewSceneBackdrop(modifier = modifier.fillMaxSize()) {
        if (demo.fullScreenPreview) {
            Box(modifier = Modifier.fillMaxSize()) { demo.content() }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) { demo.content() }
        }
    }
}

@Composable
private fun DocsNavItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val textColor = if (selected) {
        LocalDocsColorScheme.current.primary
    } else {
        LocalDocsColorScheme.current.onSurface
    }

    HyperText(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 36.dp)
            .selectable(
                selected = selected,
                onClick = onClick
            )
            .then(
                if (selected) {
                    Modifier.background(
                        LocalDocsColorScheme.current.primaryContainer,
                        RoundedCornerShape(8.dp)
                    )
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 12.dp, vertical = 9.dp),
        color = textColor,
        fontSize = 14.sp,
        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
        lineHeight = 18.sp
    )
}

@Composable
private fun DocsNavChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val background = if (selected) LocalDocsColorScheme.current.primary else LocalDocsColorScheme.current.surfaceVariant
    val textColor = if (selected) LocalDocsColorScheme.current.onPrimary else LocalDocsColorScheme.current.onSurface

    HyperText(
        text = title,
        modifier = Modifier
            .background(background, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        color = textColor,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp
    )
}

@Composable
private fun ComponentContent(
    demo: ComponentDemo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LocalDocsColorScheme.current.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.widthIn(max = 980.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            ComponentHeader(demo = demo)
            PreviewCard(demo = demo)
            CodeCard(code = demo.code)
            ApiDocumentationCard(documentPaths = demo.apiDocumentPaths)
        }
    }
}

@Composable
private fun ComponentHeader(demo: ComponentDemo) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HyperText(
            text = demo.title,
            color = LocalDocsColorScheme.current.onBackground,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 36.sp
        )
        HyperText(
            text = demo.description,
            color = LocalDocsColorScheme.current.onSurfaceVariant,
            fontSize = 15.sp,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun PreviewCard(demo: ComponentDemo) {
    DocsCard {
        SectionLabel(title = "交互示例")
        PreviewSceneBackdrop(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (demo.fullScreenPreview) Modifier.height(600.dp) else Modifier.heightIn(min = 220.dp))
                .padding(22.dp)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                demo.content()
            }
        }
        SectionLabel(title = "预览属性与样式")
        DocumentationTable(
            headers = listOf("预览项", "关键属性", "样式说明"),
            rows = demo.variants.map { variant ->
                listOf(variant.label, variant.properties, variant.style)
            },
            columnWidths = listOf(180.dp, 340.dp, 360.dp)
        )
    }
}

@Composable
private fun CodeCard(code: String) {
    DocsCard {
        SectionLabel(title = "示例代码")
        HyperText(
            text = code,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .background(DocsCodeBackground, RoundedCornerShape(8.dp))
                .padding(18.dp),
            color = DocsCodeText,
            fontSize = 13.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
internal fun DocsCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalDocsColorScheme.current.surface, RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = LocalDocsColorScheme.current.outlineVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        content()
    }
}

@Composable
internal fun SectionLabel(title: String) {
    HyperText(
        text = title,
        color = LocalDocsColorScheme.current.onSurface,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold
    )
}
