package hyper_ui.docs.data

import hyper_ui.docs.ui.DialogDemo
import hyper_ui.docs.ui.DropdownMenuDemo
import hyper_ui.docs.ui.HyperDialogDemo
import hyper_ui.docs.ui.ProgressDemo

private const val GROUP_FEEDBACK = "反馈组件"

internal fun feedbackComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "dropdown",
        group = GROUP_FEEDBACK,
        title = "HyperDropdownMenu",
        description = "浮层菜单组件，提供大圆角面板、柔和边框和自动关闭的菜单项作用域。",
        code = """
            var expanded by remember { mutableStateOf(false) }
            var selectedAction by remember { mutableStateOf("尚未选择") }

            Box(
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .height(180.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HyperButton(
                        text = "打开菜单",
                        onClick = { expanded = true },
                        variant = HyperButtonVariant.Default
                    )
                    Text(
                        text = selectedAction,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }

                HyperDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    alignment = Alignment.TopCenter
                ) {
                    Item(
                        text = "标记完成",
                        leadingIcon = Icons.Default.Check,
                        onClick = { selectedAction = "标记完成" }
                    )
                    Item(
                        text = "查看详情",
                        leadingIcon = Icons.Default.Info,
                        onClick = { selectedAction = "查看详情" }
                    )
                    Divider()
                    Item(
                        text = "删除",
                        leadingIcon = Icons.Default.Delete,
                        textColor = MaterialTheme.colorScheme.error,
                        onClick = { selectedAction = "删除" }
                    )
                }
            }
        """.trimIndent(),
        content = { DropdownMenuDemo() }
    ),
    ComponentDemo(
        id = "progress",
        group = GROUP_FEEDBACK,
        title = "HyperProgressBar",
        description = "进度条组件，传入 0f..1f 表示确定进度，传入 null 表示不确定加载（等价于 HyperLoadingProgress 快捷组件）。",
        code = """
            // 确定进度
            var progress by remember { mutableStateOf(0.42f) }

            HyperProgressBar(progress = progress)
            HyperProgressBar(
                progress = progress,
                progressColor = Color(0.12f, 0.50f, 1f, 1f)
            )

            // 不确定加载（progress = null）
            HyperProgressBar(progress = null)
            HyperProgressBar(
                progress = null,
                height = 4.dp,
                progressColor = Color(0.03f, 0.76f, 0.38f, 1f)
            )

            // 也可使用快捷组件 HyperLoadingProgress，等价于 HyperProgressBar(progress = null)
            HyperLoadingProgress()
            HyperLoadingProgress(
                height = 8.dp,
                progressColor = Color(0.12f, 0.50f, 1f, 1f)
            )
        """.trimIndent(),
        content = { ProgressDemo() }
    ),
    ComponentDemo(
        id = "custom_dialog",
        group = GROUP_FEEDBACK,
        title = "HyperDialog",
        description = "基础对话框容器，从屏幕居中淡入缩放弹出，内容通过 slot 自定义渲染，无遮罩，可放入输入框并隔离外层文本选择容器。",
        code = """
            var showDialog by remember { mutableStateOf(false) }
            var savedNote by remember { mutableStateOf("默认备注") }
            var draftNote by remember { mutableStateOf(savedNote) }

            HyperButton(
                text = "编辑备注",
                onClick = {
                    draftNote = savedNote
                    showDialog = true
                }
            )

            HyperDialog(
                show = showDialog,
                onDismissRequest = { showDialog = false },
                actions = {
                    HyperButton(
                        text = "取消",
                        variant = HyperButtonVariant.Default,
                        onClick = { showDialog = false }
                    )
                    HyperButton(
                        text = "保存",
                        onClick = {
                            savedNote = draftNote.ifBlank { "未填写备注" }
                            showDialog = false
                        }
                    )
                }
            ) {
                Text(
                    text = "编辑备注",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                HyperTextField(
                    value = draftNote,
                    onValueChange = { draftNote = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 88.dp, max = 220.dp),
                    placeholder = "请输入备注",
                    singleLine = false,
                    minHeight = 88.dp
                )
            }
        """.trimIndent(),
        content = { HyperDialogDemo() }
    ),
    ComponentDemo(
        id = "dialog",
        group = GROUP_FEEDBACK,
        title = "HyperConfirmDialog",
        description = "确认对话框，适合危险操作、二次确认和简短提示。",
        code = """
            var showDialog by remember { mutableStateOf(false) }
            var resultText by remember { mutableStateOf("等待操作") }

            Column(
                modifier = Modifier.widthIn(max = 420.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HyperButton(
                    text = "删除数据",
                    variant = HyperButtonVariant.Danger,
                    onClick = { showDialog = true }
                )
                Text(
                    text = resultText,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }

            HyperConfirmDialog(
                show = showDialog,
                title = "确认删除",
                message = "删除后无法恢复，是否继续？",
                confirmText = "继续删除",
                cancelText = "取消",
                onConfirm = {
                    resultText = "已确认删除"
                    showDialog = false
                },
                onCancel = {
                    resultText = "已取消"
                    showDialog = false
                }
            )
        """.trimIndent(),
        content = { DialogDemo() }
    )
)
