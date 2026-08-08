# HyperGroupMenus

- 包名：`hyper_ui`
- 源码：`library/src/main/java/hyper_ui/components/menu/HyperGroupMenus.kt`
- 预览：`group-menus`

`HyperGroupMenus` 是 slot-first 横向分组菜单，适合页面顶部分类、筛选分组和同级视图切换。组件只负责滚动布局、选中态、禁用态和点击边界；菜单文字、计数、图标和业务筛选规则都由调用方渲染与维护。

## 公开签名

```kotlin
@Composable
fun HyperGroupMenuItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = HyperGroupMenusDefaults.Shape,
    minHeight: Dp = HyperGroupMenusDefaults.MinHeight,
    contentPadding: PaddingValues = HyperGroupMenusDefaults.ItemContentPadding,
    colors: HyperGroupMenusColors = HyperGroupMenusDefaults.colors(),
    role: Role = Role.Tab,
    content: @Composable HyperGroupMenusItemScope.() -> Unit
)

@Composable
fun <T> HyperGroupMenus(
    items: List<T>,
    selectedItem: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = HyperGroupMenusDefaults.ContentPadding,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(HyperGroupMenusDefaults.ItemGap),
    itemEnabled: (T) -> Boolean = { true },
    itemContent: @Composable HyperGroupMenusItemScope.(item: T) -> Unit
)
```

## 最小用法

```kotlin
HyperGroupMenus(
    items = categories,
    selectedItem = selected,
    onSelected = { selected = it }
) { item ->
    Text(item)
}
```

## 约束

- 不存在内置 `label`、`count`、`icon` 或业务分类模型。
- 需要计数、图标或复杂内容时直接放入 `itemContent`。
- `HyperGroupMenusItemScope` 暴露 `selected` 和 `enabled`，slot 可据此渲染字体、徽标或辅助状态。
- 单个独立菜单项可直接使用 `HyperGroupMenuItem`，不需要横向列表时无需包一层组容器。

<WasmPreview demo="group-menus" title="HyperGroupMenus 交互预览" />
