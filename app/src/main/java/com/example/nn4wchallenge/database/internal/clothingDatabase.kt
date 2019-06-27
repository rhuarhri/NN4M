package com.example.nn4wchallenge.database.internal
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(clothing::class), version = 1, exportSchema = false)
abstract class clothingDatabase : RoomDatabase() {
    abstract fun clothingDao(): clothingDao
}