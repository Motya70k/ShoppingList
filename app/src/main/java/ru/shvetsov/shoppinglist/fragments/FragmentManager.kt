package ru.shvetsov.shoppinglist.fragments

import androidx.appcompat.app.AppCompatActivity
import ru.shvetsov.shoppinglist.R

object FragmentManager {

    var currentFragment: BaseFragment? = null
    fun setFragment(newFragment: BaseFragment, activity: AppCompatActivity) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentHolder, newFragment)
        transaction.commit()
        currentFragment = newFragment
    }
}