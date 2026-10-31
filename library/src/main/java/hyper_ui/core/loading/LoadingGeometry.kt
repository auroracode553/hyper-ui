package hyper_ui.core.loading

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.PathParser
import kotlin.math.*

internal fun rgb(r: Int, g: Int, b: Int, a: Float = 1f) = Color(r / 255f, g / 255f, b / 255f, a)
internal fun cycle(seconds: Double, duration: Double, delay: Double = 0.0): Float =
    ((((seconds + delay) / duration) % 1.0 + 1.0) % 1.0).toFloat()
internal fun mix(a: Float, b: Float, t: Float) = a + (b - a) * t
internal fun bezier(t: Float, x1: Float, y1: Float, x2: Float, y2: Float): Float {
    if (t <= 0f || t >= 1f) return t.coerceIn(0f, 1f)
    fun sample(u: Float, a: Float, b: Float) = 3 * (1-u) * (1-u) * u * a + 3 * (1-u) * u * u * b + u*u*u
    var low = 0f
    var high = 1f
    repeat(22) {
        val mid = (low + high) / 2
        if (sample(mid, x1, x2) < t) low = mid else high = mid
    }
    return sample((low + high) / 2, y1, y2)
}
internal fun linear(t: Float) = t
internal fun easeInOut(t: Float) = bezier(t, .42f, 0f, .58f, 1f)
internal fun weaveEase(t: Float) = bezier(t, .45f, 0f, .25f, 1f)
internal fun duetEase(t: Float) = bezier(t, .5f, 0f, .25f, 1f)
internal fun photonEase(t: Float) = bezier(t, .4f, 0f, .3f, 1f)
internal fun sparkEase(t: Float) = bezier(t, .4f, 0f, .2f, 1f)
internal fun turnEase(t: Float) = bezier(t, .65f, 0f, .3f, 1f)

/** 每个关键帧区间分别应用 easing，与 CSS 的按属性插值一致。 */
internal fun track(p: Float, times: List<Float>, values: List<Float>, ease: (Float) -> Float = ::linear): Float {
    for (i in 1 until times.size) {
        if (p <= times[i]) return mix(values[i-1], values[i], ease(((p-times[i-1]) / (times[i]-times[i-1])).coerceIn(0f, 1f)))
    }
    return values.last()
}
internal fun fill(color: Color) = Paint().apply { this.color = color }
internal fun stroke(color: Color, width: Float) = fill(color).apply {
    style = PaintingStyle.Stroke; strokeWidth = width; strokeCap = StrokeCap.Round; strokeJoin = StrokeJoin.Round
}
internal fun pivot(c: Canvas, x: Float, y: Float, degrees: Float, sx: Float = 1f, sy: Float = 1f) {
    c.translate(x, y); c.rotate(degrees); c.scale(sx, sy); c.translate(-x, -y)
}
internal fun cssGradient(rect: Rect, angle: Float, colors: List<Color>, stops: List<Float>): Shader {
    val radians = angle * PI.toFloat() / 180
    val direction = Offset(sin(radians), -cos(radians))
    val length = rect.width * abs(direction.x) + rect.height * abs(direction.y)
    return LinearGradientShader(rect.center - direction * (length / 2), rect.center + direction * (length / 2), colors, stops)
}
internal fun gradientPaint(rect: Rect, angle: Float, colors: List<Color>, stops: List<Float>) = Paint().apply {
    shader = cssGradient(rect, angle, colors, stops)
}
internal fun polygon(points: List<Offset>) = Path().apply {
    moveTo(points[0].x, points[0].y)
    points.drop(1).forEach { lineTo(it.x, it.y) }; close()
}
internal fun motionPath(source: String) = PathParser().parsePathString(source).toPath()
internal fun measure(path: Path) = PathMeasure().apply { setPath(path, false) }
internal fun pathSegment(c: Canvas, metric: PathMeasure, start: Float, end: Float, paint: Paint) {
    val from = start.coerceIn(0f, 1f)
    val to = end.coerceIn(0f, 1f)
    if (to > from) {
        val segment = Path()
        metric.getSegment(from * metric.length, to * metric.length, segment, true)
        c.drawPath(segment, paint)
    }
}
internal fun roundRect(c: Canvas, rect: Rect, rx: Float, ry: Float, paint: Paint) =
    c.drawRoundRect(rect.left, rect.top, rect.right, rect.bottom, rx, ry, paint)

/** 路径和弧长测量按组件缓存，不在每帧解析。 */
internal class LoadingPaths {
    val pulse = motionPath("M9 44 C21 44 17 13 31 13 C47 13 38 64 54 64 C70 64 61 23 77 23 C89 23 85 44 99 44")
    val weaveA = motionPath("M12 28 C12 8 42 8 42 28 C42 52 70 52 70 28 C70 8 98 8 98 28")
    val weaveB = motionPath("M12 56 C12 76 42 76 42 56 C42 32 70 32 70 56 C70 76 98 76 98 56")
    val ribbon = motionPath("M4 27 C14 4 48 3 66 24 C69 28 64 33 60 29 C46 12 24 12 12 31 C9 35 1 32 4 27 Z")
    val comet = motionPath("M12 50 C-2 14 32 3 47 30 C65 66 89 76 94 46 C100 15 76 8 56 38 C35 74 15 78 12 50 Z")
    val spark = motionPath("M44 10 Q46 6 48 10 C52 30 56 34 78 38 Q84 40 78 42 C56 46 52 50 48 72 Q46 78 44 72 C40 50 36 46 14 42 Q8 40 14 38 C36 34 40 30 44 10 Z")
    val satellite = motionPath("M0 -12 Q2 -2 12 0 Q2 2 0 12 Q-2 2 -12 0 Q-2 -2 0 -12 Z")
    val pulseMetric = measure(pulse)
    val weaveAMetric = measure(weaveA)
    val weaveBMetric = measure(weaveB)
    val cometMetric = measure(comet)
}
