package com.example.pruebanexos.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pruebanexos.database.dao.AuthorizationDao
import com.example.pruebanexos.database.entities.AuthorizationEntity
import com.example.pruebanexos.database.entities.Converters

@Database(entities = [AuthorizationEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AuthorizationDatabase: RoomDatabase() {

    abstract fun authorizationDao():AuthorizationDao
}