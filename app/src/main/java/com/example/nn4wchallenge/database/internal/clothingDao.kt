package com.example.nn4wchallenge.database.internal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface clothingDao {

    @Query("SELECT * FROM clothing")
    fun getAll(): Array<clothing>

    @Insert
    fun insert(vararg newClothing: clothing)

    @Delete
    fun delete(oldClothing: clothing)


}