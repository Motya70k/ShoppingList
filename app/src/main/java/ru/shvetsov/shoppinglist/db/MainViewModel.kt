package ru.shvetsov.shoppinglist.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.shvetsov.shoppinglist.entities.LibraryItem
import ru.shvetsov.shoppinglist.entities.NoteItem
import ru.shvetsov.shoppinglist.entities.ShoppingListItem
import ru.shvetsov.shoppinglist.entities.ShoppingListName

class MainViewModel(dataBase: MainDataBase) : ViewModel() {

    private val dao = dataBase.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allShoppingListNames: LiveData<List<ShoppingListName>> = dao.getAllListNames().asLiveData()

    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertShoppingListName(shoppingListName: ShoppingListName) = viewModelScope.launch {
        dao.insertShoppingListName(shoppingListName)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }

    fun updateListItem(item: ShoppingListItem) = viewModelScope.launch {
        dao.updateListItem(item)
    }

    fun updateShoppingListName(listName: ShoppingListName) = viewModelScope.launch {
        dao.updateShoppingListName(listName)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun deleteList(id: Int, deleteList: Boolean) = viewModelScope.launch {
        if (deleteList) dao.deleteList(id)
        dao.deleteListItemsByListId(id)
    }

    fun insertListItem(shoppingListItem: ShoppingListItem) = viewModelScope.launch {
        dao.insertItem(shoppingListItem)
        if (!isLibraryItemExist(shoppingListItem.name)) dao.insertLibraryItem(LibraryItem(null, shoppingListItem.name))
    }

    fun getAllItemsFromList(listId: Int): LiveData<List<ShoppingListItem>> {
        return dao.getAllListItems(listId).asLiveData()
    }

    private suspend fun isLibraryItemExist(name: String): Boolean {
        return dao.getAllLibraryItems(name).isNotEmpty()
    }

    class MainViewModelFactory(private val dataBase: MainDataBase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(dataBase) as T
            }
            throw IllegalArgumentException("Unknown ViewModelCast")
        }
    }
}