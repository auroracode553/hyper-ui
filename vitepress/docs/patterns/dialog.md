# 弹窗与草稿状态

编辑弹窗通常需要区分已保存值和弹窗内草稿。取消时丢弃草稿，确认时再提交。

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import hyper_ui.*

@Composable
fun EditNoteAction() {
    var note by remember { mutableStateOf("默认备注") }
    var draft by remember { mutableStateOf(note) }
    var showDialog by remember { mutableStateOf(false) }

    HyperButton(onClick = {
        draft = note
        showDialog = true
    }) {
        Text("编辑备注")
    }

    HyperDialog(
        visible = showDialog,
        onDismissRequest = { showDialog = false },
        actionContent = {
            HyperButton(
                tone = HyperButtonTone.Outline,
                onClick = { showDialog = false }
            ) {
                Text("取消")
            }
            HyperButton(
                onClick = {
                    note = draft
                    showDialog = false
                }
            ) {
                Text("保存")
            }
        }
    ) {
        HyperTextField(
            value = draft,
            onValueChange = { draft = it },
            singleLine = false,
            minLines = 3
        )
    }
}
```
