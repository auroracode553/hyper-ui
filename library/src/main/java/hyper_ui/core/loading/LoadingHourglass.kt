package hyper_ui.core.loading

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import kotlin.math.*

/** 沙漏的固定轮廓、沙量关键帧、颗粒与两组翻转交接，由 LoadingRenderer 调用。 */
internal class LoadingHourglass {
    private val outer=motionPath("M16 5 C5 5 4 10 7 22 C10 35 24 38 27 42 C24 46 10 49 7 62 C4 74 5 79 16 79 H48 C59 79 60 74 57 62 C54 49 40 46 37 42 C40 38 54 35 57 22 C60 10 59 5 48 5 Z")
    private val inner=motionPath("M17 7 C8 7 6 12 9 23 C12 34 26 38 29 42 C26 46 12 50 9 61 C6 72 8 77 17 77 H47 C56 77 58 72 55 61 C52 50 38 46 35 42 C38 38 52 34 55 23 C58 12 56 7 47 7 Z")
    private val rim=Path().apply { fillType=PathFillType.EvenOdd; addPath(outer); addPath(inner) }
    private val whiteDots=dots(1.5f,1.75f,3f,3.5f,.4f)
    private val whiteHalos=dots(1.5f,1.75f,3f,3.5f,.7f)
    private val darkDots=dots(3f,2.5f,4f,3f,.35f)
    private val darkHalos=dots(3f,2.5f,4f,3f,.7f)

    private fun dots(x0: Float,y0: Float,dx: Float,dy: Float,radius: Float)=Path().apply {
        var y=y0
        while(y<84) {
            var x=x0
            while(x<64) { addOval(Rect(x-radius,y-radius,x+radius,y+radius)); x+=dx }
            y+=dy
        }
    }

    fun draw(c: Canvas,seconds: Double,a: Color,b: Color) {
        val p=cycle(seconds,12.0)
        // 多层低透明椭圆近似网页 1px 柔化，不引入 Android 专属绘图依赖。
        repeat(4) { i ->
            val spread=i*.35f
            c.drawOval(Rect(23-spread,92-spread,77+spread,96+spread),fill(rgb(58,58,54,.025f)))
        }
        c.save(); c.translate(18f,5f)
        pivot(c,32f,42f,track(p,listOf(0f,.42f,.5f,.92f,1f),listOf(0f,0f,180f,180f,360f),::turnEase))
        val rect=Rect(0f,0f,64f,84f)
        c.drawPath(outer,gradientPaint(rect,90f,listOf(rgb(142,154,163,.13f),rgb(255,255,255,.05f),rgb(255,255,255,.05f),rgb(114,128,139,.15f)),listOf(0f,.26f,.68f,1f)))
        repeat(2) { run ->
            val q=cycle(seconds,12.0,run*6.0)
            val alpha=track(q,listOf(0f,.43f,.49f,.93f,.99f,1f),listOf(1f,1f,0f,0f,1f,1f))
            if(alpha>0) {
                c.save(); pivot(c,32f,42f,run*180f); c.clipPath(inner)
                c.saveLayer(rect,fill(rgb(255,255,255,alpha)))
                drawSand(c,q,seconds,a,b)
                c.restore(); c.restore()
            }
        }
        c.save(); c.clipPath(inner)
        c.drawRect(rect,gradientPaint(rect,99f,
            listOf(rgb(255,255,255,0f),rgb(255,255,255,0f),rgb(255,255,255,.48f),rgb(255,255,255,0f),rgb(255,255,255,0f),rgb(255,255,255,.36f),rgb(255,255,255,0f),rgb(255,255,255,0f)),
            listOf(0f,.18f,.21f,.26f,.72f,.76f,.8f,1f)))
        c.restore()
        c.drawPath(rim,gradientPaint(rect,110f,listOf(rgb(183,193,200),rgb(244,246,245),rgb(182,192,198),rgb(132,146,156),rgb(228,232,233)),listOf(.08f,.28f,.52f,.78f,1f)))
        listOf(2f,77f).forEach { top ->
            val cap=Rect(8f,top,56f,top+5)
            roundRect(c,cap.translate(0f,1f),18.461538f,2.5f,fill(rgb(61,68,70,.07f)))
            roundRect(c,cap,18.461538f,2.5f,gradientPaint(cap,180f,listOf(rgb(240,239,235),rgb(178,181,179),rgb(225,225,219)),listOf(0f,.65f,1f)))
            roundRect(c,cap.deflate(.5f),17.961538f,2f,stroke(rgb(116,123,124,.26f),1f))
        }
        c.restore()
    }

