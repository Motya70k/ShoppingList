package ru.shvetsov.shoppinglist.dialogs

import android.content.Context
import android.view.LayoutInflater
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
            buttonUpdate.setOnClickListener {
                listener.onClick(
                    item.copy(
                        name = edName.text.toString(),
                        itemInfo = edInfo.text.toString()
                    )
                )
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