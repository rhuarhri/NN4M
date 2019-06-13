package com.example.nn4wchallenge.database.internal

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(user::class), version = 1)
abstract class userDatabase : RoomDatabase() {
    abstract fun userDao(): userDao
}