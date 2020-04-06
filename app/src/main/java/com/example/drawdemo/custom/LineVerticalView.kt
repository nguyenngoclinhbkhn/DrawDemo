package com.example.drawdemo.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class LineVerticalView: View {
    private var paintTab: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var path: Path
    constructor(context: Context) : this(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        paintTab.color = Color.RED
        paintTab.strokeWidth = 25f
        paintTab.style = Paint.Style.STROKE
        paintTab.isDither = true // set the dither to true
        paintTab.strokeJoin = Paint.Join.ROUND // set the join to round you want
        paintTab.strokeCap = Paint.Cap.ROUND // set the paint cap to round too
        paintTab.pathEffect = CornerPathEffect(20f)
        path = Path()
//        bitmapMonth = Bitmap.createScaledBitmap(
//            BitmapFactory.decodeResource(resources, R.drawable.month),
//            widthBitmap, heightBitmap, true );
////        canvas = Canvas()
//        x = 50f
//        y = 400f
//        list = ArrayList<PointCalendar>()
//        bottomRightList = ArrayList<PointCalendar>()
//        createListPoint()
    }

}