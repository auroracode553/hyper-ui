/** 文件职责：统一展示应用更新检查结果、下载确认和下载入队反馈。 */
package hyper_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HyperUpdateDialog(
    state: HyperUpdateDialogState,
    onDismissRequest: () -> Unit,
    onRetry: () -> Unit,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier,
    texts: HyperUpdateDialogTexts = HyperUpdateDialogTexts(),
    dismissOnClickOutside: Boolean = true
) {
    if (state is HyperUpdateDialogState.Idle) return

    HyperAlertDialog(
        visible = true,
        onDismissRequest = onDismissRequest,
        title = dialogTitle(state, texts),
        modifier = modifier,
        dismissOnClickOutside = dismissOnClickOutside,
        bodyContent = {
            when (state) {
                is HyperUpdateDialogState.Checking -> CheckingContent(state, texts)
                is HyperUpdateDialogState.UpToDate -> UpToDateContent(state, texts)
                is HyperUpdateDialogState.UpdateAvailable -> UpdateAvailableContent(state, texts)
                is HyperUpdateDialogState.DownloadQueued -> DownloadQueuedContent(state, texts)
                is HyperUpdateDialogState.Error -> ErrorContent(state)
                HyperUpdateDialogState.Idle -> Unit
            }
        },
        actionContent = {
            when (state) {
                is HyperUpdateDialogState.UpdateAvailable -> {
                    HyperButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f),
                        tone = HyperButtonTone.Secondary
                    ) {
                        HyperText(texts.cancelAction)
                    }
                    HyperButton(
                        onClick = onDownload,
                        modifier = Modifier.weight(1f)
                    ) {
                        HyperText(texts.downloadAction)
                    }
                }
                is HyperUpdateDialogState.Error -> {
                    HyperButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.weight(1f),
                        tone = HyperButtonTone.Secondary
                    ) {
                        HyperText(texts.closeAction)
                    }
                    HyperButton(
                        onClick = onRetry,
                        modifier = Modifier.weight(1f)
                    ) {
                        HyperText(texts.retryAction)
                    }
                }
                else -> {
                    HyperButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.fillMaxWidth(),
                        tone = HyperButtonTone.Secondary
                    ) {
                        HyperText(texts.closeAction)
                    }
                }
            }
        }
    )
}

private fun dialogTitle(
    state: HyperUpdateDialogState,
    texts: HyperUpdateDialogTexts
): String = when (state) {
    is HyperUpdateDialogState.UpdateAvailable -> texts.updateAvailableTitle
    is HyperUpdateDialogState.DownloadQueued -> texts.downloadQueuedTitle
    is HyperUpdateDialogState.Error -> texts.errorTitle
    else -> texts.checkTitle
}

@Composable
private fun CheckingContent(
    state: HyperUpdateDialogState.Checking,
    texts: HyperUpdateDialogTexts
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HyperCircularProgressIndicator(
            progress = null,
            modifier = Modifier.size(32.dp),
            strokeWidth = 3.dp
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            HyperText(texts.checkingMessage)
            SecondaryText("${texts.currentVersionLabel}：${state.currentVersionName}")
        }
    }
}

@Composable
private fun UpToDateContent(
    state: HyperUpdateDialogState.UpToDate,
    texts: HyperUpdateDialogTexts
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HyperText(texts.upToDateMessage)
        SecondaryText("${texts.currentVersionLabel}：${state.currentVersionName}")
    }
}

@Composable
private fun UpdateAvailableContent(
    state: HyperUpdateDialogState.UpdateAvailable,
    texts: HyperUpdateDialogTexts
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HyperText(
            text = state.release.displayName.ifBlank { state.release.versionName },
            style = HyperTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        SecondaryText("${texts.currentVersionLabel}：${state.currentVersionName}")
        SecondaryText("${texts.latestVersionLabel}：${state.release.versionName}")
        SecondaryText("${texts.packageFileLabel}：${state.release.packageFileName}")

        if (state.release.isPrerelease) {
            HyperText(
                text = texts.prereleaseWarning,
                color = HyperColors.danger,
                fontWeight = FontWeight.Medium
            )
        }

        HyperText(
            text = texts.releaseNotesLabel,
            fontWeight = FontWeight.SemiBold
        )
        SecondaryText(
            state.release.releaseNotes.ifBlank { texts.emptyReleaseNotes }
        )
    }
}

@Composable
private fun DownloadQueuedContent(
    state: HyperUpdateDialogState.DownloadQueued,
    texts: HyperUpdateDialogTexts
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HyperText(texts.downloadQueuedMessage)
        SecondaryText(state.release.packageFileName)
        SecondaryText(texts.downloadNotificationHint)
    }
}

@Composable
private fun ErrorContent(state: HyperUpdateDialogState.Error) {
    HyperText(
        text = state.message,
        color = HyperColors.danger
    )
}

@Composable
private fun SecondaryText(text: String) {
    HyperText(
        text = text,
        color = HyperColors.secondaryText
    )
}