    private fun texture(c: Canvas,a: Color,b: Color) {
        val rect=Rect(0f,0f,64f,84f)
        c.drawRect(rect,gradientPaint(rect,90f,listOf(rgb(182,151,100),b,a),listOf(0f,.48f,1f)))
        c.drawPath(darkHalos,fill(rgb(108,80,42,.05f)))
        c.drawPath(darkDots,fill(rgb(108,80,42,.105f)))
        c.drawPath(whiteHalos,fill(rgb(255,255,255,.1f)))
        c.drawPath(whiteDots,fill(rgb(255,255,255,.245f)))
    }

    private fun drawSand(c: Canvas,p: Float,seconds: Double,a: Color,b: Color) {
        val times=listOf(0f,.03f,.066f,.102f,.138f,.174f,.21f,.246f,.282f,.318f,.354f,.39f,.49f,.5f,1f)
        val level=track(p,times,listOf(24.45f,24.45f,25.55f,26.65f,27.75f,28.95f,30.15f,31.465f,32.933f,34.703f,36.987f,41.936f,41.936f,24.45f,24.45f))
        val dip=track(p,times,listOf(27.65f,27.65f,28.75f,29.85f,30.95f,32.15f,33.35f,34.665f,36.106f,37.257f,38.742f,41.958f,41.958f,27.65f,27.65f))
        val h=track(p,times,listOf(0f,0f,5.69f,8.047f,9.856f,11.382f,12.744f,14f,15.181f,16.309f,17.397f,18.455f,18.455f,0f,0f))
        val radius=h/tan(34*PI.toFloat()/180)
        listOf(
            polygon(listOf(Offset(0f,level),Offset(24f,level),Offset(32f,dip),Offset(40f,level),Offset(64f,level),Offset(64f,42f),Offset(0f,42f))),
            polygon(listOf(Offset(32-radius,77f),Offset(32f,77-h),Offset(32+radius,77f),Offset(64f,84f),Offset(0f,84f)))
        ).forEach { path -> c.save(); c.clipPath(path); texture(c,a,b); c.restore() }
        val alpha=track(p,listOf(0f,.01f,.03f,.37f,.4f,1f),listOf(0f,0f,1f,1f,0f,0f))
        if(alpha<=0) return
        val length=35-h
        c.saveLayer(Rect(27f,41f,38f,79f),fill(rgb(255,255,255,alpha)))
        c.save(); c.clipRect(Rect(31.4f,42f,32.6f,42+length))
        var y=42+cycle(seconds,.22)*7-7
        while(y<78) {
            c.drawRect(Rect(31.4f,y+1,32.6f,y+2.6f),fill(rgb(199,169,116)))
            c.drawRect(Rect(31.4f,y+2.6f,32.6f,y+3.5f),fill(rgb(226,205,167)))
            y+=3.5f
        }
        c.restore()
        val impact=bezier(cycle(seconds,.18),0f,0f,.58f,1f)
        c.save(); c.translate(31.9f,42+length-.5f+mix(-1f,1f,impact))
        val scale=mix(.7f,1.3f,impact); c.scale(scale,scale)
        listOf(Triple(Offset(-1.5f,.6f),.8f,rgb(197,163,107)),Triple(Offset(1.1f,-.3f),.7f,rgb(221,195,149))).forEach { (point,r,color) ->
            val opaque=color.copy(alpha=.7f*(1-impact))
            c.drawCircle(point,r,Paint().apply { shader=RadialGradientShader(point,r,listOf(opaque,opaque,opaque.copy(alpha=0f)),listOf(0f,.65f,1f)) })
        }
        c.restore(); c.restore()
    }
}
