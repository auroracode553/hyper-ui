"""Build source-checked Props tables from the signatures in component Markdown pages.

Run with --check to verify the committed tables without changing files.
Descriptions live here so each parameter has one consistent explanation across pages.
"""

from __future__ import annotations

import argparse
import re
from html import escape
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
DOCS = ROOT / "vitepress/docs/components"
SOURCE = ROOT / "library/src/main/java/hyper_ui"
SIGNATURE_BLOCK = re.compile(r"## 公开签名[^\n]*\n\n```kotlin\n(.*?)\n```", re.S)
FUNCTION = re.compile(r"\bfun\s+(?:<[^>]+>\s+)?(?:\w+\.)?(\w+)\s*\(")

DESCRIPTIONS = dict(
    line.split("|", 1)
    for line in """
actionArrangement|操作区的排列方式。
actionContent|操作按钮或操作区内容，由调用方提供。
actionExtent|侧滑操作区的展开宽度。
actionSpacing|导航操作项之间的间距。
actions|导航栏操作项内容。
alignment|弹出菜单相对于窗口的对齐方式。
background|供玻璃表面采样的背景内容。
bodyContent|对话框主体内容。
border|容器边框配置。
bottomSafeArea|是否为底部系统安全区留白。
centerTitle|是否将标题在导航栏中居中。
charging|当前是否正在充电。
checked|由调用方持有的勾选状态。
checkedColor|勾选时的控件颜色。
checkedThumbColor|开启时滑块颜色。
checkedTrackColor|开启时轨道颜色。
checkmarkColor|勾选标记颜色。
child|导航栏附加子内容。
clearable|是否在有内容时显示清空操作。
closeContent|关闭按钮的自定义内容。
colorSize|单个颜色选项的尺寸。
colors|组件各状态的颜色配置。
containerPadding|容器内部留白。
content|组件主体内容，由调用方提供。
contentDescription|无障碍内容描述。
contentModifier|内部内容区域的布局修饰符。
contentPadding|主体内容的内部留白。
context|用于读取系统电量状态的 Android Context。
currentSpeed|当前播放速度，由调用方持有。
customActionLeadingContent|自定义速度操作的前置内容。
defaultSetPadding|是否使用抽屉默认留白。
defaultSpeed|重置操作使用的默认速度。
description|空状态的说明文字。
dismissOnBackPress|按返回键时是否请求关闭。
dismissOnClickOutside|点击外部时是否请求关闭。
dividerModifier|分隔线的布局修饰符。
dividerVisible|是否显示行分隔线。
drawerContent|抽屉中的内容。
drawerContentModifier|抽屉内容区域的布局修饰符。
drawerContentScrollEnabled|抽屉内容是否允许滚动。
drawerModifier|抽屉面板的布局修饰符。
durationMillis|提示自动消失前的持续时间，单位毫秒。
elevation|容器阴影高度。
enabled|是否允许用户交互。
endActions|向左侧滑时露出的操作项。
endContent|输入框末端的自定义内容。
equalWidth|各分段是否等宽。
expanded|菜单是否展开，由调用方持有。
firstSectionTopSpacing|第一分组顶部的间距。
floatingColors|悬浮样式的颜色配置。
headerBottomSpacing|分组标题下方间距。
headerContent|每个分组的标题内容。
headlineContent|列表行主标题内容。
hintLeadingContent|提示信息的前置内容。
horizontalAlignment|容器内内容的水平对齐方式。
horizontalArrangement|子项的水平排列方式。
horizontalSpacing|颜色选项之间的水平间距。
iconContent|图标区域的自定义内容。
innerDotColor|单选圆点的颜色。
inputModifier|输入控件自身的布局和交互修饰符。
intensity|柔和背景效果的强度。
interactionSource|输入控件的交互状态来源。
isError|是否显示错误状态。
itemContent|每个数据项的自定义内容。
itemContentPadding|单个选项的内部留白。
itemEnabled|判断单个选项是否可操作。
itemKey|为列表项生成稳定键。
itemLayout|标签栏单项的布局配置。
itemSelected|判断标签栏单项是否选中。
itemShape|单个选项的形状。
itemSlotAlignment|标签栏内容 Slot 的对齐方式。
items|由调用方提供的选项或列表数据。
keyboardActions|软键盘动作回调。
keyboardOptions|软键盘类型与输入配置。
label|数值旁显示的标签文字。
labelContent|输入框标签内容。
labelTopSpacing|颜色名称与色块之间的间距。
leadingContent|内容前方的自定义区域。
loading|是否处于加载态；加载时阻止重复点击。
maxlength|允许输入的最大字符数。
message|提示消息文本。
minimumTouchHeight|滑块可触摸区域的最小高度。
modifier|组件外部尺寸、位置和间距修饰符。
navBar|页面顶部导航栏内容。
navBarSize|页面导航栏的尺寸档位。
navigationContent|导航栏左侧导航内容。
offset|弹出菜单相对对齐位置的偏移。
onCheckedChange|勾选状态变化时通知调用方。
onClick|用户点击时执行的回调。
onCustomSpeedRequest|请求打开自定义速度设置。
onDismissRequest|请求关闭时通知调用方更新可见状态。
onDownload|用户触发下载时的回调。
onItemClick|标签栏单项点击时的回调。
onRetry|用户触发重试时的回调。
onRevealChange|侧滑展开状态变化时通知调用方。
onSelected|选中项变化时通知调用方。
onSpeedChange|速度变化时通知调用方更新状态。
onValueChange|值变化时通知调用方更新状态。
onValueChangeFinished|用户结束滑动时的回调。
onValueChangeStarted|用户开始滑动时的回调。
open|抽屉是否打开，由调用方持有。
options|可选颜色配置。
padding|导航栏内部留白。
panelModifier|播放速度面板的布局修饰符。
percentage|电量百分比，范围为 0 至 100。
placeholderContent|输入框占位内容。
position|抽屉从哪一侧进入。
progress|由调用方提供的进度值。
readOnly|是否只展示当前值而禁止修改。
resetContent|重置操作的自定义内容。
reveal|侧滑展开状态，由调用方持有。
role|组件的无障碍语义角色。
rows|多行输入的显示行数。
safeArea|是否避开顶部系统安全区。
sectionKey|为分组生成稳定键。
sectionSpacing|相邻分组之间的间距。
sections|分组数据。
segmentMarkerSize|分段刻度标记的尺寸。
segmentValues|需要标出的分段数值。
selected|由调用方持有的选中状态。
selectedBorder|选中项的边框配置。
selectedColor|选中时的控件颜色。
selectedColors|选中项的颜色配置。
selectedId|当前颜色选项 ID，由调用方持有。
selectedItem|当前选中的数据项，由调用方持有。
selectedSpeed|当前选中的播放速度。
selectedType|选中项使用的按钮形态。
shape|组件容器的形状。
showPasswordToggle|密码类型是否显示显隐操作。
showPercentage|是否显示电量百分比文字。
showScrollIndicator|内容滚动时是否显示滚动提示。
showSegmentMarkers|是否显示分段刻度标记。
showWordLimit|是否显示字符数和上限。
size|组件尺寸档位。
slotSpacing|输入框各内容 Slot 之间的间距。
spacing|导航栏各区域之间的间距。
speedOptions|可选播放速度列表。
startActions|向右侧滑时露出的操作项。
startContent|输入框起始端的自定义内容。
state|组件使用的状态对象，由调用方提供或记忆。
steps|数值范围内的离散步数。
strokeWidth|圆形进度条的描边宽度。
subtitleContent|导航栏副标题内容。
supportingContent|标题或输入框下方的辅助内容。
text|提示文字。
textStyle|输入文字的排版样式。
texts|组件内置文案的配置。
thumbBorder|滑块手柄的边框配置。
thumbShape|滑块手柄的形状。
thumbSize|滑块手柄的尺寸。
title|对话框或弹出层标题。
titleContent|导航栏标题内容。
titleSpacing|标题与其他区域的间距。
tone|提示的语义色类型。
topDivider|是否显示顶部边界线。
trackBorder|进度轨道的边框配置。
trackHeight|滑块轨道高度。
trackShape|滑块轨道形状。
trailingContent|内容末端的自定义区域。
type|组件的视觉或布局形态。
uncheckedBorderColor|未勾选时边框颜色。
uncheckedColor|未勾选时的控件颜色。
uncheckedThumbColor|关闭时滑块颜色。
uncheckedTrackColor|关闭时轨道颜色。
unselectedBorder|未选中项的边框配置。
unselectedBorderColor|未选中时边框颜色。
unselectedColor|未选中时的控件颜色。
unselectedColors|未选中项的颜色配置。
unselectedType|未选中项使用的按钮形态。
value|由调用方持有的当前值。
valueRange|可操作数值范围。
verticalAlignment|子项的垂直对齐方式。
verticalArrangement|子项的垂直排列方式。
verticalSpacing|颜色选项之间的垂直间距。
visible|是否显示组件，由调用方持有。
visualTransformation|输入文字的视觉变换方式。
""".strip().splitlines()
)


