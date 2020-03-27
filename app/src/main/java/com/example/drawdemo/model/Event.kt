package com.example.drawdemo.model

import jp.co.studioalice.pocketalice.family.common.date.DateUtil
import java.util.Date

data class Event(
    var id: Int,
    var nameevent: String,
    var date: String,
    var icon: String,
    var isLastEvent : Boolean = false,
    var isNextEvent : Boolean = false
) {
    private var mConvertedDate: Date? = null
    private var mYear: String? = null
    private var mMonth: String? = null
    private var mDay: String? = null
    private var mMonthYear : String? = null

    fun getDateObj(): Date {
        if (mConvertedDate == null) {
            mConvertedDate = DateUtil.convertYYYYMMDD(date)
        }
        return mConvertedDate as Date
    }

    fun getDay(): String {
        if (mDay == null) mDay = DateUtil.convertDay(getDateObj())
        return mDay as String
    }

    fun getMonth(): String {
        if (mMonth == null) mMonth = DateUtil.convertMM(getDateObj())
        return mMonth as String
    }

    fun getYear(): String {
        if (mYear == null) mYear = DateUtil.convertYYYY(getDateObj())
        return mYear as String
    }

    fun getMonthYear(): String {
        if (mMonthYear == null) mMonthYear = DateUtil.convertMMyyyy_2(getDateObj())
        return mMonthYear as String
    }

}