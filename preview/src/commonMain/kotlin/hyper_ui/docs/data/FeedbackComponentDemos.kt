/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/FeedbackComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.DialogDemo
import hyper_ui.docs.ui.DropdownMenuDemo
import hyper_ui.docs.ui.HyperDialogDemo
import hyper_ui.docs.ui.ProgressDemo
import hyper_ui.docs.ui.UpdateDialogDemo

private const val GROUP_FEEDBACK = "反馈组件"

internal fun feedbackComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "dropdown",
        group = GROUP_FEEDBACK,
        title = "HyperDropdownMenu",
        description = "浮层菜单使用不透明实色面板，菜单项和分割线不依赖透明度，可配置点击后是否关闭。",
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
        description = "线性与圆形轨道均使用不透明实色；progress 为 null 时表示不确定加载。",
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
        description = "基础对话框全程使用不透明实色面板和缩放动画；支持响应式宽度、固定标题与正文滚动。",
        code = """
            HyperDialog(
                visible = visible,
                onDismissRequest = onDismiss,
                title = "编辑备注",
                widthFraction = 0.9f,
                dismissOnClickOutside = dismissOnClickOutside,
                actionContent = {
                    HyperButton(onClick = onCancel) { Text("取消") }
                    HyperButton(onClick = onSave) { Text("保存") }
                }
            ) {
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
        description = "Alert 结构化对话框继承不透明实色面板；支持点击空白关闭，正文和按钮均为 slot。",
        code = """
            HyperAlertDialog(
                visible = visible,
                onDismissRequest = onDismiss,
                title = "确认删除",
                dismissOnClickOutside = dismissOnClickOutside,
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
    ),
    ComponentDemo(
        id = "update_dialog",
        group = GROUP_FEEDBACK,
        title = "HyperUpdateDialog",
        description = "使用不透明实色面板的应用更新弹窗；调用方持有状态并注入 Release 加载与下载动作。",
        code = """
            val checker = HyperUpdateChecker(
                HyperReleaseLoader { releaseUrl ->
                    repository.loadLatestRelease(releaseUrl)
                }
            )
            val result = checker.check(
                HyperUpdateRequest(
                    currentVersionName = currentVersion,
                    releaseUrl = releaseUrl
                )
            )

            HyperUpdateDialog(
                state = updateState,
                onDismissRequest = onDismiss,
                onRetry = onRetry,
                onDownload = onDownload
            )
        """.trimIndent(),
        content = { UpdateDialogDemo() }
    )
)