def matching_parenthesis(source: str, open_at: int) -> int:
    depth = 1
    for index in range(open_at + 1, len(source)):
        if source[index] == "(":
            depth += 1
        elif source[index] == ")":
            depth -= 1
            if depth == 0:
                return index
    raise ValueError("Unclosed function parameter list")


def split_top_level(value: str, delimiter: str) -> list[str]:
    parts = []
    start = 0
    depths = {"(": 0, "<": 0, "{": 0, "[": 0}
    closing = {")": "(", ">": "<", "}": "{", "]": "["}
    quoted = False
    escaped = False
    for index, char in enumerate(value):
        if char == '"' and not escaped:
            quoted = not quoted
        if not quoted:
            if char in depths:
                depths[char] += 1
            elif char in closing and depths[closing[char]] > 0:
                depths[closing[char]] -= 1
            elif char == delimiter and all(depth == 0 for depth in depths.values()):
                parts.append(value[start:index].strip())
                start = index + 1
        escaped = char == "\\" and not escaped
    parts.append(value[start:].strip())
    return [part for part in parts if part]


def functions(source: str) -> list[tuple[str, list[tuple[str, str, str | None]]]]:
    result = []
    for match in FUNCTION.finditer(source):
        end = matching_parenthesis(source, match.end() - 1)
        parameters = []
        for declaration in split_top_level(source[match.end():end], ","):
            parameter = re.match(r"(?:val\s+)?(\w+)\s*:\s*(.*)", declaration, re.S)
            if parameter is None:
                raise ValueError(f"Cannot parse {match.group(1)} parameter: {declaration}")
            type_and_default = split_top_level(parameter.group(2), "=")
            parameter_type = " ".join(type_and_default[0].split())
            default = " ".join(type_and_default[1].split()) if len(type_and_default) > 1 else None
            parameters.append((parameter.group(1), parameter_type, default))
        result.append((match.group(1), parameters))
    return result


