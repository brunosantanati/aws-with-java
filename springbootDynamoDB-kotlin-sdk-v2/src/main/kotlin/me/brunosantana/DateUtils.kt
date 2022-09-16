package me.brunosantana

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class DateUtils {
    companion object {
        fun convertStringToZonedDateTime(dateTimeString: String): ZonedDateTime{
            val dtf: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
            return ZonedDateTime.parse(dateTimeString, dtf)
        }

        fun isIncomingDateNewer(incomingDate: ZonedDateTime, existingDate: ZonedDateTime): Boolean{
            return incomingDate.isAfter(existingDate)
        }
    }
}