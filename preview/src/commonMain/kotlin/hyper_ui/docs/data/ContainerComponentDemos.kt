package hyper_ui.docs.data

import hyper_ui.docs.ui.ColorPickerDemo
import hyper_ui.docs.ui.PanelDemo

private const val GROUP_CONTAINER = "容器组件"

internal fun containerComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "color-picker",
        group = GROUP_CONTAINER,
        title = "HyperColorPicker",
        description = "主题色选择板，内置 32 种精选颜色（经典红、活力橙、海蓝、翡翠绿…），选中状态由调用方管理。",
        code = """
            var selectedColorId by remember { mutableStateOf("ocean_blue") }
            val currentOption = remember(selectedColorId) {
                HyperColorPickerDefaults.presetOptions.find { it.id == selectedColorId }
                    ?: HyperColorPickerDefaults.presetOptions.first()
            }

            Column(
                modifier = Modifier.widthIn(max = 400.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HyperColorPicker(
                    selectedId = selectedColorId,
                    onSelected = { option -> selectedColorId = option.id }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(currentOption.color)
                    )
                    Text(
                        text = "当前选择：${'$'}{currentOption.label}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        """.trimIndent(),
        content = { ColorPickerDemo() }
    ),
    ComponentDemo(
        id = "panel",
        group = GROUP_CONTAINER,
        title = "HyperPanel",
        description = "圆角面板容器，适合承载一组轻量内容和操作按钮。",
        code = """
            var acknowledged by remember { mutableStateOf(false) }

            HyperPanel(
                modifier = Modifier.widthIn(max = 520.dp),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Text(
                    text = "系统状态",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (acknowledged) "已查看状态详情" else "运行正常，最近同步 2 分钟前"
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    HyperButton(
                        text = "查看详情",
                        onClick = { acknowledged = true }
                    )
                    HyperButton(
                        text = "重置",
                        onClick = { acknowledged = false },
                        variant = HyperButtonVariant.Default
                    )
                }
            }
        """.trimIndent(),
        content = { PanelDemo() }
    )
)
