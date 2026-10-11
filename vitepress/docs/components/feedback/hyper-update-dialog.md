---
description: 展示版本信息、下载进度和安装阶段；更新任务由调用方执行。
---

# HyperUpdateDialog

展示版本信息、下载进度和安装阶段；更新任务由调用方执行。

<p class="component-doc__meta">包名 hyper_ui · Android 手机竖屏 320–430dp</p>

## 交互示例

<WasmPreview demo="update_dialog" title="HyperUpdateDialog 交互预览" />

## 使用方式

<details class="component-doc__details">
<summary>最小调用</summary>

```kotlin
import hyper_ui.*

HyperUpdateDialog(state, onDismissRequest = onClose, onRetry = onRetry, onDownload = onDownload, onInstall = onInstall)
```

</details>

## 使用约定

**样式约定**

strong 材质、radius 28dp、最大宽 420dp、padding 24dp。标识 64dp、标题 24sp、进度 28sp；版本徽章和本次更新摘要遵循参考布局，操作区固定。

**状态归属**

Idle 不显示。检查/下载/安装使用受控状态；Installing 或 mandatory 未完成时禁止外部点击与返回关闭。UI 只通知调用方，不检查网络、下载或安装。

Desktop/Wasm 仅用于文档 Preview。显式覆盖尺寸、颜色或排版后，画面会随调用方配置变化。

## 公开 API

### HyperUpdateDialog

<details class="component-doc__details">
<summary>查看公开签名</summary>

```kotlin
@Composable
fun HyperUpdateDialog(
    state: HyperUpdateDialogState,
    onDismissRequest: () -> Unit,
    onRetry: () -> Unit,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier,
    texts: HyperUpdateDialogTexts = HyperUpdateDialogTexts(),
    dismissOnClickOutside: Boolean = true,
    mandatory: Boolean = false,
    onInstall: () -> Unit = {},
    onCheck: () -> Unit = onRetry,
    onCancel: () -> Unit = onDismissRequest
)
```

</details>

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| state | HyperUpdateDialogState | 是 | — | 调用方持有的更新状态。 |
| onDismissRequest | () -&gt; Unit | 是 | — | 请求关闭，由调用方修改状态。 |
| onRetry | () -&gt; Unit | 是 | — | 通知业务重试。 |
| onDownload | () -&gt; Unit | 是 | — | 通知业务执行下载；UI 不执行下载。 |
| modifier | Modifier | 否 | Modifier | 外部尺寸、位置与间距。 |
| texts | HyperUpdateDialogTexts | 否 | HyperUpdateDialogTexts() | 可替换的更新文案。 |
| dismissOnClickOutside | Boolean | 否 | true | 点击外部时是否请求关闭。 |
| mandatory | Boolean | 否 | false | 必要更新；未完成时禁止关闭。 |
| onInstall | () -&gt; Unit | 否 | {} | 通知业务执行安装；UI 不执行安装。 |
| onCheck | () -&gt; Unit | 否 | onRetry | 通知业务检查更新。 |
| onCancel | () -&gt; Unit | 否 | onDismissRequest | 通知业务取消。 |

<details class="component-doc__details component-doc__advanced">
<summary>默认配置与数据类型</summary>

**HyperAppRelease 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| versionName | String | 是 | — | 版本号，版本区域与更新状态显示使用。 |
| displayName | String | 是 | — | Release 的应用显示名，由业务层提供。 |
| releaseNotes | String | 是 | — | 更新说明；无 highlights 时作为摘要来源。 |
| packageFileName | String | 是 | — | 更新包文件名，供调用方下载流程使用。 |
| packageDownloadUrl | String | 是 | — | 更新包地址，UI 不读取或下载。 |
| isPrerelease | Boolean | 否 | false | 预发布标记，业务层决定是否接受。 |
| highlights | List&lt;String&gt; | 否 | emptyList() | 逐条展示的更新摘要。 |
| releaseDate | String? | 否 | null | 版本区域的可选日期文案。 |
| sizeLabel | String? | 否 | null | 版本区域的可选包大小文案。 |

**HyperUpdateRequest 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| currentVersionName | String | 是 | — | 调用方提供的当前版本号。 |
| releaseUrl | String | 是 | — | 版本检查地址，由调用方的 ReleaseLoader 使用。 |

**HyperUpdateDialogTexts 配置**

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| checkTitle | String | 否 | "正在检查更新" | 检查中标题。 |
| updateAvailableTitle | String | 否 | "发现新版本" | 发现更新标题。 |
| downloadQueuedTitle | String | 否 | "已开始下载" | 系统排队状态标题。 |
| errorTitle | String | 否 | "更新遇到了一点问题" | 错误状态标题。 |
| checkingMessage | String | 否 | "正在获取版本信息，请稍候。" | 检查中说明。 |
| upToDateMessage | String | 否 | "所有新功能与体验优化，都已为你准备好。" | 已是最新的说明。 |
| downloadQueuedMessage | String | 否 | "更新包已交给系统下载管理器。" | 系统排队说明。 |
| downloadNotificationHint | String | 否 | "下载进度和完成结果可在系统通知或“下载”中查看。" | 系统排队时的进度详情文案。 |
| cancelAction | String | 否 | "取消" | 取消按钮文字。 |
| downloadAction | String | 否 | "立即更新" | 立即更新按钮文字。 |
| closeAction | String | 否 | "关闭" | 关闭按钮文字。 |
| retryAction | String | 否 | "重试" | 重试按钮文字。 |

**状态类型**

| 状态 | 必需数据 | 作用 |
| --- | --- | --- |
| Idle | 无 | 隐藏弹窗 |
| Checking | currentVersionName | 检查中的不确定加载 |
| UpToDate | currentVersionName | 已是最新，可再次检查 |
| UpdateAvailable | currentVersionName, release | 更新摘要与立即更新 |
| DownloadQueued | currentVersionName, release, downloadId | 系统下载排队，不确定进度 |
| Downloading | currentVersionName, release；progress=null，progressText=null | 下载阶段，进度 0..1 |
| ReadyToInstall | currentVersionName, release | 等待安装 |
| Installing | currentVersionName, release；progress=null，progressText=null | 安装阶段，锁定关闭 |
| Completed | currentVersionName；release=null | 完成后允许关闭 |
| Error | currentVersionName, message | 错误说明与重试 |

</details>
