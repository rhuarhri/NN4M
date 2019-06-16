package com.example.nn4wchallenge.database.internal

import android.content.Context
import androidx.room.Room
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class AddClothingThreadManager (appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams)
{
    override fun doWork(): Result {

        var input : MutableMap<String, Any> = inputData.keyValueMap

        var newClothing : clothing = input["new"] as clothing

        val accessDB = Room.databaseBuilder(applicationContext, clothingDatabase::class.java,
        "user-clothes-database").build()

        accessDB.clothingDao().insert()

        return Result.success()
    }


}