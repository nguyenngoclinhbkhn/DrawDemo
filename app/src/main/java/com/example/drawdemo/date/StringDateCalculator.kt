package jp.co.studioalice.pocketalice.family.common.date

import java.text.SimpleDateFormat
import java.util.Date

/**
 * 日付文字列の計算クラス
 */
class StringDateCalculator(private val formatPattern: String) {
    private val sdf: SimpleDateFormat = DateUtil.getSimpleDateFormatInJapan(formatPattern)

    /**
     * 現在日付を文字で取得する
     * @return 文字日付
     */
    fun getTimeNowString(): String {
        return sdf.format(Date())
    }

    /**
     * 現在日付との差分の日数を取得する
     * @param yyyyMMddHHmmss 文字日付
     * @return 日数
     */
    fun diffDaysFromNow(to: String): Int? {
        return this.diffDays(to, sdf.format(Date()))
    }

    /**
     * 差分の日数を取得する
     * @param from ここから
     * @param to ここまで
     * @return 日数
     */
    fun diffDays(from: String, to: String): Int {
        val toDate = sdf.parse(to)
        val fromDate = sdf.parse(from)
        return DateUtil.diffDays(fromDate, toDate)
    }

    /**
     * 規定日数の範囲内かどうか判定する
     * @param targetDate 文字日付
     * @param days 規定日数
     * @return true / false
     */
    fun isInRangeOfDays(targetDate: String, days: Int): Boolean {
        return DateUtil.isInRangeOfDays(sdf.parse(targetDate), days)
    }

    /**
     * 対象の日が期間内日付であるか
     * @param targetDate 対象日
     * @param fromDate 開始日
     * @param toDate 終了日
     * @return true / false
     */
    fun isInRangeDay(targetDate: String, fromDate: String, toDate: String): Boolean {
        return DateUtil.isInRangeDay(sdf.parse(targetDate), sdf.parse(fromDate), sdf.parse(toDate))
    }

    /**
     * 現在が期間内日付であるか
     * @param targetDate 対象日
     * @param fromDate 開始日
     * @param toDate 終了日
     * @return true / false
     */
    fun isInRangeDayNow(fromDate: String, toDate: String): Boolean {
        return DateUtil.isInRangeDay(Date(), sdf.parse(fromDate), sdf.parse(toDate))
    }

    /**
     * 指定日より前かどうか判定する
     * @param targetDate 文字日付
     * @return true / false
     */
    fun isBeforeToday(targetDate: String): Boolean {
        return DateUtil.isBeforeToday(sdf.parse(targetDate))
    }

    /**
     * 指定日が当日かどうか
     * @param targetDate 文字日付
     * @return true / false
     */
    fun isToday(targetDate: String): Boolean {
        return DateUtil.isToday(sdf.parse(targetDate))
    }


    /**
     * 日付が年内かどうか
     * @param yyyyMMddHHmmss 文字日付
     * @return true / false
     */
    fun isThisYear(targetDate: String): Boolean {
        val target = sdf.parse(targetDate)
        return DateUtil.isThisYear(target)
    }

}