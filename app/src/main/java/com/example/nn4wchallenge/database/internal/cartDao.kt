package com.example.nn4wchallenge.database.internal

import androidx.room.Dao
import androidx.room.Query

@Dao
interface cartDao {

    @Query("SELECT * FROM cartItem")
    fun getAll(): Array<cartItem>

}