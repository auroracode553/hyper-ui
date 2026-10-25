package hyper_ui.docs.data

import hyper_ui.docs.ui.UpdateToolDemo

internal fun toolsComponentDemos(): List<ComponentDemo> = listOf(
    ComponentDemo(id = "update_tool", group = "工具", title = "检查更新",
        description = "Android 静态 latest.json 检查、系统下载、真实进度和安装；此处只模拟交互，不访问 Gitee。",
        code = """
            val updates = rememberHyperUpdateController(BuildConfig.HYPER_UPDATE_CONFIG)
            HyperListTile(onPressed = { updates.check() },
                title = { HyperText("检查更新") }, subtitle = { HyperText(updates.summary) })
            HyperUpdateHost(updates)
        """.trimIndent(),
        variants = listOf(
            DemoVariant("配置", "publish-config.json", "repository 与 update 公开字段统一注入，不包含 Token"),
            DemoVariant("时机", "冷启动 / 恢复 / 手动", "约 5 秒、约 2 秒；手动绕过间隔并复用在途任务"),
            DemoVariant("版本", "latest.json / versionCode", "直接提供最新完整版本，旧下载完成后仍可重新查最新"),
            DemoVariant("下载", "DownloadManager", "真实字节、速度、剩余时间；关闭继续，取消才终止"),
            DemoVariant("平台", "Android-only", "Preview 仅模拟流程，真实调用见 Android 示例")
        ), apiDocumentPaths = listOf("tools/hyper-update.md"), content = { UpdateToolDemo() })
)
