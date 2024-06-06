package ru.shvetsov.shoppinglist.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.shvetsov.shoppinglist.R
import ru.shvetsov.shoppinglist.databinding.NoteListItemBinding
import ru.shvetsov.shoppinglist.entities.NoteItem
import ru.shvetsov.shoppinglist.utils.HtmlManager

class NoteAdapter(private val listener: Listener) : ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = NoteListItemBinding.bind(view)
        fun setData(note: NoteItem, listener: Listener) = with(binding) {
            tvTitle.text = note.title
            tvDescription.text = HtmlManager.getTextFromHtml(note.content).trim()
            tvTime.text = note.time
            itemView.setOnClickListener {
                listener.updateNote(note)
            }
            imDelete.setOnClickListener {
                listener.deleteNote(note.id!!)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.note_list_item, parent, false)
                )
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<NoteItem>() {
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun deleteNote(id: Int)
        fun updateNote(note: NoteItem)
    }
}