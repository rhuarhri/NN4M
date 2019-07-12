package com.example.nn4wchallenge.database.internal.userDatabaseCode

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(user::class), version = 1, exportSchema = false)
abstract class userDatabase : RoomDatabase() {
    abstract fun userDao(): userDao
}