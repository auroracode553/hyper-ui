/** 文件职责：在 hyper_ui 中负责提供 preview/src/commonMain/kotlin/hyper_ui/docs/data/FeedbackComponentDemos 可复用界面组件及交互封装。 */
package hyper_ui.docs.data

import hyper_ui.docs.ui.DialogDemo
import hyper_ui.docs.ui.DropdownMenuDemo
import hyper_ui.docs.ui.EmptyStateDemo
import hyper_ui.docs.ui.HyperDialogDemo
import hyper_ui.docs.ui.HyperPopoverDemo
import hyper_ui.docs.ui.HyperTooltipDemo
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
        description = "56dp 图标块与居中文案组成空状态；可注入恢复操作。",
        code = """
            HyperEmptyState(
                title = "暂无历史记录",
                message = "浏览过的页面会显示在这里",
                icon = {
                    HyperIcon(searchIcon, contentDescription = null)
                },
                action = {
                    HyperButton(onPressed = onRefresh) { HyperText("重新加载") }
                }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("纯标题", "description = null", "紧凑空状态文案"),
            DemoVariant("辅助说明", "description", "居中次级说明"),
            DemoVariant("图标", "iconContent", "由调用方注入图标资源"),
            DemoVariant("布局", "icon / action", "56dp 图标块、20dp 内容留白"),
            DemoVariant("操作", "actions", "由调用方持有交互和结果状态")
        ),
        apiDocumentPaths = listOf("feedback/hyper-empty-state.md"),
        content = { EmptyStateDemo() }
    ),
    ComponentDemo(
        id = "dropdown",
        group = GROUP_FEEDBACK,
        title = "HyperDropdown",
        description = "选择字段与匹配触发器宽度的锚定选项浮层，支持禁用、空选项和当前值。",
        code = """
            HyperDropdown(options = listOf(HyperOption("all", "全部"), HyperOption("favorite", "收藏")),
                value = selected, onChanged = { selected = it }, label = "范围")
        """.trimIndent(),
        variants = listOf(
            DemoVariant("受控值", "value / onChanged", "选项选择后收起"),
            DemoVariant("禁用", "enabled / HyperOption.enabled", "关闭交互"),
            DemoVariant("浮层", "menuMaxHeight", "默认最大 320dp"),
            DemoVariant("空列表", "options = emptyList()", "保留字段与占位文本")
        ),
        apiDocumentPaths = listOf("feedback/hyper-dropdown.md"),
        content = { DropdownMenuDemo() }
    ),
    ComponentDemo(
        id = "toast",
        group = GROUP_FEEDBACK,
        title = "HyperToast / hyperToast",
        description = "自适应宽度的语义色玻璃 Toast，支持自动关闭与操作区；Android 原生工具另附调用示例。",
        code = """
            HyperToast(visible, "保存成功", onClose = { visible = false }, type = "success")

            // Android-only 原生工具
            hyperToast(context, "保存成功")
            hyperToast(
                context = context,
                messageResource = R.string.saved,
                duration = HyperToastDuration.Long
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("语义色", "type", "成功、警告、错误、信息和默认样式"),
            DemoVariant("自适应宽度", "180..420dp", "短文案收缩，长文案最多三行"),
            DemoVariant("操作区", "actionLabel / onAction / action", "可注入关闭或撤销按钮"),
            DemoVariant("时长", "duration", "组件到时请求调用方关闭"),
            DemoVariant("原生时长", "HyperToastDuration", "Android 原生短/长 Toast"),
            DemoVariant("字符串资源", "messageResource", "由 Context 读取本地化文本"),
            DemoVariant("线程", "任意线程调用", "内部调度到 Android 主线程")
        ),
        apiDocumentPaths = listOf("feedback/hyper-toast.md"),
        content = { ToastDemo() }
    ),
    ComponentDemo(
        id = "progress",
        group = GROUP_FEEDBACK,
        title = "HyperProgress",
        description = "参考线性与圆形进度的三档尺寸，标签、归一化进度及不确定动画。",
        code = """
            HyperProgress(value = progress, showLabel = true)
            HyperProgress(value = null, type = "circular", size = "small", showLabel = false)
        """.trimIndent(),
        variants = listOf(
            DemoVariant("线性尺寸", "size", "4 / 6 / 12dp"),
            DemoVariant("圆形尺寸", "size", "48 / 64 / 80dp，stroke 4 / 6 / 8dp"),
            DemoVariant("不确定", "progress = null", "移动条 1400ms，圆环 1100ms"),
            DemoVariant("标签", "showLabel", "默认显示百分比")
        ),
        apiDocumentPaths = listOf("feedback/hyper-progress.md"),
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
                    HyperIcon(infoIcon, contentDescription = null)
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
        description = "紧凑型主题玻璃播放器倍速面板与覆盖层；速度、显示状态和自定义速度流程均由调用方持有。",
        code = """
            HyperPlaybackSpeedPanelOverlay(
                visible = panelVisible,
                value = playbackSpeed,
                onChanged = { playbackSpeed = it },
                onClose = { panelVisible = false },
                onCustomPressed = ::openCustomSpeedDialog
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("紧凑布局", "默认尺寸", "空间充足时约 468dp 宽、157dp 高"),
            DemoVariant("主题玻璃", "默认配色", "不跟随应用浅色/深色模式切换"),
            DemoVariant("受控状态", "visible/value", "调用方持有显示状态和实时速度"),
            DemoVariant("预设档位", "options", "默认 0.25x、1x、2x、3x、4x"),
            DemoVariant("自定义入口", "onCustomPressed", "仅分发事件，不承载输入业务"),
            DemoVariant("Lucide 默认图标", "leading 等", "Android 默认语义图标统一由 Lucide 提供，也可通过 Slot 替换"),
            DemoVariant("自定义配色", "HyperPlaybackSpeedPanelDefaults.colors", "Preview 可切换容器与强调色")
        ),
        apiDocumentPaths = listOf("feedback/hyper-playback-speed-panel.md"),
        content = { PlaybackSpeedPanelDemo() }
    ),
    ComponentDemo(
        id = "playback_speed_scale",
        group = GROUP_FEEDBACK,
        title = "HyperPlaybackSpeedScale",
        description = "播放器长按临时加速使用的紧凑型主题玻璃刻度，两端标签让出轨道上方空间。",
        code = """
            HyperPlaybackSpeedScale(
                value = temporarySpeed,
                leading = {
                    HyperIcon(fastForwardIcon, contentDescription = null)
                }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("紧凑布局", "默认尺寸", "约 310dp 宽、59dp 高，两端档位位于轨道左右"),
            DemoVariant("默认档位", "SpeedOptions", "0.25x 到 4x 八档刻度"),
            DemoVariant("当前速度", "value", "高亮最近的速度刻度"),
            DemoVariant("主题玻璃", "默认配色", "不跟随应用浅色/深色模式切换"),
            DemoVariant("图标插槽", "leading", "Android 默认使用 Lucide fast-forward，也可替换"),
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
            DemoVariant("常规电量", "percentage", "细边缘玻璃壳体内按比例填充"),
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
                onClose = onDismiss,
                title = "编辑备注",
                actions = {
                    HyperButton(onPressed = onSave) { HyperText("保存") }
                }
            ) {
                HyperTextField(
                    value = value,
                    onChanged = onValueChange
                )
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("Dialog 宿主", "Compose Dialog", "usePlatformDefaultWidth = false"),
            DemoVariant("稳定根节点", "fillMaxSize", "面板始终在固定窗口内居中"),
            DemoVariant("稳定输入", "HyperTextField", "输入重组只更新面板内容"),
            DemoVariant("外部关闭", "dismissOnClickOutside", "由背景命中层控制"),
            DemoVariant("操作区", "actions", "固定底部按钮 slot")
        ),
        apiDocumentPaths = listOf("feedback/hyper-dialog.md"),
        content = { HyperDialogDemo() }
    ),
    ComponentDemo(
        id = "custom_popup",
        group = GROUP_FEEDBACK,
        title = "HyperPopover",
        description = "贴着触发点定位的内容浮层，默认最大宽 280dp、高度不超过窗口 40%，空间不足自动翻转。",
        code = """
            HyperPopover(visible = visible, onClose = { visible = false }, child = {
                HyperButton({ visible = !visible }) { HyperText("编辑备注") }
            }) { HyperTextField(note, { note = it }) }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("锚定", "anchorContent", "6dp 间距与 12dp 安全边距"),
            DemoVariant("长内容", "content", "达到高度上限后滚动"),
            DemoVariant("受控显隐", "visible", "外部点击请求关闭")
        ),
        apiDocumentPaths = listOf("feedback/hyper-popover.md"),
        content = { HyperPopoverDemo() }
    ),
    ComponentDemo(
        id = "dialog",
        group = GROUP_FEEDBACK,
        title = "HyperAlertDialog",
        description = "Alert 结构化对话框直接居中显示；高度服从平台窗口约束，长正文可滚动。",
        code = """
            HyperAlertDialog(
                visible = visible,
                onClose = onDismiss,
                title = "确认删除",
                dismissOnClickOutside = dismissOnClickOutside,
                content = { HyperText("删除后无法恢复，是否继续？") },
                actions = {
                    HyperButton(onPressed = onDismiss) { HyperText("取消") }
                    HyperButton(
                        onPressed = onDelete,
                        colorStyle = "danger"
                    , type = "filled") { HyperText("删除") }
                }
            )
        """.trimIndent(),
        variants = listOf(
            DemoVariant("结构化标题", "title", "固定标题区"),
            DemoVariant("平台约束", "Dialog constraints", "不按内容反向计算窗口尺寸"),
            DemoVariant("长正文", "content", "超出高度后可滚动"),
            DemoVariant("操作", "actions", "普通与危险操作按钮")
        ),
        apiDocumentPaths = listOf("feedback/hyper-alert-dialog.md"),
        content = { DialogDemo() }
    ),
    ComponentDemo(
        id = "tooltip",
        group = GROUP_FEEDBACK,
        title = "HyperTooltip",
        description = "12dp 圆角轻提示；悬停与长按延迟 500ms、焦点即时显示，支持禁用和长文本宽度。",
        code = """
            HyperTooltip(message = "提示文本") {
                HyperText("悬停查看")
            }
        """.trimIndent(),
        variants = listOf(
            DemoVariant("触发", "hover / long press / focus", "悬停和长按 500ms，焦点即时"),
            DemoVariant("禁用与延迟", "enabled / hoverDelay", "可切换状态与悬停等待时间"),
            DemoVariant("宽度", "maxWidth", "长文本在 240dp 内换行")
        ),
        apiDocumentPaths = listOf("feedback/hyper-tooltip.md"),
        content = { HyperTooltipDemo() }
    ),
    ComponentDemo(
        id = "update_dialog",
        group = GROUP_FEEDBACK,
        title = "HyperUpdateDialog",
        description = "与 Flutter 相同的更新流程、状态预览和 2.7.3 → 2.8.0 示例；共用 28dp 圆角面板与固定操作区。",
        code = """
            HyperUpdateDialog(status = status, currentVersion = "2.7.3", release = release,
                onClose = onClose, onLater = onClose, onRetry = onRetry,
                onDownload = onDownload, onInstall = onInstall)
        """.trimIndent(),
        variants = listOf(
            DemoVariant("状态", "HyperUpdateStatus", "检查/更新/下载/安装/完成/错误"),
            DemoVariant("错误反馈", "status = error / message", "仅展示原因、重试与关闭，不展示图标、标题和版本框"),
            DemoVariant("操作样式", "HyperButton.type", "主操作 type = filled / colorStyle = primary，关闭、取消和稍后再说省略 type，使用默认描边"),
            DemoVariant("标识与版本", "受控状态布局", "40dp 标识块、20dp 图标；检查中无版本，其他版本信息不嵌套卡片"),
            DemoVariant("尺寸与排版", "maxWidth = 420.dp", "弹层安全区外间距 20dp，内容 24dp，圆角 28dp；正文滚动、底部操作固定"),
            DemoVariant("必要更新", "mandatory", "安装与必要更新阶段锁定关闭"),
            DemoVariant("业务回调", "onDownload / onInstall", "仅派发动作，调用方维护状态"),
            DemoVariant("模拟", "Preview", "与 Flutter 相同分组、标签、强制更新和未知总量开关；关闭弹层后下载继续")
        ),
        apiDocumentPaths = listOf("feedback/hyper-update-dialog.md"),
        content = { UpdateDialogDemo() }
    )
)
