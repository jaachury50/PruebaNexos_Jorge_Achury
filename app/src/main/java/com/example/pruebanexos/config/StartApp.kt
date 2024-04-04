package com.example.pruebanexos.config

import android.app.Application
import androidx.room.Room
import com.example.pruebanexos.database.AuthorizationDatabase

class StartApp: Application() {
    companion object{
        lateinit var db: AuthorizationDatabase
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            AuthorizationDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }
}