package com.linhphan.data.extensions

import com.google.gson.Gson
import com.linhphan.data.entity.TempResponse
import java.text.SimpleDateFormat
import java.util.*

private const val CELSIUS_SYMBOL = "\u2103"
private const val DATE_FORMAT = "EEE, dd MMM yyyy"

fun Long.toStringDate(): String {
    val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    val date = Date(this)
    return simpleDateFormat.format(date)
}

fun Int.toPercent(): String {
    return "$this%"
}

fun TempResponse.getAvgTemp(): String {
    return "${String.format("%.1f", (max + min) * 0.5f)} $CELSIUS_SYMBOL"
}

fun Any.toJson(): String {
    return Gson().toJson(this)
}