package ru.shvetsov.shoppinglist.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.shvetsov.shoppinglist.databinding.ActivityShoppingListBinding
import ru.shvetsov.shoppinglist.db.MainViewModel
import ru.shvetsov.shoppinglist.entities.ShoppingListName

class ShoppingListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingListBinding
    private var shoppingListName: ShoppingListName? = null
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).dataBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            shoppingListName = intent.getSerializableExtra(SHOPPING_LIST_NAME, ShoppingListName::class.java) as ShoppingListName
        }
    }

    companion object {
        const val SHOPPING_LIST_NAME = "shopping_list_name"
    }
}