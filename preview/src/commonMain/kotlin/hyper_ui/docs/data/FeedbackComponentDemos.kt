/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/FeedbackComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.DialogDemo
import hyper_ui.docs.ui.DropdownMenuDemo
import hyper_ui.docs.ui.HyperPopupDemo
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
                onDismissRequest = { expanded = false },
                contentModifier = Modifier.padding(vertical = 10.dp)
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
        variants = listOf(
            DemoVariant("展开/关闭", "expanded", "实色浮层菜单"),
            DemoVariant("内容布局", "contentModifier = Modifier.padding(...)", "使用 Modifier 控制菜单内部内容"),
            DemoVariant("菜单项", "Item(closeOnClick)", "图标、文字与点击回调"),
            DemoVariant("分隔线", "Divider()", "不透明实色分隔")
        ),
        apiDocumentPaths = listOf("feedback/hyper-dropdown-menu.md"),
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
        variants = listOf(
            DemoVariant("线性确定", "progress: Float", "实色轨道、指示条与描边"),
            DemoVariant("线性不确定", "progress = null", "静态居中指示段"),
            DemoVariant("圆形确定", "progress: Float", "圆形 stroke"),
            DemoVariant("圆形不确定", "progress = null", "静态 stroke 弧段")
        ),
        apiDocumentPaths = listOf("feedback/hyper-progress-indicator.md"),
        content = { ProgressDemo() }
    ),
    ComponentDemo(
        id = "custom_popup",
        group = GROUP_FEEDBACK,
        title = "HyperPopup",
        description = "窗口居中的基础浮层会跳过未定位首帧，无顶部位移或显示/关闭动画，并使用不透明实色面板。",
        code = """
            HyperPopup(
                visible = visible,
                onDismissRequest = onDismiss,
                title = "编辑备注",
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
        variants = listOf(
            DemoVariant("基础面板", "visible + content", "响应式实色浮层面板"),
            DemoVariant("窗口居中", "Popup", "跳过未定位首帧并直接在窗口中心显示"),
            DemoVariant("外部关闭", "dismissOnClickOutside", "仅点击处理，不绘制遮罩"),
            DemoVariant("操作区", "actionContent", "固定底部按钮 slot")
        ),
        apiDocumentPaths = listOf("feedback/hyper-popup.md"),
        content = { HyperPopupDemo() }
    ),
    ComponentDemo(
        id = "dialog",
        group = GROUP_FEEDBACK,
        title = "HyperAlertDialog",
        description = "Alert 结构化对话框直接居中显示；内置标准实色描边，正文和按钮均为 slot。",
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
        variants = listOf(
            DemoVariant("结构化标题", "title", "固定标题区"),
            DemoVariant("正文", "bodyContent", "可滚动内容 slot"),
            DemoVariant("操作", "actionContent", "普通与危险操作按钮")
        ),
        apiDocumentPaths = listOf("feedback/hyper-alert-dialog.md"),
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
        variants = listOf(
            DemoVariant("检查中", "HyperUpdateState.Checking", "加载状态"),
            DemoVariant("可更新", "HyperUpdateState.UpdateAvailable", "版本信息与下载操作"),
            DemoVariant("已最新/失败", "UpToDate / Error", "结果反馈与重试")
        ),
        apiDocumentPaths = listOf("feedback/hyper-update-dialog.md"),
        content = { UpdateDialogDemo() }
    )
)
