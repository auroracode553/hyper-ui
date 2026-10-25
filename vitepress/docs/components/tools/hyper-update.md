---
description: 从发布配置读取公开静态索引，统一检查、系统下载、进度恢复与安装。
---

# 检查更新

包名 `hyper_ui`，源码目录 `tools/update/`。本工具只支持 Android；Desktop/Wasm Preview 仅模拟流程，不执行网络、下载或安装。

<WasmPreview demo="update_tool" title="检查更新工具交互模拟" />

## 接入

在项目 `publish-config.json` 中复用发布仓库和真实版本，并新增 `update`：

```json
{
  "repository": "owner/application-release",
  "release": { "versionName": "1.5.0", "versionCode": 6 },
  "update": {
    "branch": "main",
    "indexFile": "latest.json"
  },
  "changelog": { "path": "changelog.md" }
}
```

这是公开更新字段示意；发布器需要的 provider、authentication、artifact 等配置仍按发布器完整格式保留。客户端不读取或打包 authentication。

应用默认不填写自动检查策略，统一使用工具内的「1 小时窗口最多 2 次」。窗口从首次自动请求起计，满 1 小时后在下次正常触发时恢复额度。仅有特殊需求时，在 update 中增加可选的 autoCheckWindowMinutes（1 到 525600 的整数分钟）、autoCheckLimit（正整数）覆盖；Kotlin 调用也可通过 HyperUpdateConfig 的对应参数覆盖。构建约定和发布器只透传显式配置，不维护另一份默认值。

在应用模块的 `build.gradle.kts` 末尾应用库提供的配置约定。它只解析配置与注入 BuildConfig，不注册运行、构建或发布任务：

```kotlin
apply(from = File(
    providers.gradleProperty("hyperUiDir")
        .getOrElse(rootProject.file("../hyper-ui").path),
    "gradle/hyper-update.gradle"
))
```

配置不在 Gradle 根目录时，先设置 `extra["hyperUpdateConfigFile"] = rootProject.file("../publish-config.json")`，并按目录实际位置调整库根路径。约定统一设置 Android versionName/versionCode、开启 BuildConfig，并注入仅含公开字段与当前 changelog 的 `HYPER_UPDATE_CONFIG`。不再在项目内重复编写配置解析或注入更新 Token。

在 Activity 的稳定根页面、HyperThemeConfig 内创建一次工具并放置宿主：

```kotlin
val updates = rememberHyperUpdateController(BuildConfig.HYPER_UPDATE_CONFIG)
HyperListTile(
    onPressed = { updates.check() },
    title = { HyperText("检查更新") },
    subtitle = { HyperText(updates.summary) }
)
HyperUpdateHost(updates)
```

更新窗口统一使用 [HyperUpdateDialog](../feedback/hyper-update-dialog.md)，不改变组件默认布局或视觉。普通组件仍保持受控；工具持有更新任务，项目只提供公开配置和入口。

## 静态索引与发布

正式发布统一依次手动执行 `ap bump patch`、`ap build`、`ap publish`。使用更新后的 android-publish 发布器；公开发布仓库的 APK 上传成功后才提交索引，发布命令不隐式构建。

工具读取 `https://gitee.com/{repository}/raw/{branch}/{indexFile}`，只接受最新完整 APK 信息，不查询 Release 列表、附件列表或按标签 OpenAPI，不带 Token，不逐级查找中间版本：

```json
{
  "versionName": "1.5.0",
  "versionCode": 6,
  "notes": "本版本更新内容",
  "downloadUrl": "https://gitee.com/owner/application-release/releases/download/1.5.0/app-release.apk",
  "size": 25000000
}
```

| 字段 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| versionName | String | 是 | — | 语义化展示版本 |
| versionCode | Int | 是 | — | 与真实 APK 一致的正整数，大于已安装值才提供更新 |
| notes | String | 否 | 空字符串 | 完整更新说明 |
| downloadUrl | String | 是 | — | 当前仓库 releases/download 下的 HTTPS APK 普通直链 |
| size | Long | 否 | 未知 | APK 精确字节数；缺失时使用系统下载器的总量 |

