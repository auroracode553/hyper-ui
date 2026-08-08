# 表单页

输入值与提交结果属于调用方。HyperUI 不执行校验、网络请求或持久化。

```kotlin
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hyper_ui.*

@Composable
fun ProfileForm(onSave: (String) -> Unit) {
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HyperTextField(
            value = name,
            onValueChange = { name = it },
            labelContent = { Text("昵称") },
            placeholderContent = { Text("请输入昵称") }
        )
        HyperButton(onClick = { onSave(name) }) {
            Text("保存")
        }
    }
}
```
