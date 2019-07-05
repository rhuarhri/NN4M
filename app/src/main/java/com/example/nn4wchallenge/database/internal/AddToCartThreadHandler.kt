package com.example.nn4wchallenge.database.internal

import android.content.Context
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters

class AddToCartThreadHandler(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {


    override fun doWork(): Result {

        var newCartItem : cartItem = cartItem()
        newCartItem.itemImage = inputData.getString("image").toString()
        newCartItem.itemName = inputData.getString("name").toString()
        newCartItem.itemprice = inputData.getDouble("price", 0.0)

        val accessDB = Room.databaseBuilder(applicationContext, cartDatabase::class.java,
            "cart-database").build()

        accessDB.cartDao().insert(newCartItem)

        return Result.success()
    }
}