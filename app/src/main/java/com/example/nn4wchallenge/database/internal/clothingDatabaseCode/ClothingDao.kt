package com.example.nn4wchallenge.database.internal.clothingDatabaseCode

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ClothingDao {

    @Query("SELECT * FROM Clothing")
    fun getAll(): Array<Clothing>

    @Query("SELECT image FROM Clothing WHERE id = :itemId")
    fun getImageLocation(itemId : Int) : Array<String>

    @Insert
    fun insert(vararg newClothing: Clothing)

    @Delete
    fun delete(oldClothing: Clothing)

    @Query("DELETE FROM Clothing WHERE id = :deleteId")
    fun deleteById(deleteId : Int)

}