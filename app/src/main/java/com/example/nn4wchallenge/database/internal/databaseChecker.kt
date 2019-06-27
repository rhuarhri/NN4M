package com.example.nn4wchallenge.database.internal

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class databaseChecker (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        var chosenDatabase : String? = inputData.getString("database")

        var isDatabaseEmpty : Boolean = true

        when (chosenDatabase)
        {
            "user" -> isDatabaseEmpty = isUserDatabaseEmpty()
            "clothing" -> isDatabaseEmpty = isClothingDatabaseEmpty()
            "cart" -> isDatabaseEmpty = isCartDatabaseEmpty()
            null -> isDatabaseEmpty = true
        }

        var output : Data = Data.Builder().putBoolean("empty", isDatabaseEmpty).build()

        return Result.success(output)
    }

    private fun isUserDatabaseEmpty() : Boolean
    {

        val accessUserDB = Room.databaseBuilder(applicationContext, userDatabase::class.java,
            "user-info-database").build()

        val rowsInDatabase : Int = accessUserDB.userDao().getAll().size

        return rowsInDatabase <= 0

    }

    private fun isClothingDatabaseEmpty() : Boolean
    {
        val accessClothingDB = Room.databaseBuilder(applicationContext, clothingDatabase::class.java,
            "user-clothes-database").build()

        val rowsInDatabase : Int = accessClothingDB.clothingDao().getAll().size

        return rowsInDatabase <= 0
    }

    private fun isCartDatabaseEmpty() : Boolean
    {
        val accessCartDB = Room.databaseBuilder(applicationContext, cartDatabase::class.java,
            "cart-database").build()

        val rowsInDatabase : Int = accessCartDB.cartDao().getAll().size

        return rowsInDatabase <= 0
    }
}