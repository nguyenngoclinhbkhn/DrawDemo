package com.example.drawdemo.model

import jp.co.studioalice.pocketalice.family.common.date.DateUtil

object EventHelper {

    fun findLastAndNextEvent(events : MutableList<Event>) {
        val todayDate = DateUtil.getCalendarInstance().time
//        for ((idx, e) in events.withIndex()) {
//            Log.d("DRAW", "Today      -: $todayDate")
//            Log.d("DRAW", "Event day  -: ${e.getDateObj()}")
//            Log.d("DRAW", "==================\n")
//        }
    }

    fun parseEventsByMonths(events: MutableList<Event>): MutableMap<String, MutableList<Event>> {
        val hmEventByMonth: MutableMap<String, MutableList<Event>> = mutableMapOf()

        // more years
        val first = events.first()
        val last = events.last()

        val startYear = first.getYear().toInt()
        val endYear = last.getYear().toInt()
        val initHmKeys : MutableList<String> = mutableListOf()
        // create all months - all year
        for (y in startYear..endYear) {
            for (m in 1..12) {
                initHmKeys.add("$m$y")
            }
        }
        for (k in initHmKeys) {
            hmEventByMonth[k] = mutableListOf()
        }
        events.map {
            val monthName = it.getMonthYear()
            if (hmEventByMonth.containsKey(monthName)) {
                val subList = hmEventByMonth[monthName]
                subList?.add(it)
            } else {
                val nSublist: MutableList<Event> = mutableListOf()
                nSublist.add(it)
                hmEventByMonth.put(monthName, nSublist)
            }
        }
        return hmEventByMonth
    }

    var cellNoReverse : MutableList<EventCell> = mutableListOf()

    fun mappingCellType(eventByMonths: MutableMap<String, MutableList<Event>>): MutableMap<String, MutableList<EventCell>> {
        val output: MutableMap<String, MutableList<EventCell>> = mutableMapOf()
        cellNoReverse = mutableListOf()

        val keys = eventByMonths.keys.toList()
        for ((kIndex, key) in keys.withIndex()) {
            val isNeedReverse = ((kIndex + 1) % 2) == 0
            // isNeedReverse -> là cột có index chẵn, bỏ qua cột đầu tiên (vd: 2,4,6,8, 10, 12).

            val events = eventByMonths[key]
            val eCells: MutableList<EventCell> = mutableListOf()
            events?.let {
                if (isNeedReverse) {
                    // line cần vẽ của cột chẵn đi từ dưới lên, code vẽ từ trên xuống nên cần reverse dữ liệu.
                    events.reverse()
                }
                // loop and mapping line type
                for ((eIndex, event) in events.withIndex()) {
                    val line = when (eIndex) {
                        0 -> {
                            if (events.size == 0) LineType.HORIZONTAL
                            else if (isNeedReverse) LineType.UP_TO_RIGHT
                            else {
                                if (kIndex == 0) { // first event of january
                                    if (events.size == 1) LineType.HORIZONTAL else LineType.DOWN_FROM_LEFT
                                } else {
                                    LineType.DOWN_FROM_LEFT
                                }
                            }
                        }
                        events.size - 1 -> {
                            if (isNeedReverse)
                                LineType.UP_FROM_LEFT
                            else
                                LineType.DOWN_TO_RIGHT
                        }
                        else -> LineType.VERTICAL
                    }

                    val eCell = EventCell(event, line)
                    // Log.e("LINE", " ${event.date}   -   ${eCell.line}")
                    eCells.add(eCell)
                }

                // add/init #LineType.HORIZONTAL for month no event.
                if (eCells.isEmpty()) {
                    val fCell = EventCell(null, LineType.HORIZONTAL)
                    eCells.add(fCell)
                }
                // check to add fake cells
                if (isNeedReverse) {
                    val previousColumnEvents = eventByMonths[keys[kIndex - 1]]
                    val diffCount = previousColumnEvents?.size!! - eCells.size
                    if (diffCount == 1) {
                        eCells.last().line = LineType.UP_TO_RIGHT
                        val fCell = EventCell(null, LineType.UP_FROM_LEFT)
                        eCells.add(fCell)
                    }
                    if (diffCount > 1) {
                        // copy last column line
                        var tmpLine = eCells.last().line
                        if (eCells.size == 1) {
                            eCells.last().line = LineType.UP_TO_RIGHT
                            tmpLine = LineType.UP_FROM_LEFT
                        } else {
                            // change it to Vertical line
                            eCells.last().line = LineType.VERTICAL
                        }

                        // add fake cell
                        for (i in 1..diffCount) {
                            val line = if (i < diffCount) LineType.VERTICAL else tmpLine
                            val fCell = EventCell(null, line)
                            eCells.add(fCell)
                        }
                    }
                } else {
                    if (/*kIndex > 0 && */kIndex < keys.size - 1) { // bỏ qua tháng 1 (cột đầu tiên) và bỏ qua cột cuối.
                        val nextColumnEvents = eventByMonths[keys[kIndex + 1]]
                        val diffCount = nextColumnEvents?.size!! - eCells.size

                        if (diffCount == 1) {
                            eCells.last().line = LineType.DOWN_FROM_LEFT
                            // them cell đường đi ngang va len tren
                            val fCell = EventCell(null, LineType.DOWN_TO_RIGHT)
                            eCells.add(fCell)
                        }
                        if (diffCount > 1) {

                            // copy last column line
                            var tmpLine = eCells.last().line
                            if (eCells.size == 1) {
                                eCells.last().line = LineType.DOWN_FROM_LEFT
                                tmpLine = LineType.DOWN_TO_RIGHT
                            } else {
                                // change it to Vertical line
                                eCells.last().line = LineType.VERTICAL
                            }

                            // add fake cell
                            for (i in 1..diffCount) {
                                val line =
                                    if (i < diffCount) LineType.VERTICAL else tmpLine
                                // vẽ các line dọc cho đến cell cuối cùng thì là line xuống dưới và đi ngang.
                                val fCell = EventCell(null, line)
                                eCells.add(fCell)
                            }
                        }
                    }
                }
                output.put(key, eCells)
            }
        }
        return output
    }
}