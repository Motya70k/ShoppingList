package ru.shvetsov.shoppinglist.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.shvetsov.shoppinglist.R
import ru.shvetsov.shoppinglist.databinding.ActivityShoppingListBinding
import ru.shvetsov.shoppinglist.db.MainViewModel
import ru.shvetsov.shoppinglist.db.ShoppingListItemAdapter
import ru.shvetsov.shoppinglist.dialogs.EditListItemDialog
import ru.shvetsov.shoppinglist.entities.LibraryItem
import ru.shvetsov.shoppinglist.entities.ShoppingListItem
import ru.shvetsov.shoppinglist.entities.ShoppingListName
import ru.shvetsov.shoppinglist.utils.ShareHelper

class ShoppingListActivity : AppCompatActivity(), ShoppingListItemAdapter.Listener {
    private lateinit var binding: ActivityShoppingListBinding
    private var shoppingListName: ShoppingListName? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShoppingListItemAdapter? = null
    private lateinit var textWatcher: TextWatcher

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).dataBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initRcView()
        listItemObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shopping_list_menu, menu)
        saveItem = menu?.findItem(R.id.save_item)!!
        val newItem = menu.findItem(R.id.new_item)
        edItem = newItem.actionView?.findViewById(R.id.edNewShopItem)
        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        textWatcher = textWatcher()
        return true
    }

    private fun textWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mainViewModel.getAllLibraryItems("%$s%")
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_item -> {
                addNewShopItem()
            }
            R.id.delete_list -> {
                mainViewModel.deleteList(shoppingListName?.id!!, true)
                finish()
            }
            R.id.clear_list -> {
                mainViewModel.deleteList(shoppingListName?.id!!, false)
            }
            R.id.share_list -> {
                startActivity(Intent.createChooser(
                    ShareHelper.shareShoppingList(adapter?.currentList!!, shoppingListName?.name!!),
                    "Share by"
                ))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewShopItem() {
        if (edItem?.text.toString().isEmpty()) return
        val item = ShoppingListItem(
            null,
            edItem?.text.toString(),
            "",
            false,
            shoppingListName?.id!!,
            0
        )
        edItem?.setText(R.string.empty_string)
        mainViewModel.insertListItem(item)
    }

    private fun listItemObserver() {
        mainViewModel.getAllItemsFromList(shoppingListName?.id!!).observe(this) {
            adapter?.submitList(it)
            binding.tvEmpty.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun libraryItemObserver() {
        mainViewModel.libraryItems.observe(this) {
            val tempShopList = ArrayList<ShoppingListItem>()
            it.forEach { item ->
                val shopItem = ShoppingListItem(
                    item.id,
                    item.name,
                    "",
                    false,
                    0,
                    1
                )
                tempShopList.add(shopItem)
            }
            adapter?.submitList(tempShopList)
            binding.tvEmpty.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun initRcView() = with(binding) {
        adapter = ShoppingListItemAdapter(this@ShoppingListActivity)
        rcView.layoutManager = LinearLayoutManager(this@ShoppingListActivity)
        rcView.adapter = adapter
    }

    private fun expandActionView(): OnActionExpandListener {
        return object : OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                saveItem.isVisible = true
                edItem?.addTextChangedListener(textWatcher)
                libraryItemObserver()
                mainViewModel.getAllItemsFromList(shoppingListName?.id!!).removeObservers(this@ShoppingListActivity)
                mainViewModel.getAllLibraryItems("%%")
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher)
                invalidateOptionsMenu()
                mainViewModel.libraryItems.removeObservers(this@ShoppingListActivity)
                edItem?.setText(R.string.empty_string)
                listItemObserver()
                return true
            }

        }
    }

    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            shoppingListName = intent.getSerializableExtra(
                SHOPPING_LIST_NAME,
                ShoppingListName::class.java
            ) as ShoppingListName
        }
    }

    companion object {
        const val SHOPPING_LIST_NAME = "shopping_list_name"
    }

    override fun onClickItem(listItem: ShoppingListItem, state: Int) {
        when (state) {
            ShoppingListItemAdapter.CHECK_BOX -> mainViewModel.updateListItem(listItem)
            ShoppingListItemAdapter.EDIT -> editListItem(listItem)
            ShoppingListItemAdapter.EDIT_LIBRARY_ITEM -> editLibraryItem(listItem)
            ShoppingListItemAdapter.DELETE_LIBRARY_ITEM -> {
                mainViewModel.deleteLibraryItem(listItem.id!!)
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
            }
        }
    }

    private fun editListItem(listItem: ShoppingListItem) {
        EditListItemDialog.showDialog(this, listItem, object : EditListItemDialog.Listener {
            override fun onClick(item: ShoppingListItem) {
                mainViewModel.updateListItem(item)
            }
        })
    }

    private fun editLibraryItem(listItem: ShoppingListItem) {
        EditListItemDialog.showDialog(this, listItem, object : EditListItemDialog.Listener {
            override fun onClick(item: ShoppingListItem) {
                mainViewModel.updateLibraryItem(LibraryItem(item.id, item.name))
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
        }
        })
    }
}