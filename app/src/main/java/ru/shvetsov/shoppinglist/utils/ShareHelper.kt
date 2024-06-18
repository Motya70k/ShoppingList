package ru.shvetsov.shoppinglist.utils

import android.content.Intent
import ru.shvetsov.shoppinglist.entities.ShoppingListItem

object ShareHelper {
    fun shareShoppingList(shoppingList: List<ShoppingListItem>, listName: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareText(shoppingList, listName))
        }
        return intent
    }

    private fun makeShareText(shoppingList: List<ShoppingListItem>, listName: String): String {
        val stringBuilder = StringBuilder()
        var counter = 0
        stringBuilder.append("<<$listName>>")
        stringBuilder.append("\n")
        shoppingList.forEach {
            stringBuilder.append("${++counter} - ${it.name} (${it.itemInfo})")
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }
}