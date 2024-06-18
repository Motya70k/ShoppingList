package ru.shvetsov.shoppinglist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.shvetsov.shoppinglist.entities.LibraryItem
import ru.shvetsov.shoppinglist.entities.NoteItem
import ru.shvetsov.shoppinglist.entities.ShoppingListItem
import ru.shvetsov.shoppinglist.entities.ShoppingListName

@Dao
interface Dao {
    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM shopping_list_name")
    fun getAllListNames(): Flow<List<ShoppingListName>>

    @Query("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Query("DELETE FROM shopping_list_name WHERE id IS :id")
    suspend fun deleteList(id: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Insert
    suspend fun insertShoppingListName(shoppingListName: ShoppingListName)

    @Update
    suspend fun updateNote(note: NoteItem)

    @Update
    suspend fun updateShoppingListName(listName: ShoppingListName)

    @Insert
    suspend fun insertItem(shoppingListItem: ShoppingListItem)

    @Query("SELECT * FROM shopping_list_item WHERE listId LIKE :listId")
    fun getAllListItems(listId: Int): Flow<List<ShoppingListItem>>

    @Update
    suspend fun updateListItem(item: ShoppingListItem)

    @Query("DELETE FROM shopping_list_item WHERE listId LIKE :listId")
    suspend fun deleteListItemsByListId(listId: Int)

    @Query("SELECT * FROM library WHERE name LIKE :name")
    suspend fun getAllLibraryItems(name: String): List<LibraryItem>

    @Insert
    suspend fun insertLibraryItem(libraryItem: LibraryItem)
}