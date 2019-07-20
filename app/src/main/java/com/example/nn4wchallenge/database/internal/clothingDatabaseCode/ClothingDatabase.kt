package com.example.nn4wchallenge.database.internal.clothingDatabaseCode
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Clothing::class), version = 1, exportSchema = false)
abstract class ClothingDatabase : RoomDatabase() {
    abstract fun clothingDao(): ClothingDao
}