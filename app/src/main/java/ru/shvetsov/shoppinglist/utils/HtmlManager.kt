package ru.shvetsov.shoppinglist.utils

import android.os.Build
import android.text.Html
import android.text.Spanned

object HtmlManager {
    fun getTextFromHtml(text: String): Spanned {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            Html.fromHtml(text)
        } else {
            Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        }
    }

    fun textToHtml(text: Spanned): String {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            Html.toHtml(text)
        } else {
            Html.toHtml(text, Html.FROM_HTML_MODE_COMPACT)
        }
    }
}