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
        description = "浮层菜单容器，菜单项内容使用 slot 渲染，可配置点击后是否关闭。",
        code = """
            HyperDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Item(onClick = onOpenDetail) {
                    Icon(Icons.Default.Info, contentDescription = null)
                    Text("查看详情")
                }
                Divider()
                Item(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Text("删除")
                }
            }
        """.trimIndent(),
        content = { DropdownMenuDemo() }
    ),
    ComponentDemo(
        id = "progress",
        group = GROUP_FEEDBACK,
        title = "HyperProgressIndicator",
        description = "线性与圆形进度指示器。progress 为 null 时表示不确定加载。",
        code = """
            HyperLinearProgressIndicator(progress = progress)
            HyperLinearProgressIndicator(progress = null)
            HyperCircularProgressIndicator(progress = progress)
            HyperCircularProgressIndicator(progress = null)
        """.trimIndent(),
        content = { ProgressDemo() }
    ),
    ComponentDemo(
        id = "custom_dialog",
        group = GROUP_FEEDBACK,
        title = "HyperDialog",
        description = "基础对话框容器，只负责浮层、尺寸、滚动内容和底部 action slot，无遮罩。",
        code = """
            HyperDialog(
                visible = visible,
                onDismissRequest = onDismiss,
                actionContent = {
                    HyperButton(onClick = onCancel) { Text("取消") }
                    HyperButton(onClick = onSave) { Text("保存") }
                }
            ) {
                Text("编辑备注")
                HyperTextField(
                    value = value,
                    onValueChange = onValueChange
                )
            }
        """.trimIndent(),
        content = { HyperDialogDemo() }
    ),
    ComponentDemo(
        id = "dialog",
        group = GROUP_FEEDBACK,
        title = "HyperAlertDialog",
        description = "Alert 结构化对话框。标题、正文和按钮均为 slot，不内置确认/取消业务文案。",
        code = """
            HyperAlertDialog(
                visible = visible,
                onDismissRequest = onDismiss,
                titleContent = { Text("确认删除") },
                bodyContent = { Text("删除后无法恢复，是否继续？") },
                actionContent = {
                    HyperButton(onClick = onDismiss) { Text("取消") }
                    HyperButton(
                        onClick = onDelete,
                        tone = HyperButtonTone.Danger
                    ) { Text("删除") }
                }
            )
        """.trimIndent(),
        content = { DialogDemo() }
    )
)
