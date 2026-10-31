# HyperLoading

包名：hyper_ui。Android 手机端 Compose 加载组件，覆盖 320–430dp 竖屏场景；Desktop/Wasm 仅用于文档 Preview。

<WasmPreview demo="loading" height="1120" />

## 使用

```kotlin
import hyper_ui.*

// 默认随机
HyperLoading()

// 固定样式
if (isLoading) {
    HyperLoading(type = HyperLoadingType.wovenSignal, label = "加载中")
}
```

## 公开 API

```kotlin
@Composable
fun HyperLoading(
    type: HyperLoadingType? = null,
    modifier: Modifier = Modifier,
    label: String? = null
)
```

| 参数 | 类型 | 必填 | 默认值 | 作用 |
| --- | --- | --- | --- | --- |
| type | HyperLoadingType? | 否 | null | 每个实例默认随机选择一款；指定值时固定形态 |
| modifier | Modifier | 否 | Modifier | 宿主布局与间距 |
| label | String? | 否 | null | 可选说明文字 |

未指定 type（null）时，每个组件实例从十款动画中等概率随机选择一款，父级重建或 label 变化时保持当前形态。显式指定 type 时固定使用该样式；切回 null 时使用该实例保存的随机选择。组件移除后重新创建会重新随机，可能再次选中同一款。

## HyperLoadingType

```kotlin
enum class HyperLoadingType {
    travelingPulse, wovenSignal, ribbonDuet, threeBody,
    cometExchange, photonRelay, elasticSpark, goldenHourglass,
    frequencyFins, flexRaster
}
```

图形尺寸单位为 dp，不包含可选文字。

| type | 显示样式 | 图形尺寸 | 固定 2× 周期 |
| --- | --- | --- | --- |
| travelingPulse | 行波脉冲 | 108 × 80 | 1.4 秒 |
| wovenSignal | 双弦错织 | 108 × 84 | 1.5 秒 |
| ribbonDuet | 弧带换向 | 82 × 68 | 1.6 秒 |
| threeBody | 三体协奏 | 80 × 80 | 1.7 秒 |
| cometExchange | 双彗交汇 | 104 × 84 | 1.5 秒 |
| photonRelay | 光子穿梭 | 86 × 64 | 0.825 秒 |
| elasticSpark | 磁光星芒 | 92 × 92 | 1.4 秒 |
| goldenHourglass | 流金沙漏 | 100 × 100 | 6 秒，两次翻转 |
| frequencyFins | 变频鳍片 | 82 × 66 | 1.2 秒 |
| flexRaster | 柔波光栅 | 108 × 80 | 1.3 秒 |

## 状态与绘制

每种 type 只有一套默认规格，label 位于图形下方 12 个逻辑单位。十款动画采用各自固定原始配色。所有动画固定以原网页节奏的 2× 播放。

组件只负责不确定加载动画的呈现，不发起任务、不表示实际百分比。业务通过条件渲染控制显示和移除；确定进度使用 HyperProgress。切换 type 从该形态起点播放，移除组件释放动画时钟，再次创建从起点播放。

路径与弧长测量按组件缓存，动画只更新画布，不逐帧触发布局；采用原生 Canvas 绘制，不依赖网页、图片或视频。沙漏通过固定沙面关键帧和双组交接完成沙堆增长、流束缩短及容器翻转。图形保持平面风格。

系统 MotionDurationScale 为 0 时保留静态画面，其他正值按系统时长倍率调整。移除组件会取消帧循环；时间只在绘制阶段读取，不逐帧重组宿主。

公开入口位于 components/loading/HyperLoading.kt；core/loading/ 内部按几何、普通绘制、沙漏绘制分离实现，调用方不直接导入内部工具。
