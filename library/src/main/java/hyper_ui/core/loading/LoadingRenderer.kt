package hyper_ui.core.loading

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import hyper_ui.HyperLoadingType
import kotlin.math.*

internal fun loadingSize(type: HyperLoadingType): Size = when(type) {
    HyperLoadingType.travelingPulse, HyperLoadingType.flexRaster -> Size(108f, 80f)
    HyperLoadingType.wovenSignal -> Size(108f, 84f)
    HyperLoadingType.ribbonDuet -> Size(82f, 68f)
    HyperLoadingType.threeBody -> Size(80f, 80f)
    HyperLoadingType.cometExchange -> Size(104f, 84f)
    HyperLoadingType.photonRelay -> Size(86f, 64f)
    HyperLoadingType.elasticSpark -> Size(92f, 92f)
    HyperLoadingType.goldenHourglass -> Size(100f, 100f)
    HyperLoadingType.frequencyFins -> Size(82f, 66f)
}
internal fun loadingPalette(type: HyperLoadingType): Pair<Color, Color> = when(type) {
    HyperLoadingType.travelingPulse -> rgb(79,120,228) to rgb(77,197,191)
    HyperLoadingType.wovenSignal -> rgb(76,124,219) to rgb(81,194,181)
    HyperLoadingType.ribbonDuet -> rgb(241,140,104) to rgb(247,193,123)
    HyperLoadingType.threeBody -> rgb(76,122,235) to rgb(103,201,225)
    HyperLoadingType.cometExchange -> rgb(73,120,229) to rgb(80,199,187)
    HyperLoadingType.photonRelay -> rgb(65,138,202) to rgb(101,203,184)
    HyperLoadingType.elasticSpark -> rgb(91,127,224) to rgb(103,201,192)
    HyperLoadingType.goldenHourglass -> rgb(197,167,116) to rgb(227,208,171)
    HyperLoadingType.frequencyFins -> rgb(66,127,209) to rgb(105,197,199)
    HyperLoadingType.flexRaster -> rgb(80,132,219) to rgb(107,200,187)
}

/** 只接收画布、时间与配色；公开组件负责布局和动画生命周期。 */
internal class LoadingRenderer {
    private val paths by lazy { LoadingPaths() }
    private val hourglass by lazy { LoadingHourglass() }

    fun draw(c: Canvas, type: HyperLoadingType, seconds: Double, a: Color, b: Color) {
        when(type) {
            HyperLoadingType.travelingPulse -> {
                c.drawPath(paths.pulse, stroke(rgb(237,241,246), 3.5f))
                repeat(2) { i ->
                    val start = cycle(seconds, 2.8, i * 1.4)
                    val end = start + if(i == 0) .32f else .23f
                    val paint = stroke(if(i == 0) a else b, 8.5f)
                    pathSegment(c, paths.pulseMetric, start, end, paint)
                    if(end > 1) pathSegment(c, paths.pulseMetric, 0f, end - 1, paint)
                }
            }
            HyperLoadingType.wovenSignal -> {
                c.save(); c.translate(0f, (84f - 84f * 108 / 110) / 2); c.scale(108f/110, 108f/110)
                repeat(2) { i ->
                    val offset = track(cycle(seconds, 3.0, if(i == 0) .45 else 1.25),
                        listOf(0f,.3f,.6f,.9f,1f), listOf(1f,0f,0f,-1f,-1f), ::weaveEase)
                    pathSegment(c, if(i == 0) paths.weaveAMetric else paths.weaveBMetric,
                        max(0f, -offset), min(1f, 1-offset), stroke(if(i == 0) a else b, 9f))
                }
                c.restore()
            }
            HyperLoadingType.ribbonDuet -> repeat(2) { i ->
                var p = cycle(seconds, 3.2, i * 1.6)
                if(i == 1) p = 1-p
                val times = listOf(0f,.5f,1f)
                c.save(); c.translate(5f,12f)
                pivot(c,36f,22f,track(p,times,listOf(-30f,150f,330f),::duetEase),
                    track(p,times,listOf(.85f,1.1f,.85f),::duetEase))
                c.drawPath(paths.ribbon,fill(if(i == 0) a else b)); c.restore()
            }
            HyperLoadingType.threeBody -> {
                val theta = cycle(seconds,3.4)*2*PI.toFloat()
                val radius = 17 + sin(theta)*7
                repeat(3) { i ->
                    val angle = theta + i*2*PI.toFloat()/3
                    c.drawCircle(Offset(40+cos(angle)*radius,40+sin(angle)*radius),
                        6*(1+sin(angle)*.22f),fill(if(i == 0) a else if(i == 1) b else srgbMix(a,b,.45f)))
                }
            }
            HyperLoadingType.cometExchange -> comets(c,seconds,a,b)
            HyperLoadingType.photonRelay -> photons(c,seconds,a,b)
            HyperLoadingType.elasticSpark -> spark(c,seconds,a,b)
            HyperLoadingType.goldenHourglass -> hourglass.draw(c,seconds,a,b)
            HyperLoadingType.frequencyFins -> fins(c,seconds,a,b)
            HyperLoadingType.flexRaster -> repeat(6) { i ->
                val p = cycle(seconds,2.6,i*.15)
                val x = track(p,listOf(0f,.5f,1f),listOf(-16f,16f,-16f),::easeInOut)
                c.save(); c.translate(18f+i*14,0f)
                c.drawPath(Path().apply { moveTo(0f,12f); cubicTo(x,28f,-x,44f,0f,64f) },stroke(if(i%2 == 0) a else b,7.5f))
                c.restore()
            }
        }
    }

