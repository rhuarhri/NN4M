package com.example.nn4wchallenge.database.internal.cartDatabaseCode
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(CartItem::class), version = 1, exportSchema = false)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}