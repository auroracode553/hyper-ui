# AGENTS.md

## 代码架构规范

- 遵循单一职责原则，组件、工具、配置、业务逻辑分层隔离。
- 禁止把业务状态、网络请求、数据库访问或权限申请写进 UI 组件库。
- 禁止全局变量滥用；组件依赖通过参数、回调或接口注入。
- 公共通用逻辑抽离至独立工具文件，不重复粘贴。
- 涉及重构、组件 API 调整或架构修正时，优先采用成熟、清晰的破坏性重构方案；不要为了兼容旧调用而堆叠废弃别名、兼容壳或双轨实现。完成后必须同步更新所有调用点、文档说明与必要示例，保持组件库只有一套明确方案。

## 文件规模限制

- 单个源码文件有效代码行数不超过 1000 行。
- 临近上限时必须主动拆分文件，并说明文件用途与引入关系。
- 不默认扫描 `dist` 和 `node_modules` 目录。

## 文件清理规则

- 禁止新增版权、许可、授权等声明文件，也不要在 README、组件文档或源码注释中追加这类声明。
- 禁止创建与当前功能、构建或必要文档无关的文件；优先复用现有文件，不额外生成过程报告、备份或一次性脚本。
- 验证截图、日志等临时产物放在系统临时目录，任务完成后清理，不保存在项目目录。
- 修改完成后清理重复、废弃和未使用的文件，同时删除失效引用。

## 平台范围

- 当前项目的产品目标是 Android 手机端 Compose UI，优先覆盖 320dp 至 430dp 的竖屏宽度和系统安全区。
- 不为平板、桌面、横屏或多栏桌面布局增加专用 API；需要更宽空间时由调用方通过 `Modifier` 组合布局。
- `preview/` 的 Desktop/Wasm 目标仅用于组件文档和交互验收，不代表公开 API 支持 Desktop、Wasm 或其他消费平台。
- 文档必须明确区分“Android 手机端支持范围”和“Preview 运行方式”，不要把预览目标写成产品平台。

## 颜色使用规范

- 禁止使用 `Color(0xFFRRGGBB)` 十六进制硬编码颜色。
- 必须使用 RGBA 分量格式：`Color(red, green, blue, alpha)`，四个参数均为 `Float`，范围 `0f..1f`。
- 示例：`Color(1f, 0f, 0f, 1f)` 表示红色，`Color(0.2f, 0.6f, 1f, 1f)` 表示蓝色。

## 组件与 Preview 规范

- 对应组件预览以 `D:/my_project/flutter_project/doc_ui_kit` 为视觉基准；手机外框、背景、安全区、分组顺序、文案、图标、尺寸、间距与展示状态保持一致，不添加仅 Compose 端存在的标题、控制项或计数。

- 本项目与 `D:/my_project/flutter_project/doc_ui_kit` 的对应组件保持默认 UI 一致。修改默认布局、尺寸、按钮类型或状态展示规则时，同时更新另一端的实现、文档与预览示例；框架 API 差异不能导致默认视觉规则分叉。

- UI 库中所有配置、组件、工具方法和可见功能，都必须在 `preview/` 中呈现。
- 新增或修改组件时，必须同步更新 `preview/src/commonMain/kotlin/hyper_ui/docs/data/ComponentDemos.kt` 聚合入口和对应分组的 `*ComponentDemos.kt`。
- 新增或修改交互效果时，必须同步更新 `preview/src/commonMain/kotlin/hyper_ui/docs/ui/` 下对应分组的 `*ComponentShowcases.kt` 可交互示例。
- Preview 示例必须允许用户操作查看状态变化、变体、禁用态、方向、颜色、进度或反馈结果。
- Android-only 工具方法不能直接引入 Desktop preview 编译链；应在 preview 中提供可交互模拟和 Android 端调用示例代码。
- 文档代码片段应保持简洁，展示最小可用调用方式。

## 文档同步

- 新增或修改组件、配置、枚举、工具方法后，必须同步更新 `README.md`、`vitepress/docs/component-index.md` 和 `vitepress/docs/components/` 下对应组件页。
- `vitepress/docs/index.md` 是 AI 与调用方的首读入口；具体组件页必须包含包名、真实公开签名、参数默认值、状态归属、使用约束和最小调用方式。
- 每个 VitePress 组件页必须保留完整的 API/Props 参数表格，列出参数、类型、是否必填、默认值和作用；不得在重构或精简文档时删除表格。修改公开 API 时同步更新表格并核对源码；表格单元格允许长内容换行，内容不用反引号包裹。
- VitePress 负责渲染 `vitepress/docs/` 中的 Markdown；不得维护第二份组件正文。
- Wasm 只负责交互预览，不能作为 AI 推断 API 的事实来源。新增组件页时应通过 `WasmPreview` 引用对应 Preview ID。
- HyperUI 公开 API 统一声明在 `hyper_ui` 包，示例可使用 `import hyper_ui.*`；`hyper_ui.core` 仅为 UI 库内部公共工具目录，调用方示例不要直接导入。

## 两端 API 一致性

- 对应组件的业务参数、状态枚举、回调名称和默认语义必须保持一致；仅保留框架布局、宿主及原生类型差异。
- 点击统一 onPressed，值变化统一 onChanged，关闭统一 onClose；页签采用 selectedIndex，单选采用 value/groupValue，插槽使用 title/subtitle/leading/trailing/actions/footer/child。
- type 表示形态；按钮预设颜色使用 colorStyle，自定义颜色使用 color，禁止恢复 ghost、outline 或把颜色写回按钮 type。
- API 重构直接删除旧参数、旧类型和旧名称，更新所有调用方；禁止废弃别名、兼容入口和双轨实现。
- 文档仅描述当前 API、默认值和使用方式，不保留迁移对照、旧写法或历史重构说明；同步两端实现、预览、README 和完整参数表。
