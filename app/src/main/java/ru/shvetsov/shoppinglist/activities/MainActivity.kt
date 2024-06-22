package ru.shvetsov.shoppinglist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import ru.shvetsov.shoppinglist.R
import ru.shvetsov.shoppinglist.databinding.ActivityMainBinding
import ru.shvetsov.shoppinglist.dialogs.NewListDialog
import ru.shvetsov.shoppinglist.fragments.FragmentManager
import ru.shvetsov.shoppinglist.fragments.NoteFragment
import ru.shvetsov.shoppinglist.fragments.ShoppingListNameFragment
import ru.shvetsov.shoppinglist.settings.SettingsActivity

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    private lateinit var binding: ActivityMainBinding
    private var currentMenuItemId = R.id.shop_list
    private lateinit var defaultPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        defaultPreference = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(binding.root)
        FragmentManager.setFragment(ShoppingListNameFragment.newInstance(), this)
        setBottomNavListener()
    }

    private fun setBottomNavListener() {
        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.notes -> {
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.shop_list -> {
                    currentMenuItemId = R.id.shop_list
                    FragmentManager.setFragment(ShoppingListNameFragment.newInstance(), this)
                }
                R.id.new_item -> {
                    FragmentManager.currentFragment?.onClickNew()
                }
            }
            true
        }
    }

    override fun onClick(name: String) {
        Log.d("Log", "Name: $name")
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavView.selectedItemId = currentMenuItemId
    }

    private fun getSelectedTheme(): Int {
        return if (defaultPreference.getString("theme_key", "green") == "green") {
            R.style.Theme_ShoppingListGreen
        } else {
            R.style.Theme_ShoppingListBlue
        }
    }
}