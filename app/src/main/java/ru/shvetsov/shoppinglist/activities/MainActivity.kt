package ru.shvetsov.shoppinglist.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.shvetsov.shoppinglist.R
import ru.shvetsov.shoppinglist.databinding.ActivityMainBinding
import ru.shvetsov.shoppinglist.fragments.FragmentManager
import ru.shvetsov.shoppinglist.fragments.NoteFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavListener()
    }

    private fun setBottomNavListener() {
        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {}
                R.id.notes -> {
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }

                R.id.shop_list -> {}
                R.id.new_item -> {
                    FragmentManager.currentFragment?.onClickNew()
                }
            }
            true
        }
    }
}