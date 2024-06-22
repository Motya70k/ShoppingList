package ru.shvetsov.shoppinglist.utils

import android.content.SharedPreferences
import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

object TimeManager {
    private const val DEFAULT_TIME_FORMAT = "hh:mm:ss - yyyy/MM/dd"

    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    fun getTimeFormat(time: String, defaultPreference: SharedPreferences): String {
        val defaultFormatter = SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.getDefault())
        val defaultDate = defaultFormatter.parse(time)
        val newFormat = defaultPreference.getString("time_format_key", DEFAULT_TIME_FORMAT)
        val newFormatter = SimpleDateFormat(newFormat, Locale.getDefault())
        return if (defaultDate != null) {
            newFormatter.format(defaultDate)
        } else {
            time
        }
    }
}