项目发布器先上传并确认 APK 附件成功，最后提交索引；索引必须持续指向最高已发布的完整版本，禁止旧 versionCode 覆盖新索引。`D:/my_project/sdk-project/android-publish` 已负责这一流程。AAB 和预发行版不替换正式 APK 索引。发布器调用管理接口创建 Release 和提交仓库文件，客户端不使用这些接口。

设备在 1.0.0、索引指向 1.5.0 时直接提供 1.5.0。已有正在下载的任务复用；旧包已经下载但未安装时，重新检查仍读取最新索引。HTTP 缓存关闭并请求重新验证；托管平台的 CDN 刷新仍受平台管理。404、无效 JSON 或协议错误明确显示失败，不当作已是最新。

## 时机、状态与生命周期

| 场景 | 行为 |
| --- | --- |
| 页面首次呈现 | 约 5 秒后判断自动额度，默认本小时窗口未满 2 次才发请求 |
| 应用返回前台 | 稳定约 2 秒后判断自动额度，用满则不发请求 |
| 手动 check | 不受自动额度限制，也不占自动次数；已有任务则复用并打开窗口 |
| 进入后台 | 移除待检查回调、停止进度观察，系统下载继续 |
| 关闭窗口 | 只隐藏视图，保留任务与下载记录 |
| 取消下载 | 删除对应系统下载任务，恢复可下载状态 |
| 工具关闭 | 释放观察、HTTP 与线程，系统任务和持久记录保留 |
| 本应用升级完成 | 核对实际已安装版本码，删除已安装版本对应的系统下载任务和 APK |

仅这三个检查入口，不增加前台周期检查、联网恢复检查、后台作业或额外前台服务。自动请求发起前占用一次额度，失败同样计次且不立即重试；用满 2 次后跳过后续自动请求，直到原窗口满 1 小时。窗口和次数持久化，关闭应用或切换前后台不会重置。自动失败不额外弹窗，更新入口保留失败状态和手动重试；已有下载继续复用。手动检查不占自动额度。同版本选择稍后后 24 小时内不自动重复提醒，手动入口仍可用。

下载进度来自 DownloadManager：前台约每 700 毫秒观察字节量，提供速度及可计算的剩余时间；未知总量显示不定进度。重建页面或进程后通过真实下载 ID 恢复。每个工具持有一个串行工作线程，空闲 10 秒回收，队列容量 8；检查、启动下载和观察不会重复并发。

点击立即安装时通过系统安装器确认；未允许该应用安装未知来源时打开系统授权页，返回后再次点击安装。不替用户确认安装，也不把安装器已打开当作升级完成。库 Manifest 声明 INTERNET 与 REQUEST_INSTALL_PACKAGES，业务项目不再重复封装安装流程。

## 安装完成后的 APK 清理

