/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/FeedbackComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.DialogDemo
import hyper_ui.docs.ui.DropdownMenuDemo
import hyper_ui.docs.ui.HyperDialogDemo
import hyper_ui.docs.ui.HyperPopupDemo
import hyper_ui.docs.ui.LevelCapsuleDemo
import hyper_ui.docs.ui.ProgressDemo
import hyper_ui.docs.ui.ToastDemo
import hyper_ui.docs.ui.UpdateDialogDemo

private const val GROUP_FEEDBACK = "反馈组件"

internal fun feedbackComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "dropdown",
        group = GROUP_FEEDBACK,
        title = "HyperDropdown",
        description = "浮层菜单使用不透明实色面板，菜单项和分割线不依赖透明度，可配置点击后是否关闭。",
        code = """
            HyperDropdown(
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
        apiDocumentPaths = listOf("feedback/hyper-dropdown.md"),
        content = { DropdownMenuDemo() }
    ),
    ComponentDemo(
        id = "toast",
        group = GROUP_FEEDBACK,
        title = "hyperToast",
        description = "Android 原生 Toast 封装，统一短/长时长并自动切换到主线程；跨平台 Preview 使用交互模拟。",
        code = """
            hyperToast(context, "保存成功")
            hyperToast(
                context = context,
                messageResource = R.string.saved,
                duration = HyperToastDuration.Long
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("短提示", "HyperToastDuration.Short", "Android 原生短 Toast"),
            DemoVariant("长提示", "HyperToastDuration.Long", "Android 原生长 Toast"),
            DemoVariant("字符串资源", "messageResource", "由 Context 读取本地化文本"),
            DemoVariant("线程", "任意线程调用", "内部调度到 Android 主线程")
        ),
        apiDocumentPaths = listOf("feedback/hyper-toast.md"),
        content = { ToastDemo() }
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
        id = "level_capsule",
        group = GROUP_FEEDBACK,
        title = "HyperLevelCapsule",
        description = "面向播放器亮度、音量等连续比例反馈的竖向胶囊；手势与显示时机由调用方管理。",
        code = """
            HyperLevelCapsule(
                progress = brightness,
                label = "${'$'}{(brightness * 100).toInt()}%"
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("实时比例", "progress: Float", "从底部填充并限制在 0..1"),
            DemoVariant("百分比文案", "label", "居中单行反馈"),
            DemoVariant("自定义尺寸", "modifier.width/height", "覆盖默认 40×140dp"),
            DemoVariant("自定义配色", "HyperLevelCapsuleDefaults.colors", "容器、填充、文字与描边")
        ),
        apiDocumentPaths = listOf("feedback/hyper-level-capsule.md"),
        content = { LevelCapsuleDemo() }
    ),
    ComponentDemo(
        id = "hyper_dialog",
        group = GROUP_FEEDBACK,
        title = "HyperDialog",
        description = "固定窗口根尺寸的模态对话框；输入和正文变化不会反复调整平台窗口。",
        code = """
            HyperDialog(
                visible = visible,
                onDismissRequest = onDismiss,
                title = "编辑备注",
                actionContent = {
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
            DemoVariant("固定窗口", "fillMaxSize root", "正文变化只重排内部面板"),
            DemoVariant("稳定输入", "HyperTextField", "输入重组不重建 Dialog 窗口"),
            DemoVariant("外部关闭", "dismissOnClickOutside", "透明命中层，不绘制蒙层"),
            DemoVariant("操作区", "actionContent", "固定底部按钮 slot")
        ),
        apiDocumentPaths = listOf("feedback/hyper-dialog.md"),
        content = { HyperDialogDemo() }
    ),
    ComponentDemo(
        id = "custom_popup",
        group = GROUP_FEEDBACK,
        title = "HyperPopup",
        description = "窗口居中的基础浮层最大高度为窗口的 70%，长内容滚动且操作区固定。",
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
            DemoVariant("响应式高度", "MaxHeightFraction", "最大为窗口高度的 70%"),
            DemoVariant("窗口居中", "Popup host", "自定义位置提供器忽略调用节点锚点"),
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
        description = "Alert 结构化对话框直接居中显示；最大高度为窗口高度的 70%，长正文可滚动。",
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
            DemoVariant("响应式高度", "MaxHeightFraction", "最大为窗口高度的 70%"),
            DemoVariant("长正文", "bodyContent", "超出高度后可滚动"),
            DemoVariant("操作", "actionContent", "普通与危险操作按钮")
        ),
        apiDocumentPaths = listOf("feedback/hyper-alert-dialog.md"),
        content = { DialogDemo() }
    ),
    ComponentDemo(
        id = "update_dialog",
        group = GROUP_FEEDBACK,
        title = "HyperUpdateDialog",
        description = "最大高度为窗口 70% 的应用更新弹窗；调用方持有状态并注入 Release 加载与下载动作。",
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