    private fun comets(c: Canvas, seconds: Double, a: Color, b: Color) {
        repeat(2) { group -> repeat(6) { i ->
            val distance = cycle(seconds,3.0,i*.065+group*1.5)*paths.cometMetric.length
            val position = paths.cometMetric.getPosition(distance)
            val tangent = paths.cometMetric.getTangent(distance)
            val w = 6f+i*2; val h = 4f+i*1.25f
            val color = if(group == 0) a else b
            val rect = Rect(-w/2,-h/2,w/2,h/2)
            val left = CornerRadius(w*.2f/1.2f,h*.4f/1.2f)
            val right = CornerRadius(w*.75f/1.2f,h*.6f/1.2f)
            val shape = Path().apply { addRoundRect(RoundRect(rect,left,right,right,left)) }
            c.save(); c.translate(position.x,position.y); c.rotate(atan2(tangent.y,tangent.x)*180/PI.toFloat())
            c.drawPath(shape,fill(color.copy(alpha=color.alpha*(.12f+i*.17f))))
            if(i == 5) c.drawPath(shape,Paint().apply {
                shader=LinearGradientShader(rect.topLeft,rect.bottomRight,listOf(rgb(255,255,255,.35f),rgb(255,255,255,0f)),listOf(0f,.45f))
            })
            c.restore()
        } }
    }

    private fun photons(c: Canvas, seconds: Double, a: Color, b: Color) {
        c.save(); pivot(c,43f,32f,-28f)
        repeat(3) { i ->
            val p=cycle(seconds,1.65,i*.22)
            val times=listOf(0f,.55f,1f)
            val dx=track(p,times,listOf(-20f,10f,24f),::photonEase)
            val sx=track(p,times,listOf(.3f,1f,.3f),::photonEase)
            val alpha=track(p,listOf(0f,.2f,.85f,1f),listOf(0f,1f,1f,0f),::photonEase)
            val color=if(i==0) a else if(i==1) b else srgbMix(a,b,.45f)
            c.save(); c.translate(43+dx,17f+i*15); c.scale(sx,1f)
            roundRect(c,Rect(-18f,-3f,18f,3f),3f,3f,fill(color.copy(alpha=color.alpha*alpha)))
            c.restore()
        }
        c.restore()
    }

    private fun spark(c: Canvas, seconds: Double, a: Color, b: Color) {
        val p=cycle(seconds,2.8)
        val times=listOf(0f,.48f,.68f,1f)
        c.save()
        pivot(c,46f,42f,track(p,times,listOf(-14f,12f,5f,-14f),::sparkEase),
            track(p,times,listOf(.68f,1f,.92f,.68f),::sparkEase),track(p,times,listOf(.86f,.92f,1.04f,.86f),::sparkEase))
        c.drawPath(paths.spark,fill(a)); c.restore()
        val echo=listOf(0f,.45f,.7f,1f)
        c.save()
        c.translate(track(p,echo,listOf(72f,77f,75f,72f),::sparkEase),track(p,echo,listOf(15f,17f,19f,15f),::sparkEase))
        c.rotate(track(p,echo,listOf(15f,-8f,8f,15f),::sparkEase))
        val scale=track(p,echo,listOf(.25f,.9f,.65f,.25f),::sparkEase)
        c.scale(scale,scale)
        c.drawPath(paths.satellite,fill(b.copy(alpha=b.alpha*track(p,echo,listOf(.3f,1f,.8f,.3f),::sparkEase))))
        c.restore()
    }

    private fun fins(c: Canvas, seconds: Double, a: Color, b: Color) {
        c.save(); pivot(c,41f,33f,-10f)
        repeat(5) { index ->
            val delay=(index-2)*.16
            val p=cycle(seconds,2.4,delay)
            val active=seconds+delay>=0
            val times=listOf(0f,.5f,1f)
            c.save(); c.translate(15.5f+index*12,50+(if(active) track(p,times,listOf(5f,-3f,5f),::easeInOut) else 0f))
            if(active) {
                c.rotate(track(p,times,listOf(-13f,13f,-13f),::easeInOut))
                c.scale(1f,track(p,times,listOf(.55f,1.15f,.55f),::easeInOut))
            }
            val rect=Rect(-3.5f,-38f,3.5f,0f)
            roundRect(c,rect,3.5f,3.5f,if(index%2==1) fill(b) else gradientPaint(rect,180f,listOf(b,a),listOf(0f,1f)))
            c.restore()
        }
        c.restore()
    }
}

// CSS color-mix(in srgb) 使用编码后的 sRGB 分量插值。
internal fun srgbMix(a: Color, b: Color, t: Float) = Color(
    mix(a.red,b.red,t),mix(a.green,b.green,t),mix(a.blue,b.blue,t),mix(a.alpha,b.alpha,t))
