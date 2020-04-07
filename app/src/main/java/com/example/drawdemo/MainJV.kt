package com.example.drawdemo

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.drawdemo.custom.ImageLineView
import com.example.drawdemo.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.otaliastudios.zoom.ZoomApi
import com.otaliastudios.zoom.ZoomLayout
import kotlinx.android.synthetic.main.activity_main_2.*
import kotlinx.android.synthetic.main.character_animate_view.view.*


class MainJV : AppCompatActivity(), View.OnClickListener {

    lateinit var rootFrame: FrameLayout
    lateinit var zoomLayout: ZoomLayout
    lateinit var handler: Handler
    private var startY = 0.0f
    private var startX = 0.0f
    private var startColumnIndex = 0

    private var movePoints: MutableList<MoveItem> = mutableListOf()
    private var isNeedGetMovingPoint = false

    private val arrayColor = arrayListOf<Int>(
        Color.RED,
        Color.BLACK, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GREEN,
        Color.LTGRAY, Color.MAGENTA, Color.YELLOW
    )

    private var cellStep = 0.0f
    private var headerHeight = 0.0f
    private var initZoom = 0.0f
    private lateinit var startEvent: Event
    private lateinit var events: List<Event>
    private lateinit var arrayList: ArrayList<ImageLineView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_2)

        cellStep = resources.getDimension(R.dimen.cell_size)
        headerHeight = resources.getDimension(R.dimen.header_row_height)

        rootFrame = findViewById(R.id.rootFrame)
        zoomLayout = findViewById(R.id.zoom_layout)

        val js = assets.open("json_events").bufferedReader().use { it.readText() }

        events = Gson().fromJson<List<Event>>(
            js,
            object :
                TypeToken<List<Event?>?>() {}.type
        )

        sortEventIncrease(eventList = events)

        // fake last event
        events[20].isLastEvent = true
        events[21].isNextEvent = true

        for (test in events) {
            Log.e("TAG", "Date ${test.date}")
        }

        handler = Handler()
        EventHelper.findLastAndNextEvent(events = events as MutableList<Event>)

        Log.e("LINE", "Start")
        val eventByMonths =
            EventHelper.parseEventsByMonths(events = events as MutableList<Event>)
        Log.d("TAG", "check: " + eventByMonths.size)

        // get scale value
        val firstEvent = events.first()
        val lastEvent = events.last()
        val startYear = firstEvent.getYear().toInt()
        val endYear = lastEvent.getYear().toInt()


        arrayList = ArrayList()
        var minZoom = Constants.MIN_ZOOM
        var maxZoom = Constants.MAX_ZOOM
        if (endYear > startYear) {
            var delta = (endYear - startYear) + 1
            minZoom = delta * Constants.MIN_ZOOM
            maxZoom = delta * Constants.MAX_ZOOM
        }
        initZoom = (minZoom + maxZoom) / 2
        zoomLayout.setMinZoom(minZoom, ZoomApi.TYPE_ZOOM)
        zoomLayout.setMaxZoom(maxZoom, ZoomApi.TYPE_ZOOM)

        val eventCells = EventHelper.mappingCellType(eventByMonths)
        Log.e("LINE", "Start.draw")

        drawMap(eventCells)
        drawAnimation(eventCells)

        Log.e("LINE", "END: prepared.")

        btnChageColor.setOnClickListener(this)
        btnDecreaseSize.setOnClickListener(this)
        btnIncreaseSize.setOnClickListener(this)
        btnLef.setOnClickListener(this)
    }

    private fun sortEventIncrease(eventList: List<Event>): List<Event> {
        for (i in 0 until eventList.size - 1) {
            var minArr = i
            for (j in i + 1 until eventList.size) {
                val arrayDateMin = eventList[minArr].date.split("/")
                val arrayDateCompare = eventList[j].date.split("/")
                val yearMin = arrayDateMin[2].toInt()
                val monthMin = arrayDateMin[1].toInt()
                val dayMin = arrayDateMin[0].toInt()
                val yearCompare = arrayDateCompare[2].toInt()
                val monthCompare = arrayDateCompare[1].toInt()
                val dayCompare = arrayDateCompare[0].toInt()
                if (yearMin > yearCompare) {
                    minArr = j
                } else if (yearMin == yearCompare) {
                    if (monthMin > monthCompare) {
                        minArr = j
                    } else if (monthMin == monthCompare) {
                        if (dayMin > dayCompare) {
                            minArr = j
                        }
                    }
                }
            }
            if (i != minArr) swap(eventList, minArr, i)
        }

        Log.e("TAG", "Sort ok")

        return eventList
    }

    // sau nay se dua vao background thread, hoac se chia nhieu luong de sap xep
    private fun swap(eventList: List<Event>, a: Int, b: Int) {
        val id = eventList[a].id
        val date = eventList[a].date
        val icon = eventList[a].icon
        val name = eventList[a].nameevent
        val lastEvent = eventList[a].isLastEvent
        val nextEvent = eventList[a].isNextEvent

        eventList[a].id = eventList[b].id
        eventList[a].date = eventList[b].date
        eventList[a].icon = eventList[b].icon
        eventList[a].nameevent = eventList[b].nameevent
        eventList[a].isLastEvent = eventList[b].isLastEvent
        eventList[a].isNextEvent = eventList[b].isNextEvent

        eventList[b].id = id
        eventList[b].date = date
        eventList[b].icon = icon
        eventList[b].nameevent = name
        eventList[b].isLastEvent = lastEvent
        eventList[b].isNextEvent = nextEvent
    }

    private fun moveView(lastEvent: Event) {
        val arrayDate = events[0].date.split("/")
        val monthFirst = arrayDate[1].toInt()
        val yearFirst = arrayDate[2].toInt()
        val monthLast = lastEvent.date.split("/")[1].toInt()
        val yearLast = lastEvent.date.split("/")[2].toInt()
        val numberMonth = (yearLast - yearFirst) * 2 - (12 - monthFirst) - monthLast - 1
        val postition = (numberMonth * cellStep)
        Log.e("TAG", "postition need $postition")
        zoomLayout.moveTo(3f, postition, 0f, true)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            //            zoomLayout.moveTo(3f, 9240.0f, 0f, true)
            moveView(events[21])
//            zoomLayout.moveTo(3f, -9240f, 0f, true)
            Log.e("TAG", "ve ban dau")
        }, 1000)



    }


    private val runnable: Runnable = object : Runnable {
        override fun run() {
            val color = (Math.random() * arrayColor.size.toInt() - 1).toInt()
            for (test in arrayList) {
                test.setColor(arrayColor[color])
            }
            Log.e("TAG", "run")
            handler.postDelayed(this, 500)

        }
    }

    private fun drawMap(hmEventCells: MutableMap<String, MutableList<EventCell>>) {
        hmEventCells.let {
            val keys = hmEventCells.keys
//            rootFrame.removeAllViews()
            val inflater = LayoutInflater.from(this)
            val hrzContainer =
                inflater.inflate(R.layout.horizontal_column_container, null, false) as LinearLayout

            for ((columnIndex, key) in keys.withIndex()) {
                val events = hmEventCells[key]
                // create column container
                val columnContainer =
                    inflater.inflate(R.layout.column_container, null, false) as LinearLayout

                events?.let {
                    // loop and mapping line type
                    for ((eIndex, cell) in events.withIndex()) {
                        val cellView = if (eIndex == 0) {
                            inflater.inflate(R.layout.cell_top_view, columnContainer, false)
                        } else {
                            inflater.inflate(R.layout.cell_normal_view, columnContainer, false)
                        }
                        if (eIndex == 0) { // cell top of column
                            val tvColumnName = cellView.findViewById<TextView>(R.id.tv_month)
                            val tvYear = cellView.findViewById<TextView>(R.id.tv_year)
                            val m = key.substring(0, key.length - 4)
                            val y = key.substring(key.length - 4, key.length)
                            tvColumnName.text = m
                            tvYear.text = y
                        }

                        val img = cellView.findViewById<ImageLineView>(R.id.img_line)
                        img.setLineType(cell.line)
                        arrayList.add(img)
//                        Picasso.get().load(cell.line.imgRes).into(img)

                        val tvDate = cellView.findViewById<TextView>(R.id.tv_event)
                        if (cell.event != null) {
                            tvDate.visibility = View.VISIBLE
                            tvDate.text = cell.event.getDay()
                        } else {
                            tvDate.visibility = View.GONE
                        }
                        tvDate.text = cell.event?.getDay()
                        columnContainer.addView(cellView)

                    } // end for cells
                }
                hrzContainer.addView(columnContainer)
            } // end for columns

            rootFrame.addView(hrzContainer)
        }

//        zoomLayout.moveTo()
        // fun move view to current event: need to calculate x,y
//        zoomLayout.postDelayed({
//            val x = if (startColumnIndex < 2) 0f else ((startColumnIndex) * cellStep)
//            Log.d("RULE", "zoom to: x = $x")
////            zoomLayout.moveTo(initZoom, x, 888f, false)
//            zoomLayout.panTo(x, 8f, false)
//        }, 200)
    }

    private fun drawAnimation(hmEventCells: MutableMap<String, MutableList<EventCell>>) {
        hmEventCells.let {
            val keys = hmEventCells.keys

            for ((columnIndex, key) in keys.withIndex()) {
                val isNeedReverse = ((columnIndex + 1) % 2) == 0
                val cells = hmEventCells[key]

                cells?.let {
                    if (isNeedReverse) {
                        cells.reverse()
                    }
                    // loop and mapping line type
                    for ((eIndex, cell) in cells.withIndex()) {
                        // calculate animation.
                        if (cell.event?.isLastEvent == true) {
                            // reset
                            startX = 0.0f
                            // calculate animation start points (x,y)
                            startX = (columnIndex) * cellStep
                            // save start column
                            startColumnIndex = columnIndex
                            isNeedGetMovingPoint = true

                            // clear moving list
                            movePoints.clear()
                        }

                        if ((isNeedGetMovingPoint) || cell.event?.isNextEvent == true) {
                            // calculate animation start points (x,y)
                            if (startColumnIndex == columnIndex) { // cac lines cung cot start thi dich chuyen truc Y
                                val y = if (isNeedReverse) {
                                    headerHeight + ((cells.size - eIndex) - 1) * cellStep
                                } else {
                                    headerHeight + (eIndex) * cellStep
                                }
                                movePoints.add(MoveItem("y", y))

                            } else {
                                // 3 lines loai nay dich chuyen truc Y
                                if (cell.line == LineType.VERTICAL || cell.line == LineType.DOWN_TO_RIGHT || cell.line == LineType.UP_TO_RIGHT) {
                                    val y = if (isNeedReverse) {
                                        headerHeight + ((cells.size - eIndex) - 1) * cellStep
                                    } else {
                                        headerHeight + (eIndex) * cellStep
                                    }
                                    movePoints.add(MoveItem("y", y))
                                } else { // cac line con lai dich chuyen X
                                    val x = columnIndex * cellStep
                                    movePoints.add(MoveItem("x", x))
                                }
                            }
                            if (cell.event?.isNextEvent == true) {
                                isNeedGetMovingPoint = false
                            }
                        }
                    } // end for cells
                }
            } // end for columns
        }
        startY = movePoints[0].value
        animateCharacter()
    }

    private fun animateCharacter() {
        val aniView =
            LayoutInflater.from(this).inflate(R.layout.character_animate_view, rootFrame, false)
        rootFrame.addView(aniView)
        aniView.x = startX
        aniView.y = startY

        rootFrame.postDelayed({

            val animators: MutableList<ObjectAnimator> = mutableListOf()
            movePoints.removeAt(0)
            for (p in movePoints) {
                animators.add(getNextAnimator(aniView.layout_animate, p.xOrY, p.value))
            }
            AnimatorSet().apply {
                playSequentially(animators as List<Animator>?)
                start()
                moveView(events[21])
            }
            Log.d("RULE", "current : x = ${zoomLayout.panX}")
        }, 100)
    }

    private fun getNextAnimator(view: View, xOrY: String, point: Float): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, xOrY, point).setDuration(1600)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnChageColor -> {
                val color = (Math.random() * arrayColor.size.toInt() - 1).toInt()
                for (test in arrayList) {
                    test.setColor(arrayColor[color])
                }
            }
            R.id.btnIncreaseSize -> {

            }
            R.id.btnDecreaseSize -> {

            }
            R.id.btnLef -> {
                handler.postDelayed({
                    mRunnable.run()
                }, 500)
            }
        }
    }
    private var mRunnable = object: Runnable {
        override fun run() {
            val color = (Math.random() * arrayColor.size.toInt() - 1).toInt()
            for (test in arrayList) {
                test.setColor(arrayColor[color])
            }
            handler.postDelayed(this, 500)
        }

    }
}