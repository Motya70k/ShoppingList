package ru.shvetsov.shoppinglist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.shvetsov.shoppinglist.R
import ru.shvetsov.shoppinglist.databinding.ListNameItemBinding
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