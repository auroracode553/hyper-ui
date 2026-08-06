# AGENTS.md

## 项目运行约束

- 禁止自动运行、编译、打包、部署任何代码项目。
- 不生成一键启动脚本、自动构建命令或自动执行逻辑。
- 如需运行指令，只能作为手动执行说明提供，由使用者自主执行。
- 不主动安装依赖或配置系统环境；依赖清单需要单独罗列。

## 代码架构规范

- 遵循单一职责原则，组件、工具、配置、业务逻辑分层隔离。
- 禁止把业务状态、网络请求、数据库访问或权限申请写进 UI 组件库。
- 禁止全局变量滥用；组件依赖通过参数、回调或接口注入。
- 公共通用逻辑抽离至独立工具文件，不重复粘贴。
- **禁止任何组件添加遮罩/蒙层（scrim/overlay）**：弹窗、对话框、菜单等所有组件均不加半透明遮罩背景，组件只负责自身面板样式的渲染。

## 文件规模限制

- 单个源码文件有效代码行数不超过 1000 行。
- 临近上限时必须主动拆分文件，并说明文件用途与引入关系。
- 不默认扫描 `dist` 和 `node_modules` 目录。

## 颜色使用规范

- 禁止使用 `Color(0xFFRRGGBB)` 十六进制硬编码颜色。
- 必须使用 RGBA 分量格式：`Color(red, green, blue, alpha)`，四个参数均为 `Float`，范围 `0f..1f`。
- 示例：`Color(1f, 0f, 0f, 1f)` 表示红色，`Color(0.2f, 0.6f, 1f, 1f)` 表示蓝色。

## 组件与 Preview 规范

- UI 库中所有配置、组件、工具方法和可见功能，都必须在 `preview/` 中呈现。
- 新增或修改组件时，必须同步更新 `preview/src/commonMain/kotlin/hyper_ui/docs/data/ComponentDemos.kt` 聚合入口和对应分组的 `*ComponentDemos.kt`。
- 新增或修改交互效果时，必须同步更新 `preview/src/commonMain/kotlin/hyper_ui/docs/ui/` 下对应分组的 `*ComponentShowcases.kt` 可交互示例。
- Preview 示例必须允许用户操作查看状态变化、变体、禁用态、方向、颜色、进度或反馈结果。
- Android-only 工具方法不能直接引入 Desktop preview 编译链；应在 preview 中提供可交互模拟和 Android 端调用示例代码。
- 文档代码片段应保持简洁，展示最小可用调用方式。

## 文档同步

- 新增或修改组件、配置、枚举、工具方法后，必须同步更新 `README.md`、`vitepress/docs/component-index.md` 和 `vitepress/docs/components/` 下对应组件页。
- `vitepress/docs/index.md` 是 AI 与调用方的首读入口；具体组件页必须包含包名、真实公开签名、参数默认值、状态归属、使用约束和最小调用方式。
- VitePress 负责渲染 `vitepress/docs/` 中的 Markdown；不得维护第二份组件正文。
- Wasm 只负责交互预览，不能作为 AI 推断 API 的事实来源。新增组件页时应通过 `WasmPreview` 引用对应 Preview ID。
- HyperUI 公开 API 统一声明在 `hyper_ui` 包，示例可使用 `import hyper_ui.*`；`hyper_ui.core` 仅为 UI 库内部公共工具目录，调用方示例不要直接导入。
