package ru.shvetsov.shoppinglist.activities

import android.app.Application
import ru.shvetsov.shoppinglist.db.MainDataBase

class MainApp : Application() {
    val dataBase by lazy { MainDataBase.getDataBase(this) }
}