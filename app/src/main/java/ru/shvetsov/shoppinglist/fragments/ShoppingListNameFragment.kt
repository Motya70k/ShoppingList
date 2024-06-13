package ru.shvetsov.shoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.shvetsov.shoppinglist.activities.MainApp
import ru.shvetsov.shoppinglist.activities.ShoppingListActivity
import ru.shvetsov.shoppinglist.databinding.FragmentShoppingListNameBinding
import ru.shvetsov.shoppinglist.db.MainViewModel
import ru.shvetsov.shoppinglist.db.ShoppingListNameAdapter
import ru.shvetsov.shoppinglist.dialogs.DeleteDialog
import ru.shvetsov.shoppinglist.dialogs.NewListDialog
import ru.shvetsov.shoppinglist.entities.ShoppingListName
import ru.shvetsov.shoppinglist.utils.TimeManager
class ShoppingListNameFragment : BaseFragment(), ShoppingListNameAdapter.Listener {

    private lateinit var binding: FragmentShoppingListNameBinding
    private lateinit var adapter: ShoppingListNameAdapter

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).dataBase)
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                val shoppingListName = ShoppingListName(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertShoppingListName(shoppingListName)
            }
        }, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.allNotes.observe(this) { it }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingListNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observer()
    }

    private fun initRecyclerView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = ShoppingListNameAdapter(this@ShoppingListNameFragment)
        rcView.adapter = adapter
    }

    private fun observer() {
        mainViewModel.allShoppingListNames.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = ShoppingListNameFragment()
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(context as AppCompatActivity, object : DeleteDialog.Listener {
            override fun onClick() {
                mainViewModel.deleteList(id)
            }
        })
    }

    override fun editItem(listName: ShoppingListName) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                mainViewModel.updateShoppingListName(listName.copy(name = name))
            }
        }, listName.name)
    }

    override fun onClickItem(listName: ShoppingListName) {
        val intent = Intent(activity, ShoppingListActivity::class.java).apply {
            putExtra(ShoppingListActivity.SHOPPING_LIST_NAME, listName)
        }
        startActivity(intent)
    }
}