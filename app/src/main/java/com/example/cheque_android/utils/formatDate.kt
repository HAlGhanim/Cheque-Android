package com.example.cheque_android.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField

fun formatDate(isoDate: String): String {
    return try {
        val formatter = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .optionalStart()
            .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
            .optionalEnd()
            .toFormatter()

        val outputFormat = DateTimeFormatter.ofPattern("MMMM d, yyyy, h:mm a")

        val dateTime = LocalDateTime.parse(isoDate, formatter)
        dateTime.format(outputFormat)
    } catch (e: DateTimeParseException) {
        isoDate
    }
}

