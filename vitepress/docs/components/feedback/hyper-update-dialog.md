---
description: 展示版本信息、下载进度和安装阶段；更新任务由调用方执行。
---

# HyperUpdateDialog

展示版本信息、下载进度和安装阶段；更新任务由调用方执行。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="update_dialog" title="HyperUpdateDialog 更新流程" />

预览与 Flutter UpdateDialogComponentExample 使用相同的“更新流程 / 状态预览”分组、十种状态标签、强制更新与总大小未知开关。默认内嵌展示 2.7.3 → 2.8.0、2026.10.10、48.6 MB 和相同的三条更新日志；点击“检查更新”打开模态弹层。状态标签用于静止观察，流程按钮执行页面持有的模拟任务，关闭弹层后下载继续。

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperUpdateDialog(
    status = HyperUpdateStatus.available,
    currentVersion = "2.7.3",
    release = HyperUpdateRelease(version = "2.8.0", highlights = listOf("修复已知问题，提升使用稳定性。")),
    onDownload = onDownload,
    onLater = onClose,
    onClose = onClose
)
```

</details>

## 使用约定

**样式约定**

strong 材质、radius 28dp、最大宽 420dp、内容边距 24dp。模态宿主在系统安全区内保留四边 20dp 外间距；窄屏面板宽度为 min(420dp, 安全区宽度 − 40dp)，350dp 安全区内为 310dp。正文滚动、底部操作区固定，正文与分隔线之间保留 24dp，分隔线与按钮间距 20dp，两个按钮间距 8dp。

各状态标识块统一为 40dp、图标 20dp、环形进度 30dp。标题 24sp / 1.2、说明 14sp / 1.55、更新日志 13sp / 1.5、进度 28sp / 1.2；版本标签 12sp / 1.2，其余文字按 Flutter 主题的 1.4 行高随字号换算，12sp 对应 16.8sp、13sp 对应 18.2sp。主按钮高 38dp，关闭按钮 32dp、图标 18dp。检查中不展示当前版本；版本箭头只在当前版本与新版本同时存在时显示，完成态缺少 release 时仍显示 currentVersion。下载量下方的速度与剩余时间以 12dp 间距排版，可分别换行。

内嵌状态预览与模态弹层共用组件面板和状态映射；内嵌面板跟随示例父级宽度，宿主页面边距 16dp，弹层使用上述 20dp 外间距。

Error 使用简洁布局：保留“软件更新”页头、具体错误说明、重试与关闭；不展示状态图标、大标题、当前版本框或操作区分隔线。错误说明由调用方传入。

主操作显式使用 filled 与 colorStyle = primary 按钮；关闭、取消和稍后再说省略 type 使用默认描边，颜色、轮廓和圆角均采用 `HyperButton` 默认值。

**状态归属**

idle 不显示。检查/下载/安装使用受控状态；installing 或 mandatory 未完成时禁止外部点击与返回关闭。UI 只通知调用方，不检查网络、下载或安装。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperUpdateDialog

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperUpdateDialog(
    status: HyperUpdateStatus = HyperUpdateStatus.idle,
    currentVersion: String? = null,
    release: HyperUpdateRelease? = null,
    progress: Float? = null,
    downloadProgress: HyperUpdateDownloadProgress? = null,
    progressText: String? = null,
    title: String? = null,
    message: String? = null,
    mandatory: Boolean = false,
    primaryLabel: String? = null,
    secondaryLabel: String? = null,
    onCheck: (() -> Unit)? = null,
    onDownload: (() -> Unit)? = null,
    onInstall: (() -> Unit)? = null,
    onRetry: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    onLater: (() -> Unit)? = null,
    onClose: (() -> Unit)? = null,
    maxWidth: Dp = 420.dp,
    modifier: Modifier = Modifier
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| status | HyperUpdateStatus | 否 | HyperUpdateStatus.idle | 由业务层提供的更新展示状态。 |
| currentVersion | String? | 否 | null | 可选的当前版本号。 |
| release | HyperUpdateRelease? | 否 | null | 可选的新版本展示信息。 |
| progress | Float? | 否 | null | 由调用方提供的进度值。 |
| downloadProgress | HyperUpdateDownloadProgress? | 否 | null | 业务层提供的字节下载快照。 |
| progressText | String? | 否 | null | 自定义进度说明。 |
| title | String? | 否 | null | 组件标题文字或内容。 |
| message | String? | 否 | null | 提示消息文本。 |
| mandatory | Boolean | 否 | false | 必要更新；未完成时限制关闭。 |
| primaryLabel | String? | 否 | null | 覆盖当前状态的主操作文案。 |
| secondaryLabel | String? | 否 | null | 覆盖当前状态的次操作文案。 |
| onCheck | (() -&gt; Unit)? | 否 | null | 通知业务层检查更新。 |
| onDownload | (() -&gt; Unit)? | 否 | null | 用户触发下载时的回调。 |
| onInstall | (() -&gt; Unit)? | 否 | null | 通知业务层启动安装。 |
| onRetry | (() -&gt; Unit)? | 否 | null | 用户触发重试时的回调。 |
| onCancel | (() -&gt; Unit)? | 否 | null | 通知业务层取消检查或下载。 |
| onLater | (() -&gt; Unit)? | 否 | null | 通知业务层稍后处理。 |
| onClose | (() -&gt; Unit)? | 否 | null | 请求关闭时通知调用方更新可见状态。 |
| maxWidth | Dp | 否 | 420.dp | 面板最大宽度；模态弹层还受安全区宽度减去两侧 20dp 的约束。 |
| modifier | Modifier | 否 | Modifier | 组件外部尺寸、位置和间距修饰符。 |

### HyperUpdateRelease

```kotlin
data class HyperUpdateRelease(
    val version: String,
    val releaseDate: String? = null,
    val sizeLabel: String? = null,
    val highlights: List<String> = emptyList()
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| version | String | 是 | — | 非空版本号。 |
| releaseDate | String? | 否 | null | 版本发布日期文案。 |
| sizeLabel | String? | 否 | null | 版本包大小文案。 |
| highlights | List&lt;String&gt; | 否 | emptyList() | 逐条展示的更新摘要。 |

### HyperUpdateDownloadProgress

```kotlin
data class HyperUpdateDownloadProgress(
    val downloadedBytes: Long,
    val totalBytes: Long? = null,
    val bytesPerSecond: Double? = null
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| downloadedBytes | Long | 是 | — | 已下载字节数；负数按 0 展示。 |
| totalBytes | Long? | 否 | null | 总字节数；null、0 或负值表示未知。 |
| bytesPerSecond | Double? | 否 | null | 每秒下载字节数；非正数或非有限值表示未知。 |

| 只读字段 | 类型 | 作用 |
| --- | --- | --- |
| fraction | Float? | 0..1 下载比例；总量未知时为 null。 |
| sizeLabel | String | 已下载量和可用的总量，使用十进制字节单位。 |
| speedLabel | String? | 可用的速度文案。 |
| remainingLabel | String? | 总量与速度有效时的预计剩余时间。 |

### HyperUpdateStatus

| 状态 | 展示 | 操作 |
| --- | --- | --- |
| idle | 隐藏弹窗 | 外部入口开始检查 |
| checking | 检查中，隐藏版本信息 | onCancel / onClose |
| available | 新版本信息与更新摘要 | onDownload / onLater |
| upToDate | 已是最新版本 | onCheck / onClose |
| downloadQueued | 系统下载排队、不定进度 | onCancel / onClose |
| downloading | 下载比例、字节数、速度及剩余时间 | onCancel / onClose |
| readyToInstall | 更新已准备就绪 | onInstall / onLater |
| installing | 安装进度，禁止关闭 | 业务完成后切换状态 |
| completed | 更新完成 | onClose |
| error | 仅错误原因、重试与关闭 | onRetry / onClose |

回调为 null 时不显示对应次操作，主操作不可点击。关闭视图与取消任务由不同回调负责，组件不会替业务层取消下载。

downloadProgress 在 downloadQueued / downloading 阶段优先于 progress；安装阶段只使用 progress。progressText 覆盖进度说明，总大小未知时不显示百分比或预计剩余时间。

业务层提供 status、release、downloadProgress 和 progress，通过 title、message、primaryLabel 与 secondaryLabel 设置文案；检查、下载、安装和取消通过对应回调处理。
