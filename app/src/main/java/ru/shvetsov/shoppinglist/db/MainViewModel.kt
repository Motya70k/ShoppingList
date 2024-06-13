package ru.shvetsov.shoppinglist.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.shvetsov.shoppinglist.entities.NoteItem
import ru.shvetsov.shoppinglist.entities.ShoppingListName

class MainViewModel(dataBase: MainDataBase) : ViewModel() {

    val dao = dataBase.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allShoppingListNames: LiveData<List<ShoppingListName>> = dao.getAllListNames().asLiveData()
    fun insertNote(note: NoteItem?) = viewModelScope.launch {
        note?.let { dao.insertNote(it) }
    }
    fun insertShoppingListName(shoppingListName: ShoppingListName) = viewModelScope.launch {
        dao.insertShopListName(shoppingListName)
    }
    fun updateNote(note: NoteItem?) = viewModelScope.launch {
        note?.let { dao.updateNote(it) }
    }
    fun updateShoppingListName(listName: ShoppingListName) = viewModelScope.launch {
        dao.updateShoppingListName(listName)
    }
    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }
    fun deleteList(id: Int) = viewModelScope.launch {
        dao.deleteList(id)
    }

    class MainViewModelFactory(val dataBase: MainDataBase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(dataBase) as T
            }
            throw IllegalArgumentException("Unknown ViewModelCast")
        }
    }
}