# HyperUI 使用文档

> 本目录是 HyperUI 面向开发者、AI 与代码生成工具的权威使用文档。Markdown 正文是事实来源；VitePress 只负责把这些文件渲染为网页，Wasm 只负责提供可交互预览。
>
> **GitHub 仓库**: [auroracode553/hyper-ui-doc](https://github.com/auroracode553/hyper-ui-doc) · **在线文档**: [auroracode553.github.io/hyper-ui-doc/](https://auroracode553.github.io/hyper-ui-doc/)

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

- 公开 API 统一从 `hyper_ui` 包导入；不要导入 `hyper_ui.core.*`。
- 只使用具体组件页“公开签名”中存在的参数，不根据其他 Compose 库猜测参数名。
- `value`、`checked`、`selected`、`show`、`open`、`expanded`、进度和导航选择等业务状态均由调用方持有。
- 不把网络请求、数据库访问、权限申请、路由实现或 ViewModel 写入 HyperUI 组件。
- 弹窗、菜单、抽屉不渲染遮罩或半透明蒙层。
- 文档示例中的 Compose、Material Icons 和状态 API 仍需按 AndroidX 标准包导入。
- `preview/` 与 Wasm 预览是文档演示工程，不是调用方依赖。

## 文档与预览的职责

| 层级 | 面向对象 | 职责 | 是否为 API 事实来源 |
| --- | --- | --- | --- |
| `vitepress/docs/` Markdown | AI、开发者 | 参数、默认值、状态、约束、示例 | 是 |
| VitePress | 浏览器访问者 | 将 Markdown 渲染为可检索的语义化 HTML | 否 |
| Desktop preview | 维护者 | 本地查看真实组件与交互 | 否 |
| Wasm preview | 浏览器访问者 | 在文档页中操作跨平台预览 | 否 |
| `hyper_ui` 源码 | 维护者 | 最终实现 | 文档冲突时以当前源码为准并修正文档 |

## 平台与版本

- 使用平台：Android
- UI 技术：Jetpack Compose / Material 3
- Maven 坐标：`com.hyperui:hyper-ui:1.0.0`
- `minSdk`：30
- `compileSdk`：36
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

部署 VitePress 后，AI 也可以从站点根路径的 `llms.txt` 发现主要文档页；该索引只负责导航，具体 API 仍以本目录 Markdown 为准。
