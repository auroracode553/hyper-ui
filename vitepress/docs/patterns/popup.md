# 浮层与草稿状态

编辑浮层通常需要区分已保存值和浮层内草稿。取消时丢弃草稿，确认时再提交。

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import hyper_ui.HyperText
import hyper_ui.*

@Composable
fun EditNoteAction() {
    var note by remember { mutableStateOf("默认备注") }
    var draft by remember { mutableStateOf(note) }
    var showPopup by remember { mutableStateOf(false) }

    HyperButton(onPressed = {
        draft = note
        showPopup = true
    }) {
        HyperText("编辑备注")
    }

    HyperDialog(
        visible = showPopup,
        onClose = { showPopup = false },
        title = "编辑备注",
        actions = {
            HyperButton(
                onPressed = { showPopup = false }
            ) {
                HyperText("取消")
            }
            HyperButton(
                onPressed = {
                    note = draft
                    showPopup = false
                }
            ) {
                HyperText("保存")
            }
        }
    ) {
        HyperTextField(
            value = draft,
            onChanged = { draft = it },
            type = "textarea",
            rows = 3
        )
    }
}
```
