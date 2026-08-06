package hyper_ui.docs.ui

import hyper_ui.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropdownMenuDemo() {
    var expanded by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("尚未选择") }

    Box(
        modifier = Modifier
            .widthIn(max = 420.dp)
            .height(180.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HyperButton(
                text = "打开菜单",
                onClick = { expanded = true },
                variant = HyperButtonVariant.Default
            )
            Text(
                text = selectedAction,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        }

        HyperDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            alignment = Alignment.TopCenter
        ) {
            Item(
                text = "标记完成",
                leadingIcon = Icons.Default.Check,
                onClick = { selectedAction = "标记完成" }
            )
            Item(
                text = "查看详情",
                leadingIcon = Icons.Default.Info,
                onClick = { selectedAction = "查看详情" }
            )
            Divider()
            Item(
                text = "删除",
                leadingIcon = Icons.Default.Delete,
                textColor = MaterialTheme.colorScheme.error,
                onClick = { selectedAction = "删除" }
            )
        }
    }
}

@Composable
fun ProgressDemo() {
    var progress by remember { mutableStateOf(0.42f) }

    Column(
        modifier = Modifier.widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "确定进度",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
            HyperProgressBar(progress = progress)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                HyperButton(
                    text = "减少",
                    variant = HyperButtonVariant.Default,
                    onClick = { progress = (progress - 0.1f).coerceAtLeast(0f) }
                )
                HyperButton(
                    text = "增加",
                    onClick = { progress = (progress + 0.1f).coerceAtMost(1f) }
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "自定义颜色",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "可以通过 color 设置进度条的颜色，color 可以接受颜色字符串，函数和数组。",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
            var customProgress by remember { mutableStateOf(0.7f) }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                HyperProgressBar(
                    progress = customProgress,
                    progressColor = Color(0.12f, 0.50f, 1f, 1f)
                )
                HyperProgressBar(
                    progress = customProgress,
                    progressColor = Color(0.32f, 0.77f, 0.10f, 1f)
                )
                HyperProgressBar(
                    progress = customProgress,
                    progressColor = MaterialTheme.colorScheme.primary
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                HyperButton(
                    text = "-",
                    variant = HyperButtonVariant.Default,
                    onClick = { customProgress = (customProgress - 0.1f).coerceAtLeast(0f) }
                )
                HyperButton(
                    text = "+",
                    onClick = { customProgress = (customProgress + 0.1f).coerceAtMost(1f) }
                )
            }
        }
    }
}

@Composable
fun LoadingProgressDemo() {
    Column(
        modifier = Modifier.widthIn(max = 560.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "默认加载",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            HyperLoadingProgress()
        }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "自定义样式",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            HyperLoadingProgress(
                height = 4.dp,
                trackColor = MaterialTheme.colorScheme.outlineVariant,
                progressColor = Color(0.03f, 0.76f, 0.38f, 1f)
            )
            HyperLoadingProgress(
                height = 8.dp,
                progressColor = Color(0.12f, 0.50f, 1f, 1f)
            )
        }
    }
}

@Composable
fun DialogDemo() {
    var showDialog by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("等待操作") }

    Column(
        modifier = Modifier.widthIn(max = 420.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperButton(
            text = "删除数据",
            variant = HyperButtonVariant.Danger,
            onClick = { showDialog = true }
        )
        Text(
            text = resultText,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
    }

    HyperConfirmDialog(
        show = showDialog,
        title = "确认删除",
        message = "删除后无法恢复，是否继续？",
        confirmText = "继续删除",
        cancelText = "取消",
        onConfirm = {
            resultText = "已确认删除"
            showDialog = false
        },
        onCancel = {
            resultText = "已取消"
            showDialog = false
        }
    )
}

@Composable
fun HyperDialogDemo() {
    var showDialog by remember { mutableStateOf(false) }
    var savedNote by remember { mutableStateOf("默认备注") }
    var draftNote by remember { mutableStateOf(savedNote) }

    Column(
        modifier = Modifier.widthIn(max = 420.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperButton(
            text = "编辑备注",
            onClick = {
                draftNote = savedNote
                showDialog = true
            }
        )
        Text(
            text = "当前备注：$savedNote",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }

    HyperDialog(
        show = showDialog,
        onDismissRequest = { showDialog = false },
        horizontalAlignment = Alignment.Start,
        actions = {
            HyperButton(
                text = "取消",
                variant = HyperButtonVariant.Default,
                onClick = { showDialog = false }
            )
            HyperButton(
                text = "保存",
                onClick = {
                    savedNote = draftNote.ifBlank { "未填写备注" }
                    showDialog = false
                }
            )
        }
    ) {
        dialogContent(draftNote) { draftNote = it }
    }
}

@Composable
private fun ColumnScope.dialogContent(
    value: String,
    onValueChange: (String) -> Unit
) {
    Text(
        text = "编辑备注",
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp
    )
    Text(
        text = "标题、输入框和按钮均由调用方通过 slot 传入。",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 13.sp,
        lineHeight = 18.sp
    )
    HyperTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp, max = 220.dp),
        placeholder = "请输入备注",
        singleLine = false,
        minHeight = 88.dp
    )
}
