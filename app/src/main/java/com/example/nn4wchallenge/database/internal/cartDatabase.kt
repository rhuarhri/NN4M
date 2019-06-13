package com.example.nn4wchallenge.database.internal
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(cartItem::class), version = 1)
abstract class cartDatabase : RoomDatabase() {
    abstract fun cartDao(): cartDao
}