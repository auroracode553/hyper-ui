/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/FeedbackComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.DialogDemo
import hyper_ui.docs.ui.DropdownMenuDemo
import hyper_ui.docs.ui.EmptyStateDemo
import hyper_ui.docs.ui.HyperDialogDemo
import hyper_ui.docs.ui.HyperPopupDemo
import hyper_ui.docs.ui.LevelCapsuleDemo
import hyper_ui.docs.ui.BatteryIndicatorDemo
import hyper_ui.docs.ui.PlaybackSpeedPanelDemo
import hyper_ui.docs.ui.PlaybackSpeedScaleDemo
import hyper_ui.docs.ui.ProgressDemo
import hyper_ui.docs.ui.ToastDemo
import hyper_ui.docs.ui.UpdateDialogDemo

private const val GROUP_FEEDBACK = "反馈组件"

internal fun feedbackComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(
        id = "empty_state",
        group = GROUP_FEEDBACK,
        title = "HyperEmptyState",
        description = "页面级空数据状态统一由 HyperPanel 承载，图标和操作通过 Slot 注入。",
        code = """
            HyperEmptyState(
                title = "暂无历史记录",
                description = "浏览过的页面会显示在这里",
                iconContent = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                actionContent = {
                    HyperButton(onClick = onRefresh) { Text("重新加载") }
                }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("纯标题", "description = null", "紧凑空状态文案"),
            DemoVariant("辅助说明", "description", "居中次级说明"),
            DemoVariant("图标", "iconContent", "由调用方注入图标资源"),
            DemoVariant("统一面板", "HyperPanel", "直接复用通用面板的默认样式"),
            DemoVariant("操作", "actionContent", "由调用方持有交互和结果状态")
        ),
        apiDocumentPaths = listOf("feedback/hyper-empty-state.md"),
        content = { EmptyStateDemo() }
    ),
    ComponentDemo(
        id = "dropdown",
        group = GROUP_FEEDBACK,
        title = "HyperDropdown",
        description = "参考系统菜单的内容自适应柔雾浮层：宽圆角、公共浮层描边与阴影、18sp 文本及危险项语义。",
        code = """
            HyperDropdown(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Item(onClick = onChangeBackground) {
                    Text("更换背景")
                }
                Item(
                    onClick = onDelete,
                    tone = HyperDropdownItemTone.Danger
                ) {
                    Text("删除")
                }
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("展开/关闭", "expanded", "乳白/炭灰面板与公共浮层深度"),
            DemoVariant("自适应宽度", "IntrinsicSize.Max", "按最宽菜单项收缩，最大 220dp"),
            DemoVariant("菜单项", "Item(closeOnClick)", "48dp 行高与即时按压反馈"),
            DemoVariant("危险项", "tone = Danger", "自动使用主题危险色"),
            DemoVariant("禁用态", "enabled = false", "降低文字对比度并取消反馈"),
            DemoVariant("自定义色", "HyperDropdownDefaults.colors", "保留调用方容器色 alpha"),
            DemoVariant("可选分隔线", "Divider()", "默认示例保持参考图的无分隔布局")
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
        description = "柔性玻璃竖向比例胶囊，支持亮度、音量等图标插槽；手势与显示时机由调用方管理。",
        code = """
            HyperLevelCapsule(
                progress = brightness,
                label = "${'$'}{(brightness * 100).toInt()}%",
                iconContent = {
                    Icon(Icons.Default.Info, contentDescription = null)
                }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("实时比例", "progress: Float", "从底部填充并限制在 0..1"),
            DemoVariant("百分比文案", "label", "居中单行反馈"),
            DemoVariant("图标插槽", "iconContent", "亮度、音量或业务状态图标"),
            DemoVariant("自定义尺寸", "modifier.width/height", "覆盖默认 52×156dp"),
            DemoVariant("自定义配色", "HyperLevelCapsuleDefaults.colors", "容器、填充与内容色；材质光影保持统一")
        ),
        apiDocumentPaths = listOf("feedback/hyper-level-capsule.md"),
        content = { LevelCapsuleDemo() }
    ),
    ComponentDemo(
        id = "playback_speed_panel",
        group = GROUP_FEEDBACK,
        title = "HyperPlaybackSpeedPanel",
        description = "紧凑型固定深色播放器倍速面板与覆盖层；速度、显示状态和自定义速度流程均由调用方持有。",
        code = """
            HyperPlaybackSpeedPanelOverlay(
                visible = panelVisible,
                currentSpeed = playbackSpeed,
                onSpeedChange = { playbackSpeed = it },
                onDismissRequest = { panelVisible = false },
                onCustomSpeedRequest = ::openCustomSpeedDialog
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("紧凑布局", "默认尺寸", "空间充足时约 468dp 宽、157dp 高"),
            DemoVariant("固定深色", "默认配色", "不跟随应用浅色/深色模式切换"),
            DemoVariant("受控状态", "visible/currentSpeed", "调用方持有显示状态和实时速度"),
            DemoVariant("预设档位", "speedOptions", "默认 0.25x、1x、2x、3x、4x"),
            DemoVariant("自定义入口", "onCustomSpeedRequest", "仅分发事件，不承载输入业务"),
            DemoVariant("Lucide 默认图标", "leadingContent 等", "Android 默认语义图标统一由 Lucide 提供，也可通过 Slot 替换"),
            DemoVariant("自定义配色", "HyperPlaybackSpeedPanelDefaults.colors", "Preview 可切换容器与强调色")
        ),
        apiDocumentPaths = listOf("feedback/hyper-playback-speed-panel.md"),
        content = { PlaybackSpeedPanelDemo() }
    ),
    ComponentDemo(
        id = "playback_speed_scale",
        group = GROUP_FEEDBACK,
        title = "HyperPlaybackSpeedScale",
        description = "播放器长按临时加速使用的紧凑型固定深色玻璃刻度，两端标签让出轨道上方空间。",
        code = """
            HyperPlaybackSpeedScale(
                selectedSpeed = temporarySpeed,
                leadingContent = {
                    Icon(Icons.Default.FastForward, contentDescription = null)
                }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("紧凑布局", "默认尺寸", "约 310dp 宽、59dp 高，两端档位位于轨道左右"),
            DemoVariant("默认档位", "SpeedOptions", "0.25x 到 4x 八档刻度"),
            DemoVariant("当前速度", "selectedSpeed", "高亮最近的速度刻度"),
            DemoVariant("固定深色", "默认配色", "不跟随应用浅色/深色模式切换"),
            DemoVariant("图标插槽", "leadingContent", "Android 默认使用 Lucide fast-forward，也可替换"),
            DemoVariant("自定义配色", "HyperPlaybackSpeedScaleDefaults.colors", "容器、轨道、刻度与文案；不暴露硬边框")
        ),
        apiDocumentPaths = listOf("feedback/hyper-playback-speed-scale.md"),
        content = { PlaybackSpeedScaleDemo() }
    ),
    ComponentDemo(
        id = "battery_indicator",
        group = GROUP_FEEDBACK,
        title = "HyperBatteryIndicator",
        description = "紧凑系统电池图标；Android 端可用 rememberHyperBatteryState 自动订阅电量，Preview 使用按钮模拟状态。",
        code = """
            val batteryState by rememberHyperBatteryState()

            if (batteryState.isAvailable) {
                HyperBatteryIndicator(
                    percentage = batteryState.percentage,
                    charging = batteryState.isCharging
                )
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("常规电量", "percentage", "无描边玻璃壳体内按比例填充"),
            DemoVariant("低电量", "percentage <= 20", "默认切换红色填充"),
            DemoVariant("充电中", "charging = true", "绿色填充且右侧展示 Lucide zap"),
            DemoVariant("隐藏数字", "showPercentage = false", "仅展示图形电量")
        ),
        apiDocumentPaths = listOf(
            "feedback/hyper-battery-indicator.md",
            "tools/hyper-battery-state.md"
        ),
        content = { BatteryIndicatorDemo() }
    ),
    ComponentDemo(
        id = "hyper_dialog",
        group = GROUP_FEEDBACK,
        title = "HyperDialog",
        description = "禁用平台默认宽度，在稳定的全尺寸 Dialog 根节点内居中面板。",
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
            DemoVariant("Dialog 宿主", "Compose Dialog", "usePlatformDefaultWidth = false"),
            DemoVariant("稳定根节点", "fillMaxSize", "面板始终在固定窗口内居中"),
            DemoVariant("稳定输入", "HyperTextField", "输入重组只更新面板内容"),
            DemoVariant("外部关闭", "dismissOnClickOutside", "由背景命中层控制"),
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
        description = "Alert 结构化对话框直接居中显示；高度服从平台窗口约束，长正文可滚动。",
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
            DemoVariant("平台约束", "Dialog constraints", "不按内容反向计算窗口尺寸"),
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
        description = "继承稳定全尺寸 Dialog 根节点的应用更新弹窗；调用方持有状态并注入 Release 加载与下载动作。",
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
