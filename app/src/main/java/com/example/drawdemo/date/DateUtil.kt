package jp.co.studioalice.pocketalice.family.common.date

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class DateUtil {
    object Extensions {

        /**
         * この日付が、指定された日付以降であるかどうかをチェックします。
         *
         * @param other
         * @return この日付が[other]以降だった場合true this >= other
         */
        fun Date.isAfterEqual(other: Date): Boolean {
            val sourceTime = time
            val destinationTime = other.time
            return sourceTime >= destinationTime
        }

        /**
         * この日付が、指定された日付より前にあるかどうかをチェックします。
         *
         * @param other
         * @return この日付が[other]より前だった場合true this < other
         */
        fun Date.isBefore(other: Date): Boolean {
            return !this.isAfterEqual(other)
        }
    }

    companion object {

        private const val HEISEI_START_YEAR = 1988

        /**
         * 日本のタイムゾーンを取得
         */
        private fun getJapanTimeZone(): TimeZone {
            return TimeZone.getTimeZone("Asia/Tokyo")
        }

        /**
         * Dateの取り得る最大の値を返す
         */
        fun maxDate(): Date {
            return Date(193412548799999)
        }

        /**
         * Dateの取り得る最小の値を返す
         */
        fun minDate(): Date {
            return Date(0)
        }

        /**
         * 日本時間のSimpleDateFormatを取得
         */
        fun getSimpleDateFormatInJapan(pattern: String): SimpleDateFormat {
            val sdf = SimpleDateFormat(pattern, Locale.JAPAN)
            sdf.timeZone = getJapanTimeZone()
            return sdf
        }

        /** 日本時間の年を取得 */
        fun getCurrentYear(): Int {
            return Calendar.getInstance(getJapanTimeZone()).get(Calendar.YEAR)
        }

        /* 西暦の年を和暦に変換する */
        fun getJapanYear(year: Int): Int {
            //  AndroidのCalendarでは和暦を扱えない
            return year - HEISEI_START_YEAR
        }

        fun japanYearToDefaultYear(year: Int) : Int {
            return year + HEISEI_START_YEAR
        }

        /** 日本時間のCalendarインタンスを取得 */
        fun getCalendarInstance(): Calendar {
            return Calendar.getInstance(getJapanTimeZone())
        }

        /**
         * 日付が年内かどうか
         * @param yyyyMMddHHmmss 文字日付
         * @return true / false
         */
        fun isThisYear(target: Date): Boolean {
            val calendarNow = DateUtil.getCalendarInstance()
            val calendar = DateUtil.getCalendarInstance()
            try {
                calendar.time = target
            } catch (e: ParseException) {
                return false
            }

            if (calendarNow.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                return true
            }
            return false
        }

        /**
         * 規定日数の範囲内かどうか判定する
         * @param yyyyMMddHHmmss 文字日付
         * @param days 規定日数
         * @return true / false
         */
        fun isInRangeOfDays(targetDate: Date, days: Int): Boolean {
            val diffDays = diffDaysFromNow(targetDate)

            if (diffDays <= 0 && days >= -diffDays) {
                return true
            }

            return false
        }

        /**
         * 対象の日が期間内日付であるか
         * @param targetDate 対象日
         * @param fromDate 開始日
         * @param toDate 終了日
         * @return true / false
         */
        fun isInRangeDay(targetDate: Date, fromDate: Date, toDate: Date): Boolean {
            val from = diffDays(targetDate, fromDate)
            val to = diffDays(targetDate, toDate)
            if (from >= 0 && to <= 0) {
                return true
            }
            return false
        }

        /**
         * 指定日が現在より前かどうか判定する
         * @param targetDate 文字日付
         * @return true / false
         */
        fun isBeforeToday(targetDate: Date): Boolean {
            val diffDays = diffDaysFromNow(targetDate)

            if (diffDays < 0) {
                return true
            }

            return false
        }

        /**
         * 指定日が現在より後かどうか判定する
         * @param targetDate 文字日付
         * @return true / false
         */
        fun isAfterToday(targetDate: Date): Boolean {
            val diffDays = diffDaysFromNow(targetDate)

            if (diffDays > 0) {
                return true
            }

            return false
        }

        /**
         * 現在日付との差分の日数を取得する
         * @param to 対象の日付
         * @return 日数
         */
        fun diffDaysFromNow(to: Date): Int {
            return diffDays(Date(), to)
        }

        /**
         * 差分の日数を取得する
         * @param from ここから
         * @param to ここまで
         * @return 日数
         */
        fun diffDays(from: Date, to: Date): Int {

            val calFrom = getCalendarInstance()
            calFrom.time = from
            val calTo = getCalendarInstance()
            calTo.time = to

            val calFromTruncateTime = calFrom.truncateTime()
            val calToTruncateTime = calTo.truncateTime()

            val fromTime = calFromTruncateTime.time.time
            val toTime = calToTruncateTime.time.time
            val doubleDiffDays = ((toTime - fromTime) / DateConstants.DAY_MILLISECONDS.toDouble())
            return Math.ceil(doubleDiffDays).toInt()
        }

        /**
         * 日時を比較する
         * @param source 比較元日時
         * @param destination 比較対象日時
         * @return 比較対象日時が比較元日時より後ならtrue
         */
        fun after(source: Date, destination: Date): Boolean {
            val calSource = getCalendarInstance()
            calSource.time = source
            val calDestination = getCalendarInstance()
            calDestination.time = destination

            val sourceTime = calSource.time.time
            val destinationTime = calDestination.time.time
            return destinationTime > sourceTime
        }

        /**
         * 現在の日付から与えられた日数分プラスされた日付を返す
         * @param from ここから
         * @param to ここまで
         * @return 日数
         */
        fun addDaysDateFromNow(add: Int): Date {

            val calFrom = getCalendarInstance()
            calFrom.time = Date()

            val calFromTruncateTime = calFrom.truncateTime()
            val fromTime = calFromTruncateTime.time.time
            calFromTruncateTime.add(Calendar.DAY_OF_MONTH, add);
            var addedDateDate = Date()
            addedDateDate = calFromTruncateTime.time
            return addedDateDate
        }


        /**
         * 時間以外切り捨て
         */
        private fun Calendar.truncateTime(): Calendar {
            this.set(Calendar.HOUR_OF_DAY, 0)
            this.set(Calendar.MINUTE, 0)
            this.set(Calendar.SECOND, 0)

            return this;
        }


        /**
         * 指定日が当日かどうか
         * @param targetDate 文字日付
         * @return true / false
         */
        fun isToday(targetDate: Date): Boolean {
            return diffDaysFromNow(targetDate) == 0
        }

        /**
         * 指定日付が不正であるか調べる
         * @param 日付文字列
         * @return true 日付が正しい false 日付が異常
         */
        fun isNotErrorDate(s: String) : Boolean {
            val fmt = getSimpleDateFormatInJapan(DateConstants.DATE_SLASH_FORMAT_PATTERN_NOT_FILL)
            fmt.isLenient = false
            return try {
                fmt.parse( s )
                true
            } catch (e: ParseException) {
                false
            }
        }

        /**
         * 日付形式yyyymmddの文字列をDate型に変換する
         */
        fun convertYYYYMMDD(date: String) : Date {
            return DateUtil.getSimpleDateFormatInJapan("dd/MM/yyyy").parse(date)
        }

        /**
         * 日付形式yyyymmdd:hhMMssの文字列をDate型に変換する
         */
        fun convertYYYYMMDD_hhmmss(date: String) : Date {
            return DateUtil.getSimpleDateFormatInJapan(DateConstants.ALICE_API_DATE_TIME_FORMAT_PATTERN).parse(date)
        }

        fun toDateString(date: Date, format: String) : String {
            return DateUtil.getSimpleDateFormatInJapan(format).format(date)
        }

        /**
         * 文字日付から日付を取得する
         * @param dateString 文字日付
         * @return 日付
         */
        fun toDateString(dateString: String, beforeFormat: String, afterFormat: String): String {
            val date = DateUtil.getSimpleDateFormatInJapan(beforeFormat).parse(dateString)
            return toDateString(date, afterFormat)
        }

        /**
         * Date型を日付形式yyyymmddの文字列に変換する
         */
        fun convertYYYYMMDD(date: Date): String {
            return DateUtil.getSimpleDateFormatInJapan(DateConstants.ALICE_API_DATE_FORMAT_PATTERN).format(date)
        }

        /**
         * 年月日が同じかどうか
         * @param target 日付
         * @return true / false
         */
        fun isNotSameDay(target: Date): Boolean {
            val calendarNow = DateUtil.getCalendarInstance()
            val calendar = DateUtil.getCalendarInstance()
            try {
                calendar.time = target
            } catch (e: ParseException) {
                return true
            }

            val isSameMonth = calendarNow.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
            val isSameDate = calendarNow.get(Calendar.DATE) == calendar.get(Calendar.DATE)
            if (isThisYear(target) && isSameMonth && isSameDate) {
                return false
            }
            return true
        }

        /**
         * Date型を日付形式mmyyyyの文字列に変換する
         */
        fun convertMMyyyy(date: Date): String {
            return DateUtil.getSimpleDateFormatInJapan(DateConstants.DATE_FORMAT_MY).format(date)
        }
        fun convertMMyyyy_2(date: Date): String {
            return DateUtil.getSimpleDateFormatInJapan(DateConstants.DATE_FORMAT_MY_2).format(date)
        }

        /**
         * Date型を日付形式yyyyの文字列に変換する
         */
        fun convertYYYY(date: Date): String {
            return DateUtil.getSimpleDateFormatInJapan(DateConstants.DATE_FORMAT_Y).format(date)
        }

        fun convertMM(date: Date): String {
            return DateUtil.getSimpleDateFormatInJapan(DateConstants.DATE_FORMAT_M).format(date)
        }

        fun convertDay(date: Date): String {
            return DateUtil.getSimpleDateFormatInJapan(DateConstants.DATE_FORMAT_D).format(date)
        }
    }
}
