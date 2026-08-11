# 浮层与草稿状态

编辑浮层通常需要区分已保存值和浮层内草稿。取消时丢弃草稿，确认时再提交。

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
    var showPopup by remember { mutableStateOf(false) }

    HyperButton(onClick = {
        draft = note
        showPopup = true
    }) {
        Text("编辑备注")
    }

    HyperPopup(
        visible = showPopup,
        onDismissRequest = { showPopup = false },
        title = "编辑备注",
        actionContent = {
            HyperButton(
                tone = HyperButtonTone.Outline,
                onClick = { showPopup = false }
            ) {
                Text("取消")
            }
            HyperButton(
                onClick = {
                    note = draft
                    showPopup = false
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
