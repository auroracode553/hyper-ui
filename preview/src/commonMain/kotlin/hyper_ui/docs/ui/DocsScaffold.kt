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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hyper_ui.docs.LocalThemeColor
import hyper_ui.docs.ThemeColorController
import hyper_ui.docs.data.ComponentDemo
import hyper_ui.docs.data.componentDemos
import hyper_ui.docs.theme.DocsBackground
import hyper_ui.docs.theme.DocsBorder
import hyper_ui.docs.theme.DocsCodeBackground
import hyper_ui.docs.theme.DocsCodeText
import hyper_ui.docs.theme.DocsPreviewBackground
import hyper_ui.docs.theme.DocsSidebar

@Composable
fun HyperDocsApp(
    themeColorController: ThemeColorController,
    initialSelectedId: String? = null
) {
    CompositionLocalProvider(LocalThemeColor provides themeColorController) {
        val demos = remember { componentDemos() }
        var selectedId by remember(initialSelectedId) {
            mutableStateOf(
                initialSelectedId
                    ?.takeIf { candidateId -> demos.any { it.id == candidateId } }
                    ?: demos.first().id
            )
        }
        val selectedDemo = demos.firstOrNull { it.id == selectedId } ?: demos.first()

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
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
        MobileTopNav(
            demos = demos,
            selectedId = selectedId,
            onSelect = onSelect
        )
        ComponentContent(
            demo = selectedDemo,
            modifier = Modifier.weight(1f)
        )
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
            .background(DocsSidebar)
            .border(width = 1.dp, color = DocsBorder)
            .verticalScroll(rememberScrollState())
            .padding(22.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "HyperUI",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp
            )
            Text(
                text = "Android Compose 组件文档",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }

        ThemeColorPicker(
            currentColor = themeController.color,
            onColorChange = { themeController.update(it) }
        )

        groupedDemos.forEach { (group, items) ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = group,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
private fun ThemeColorPicker(
    currentColor: Color,
    onColorChange: (Color) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "主题色",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 16.sp
        )

        HyperColorPicker(
            selectedId = HyperColorPickerDefaults.presetOptions
                .minByOrNull {
                    val rDiff = it.color.red - currentColor.red
                    val gDiff = it.color.green - currentColor.green
                    val bDiff = it.color.blue - currentColor.blue
                    rDiff * rDiff + gDiff * gDiff + bDiff * bDiff
                }?.id ?: "",
            onSelected = { option -> onColorChange(option.color) },
            colorSize = 28.dp,
            horizontalSpacing = 6.dp,
            verticalSpacing = 8.dp,
            labelTopSpacing = 3.dp
        )

    }
}

@Composable
private fun MobileTopNav(
    demos: List<ComponentDemo>,
    selectedId: String,
    onSelect: (String) -> Unit
) {
    val themeController = LocalThemeColor.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DocsSidebar)
            .border(width = 1.dp, color = DocsBorder)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "HyperUI",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            demos.forEach { demo ->
                DocsNavChip(
                    title = demo.title,
                    selected = selectedId == demo.id,
                    onClick = { onSelect(demo.id) }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                .border(width = 1.dp, color = DocsBorder, shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(themeController.color)
                    .border(width = 1.dp, color = DocsBorder, shape = CircleShape)
            )
            Text(
                text = "主题色",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun DocsNavItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val background = if (selected) MaterialTheme.colorScheme.primaryContainer else rgba(0, 0, 0, 0f)
    val textColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 36.dp)
            .selectable(
                selected = selected,
                onClick = onClick
            )
            .background(background, RoundedCornerShape(8.dp))
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
    val background = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Text(
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
            .background(DocsBackground)
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
        }
    }
}

@Composable
private fun ComponentHeader(demo: ComponentDemo) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = demo.title,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 36.sp
        )
        Text(
            text = demo.description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 15.sp,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun PreviewCard(demo: ComponentDemo) {
    DocsCard {
        SectionLabel(title = "交互示例")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 220.dp)
                .background(DocsPreviewBackground, RoundedCornerShape(8.dp))
                .border(width = 1.dp, color = DocsBorder, shape = RoundedCornerShape(8.dp))
                .padding(22.dp),
            contentAlignment = Alignment.Center
        ) {
            demo.content()
        }
    }
}

@Composable
private fun CodeCard(code: String) {
    DocsCard {
        SectionLabel(title = "示例代码")
        Text(
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
private fun DocsCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = DocsBorder, shape = RoundedCornerShape(8.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        content()
    }
}

@Composable
private fun SectionLabel(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold
    )
}
