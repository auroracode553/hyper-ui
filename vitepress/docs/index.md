# HyperUI 使用文档

> 本目录是 HyperUI 面向开发者、AI 与代码生成工具的权威使用文档。Markdown 正文是事实来源；VitePress 只负责把这些文件渲染为网页，Wasm 只负责提供可交互预览。
>
> **GitHub 仓库**: [auroracode553/hyper-ui](https://github.com/auroracode553/hyper-ui) · **在线文档**: [auroracode553.github.io/hyper-ui](https://auroracode553.github.io/hyper-ui)

## 一句话说明

HyperUI 是 Android Jetpack Compose UI 组件库。它负责组件样式与交互回调，不负责网络、数据库、权限、路由、ViewModel 或其他业务规则。

## AI 阅读顺序

1. [接入与最小配置](getting-started.md)
2. [主题与颜色](theme.md)
3. [状态与架构边界](state-model.md)
4. [组件索引](component-index.md)
5. 当前任务涉及的具体组件页
6. 需要组合多个组件时，再查看 [常见页面组合](patterns/index.md)

AI 生成代码时必须遵守：

- 生成或更新依赖声明前，从 [JitPack](https://jitpack.io/#auroracode553/hyper-ui) 或 [GitHub Tags](https://github.com/auroracode553/hyper-ui/tags) 读取最新可用 tag；文档不提供固定版本号。
- 公开 API 统一从 `hyper_ui` 包导入；不要导入 `hyper_ui.core.*`。
- 只使用具体组件页“公开签名”中存在的参数，不根据其他 Compose 库猜测参数名。
- 宽、高、最小尺寸和外部间距优先使用组件的首个 `modifier`；不要猜测 `size`、`width`、`height`、`minHeight`、`contentPadding` 等重复具名参数。作用于内部独立节点的修饰符以具体签名为准，例如 `contentModifier`、`HyperDrawer.drawerModifier`、`HyperTextField.inputModifier`。
- `value`、`checked`、`selected`、`show`、`open`、`expanded`、进度和导航选择等业务状态均由调用方持有。
- 单一连续卡片的页面级数据列表使用 `HyperList`；按日期或类别形成多个独立卡片分组的动态列表使用 `HyperSectionedList`；`HyperMenuList` 只能用于少量菜单、设置项和操作入口，不要用于历史、文件、媒体、日志或搜索结果列表。
- 页面级空数据或筛选无结果使用 `HyperEmptyState`；图标和可选操作通过 Slot 注入，加载中与错误态仍由页面分别处理。
- 不把网络请求、数据库访问、权限申请、路由实现或 ViewModel 写入 HyperUI 组件。
- `hyperToast` 是 Android-only 工具；传入 `Context` 与文本或字符串资源 ID，内部只负责主线程调度和原生 Toast 显示。
- 模态 `HyperDialog`、`HyperAlertDialog` 和 `HyperUpdateDialog` 保留平台标准背景调暗；Popup、菜单和抽屉只渲染自身面板。需要描边与阴影的玻璃组件统一复用内部公共深度层，也可只复用其中的公共阴影能力。
- `HyperIconButton` 只使用紧凑级阴影，`HyperDropdown` 使用内容自适应的浮层级深度，`HyperDrawer` 使用结构级深度；透明 `HyperNavBar` 不绘制描边和阴影。这些视觉参数不作为公开 border 或 outline API 暴露。
- `HyperTextField` 使用同一玻璃语言的结构性变体：投影低于按钮，普通态无硬边框，聚焦和错误状态只使用一条渐变语义边缘。
- `HyperPlaybackSpeedPanel`、`HyperPlaybackSpeedPanelOverlay` 与 `HyperPlaybackSpeedScale` 默认固定使用深色播放器视觉，不随外层 `MaterialTheme` 的明暗模式变化；强调色仍读取 `HyperThemeConfig`。
- HyperUI 不强制绑定图标库；Android 项目需要图标时，默认优先推荐 `com.composables:icons-lucide-android:2.2.1`，通过 `painterResource` 使用其 VectorDrawable 资源。
- 除非调用方已有明确依赖，否则不要为少量图标引入 `material-icons-extended`；Release 构建应开启代码与资源裁剪。
- 文档示例中的 Compose、图标和状态 API 仍需从各自标准包导入。
- `preview/` 与 Wasm 预览是文档演示工程，不是调用方依赖。

## 文档与预览的职责

| 层级 | 面向对象 | 职责 | 是否为 API 事实来源 |
| --- | --- | --- | --- |
| `vitepress/docs/` Markdown | AI、开发者 | 参数、默认值、状态、约束、示例 | 是 |
| VitePress | 浏览器访问者 | 将 Markdown 渲染为可检索的语义化 HTML | 否 |
| Desktop preview | 维护者 | 本地查看真实组件与交互 | 否 |
| Wasm preview | 浏览器访问者 | 在文档页中操作跨平台预览 | 否 |
| `hyper_ui` 源码 | 维护者 | 最终实现 | 文档冲突时以当前源码为准并修正文档 |

## 平台信息

- 使用平台：Android
- UI 技术：Jetpack Compose / Material 3
- JitPack 坐标格式：`com.github.auroracode553:hyper-ui:<tag>`
- `minSdk`：30
- `compileSdk`：37
- 公开包：`hyper_ui`

## 组件分组

- [基础组件](component-index.md#基础组件)
- [表单组件](component-index.md#表单组件)
- [容器组件](component-index.md#容器组件)
- [导航组件](component-index.md#导航组件)
- [列表组件](component-index.md#列表组件)
- [反馈组件](component-index.md#反馈组件)

## 维护入口

新增、删除、重命名或修改公开 API 时，按照 [文档维护规则](maintenance.md) 同步源码、Markdown、Desktop/Wasm preview 与 VitePress 导航。需要让文档 iframe 显示最新组件时，按 [组件更新后刷新预览](preview-update-workflow.md) 手动发布 Wasm 静态产物。

部署 VitePress 后，AI 可以使用以下机器入口：

- [`llms.txt`](https://auroracode553.github.io/hyper-ui/llms.txt)：完整 Markdown 文档索引。
- [`llms-full.txt`](https://auroracode553.github.io/hyper-ui/llms-full.txt)：单次响应包含全部权威文档，适合不继续跟链的抓取器。
- [`index.md`](https://auroracode553.github.io/hyper-ui/index.md)：本页的纯 Markdown 版本；其他页面也提供同路径 `.md` 版本。
- [`sitemap.xml`](https://auroracode553.github.io/hyper-ui/sitemap.xml)：网页发现入口。

这些纯文本文件由构建过程直接从本目录派生，不维护第二份组件正文。具体 API 仍以本目录 Markdown 为准。
