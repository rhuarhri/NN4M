package com.example.nn4wchallenge.database.internal

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nn4wchallenge.database.internal.cartDatabaseCode.CartDatabase
import com.example.nn4wchallenge.database.internal.clothingDatabaseCode.ClothingDatabase
import com.example.nn4wchallenge.database.internal.userDatabaseCode.UserDatabase

class DatabaseChecker (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val chosenDatabase : String? = inputData.getString("database")

        var isDatabaseEmpty = true

        when (chosenDatabase)
        {
            "user" -> isDatabaseEmpty = isUserDatabaseEmpty()
            "clothing" -> isDatabaseEmpty = isClothingDatabaseEmpty()
            "cart" -> isDatabaseEmpty = isCartDatabaseEmpty()
            null -> isDatabaseEmpty = true
        }

        val output : Data = Data.Builder().putBoolean("empty", isDatabaseEmpty).build()

        return Result.success(output)
    }

    private fun isUserDatabaseEmpty() : Boolean
    {

        val accessUserDB = Room.databaseBuilder(applicationContext, UserDatabase::class.java,
            "user-info-database").build()

        val rowsInDatabase : Int = accessUserDB.userDao().getAll().size

        return rowsInDatabase <= 0

    }

    private fun isClothingDatabaseEmpty() : Boolean
    {
        val accessClothingDB = Room.databaseBuilder(applicationContext, ClothingDatabase::class.java,
            "user-clothes-database").build()

        val rowsInDatabase : Int = accessClothingDB.clothingDao().getAll().size

        return rowsInDatabase <= 0
    }

    private fun isCartDatabaseEmpty() : Boolean
    {
        val accessCartDB = Room.databaseBuilder(applicationContext, CartDatabase::class.java,
            "cart-database").build()

        val rowsInDatabase : Int = accessCartDB.cartDao().getAll().size

        return rowsInDatabase <= 0
    }
}