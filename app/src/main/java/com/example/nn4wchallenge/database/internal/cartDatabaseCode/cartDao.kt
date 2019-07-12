package com.example.nn4wchallenge.database.internal.cartDatabaseCode

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface cartDao {

    @Query("SELECT * FROM cartItem")
    fun getAll(): Array<cartItem>

    @Insert
    fun insert(vararg newCartItem: cartItem)

    @Delete
    fun delete(oldcartItem: cartItem)

}