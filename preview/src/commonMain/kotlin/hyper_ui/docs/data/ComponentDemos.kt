package hyper_ui.docs.data

import androidx.compose.runtime.Composable

data class ComponentDemo(
    val id: String,
    val group: String,
    val title: String,
    val description: String,
    val code: String,
    val content: @Composable () -> Unit
)

fun componentDemos(): List<ComponentDemo> = listOf(
    basicComponentDemos(),
    formComponentDemos(),
    containerComponentDemos(),
    navigationComponentDemos(),
    listComponentDemos(),
    feedbackComponentDemos()
).flatten()