库 Manifest 自动合并非导出的 HyperUpdateInstallReceiver，只接收系统的 [MY_PACKAGE_REPLACED](https://developer.android.com/reference/android/content/Intent#ACTION_MY_PACKAGE_REPLACED) 自身更新完成通知。它在有明确结束时间的工作线程上执行清理，不创建常驻服务、不发起网络检查。工具下次初始化也调用同一清理入口，补做系统通知未及时执行的清理。

清理前使用 PackageManager 读取本应用真实已安装 versionCode，只有记录中的目标版本码不高于实际已安装值时，才按保存的下载 ID 调用 [DownloadManager.remove](https://developer.android.com/reference/android/app/DownloadManager#remove(long...))，删除对应任务及 APK。安装器刚打开、用户取消、安装失败、只授权未知来源时，版本未升级，不会删除等待安装的包。

记录存储在本应用私有偏好中，不包含发布凭据。已下载旧包被较新索引替代时，保存其下载 ID 和目标版本码；以后安装到该版本或更高版本时一并清理。删除失败保留记录，后续启动重试；重复通知和文件已被用户移除都允许重复处理。不会扫描公共 Downloads 目录，也不会删除用户手动下载或其他应用的文件。此前已经丢失下载 ID 的历史文件不做目录扫描清理。

入口和恢复逻辑位于 tools/update/android/HyperUpdateController.kt，记录与清理位于 HyperUpdateDownloadRecords.kt，系统通知入口位于 HyperUpdateInstallReceiver.kt；这些 Android 实现不进入 Wasm/Desktop，Preview 仅模拟安装完成后的文件状态。

## 公开 API

```kotlin
data class HyperUpdateConfig(
    val repository: String,
    val branch: String,
    val indexFile: String,
    val autoCheckWindowMs: Long = HyperUpdateCheckHistory.DEFAULT_WINDOW_MS,
    val autoCheckLimit: Int = HyperUpdateCheckHistory.DEFAULT_MAX_CHECKS,
    val currentNotes: String = ""
) {
    val indexUrl: String
    companion object {
        fun fromJson(json: String): HyperUpdateConfig
    }
}

@Composable fun rememberHyperUpdateController(configurationJson: String): HyperUpdateController
@Composable fun rememberHyperUpdateController(configuration: HyperUpdateConfig): HyperUpdateController
@Composable fun HyperUpdateHost(controller: HyperUpdateController, visible: Boolean = true)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| repository | String | 是 | — | owner/repository；主通道复用发布仓库 |
| branch | String | 是 | — | 仓库索引分支 |
| indexFile | String | 是 | — | 仓库内 JSON 路径 |
| autoCheckWindowMs | Long | 否 | 3600000 | 自动检查计数窗口，至少 60000 毫秒 |
| autoCheckLimit | Int | 否 | 2 | 每个窗口最多自动请求次数，失败也计次 |
| update.autoCheckWindowMinutes | Int | 否 | 省略时使用库默认 60 分钟 | JSON 中的可选窗口覆盖，默认应用不传 |
| update.autoCheckLimit | Int | 否 | 省略时使用库默认 2 | JSON 中的可选次数覆盖，默认应用不传 |
| currentNotes | String | 否 | 空字符串 | 当前安装版本随包提供的更新说明 |
| configurationJson | String | 是 | — | 配置约定注入的公开 JSON |
| configuration | HyperUpdateConfig | 是 | — | 自行构造的公开配置，与 JSON 重载二选一 |
| controller | HyperUpdateController | 是 | — | 页面持有的唯一工具实例 |
| visible | Boolean | 否 | true | 只控制更新窗口能否显示，不改变任务或宿主生命周期 |
| json | String | 是 | — | 公开 JSON，不传完整发布配置或凭据 |

`HyperUpdateController(context: Context, configuration: HyperUpdateConfig)` 也可由非 Compose 页面持有，需自行调用生命周期并在结束时 close。remember 入口自动在 Composition 销毁时关闭；HyperUpdateHost 自动接入当前 Activity 的前后台及首次页面就绪。同一实例只挂载一个宿主。

| 属性或方法 | 类型/参数 | 默认值 | 作用 |
| --- | --- | --- | --- |
| configuration | HyperUpdateConfig | 构造参数 | 公开配置与当前说明 |
| currentVersionName | String | 已安装包真实值 | 当前版本名称 |
| currentVersionCode | Long | 已安装包真实值 | 当前版本代码 |
| state | HyperUpdateState | idle | Compose 可观察的只读任务快照 |
| summary | String | 根据状态派生 | 设置入口显示文字 |
| dialogVisible | Boolean | false | 窗口请求状态 |
| canCancel | Boolean | 根据任务派生 | 是否能取消当前下载 |
| check | manual: Boolean | true | 手动或到期检查 |
| download | 无 | — | 下载当前可用 APK，重复调用复用 |
| install | 无 | — | 交给系统确认安装或申请安装来源许可 |
| cancelDownload | 无 | — | 取消真实系统任务 |
| dismiss | 无 | — | 关闭窗口并记录稍后提醒时间 |
| retry | 无 | — | 重试实际失败阶段 |
| pageReady | 无 | — | 登记首次呈现；重复登记不叠加检查 |
| foreground | visible: Boolean | — | 同步真实前后台，重复状态不叠加任务 |
| close | 无 | — | 释放工具拥有的观察资源 |

```kotlin
data class HyperUpdateState(
    val status: HyperUpdateStatus = HyperUpdateStatus.idle,
    val version: String? = null,
    val notes: String = "",
    val downloadedBytes: Long = 0,
    val totalBytes: Long? = null,
    val bytesPerSecond: Double? = null,
    val message: String? = null
) { val summary: String }
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| status | HyperUpdateStatus | 否 | idle | 与更新展示组件共用的状态 |
| version | String? | 否 | null | 本次候选版本 |
| notes | String | 否 | 空字符串 | 完整更新说明 |
| downloadedBytes | Long | 否 | 0 | 已下载字节 |
| totalBytes | Long? | 否 | null | 总量，未知为 null |
| bytesPerSecond | Double? | 否 | null | 最近采样速度 |
| message | String? | 否 | null | 当前阶段或原始失败原因 |

## 与业务资源的边界

本模块的公开更新配置只描述 APK，没有 channels 或资源通道配置。Web 资源、词库与模型的资源地址、索引协议、检查调度、签名、解压、兼容性检查和活动版本切换全部属于各项目业务，不交给 APK 更新工具。

下面的基础传输、版本排序与计时工具不定义业务资源结构，项目可按需独立复用。输入法资源配置由该项目的 config/resource-update.json 管理，FastBuy 网页资源配置由该项目的 config/web-resource-update.json 管理；这些配置不进入 HYPER_UPDATE_CONFIG，库也不解析或导出它们。

```kotlin
class HyperUpdateHttpClient(allowLocalHttp: Boolean = false) : AutoCloseable {
    fun text(url: String): String
    fun bytes(url: String, maxBytes: Int = 2097152): ByteArray
    fun download(url: String, target: File, limit: Long,
        onChunk: ((ByteArray, Int) -> Unit)? = null,
        onProgress: (Long, Double?) -> Unit)
    override fun close()
}
class HyperUpdateCheckHistory(context: Context, key: String,
    windowMs: Long = DEFAULT_WINDOW_MS, maxChecks: Int = DEFAULT_MAX_CHECKS) {
    companion object {
        const val DEFAULT_WINDOW_MS: Long = 3600000
        const val DEFAULT_MAX_CHECKS: Int = 2
    }
    fun tryStartAutomaticCheck(): Boolean
}
class HyperUpdateCheckSchedule(main: Handler, check: () -> Unit) : AutoCloseable {
    fun pageReady()
    fun foreground(visible: Boolean)
    override fun close()
}
data class HyperUpdateVersion(val value: String) : Comparable<HyperUpdateVersion>
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| allowLocalHttp | Boolean | 否 | false | 仅调试允许 localhost、127.0.0.1、10.0.2.2 的 HTTP，正式下载为 HTTPS |
| url | String | 是 | — | 公开索引或资源地址 |
| maxBytes | Int | 否 | 2097152 | 原始索引字节上限，最大 8 MiB；签名目录保留原字节 |
| target | File | 是 | — | 由调用方选定的临时文件 |
| limit | Long | 是 | — | 下载声明大小上限 |
| onChunk | ((ByteArray, Int) -> Unit)? | 否 | null | 同步校验或取消检查；缓冲区不可保存后异步读取 |
| onProgress | (Long, Double?) -> Unit | 是 | — | 已下载量及可计算速度，最多约每 500 毫秒回调，结束补发 |
| context | Context | 是 | — | 工具只保留应用上下文或应用偏好 |
| key | String | 是 | — | 明确通道名，记录彼此隔离 |
| windowMs | Long | 否 | DEFAULT_WINDOW_MS（3600000） | 正数计数窗口，从首次自动请求起计 |
| maxChecks | Int | 否 | DEFAULT_MAX_CHECKS（2） | 每个窗口的自动请求上限，必须为正数 |
| main | Handler | 是 | — | 主线程调度器 |
| check | () -> Unit | 是 | — | 到期判断及实际检查入口，不在调度器中自动轮询 |
| visible | Boolean | 是 | — | 真实前后台状态 |
| value | String | 是 | — | 三段式语义化版本，可带 v、预发行与构建标记 |

HTTP 工具单实例供单个串行通道使用，索引最多读取有限字节，下载不超过声明上限。close 会中断所属 HTTP，不影响其他实例、页面或市场流。资源签名、文件安全与安装事务由资源业务继续执行。

DEFAULT_WINDOW_MS 与 DEFAULT_MAX_CHECKS 是默认策略的唯一常量，应用无需传入。自动请求即将发起时调用 tryStartAutomaticCheck：有额度时计次并返回 true，用满时返回 false；它不发网络请求。手动检查不调用此方法。