def code(value: str) -> str:
    return escape(value).replace("|", "\\|")


def make_table(name: str, parameters: list[tuple[str, str, str | None]]) -> str:
    rows = [
        "| 参数 | 类型 | 必填 | 默认值 | 作用 |",
        "| --- | --- | --- | --- | --- |",
    ]
    for parameter_name, parameter_type, default in parameters:
        if parameter_name not in DESCRIPTIONS:
            raise ValueError(f"Missing description for {name}.{parameter_name}")
        rows.append(
            f"| {code(parameter_name)} | {code(parameter_type)} | "
            f"{'是' if default is None else '否'} | "
            f"{'—' if default is None else code(default)} | {DESCRIPTIONS[parameter_name]} |"
        )
    return "\n".join(rows)


def main(check: bool) -> int:
    source_signatures: dict[str, list[list[tuple[str, str, str | None]]]] = {}
    for path in SOURCE.rglob("*.kt"):
        for name, parameters in functions(path.read_text(encoding="utf-8")):
            source_signatures.setdefault(name, []).append(parameters)

    changed = []
    for path in sorted(DOCS.rglob("*.md")):
        original = path.read_bytes().decode("utf-8")
        newline = "\r\n" if "\r\n" in original else "\n"
        content = original.replace("\r\n", "\n")
        match = SIGNATURE_BLOCK.search(content)
        if match is None:
            raise ValueError(f"Missing public signature block: {path}")
        documented = functions(match.group(1))
        for name, parameters in documented:
            signature = tuple(parameter[0] for parameter in parameters)
            candidates = [
                source_parameters
                for source_parameters in source_signatures.get(name, [])
                if tuple(parameter[0] for parameter in source_parameters) == signature
            ]
            if not candidates:
                raise ValueError(f"Documented signature differs from source: {path}: {name}{signature}")
            if parameters not in candidates:
                raise ValueError(f"Documented types or defaults differ from source: {path}: {name}")
        tables = []
        for index, (name, parameters) in enumerate(documented):
            if not parameters:
                continue
            if len(documented) > 1:
                label = name
                if sum(candidate == name for candidate, _ in documented) > 1:
                    label += "（数据项形式）" if parameters[0][0] == "items" else "（内容 Slot 形式）"
                tables.append(f"### {label}\n\n{make_table(name, parameters)}")
            else:
                tables.append(make_table(name, parameters))
        if path.stem == "hyper-battery-state":
            tables.insert(0, "### HyperBatteryState 返回字段\n\n"
                "| 字段 | 类型 | 作用 |\n| --- | --- | --- |\n"
                "| percentage | Int | 电量百分比，范围为 0 至 100。 |\n"
                "| isCharging | Boolean | 是否正在充电或已充满且连接电源。 |\n"
                "| isAvailable | Boolean | 系统是否提供有效电量数据。 |\n"
                "| Unavailable | HyperBatteryState | 无法读取电量时使用的状态。 |")
        if not tables:
            raise ValueError(f"No API table produced: {path}")
        table_section = "\n## Props（参数）\n\n" + "\n\n".join(tables)
        table_start = content.find("\n## Props（参数）\n")
        if table_start >= 0:
            next_heading = content.find("\n## ", table_start + 1)
            section_end = len(content) if next_heading < 0 else next_heading
            existing_section = content[table_start:section_end]
            table_rows = list(re.finditer(r"(?m)^\|.*\|$", existing_section))
            if not table_rows:
                raise ValueError(f"Props section has no table rows: {path}")
            old_end = table_start + table_rows[-1].end()
            updated = content[:table_start] + table_section + content[old_end:]
        else:
            updated = content[:match.end()] + "\n" + table_section + "\n" + content[match.end():]
        if updated != content:
            changed.append(path.relative_to(ROOT))
            if not check:
                path.write_bytes(updated.replace("\n", newline).encode("utf-8"))
    if changed:
        print(("Out of date" if check else "Updated") + f" {len(changed)} component pages:")
        for path in changed:
            print(f"  {path}")
    else:
        print("All component API tables are current.")
    return 1 if check and changed else 0


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true", help="verify without writing files")
    args = parser.parse_args()
    raise SystemExit(main(args.check))
