/** 文件职责：编排通用 Release 加载与版本判断；网络实现由调用方注入。 */
package hyper_ui

class HyperUpdateChecker(
    private val releaseLoader: HyperReleaseLoader
) {
    suspend fun check(request: HyperUpdateRequest): HyperUpdateCheckResult {
        require(request.currentVersionName.isNotBlank()) { "currentVersionName 不能为空" }
        require(request.releaseUrl.isNotBlank()) { "releaseUrl 不能为空" }

        val release = releaseLoader.load(request.releaseUrl)
        require(release.versionName.isNotBlank()) { "Release versionName 不能为空" }

        return if (
            HyperVersionNameComparator.isNewer(
                candidate = release.versionName,
                current = request.currentVersionName
            )
        ) {
            HyperUpdateCheckResult.UpdateAvailable(
                currentVersionName = request.currentVersionName,
                release = release
            )
        } else {
            HyperUpdateCheckResult.UpToDate(request.currentVersionName)
        }
    }
}

fun HyperUpdateCheckResult.toDialogState(): HyperUpdateDialogState {
    return when (this) {
        is HyperUpdateCheckResult.UpToDate -> {
            HyperUpdateDialogState.UpToDate(currentVersionName)
        }
        is HyperUpdateCheckResult.UpdateAvailable -> {
            HyperUpdateDialogState.UpdateAvailable(
                currentVersionName = currentVersionName,
                release = release
            )
        }
    }
}
