package com.example.nn4wchallenge.database.internal.cartDatabaseCode

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface cartDao {

    @Query("SELECT * FROM cartItem")
    fun getAll(): Array<cartItem>

    @Query("SELECT price FROM cartItem")
    fun getPriceList() : Array<Double>

    @Insert
    fun insert(vararg newCartItem: cartItem)

    @Delete
    fun delete(oldcartItem: cartItem)

    @Query("DELETE FROM cartItem WHERE id = :deleteId")
    fun deleteById(deleteId : Int)

    @Query("SELECT * FROM cartItem WHERE id = :selectItemId")
    fun getItem(selectItemId : Int) : Array<cartItem>

}