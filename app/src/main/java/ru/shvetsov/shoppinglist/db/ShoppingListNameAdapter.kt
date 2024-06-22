package ru.shvetsov.shoppinglist.db

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.shvetsov.shoppinglist.R
import ru.shvetsov.shoppinglist.databinding.ListNameItemBinding
import ru.shvetsov.shoppinglist.entities.ShoppingListItem
import ru.shvetsov.shoppinglist.entities.ShoppingListName

class ShoppingListNameAdapter(private val listener: Listener) : ListAdapter<ShoppingListName, ShoppingListNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ListNameItemBinding.bind(view)
        fun setData(listName: ShoppingListName, listener: Listener) = with(binding) {
            tvListName.text = listName.name
            tvTime.text = listName.time
            val counterText = "${listName.checkedItemsCounter}/${listName.allItemsCounter}"
            tvCounter.text = counterText
            progressBar.max = listName.allItemsCounter
            progressBar.progress = listName.checkedItemsCounter
            val colorState = ColorStateList.valueOf(getProgressColorState(listName, binding.root.context))
            progressBar.progressTintList = colorState
            itemsCounter.backgroundTintList = colorState
            itemView.setOnClickListener {
                listener.onClickItem(listName)
            }
            ibDelete.setOnClickListener {
                listener.deleteItem(listName.id!!)
            }
            ibEdit.setOnClickListener {
                listener.editItem(listName)
            }
        }

        private fun getProgressColorState(item: ShoppingListName, context: Context): Int {
            return if(item.checkedItemsCounter == item.allItemsCounter) {
                ContextCompat.getColor(context, R.color.picker_green)
            } else
                ContextCompat.getColor(context, R.color.picker_red)
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_name_item, parent, false)
                )
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<ShoppingListName>() {
        override fun areItemsTheSame(oldItem: ShoppingListName, newItem: ShoppingListName): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingListName, newItem: ShoppingListName): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun deleteItem(id: Int)
        fun editItem(listName: ShoppingListName)
        fun onClickItem(listName: ShoppingListName)
    }
}