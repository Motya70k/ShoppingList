package ru.shvetsov.shoppinglist.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import ru.shvetsov.shoppinglist.databinding.EditListItemDialogBinding
import ru.shvetsov.shoppinglist.entities.ShoppingListItem

object EditListItemDialog {
    fun showDialog(context: Context, item: ShoppingListItem, listener: Listener) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = EditListItemDialogBinding.inflate(LayoutInflater.from(context))

        builder.setView(binding.root)
        binding.apply {
            edName.setText(item.name)
            edInfo.setText(item.itemInfo)
            if (item.itemType == 1) edInfo.visibility = View.GONE
            buttonUpdate.setOnClickListener {
                if (edName.text.toString().isNotEmpty()) {
                    val updatedItem = item.copy(
                        name = edName.text.toString(),
                        itemInfo = edInfo.text.toString()
                    )
                    listener.onClick(updatedItem)
                }
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick(item: ShoppingListItem)
    }
}