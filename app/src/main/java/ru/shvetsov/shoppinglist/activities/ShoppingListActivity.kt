package ru.shvetsov.shoppinglist.activities

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ru.shvetsov.shoppinglist.R
import ru.shvetsov.shoppinglist.databinding.ActivityShoppingListBinding
import ru.shvetsov.shoppinglist.db.MainViewModel
import ru.shvetsov.shoppinglist.db.ShoppingListItemAdapter
import ru.shvetsov.shoppinglist.entities.ShoppingListItem
import ru.shvetsov.shoppinglist.entities.ShoppingListName

class ShoppingListActivity : AppCompatActivity(), ShoppingListItemAdapter.Listener {
    private lateinit var binding: ActivityShoppingListBinding
    private var shoppingListName: ShoppingListName? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShoppingListItemAdapter? = null

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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_item) {
            addNewShopItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewShopItem() {
        if (edItem?.text.toString().isEmpty()) return
        val item = ShoppingListItem(
            null,
            edItem?.text.toString(),
            null,
            0,
            shoppingListName?.id!!,
            0
        )
        edItem?.setText("")
        mainViewModel.insertListItem(item)
    }

    private fun listItemObserver() {
        mainViewModel.getAllItemsFromList(shoppingListName?.id!!).observe(this) {
            adapter?.submitList(it)
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
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                saveItem.isVisible = false
                invalidateOptionsMenu()
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

    override fun deleteItem(id: Int) {

    }

    override fun editItem(listName: ShoppingListName) {

    }

    override fun onClickItem(listName: ShoppingListName) {

    }
}