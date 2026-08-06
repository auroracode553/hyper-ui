package hyper_ui.docs.data

import hyper_ui.docs.ui.ButtonDemo
import hyper_ui.docs.ui.IconButtonDemo

private const val GROUP_BASIC = "基础组件"

internal fun basicComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "button",
        group = GROUP_BASIC,
        title = "HyperButton",
        description = "按钮组件，默认最小高度 40.dp，支持 Default、Primary、Success、Info、Warning、Danger 六种视觉变体、禁用状态和尺寸参数。",
        code = """
            var clicks by remember { mutableStateOf(0) }

            Column(
                modifier = Modifier.widthIn(max = 560.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    HyperButton(
                        text = "默认按钮",
                        onClick = { clicks += 1 },
                        variant = HyperButtonVariant.Default
                    )
                    HyperButton(
                        text = "主要按钮",
                        onClick = { clicks += 1 },
                        variant = HyperButtonVariant.Primary
                    )
                    HyperButton(
                        text = "成功按钮",
                        onClick = { clicks += 1 },
                        variant = HyperButtonVariant.Success
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    HyperButton(
                        text = "信息按钮",
                        onClick = { clicks += 1 },
                        variant = HyperButtonVariant.Info
                    )
                    HyperButton(
                        text = "警告按钮",
                        onClick = { clicks += 1 },
                        variant = HyperButtonVariant.Warning
                    )
                    HyperButton(
                        text = "危险按钮",
                        onClick = { clicks = 0 },
                        variant = HyperButtonVariant.Danger
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    HyperButton(
                        text = "禁用默认",
                        onClick = {},
                        enabled = false,
                        variant = HyperButtonVariant.Default
                    )
                    HyperButton(
                        text = "禁用主要",
                        onClick = {},
                        enabled = false
                    )
                }
                HyperButton(
                    text = "小按钮",
                    onClick = { clicks += 1 },
                    minHeight = 32.dp,
                    horizontalPadding = 10.dp,
                    verticalPadding = 5.dp,
                    fontSize = 13.sp
                )
                Text(
                    text = "点击次数：${'$'}clicks",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        """.trimIndent(),
        content = { ButtonDemo() }
    ),
    ComponentDemo(
        id = "icon_button",
        group = GROUP_BASIC,
        title = "HyperIconButton",
        description = "圆形图标按钮，适合放在顶部栏、列表操作位和轻量工具位。",
        code = """
            var selectedAction by remember { mutableStateOf("未选择操作") }

            Column(
                modifier = Modifier.widthIn(max = 420.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HyperIconButton(
                        imageVector = Icons.Default.Search,
                        contentDescription = "搜索",
                        onClick = { selectedAction = "搜索" },
                        backgroundColor = MaterialTheme.colorScheme.surface
                    )
                    HyperIconButton(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "通知",
                        onClick = { selectedAction = "通知" },
                        tint = MaterialTheme.colorScheme.primary,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer
                    )
                    HyperIconButton(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "删除",
                        onClick = { selectedAction = "删除" },
                        tint = MaterialTheme.colorScheme.error,
                        backgroundColor = MaterialTheme.colorScheme.errorContainer
                    )
                    HyperIconButton(
                        imageVector = Icons.Default.Close,
                        contentDescription = "关闭",
                        onClick = {},
                        enabled = false,
                        backgroundColor = MaterialTheme.colorScheme.surface
                    )
                }
                Text(
                    text = selectedAction,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
        """.trimIndent(),
        content = { IconButtonDemo() }
    )
)
