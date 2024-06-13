package ru.shvetsov.shoppinglist.utils

import android.icu.util.Calendar
import java.text.SimpleDateFormat

object TimeManager {
    fun getCurrentTime(): String {
        val format = SimpleDateFormat.getDateTimeInstance()
        return format.format(Calendar.getInstance().time)
    }
}