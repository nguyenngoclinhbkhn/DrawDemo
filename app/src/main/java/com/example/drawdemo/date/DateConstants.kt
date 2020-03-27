package jp.co.studioalice.pocketalice.family.common.date


class DateConstants {
    companion object {
        /** 画面表示のデフォルト日付フォーマットパターン */
        const val DATE_DEFAULT_FORMAT_PATTERN = "yyyy年MM月dd日"

        /** スラッシュ区切り　空白埋めなし */
        const val DATE_SLASH_FORMAT_PATTERN_NOT_FILL = "yyyy/M/d"

        /** スラッシュ区切り　空白埋めなし */
        const val DATE_SLASH_FORMAT_NO_YEAR_PATTERN_NOT_FILL = "M/d"

        /** YYYY/MM/DD */
        const val DATE_FORMAT_PATTERN = "yyyy/MM/dd"

        /** 画面表示のデフォルト日時フォーマットパターン */
        const val DATE_TIME_DEFAULT_FORMAT_PATTERN = "yyyy年MM月dd日 HH時mm分ss秒"

        /** 画面表示のドット区切り日付フォーマットパターン */
        const val DATE_DOT_FORMAT_PATTERN = "yyyy.MM.dd"
        /** 画面表示のドット区切り日付フォーマットパターン + 時刻*/
        const val DATE_TIME_DOT_FORMAT_PATTERN = "yyyy.MM.dd\nHH:mm:ss"

        const val DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss"
        const val DATE_FORMAT_YMDHMS = "yyyyMMddHHmmss"
        const val DATE_FORMAT_YMD = "yyyyMMdd"
        const val DATE_FORMAT_MD = "MMdd"

        const val DATE_FORMAT_D = "d"
        const val DATE_FORMAT_MM = "MM"
        const val DATE_FORMAT_M = "M"
        const val DATE_FORMAT_MY = "MMyyyy"
        const val DATE_FORMAT_MY_2 = "Myyyy"
        const val DATE_FORMAT_Y = "yyyy"

        /** APIから返ってくる日付文字列のフォーマットパターン */
        const val ALICE_API_DATE_FORMAT_PATTERN = DATE_FORMAT_YMD
        /** APIから返ってくる日付文字列のフォーマットパターン + 時刻 */
        const val ALICE_API_DATE_TIME_FORMAT_PATTERN = "yyyyMMddHHmmss"
        /** APIから返ってくる日付文字列のフォーマットパターン + 時刻 */
        const val ALICE_API_DATE_TIME_FORMAT_MS_PATTERN = "yyyyMMddHHmmssSSS"

        /** 一日のミリ秒 */
        const val DAY_MILLISECONDS = (1000 * 60 * 60 * 24)


        const val TIME_OF_NOON = "000000"
        const val DATE_ALLOW_DAY_OF_MONTH = "2/29"
    }
}