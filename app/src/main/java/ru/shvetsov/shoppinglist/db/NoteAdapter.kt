package ru.shvetsov.shoppinglist.db

import android.content.SharedPreferences
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
import ru.shvetsov.shoppinglist.utils.TimeManager

class NoteAdapter(private val listener: Listener, private val defaultPreference: SharedPreferences) : ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener, defaultPreference)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = NoteListItemBinding.bind(view)
        fun setData(note: NoteItem, listener: Listener, defaultPreference: SharedPreferences) = with(binding) {
            tvTitle.text = note.title
            tvDescription.text = HtmlManager.getTextFromHtml(note.content).trim()
            tvTime.text = TimeManager.getTimeFormat(note.time, defaultPreference)
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