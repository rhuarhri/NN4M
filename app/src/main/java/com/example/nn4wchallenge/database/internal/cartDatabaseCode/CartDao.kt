package com.example.nn4wchallenge.database.internal.cartDatabaseCode

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {

    @Query("SELECT * FROM CartItem")
    fun getAll(): Array<CartItem>

    @Query("SELECT price FROM CartItem")
    fun getPriceList() : Array<Double>

    @Insert
    fun insert(vararg newCartItem: CartItem)

    @Delete
    fun delete(oldcartItem: CartItem)

    @Query("DELETE FROM CartItem WHERE id = :deleteId")
    fun deleteById(deleteId : Int)

    @Query("SELECT * FROM CartItem WHERE id = :selectItemId")
    fun getItem(selectItemId : Int) : Array<CartItem>

}