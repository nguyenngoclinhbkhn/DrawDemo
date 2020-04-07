package com.example.drawdemo.custom

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import com.example.drawdemo.R
import com.example.drawdemo.model.LineType
import com.example.drawdemo.model.PointCalendar
import kotlin.math.tan

class ImageLineView: ImageView {
    private var paintTab: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var path: Path
    private var lineTypeTop: LineType ?= null
    private var DISTANCE = 2
    private var x: Float? = null
    private var y: Float? = null
    private lateinit var bitmap: Bitmap
    private val ANGLE = 30
    private val startY = 100f
    private var DISTANCE_DISTANCE = 140
    private var canvas: Canvas? = null
    private lateinit var list: ArrayList<PointCalendar>
    private lateinit var bottomRightList: ArrayList<PointCalendar>



    constructor(context: Context) : this(context, null) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
//        View.inflate(context, R.layout.cell_top_view,this)
        paintTab.color = Color.BLUE
        paintTab.strokeWidth = 30f
        paintTab.style = Paint.Style.STROKE
        paintTab.isDither = true // set the dither to true
        paintTab.strokeJoin = Paint.Join.ROUND // set the join to round you want
        paintTab.strokeCap = Paint.Cap.ROUND // set the paint cap to round too
        paintTab.pathEffect = CornerPathEffect(20f)
        list = ArrayList<PointCalendar>()
        bottomRightList = ArrayList<PointCalendar>()
        createListPoint()
        DISTANCE_DISTANCE = convertDpToPixel(140f, context)
        DISTANCE = convertDpToPixel(4f, context)
    }

    fun setLineType(lineType: LineType){
        lineTypeTop = lineType
        invalidate()

    }

    fun setStrokeLine(width : Float){
        paintTab.strokeWidth = width
        invalidate()
    }
    fun setColor(color: Int){
        paintTab.setColor(color)
        invalidate()
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawPath(createLinePath(), paintTab)

    }
    private fun createLinePath(): Path{
        val path = Path()
        when(lineTypeTop){
            LineType.DOWN_FROM_LEFT -> {
                path.moveTo(0f, (height / 2).toFloat())
                path.lineTo(convertDpToPixel(30f, context).toFloat(), (height / 2).toFloat())
                for (i in bottomRightList.indices) {
                    path.lineTo(
                        1 * (convertDpToPixel(30f, context).toFloat() + bottomRightList[i].x),
                        (height / 2 + bottomRightList[i].y).toFloat()
                    )
                }
                path.lineTo((width / 2).toFloat(), height.toFloat())
            }
            LineType.DOWN_TO_RIGHT -> {
                path.moveTo((width / 2).toFloat(), 0f)
                path.lineTo((width / 2).toFloat(), convertDpToPixel(28f, context).toFloat())
                //draw corner bottom left
                for (p in list) {
                    path.lineTo((width / 2).toFloat() + p.x, convertDpToPixel(30f, context) +  p.y)
                }
                path.lineTo(width.toFloat(), (height / 2).toFloat())
            }
            LineType.HORIZONTAL -> {
                path.moveTo(0f, (height / 2).toFloat())
                path.lineTo(width.toFloat(), (height / 2).toFloat())
            }
            LineType.UP_FROM_LEFT -> {
                path.moveTo(0f, (height / 2).toFloat())
                path.lineTo(convertDpToPixel(30f, context).toFloat(), (height / 2).toFloat())
                for (i in bottomRightList.indices) {
                    path.lineTo(
                        1 * (convertDpToPixel(30f, context) + bottomRightList[i].x),
                        1 * (height / 2 - bottomRightList[i].y)
                    )
                }
                path.lineTo((width / 2).toFloat(), 0f)
            }
            LineType.UP_TO_RIGHT -> {
                path.moveTo((width / 2).toFloat(), height.toFloat())
                path.lineTo((width / 2).toFloat(), (height - convertDpToPixel(30f, context).toFloat()))
                for (i in list.indices) {
                    path.lineTo(
                        ((width / 2).toFloat() + list[i].x),
                        (height - convertDpToPixel(30f, context).toFloat()) - list.get(i).y
                    )
                }
                path.lineTo(width.toFloat(), (height / 2).toFloat())
            }
            LineType.VERTICAL -> {
                path.moveTo((width / 2).toFloat(), 0f)
                path.lineTo((width / 2).toFloat(), height.toFloat())
            }
        }
        return path

    }

    private fun createListPoint() {
        var countBottom = 1
        for (i in 10 downTo 1) { //tính toán các điểm với toạ độ x từ bé đến lớn ở góc dưới bên trái, và toạ độ y từ bé đến lớn
            val v: Double =
                DISTANCE * countBottom * tan(Math.PI * (ANGLE / i) / 180)
            list.add(
                PointCalendar(
                    v.toFloat(),
                    DISTANCE * countBottom.toFloat()
                )
            )
            //            tính toá các điểm với toạ độ x từ bé đến lớn ở góc dưới bên phải, và toạ độ y từ bé đến lớn
            bottomRightList.add(
                PointCalendar(
                    DISTANCE * countBottom.toFloat(),
                    v.toFloat()
                )
            )
            countBottom++
        }
    }

    private fun convertDpToPixel(dp: Float, context: Context): Int {
        val resources: Resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px.toInt()
    }

}