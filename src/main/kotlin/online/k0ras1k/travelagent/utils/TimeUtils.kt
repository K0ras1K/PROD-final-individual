package online.k0ras1k.travelagent.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object TimeUtils {
    fun convertLongToISO8601(time: Long): String {
        val instant = Instant.ofEpochMilli(time)
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(instant.atOffset(ZoneOffset.UTC))
    }

    fun toTimeString(time: Long): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        val date = java.util.Date(time)
        return sdf.format(date)
    }

    fun toMillis(timeString: String): Long {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        val date = sdf.parse(timeString)
        return date?.time ?: -1
    }

    fun toTicketString(time: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val date = java.util.Date(time)
        return sdf.format(date)
    }
}
